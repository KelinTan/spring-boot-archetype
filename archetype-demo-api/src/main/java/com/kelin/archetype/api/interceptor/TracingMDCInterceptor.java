// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.interceptor;

import static com.kelin.archetype.common.constants.TracingConstants.MDC_TRACING;

import com.kelin.archetype.common.tracing.CurrentTracer;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kelin Tan
 */
@Slf4j
public class TracingMDCInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
            @NotNull Object handler) {
        Tracer tracer = CurrentTracer.get();
        if (tracer instanceof MockTracer) {
            MDC.put(MDC_TRACING, "");
        } else {
            Span activeSpan = tracer.activeSpan();
            if (activeSpan != null) {
                MDC.put(MDC_TRACING, activeSpan.context().toTraceId());
            } else {
                log.error("Empty active span: " + request.getRequestURL());
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
            @NotNull Object handler,
            @Nullable Exception ex) {
        MDC.clear();
    }
}
