/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.api.model.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiError {

    private int status;
    private String message;

    public ApiError(Throwable throwable, HttpStatus status) {
        this(throwable.getMessage(), status);
    }

    public ApiError(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }

}