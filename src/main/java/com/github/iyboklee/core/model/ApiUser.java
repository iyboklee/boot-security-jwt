/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.core.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.iyboklee.security.JWT;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
public class ApiUser {

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private int loginCount;

    private LocalDateTime loginTime;

    public ApiUser(String username, String email) {
        this(username, null, email);
    }

    public ApiUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public long afterLoginSuccess() {
        this.loginTime = LocalDateTime.now();
        return ++loginCount;
    }

    public String generateToken(JWT jwt, Collection<? extends GrantedAuthority> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("email", email);
        claims.put("roles", roles);
        return jwt.generateToken(claims);
    }

}