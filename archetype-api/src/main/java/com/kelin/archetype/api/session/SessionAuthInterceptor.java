// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.session;

import com.kelin.archetype.database.entity.biz.BizAccount;
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
                BizAccount account = sessionService.getCurrentAccount(request);
                if (account == null) {
                    throw SessionExceptionFactory.toAccountSessionExpired();
                }
                SessionCache.ACCOUNT.set(account);
            }
        }
        return true;
    }
}
