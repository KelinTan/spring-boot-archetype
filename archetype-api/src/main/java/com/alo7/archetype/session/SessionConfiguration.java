// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.session;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Kelin Tan
 */
@Configuration
@RequiredArgsConstructor
public class SessionConfiguration implements WebMvcConfigurer {
    private final SessionService sessionService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthInterceptor(sessionService));
    }
}
