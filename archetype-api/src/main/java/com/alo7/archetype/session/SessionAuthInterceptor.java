// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kelin Tan
 */
@Slf4j
public class SessionAuthInterceptor implements HandlerInterceptor {
    private SessionService sessionService;

    public SessionAuthInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod && SessionCache.ACCOUNT.get() == null) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.hasMethodAnnotation(SessionAuth.class)) {
                SessionCache.ACCOUNT.set(sessionService.getCurrentAccount(request));
            }
        }
        return true;
    }
}
