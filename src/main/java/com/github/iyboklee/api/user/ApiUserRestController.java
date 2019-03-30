/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.iyboklee.api.model.response.ApiResult;
import com.github.iyboklee.core.model.ApiUser;
import com.github.iyboklee.core.service.ApiUserService;
import com.github.iyboklee.exception.NotFoundException;
import com.github.iyboklee.security.JwtAuthentication;

@RestController
@RequestMapping("api/user")
public class ApiUserRestController {

    @Autowired private ApiUserService apiUserService;

    @GetMapping(path = "me")
    public ApiResult<ApiUser> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        ApiUser me = apiUserService.findOne(authentication.getUsername());
        if (me == null)
            throw new NotFoundException(ApiUser.class.getSimpleName(), authentication.getUsername());
        return new ApiResult<>(me);
    }

}
