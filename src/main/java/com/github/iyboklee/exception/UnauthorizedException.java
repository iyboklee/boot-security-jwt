/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.exception;

import com.github.iyboklee.core.service.MessageSourceService;

public class UnauthorizedException extends ServiceRuntimeException {

    public static final String MESSAGE_KEY = "error.auth";
    public static final String MESSAGE_DETAIL = "error.auth.detail";

    public UnauthorizedException(String message) {
        super(MESSAGE_KEY, MESSAGE_DETAIL, new Object[]{message});
    }

    @Override
    public String getMessage() {
        return MessageSourceService.getInstance().getMessage(getDetailKey(), getParams());
    }

    @Override
    public String toString() {
        return MessageSourceService.getInstance().getMessage(getMessageKey());
    }

}
