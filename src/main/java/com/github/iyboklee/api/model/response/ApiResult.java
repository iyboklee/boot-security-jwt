/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.api.model.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiResult<T> {

    private boolean success;
    private ApiError error;
    private T response;

    public ApiResult(T response) {
        this.response = response;
        this.success = true;
    }

    public ApiResult(String message, HttpStatus status) {
        this.success = false;
        this.response = null;
        this.error = new ApiError(message, status);
    }

    public ApiResult(Throwable throwable, HttpStatus status) {
        this.success = false;
        this.response = null;
        this.error = new ApiError(throwable, status);
    }

}