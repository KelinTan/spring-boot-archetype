// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing.log;

import static com.kelin.archetype.core.tracing.TracingConstants.MDC_TRACING;

import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kelin Tan
 */
public class TracingMDCInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
            @NotNull Object handler) {
        Span activeSpan = GlobalTracer.get().activeSpan();
        if (activeSpan != null) {
            MDC.put(MDC_TRACING, activeSpan.context().toString());
        }
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
            @NotNull Object handler,
            @Nullable Exception ex) {
        MDC.remove(MDC_TRACING);
    }
}
