/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.iyboklee.api.model.request.security.AuthenticationRequest;
import com.github.iyboklee.api.model.response.ApiResult;
import com.github.iyboklee.api.model.response.security.AuthenticationResult;
import com.github.iyboklee.exception.UnauthorizedException;
import com.github.iyboklee.security.ApiUserAuthenticationToken;

@RestController
@RequestMapping("api/auth")
public class AuthenticationRestController {

    @Autowired private AuthenticationManager authenticationManager;

   @PostMapping
    public ApiResult<AuthenticationResult> authentication(@RequestBody AuthenticationRequest authRequest) throws UnauthorizedException {
        try {
            Authentication authentication = new ApiUserAuthenticationToken(authRequest.getPrincipal(), authRequest.getCredentials());
            authentication = authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ApiResult<>((AuthenticationResult) authentication.getDetails());
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

}