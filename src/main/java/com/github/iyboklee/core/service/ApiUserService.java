/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.core.service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.iyboklee.core.model.ApiUser;
import com.github.iyboklee.exception.NotFoundException;

@Service
public class ApiUserService {

    public Set<ApiUser> apiUsers;

    @PostConstruct
    public void init() {
        apiUsers = new HashSet<>();
        apiUsers.add(new ApiUser("user1", "passwd1", "user1@email.com"));
        apiUsers.add(new ApiUser("user2", "passwd2", "user2@email.com"));
    }

    public ApiUser findOne(String username) {
        if (StringUtils.isEmpty(username))
            throw new IllegalArgumentException("Mandatory parameter `username` missing");

        Optional<ApiUser> apiUser = apiUsers.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
        return apiUser.orElse(null);
    }

    public ApiUser login(String username, String password) throws UsernameNotFoundException, BadCredentialsException {
        if (StringUtils.isEmpty(username))
            throw new IllegalArgumentException("Mandatory parameter `username` missing");
        if (StringUtils.isEmpty(password))
            throw new IllegalArgumentException("Mandatory parameter `password` missing");

        ApiUser apiUser = findOne(username);
        if (apiUser == null)
            throw new NotFoundException("Could not found user");
        if (!apiUser.getPassword().equals(password))
            throw new IllegalArgumentException("Bad credential");

        apiUser.afterLoginSuccess();
        return apiUser;
    }

}