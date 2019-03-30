/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.github.iyboklee.api.model.response.security.AuthenticationResult;
import com.github.iyboklee.core.model.ApiUser;
import com.github.iyboklee.core.service.ApiUserService;
import com.github.iyboklee.exception.NotFoundException;

@Component
public class ApiUserAuthenticationProvider implements AuthenticationProvider {

    @Value("${jwt.token.role}") private String role;

    @Autowired private JWT jwt;

    @Autowired private ApiUserService apiUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();
        String credentials = (String) authentication.getCredentials();

        try {
            ApiUser user = apiUserService.login(principal, credentials);
            ApiUserAuthenticationToken authenticated =
                    new ApiUserAuthenticationToken(user.getUsername(), null, generateAuthorities());
            String apiToken = user.generateToken(jwt, generateAuthorities());
            authenticated.setDetails(new AuthenticationResult(apiToken, user));
            return authenticated;
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    private Collection<GrantedAuthority> generateAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_" + role);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiUserAuthenticationToken.class.isAssignableFrom(authentication);
    }

}