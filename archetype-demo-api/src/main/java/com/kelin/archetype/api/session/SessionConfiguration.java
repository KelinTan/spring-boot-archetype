// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.session;

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
