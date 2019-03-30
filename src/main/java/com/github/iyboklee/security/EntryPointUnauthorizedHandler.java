/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.iyboklee.api.model.response.ApiResult;

@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

    private static ApiResult E401 = new ApiResult("Access denied", HttpStatus.UNAUTHORIZED);

    @Autowired private ObjectMapper om;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("content-type", "application/json");
        response.getWriter().write(om.writeValueAsString(E401));
        response.getWriter().flush();
        response.getWriter().close();
    }

}