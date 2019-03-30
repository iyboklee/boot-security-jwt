/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.exception;

import org.apache.commons.lang3.StringUtils;

import com.github.iyboklee.core.service.MessageSourceService;

public class NotFoundException extends ServiceRuntimeException {

    public static final String MESSAGE_KEY = "error.notfound";
    public static final String MESSAGE_DETAIL = "error.notfound.detail";

    public NotFoundException(String targetName, Object... values) {
        super(MESSAGE_KEY, MESSAGE_DETAIL,
                new String[]{targetName, (values != null && values.length > 0) ? StringUtils.join(values, ",") : "N/A"});
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
