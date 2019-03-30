/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationTokenFilter extends GenericFilterBean {

    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    @Value("${jwt.token.header}") private String tokenHeader;

    @Autowired private JWT jwt;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;


        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String authorizationToken = obtainAuthorizationToken(request);
            if (authorizationToken != null) {
                try {
                    Map<String, Object> claims = verify(authorizationToken);
                    if (log.isDebugEnabled())
                        log.debug("Jwt parse result: {}", claims);

                    //-- 만료 10분 전
                    if (canRefresh(claims, 60 * 10)) {
                        String newAuthorizationToken = jwt.refreshToken(authorizationToken);
                        response.setHeader("api_key", newAuthorizationToken);
                    }

                    String username = MapUtils.getString(claims, "username");
                    String email = MapUtils.getString(claims, "email");
                    List<GrantedAuthority> authorities = obtainAuthorities(claims);

                    if (username != null && email != null && authorities.size() > 0) {
                        ApiUserAuthenticationToken authentication =
                                new ApiUserAuthenticationToken(new JwtAuthentication(username, email), null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    log.warn("Jwt processing failed: {}", e.getMessage());
                }
            }
        }
        else {
            if (log.isDebugEnabled()) {
                log.debug("SecurityContextHolder not populated with jwt token, as it already contained: '{}'",
                        SecurityContextHolder.getContext().getAuthentication());
            }
        }

        chain.doFilter(request, response);
    }

    private boolean canRefresh(Map<String, Object> claims, int rangeMinutes) {
        if (claims.containsKey("exp")) {
            long expiration = MapUtils.getLongValue(claims, "exp", 0);
            if (expiration != 0) {
                long remainTime = expiration - (System.currentTimeMillis() / 1000L);
                return remainTime < rangeMinutes;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private List<GrantedAuthority> obtainAuthorities(Map<String, Object> claims) {
        Collection<Map<String, String>> authMaps = (Collection<Map<String, String>>) claims.get("roles");
        return authMaps.stream()
                .map(authMap -> new SimpleGrantedAuthority(MapUtils.getString(authMap, "authority", "ROLE_ANONYMOUS")))
                .collect(Collectors.toList());
    }

    protected String obtainAuthorizationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (token != null) {
            if (log.isDebugEnabled())
                log.debug("Jwt authorization request detected: {}", token);
            try {
                token = URLDecoder.decode(token, CharEncoding.UTF_8);
                String[] parts = token.split(" ");
                if (parts.length == 2) {
                    String scheme = parts[0];
                    String credentials = parts[1];
                    return BEARER.matcher(scheme).matches() ? credentials : null;
                }
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }

        return null;
    }

    protected Map<String, Object> verify(String token) throws Exception {
        return jwt.verify(token);
    }

}