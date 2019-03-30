/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.exception;

public abstract class ServiceRuntimeException extends RuntimeException {

    protected String messageKey;
    protected String detailKey;
    protected Object[] params;

    public ServiceRuntimeException(String messageKey, String detailKey, Object[] params) {
        this.messageKey = messageKey;
        this.detailKey = detailKey;
        this.params = params;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getDetailKey() {
        return detailKey;
    }

    public Object[] getParams() {
        return params;
    }

}