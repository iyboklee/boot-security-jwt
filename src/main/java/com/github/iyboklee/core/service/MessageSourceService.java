/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

@Service
public class MessageSourceService {

    @Autowired private MessageSourceAccessor messageSourceAccessor;

    private static MessageSourceService instance = new MessageSourceService();

    @Bean(name = "messageSourceService")
    public static MessageSourceService getInstance() {
        return instance;
    }

    public String getMessage(String key) {
        return messageSourceAccessor.getMessage(key);
    }

    public String getMessage(String key, Object... params) {
        return messageSourceAccessor.getMessage(key, params);
    }

}