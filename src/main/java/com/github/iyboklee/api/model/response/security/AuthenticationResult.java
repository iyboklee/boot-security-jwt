/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.api.model.response.security;

import com.github.iyboklee.core.model.ApiUser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResult {

    private String apiToken;
    private ApiUser user;

}
