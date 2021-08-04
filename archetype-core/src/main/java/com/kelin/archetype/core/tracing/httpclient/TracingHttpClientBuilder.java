// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing.httpclient;

import com.kelin.archetype.core.tracing.httpclient.ApacheClientSpanDecorator.StandardTags;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.execchain.ClientExecChain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * extends HttpClientBuilder and support open-tracing
 *
 * @author Pavol Loffay
 * @author Kelin Tan
 */
public class TracingHttpClientBuilder extends HttpClientBuilder {
    private final RedirectStrategy redirectStrategy;
    private final boolean redirectHandlingDisabled;
    private Tracer tracer;
    private List<ApacheClientSpanDecorator> spanDecorators;
    private boolean injectDisabled;

    /**
     * When using this constructor tracer should be registered via {@link GlobalTracer#registerIfAbsent(Tracer)}.
     */

    public TracingHttpClientBuilder() {
        this(DefaultRedirectStrategy.INSTANCE,
                false,
                GlobalTracer.get(),
                Collections.singletonList(new ApacheClientSpanDecorator.StandardTags()));
    }

    /**
     * @param redirectStrategy redirect strategy, do not call {@link HttpClientBuilder#disableRedirectHandling()}
     * @param redirectHandlingDisabled disable redirect strategy, do not call {@link
     *         HttpClientBuilder#setRedirectStrategy(RedirectStrategy)}
     */
    public TracingHttpClientBuilder(
            RedirectStrategy redirectStrategy,
            boolean redirectHandlingDisabled) {
        this(redirectStrategy,
                redirectHandlingDisabled,
                GlobalTracer.get(),
                Collections.singletonList(new StandardTags()));
    }

    /**
     * @param redirectStrategy redirect strategy, do not call {@link HttpClientBuilder#disableRedirectHandling()}
     * @param redirectHandlingDisabled disable redirect strategy, do not call {@link
     *         HttpClientBuilder#setRedirectStrategy(RedirectStrategy)}
     * @param tracer tracer instance
     * @param spanDecorators decorators
     */
    public TracingHttpClientBuilder(
            RedirectStrategy redirectStrategy,
            boolean redirectHandlingDisabled,
            Tracer tracer,
            List<ApacheClientSpanDecorator> spanDecorators) {
        this.redirectStrategy = redirectStrategy;
        this.redirectHandlingDisabled = redirectHandlingDisabled;
        this.tracer = tracer;
        this.spanDecorators = new ArrayList<>(spanDecorators);

        super.setRedirectStrategy(redirectStrategy);
        if (redirectHandlingDisabled) {
            super.disableRedirectHandling();
        }
    }

    public static TracingHttpClientBuilder create() {
        return new TracingHttpClientBuilder();
    }

    public TracingHttpClientBuilder withTracer(Tracer tracer) {
        this.tracer = tracer;
        return this;
    }

    public TracingHttpClientBuilder withSpanDecorators(List<ApacheClientSpanDecorator> decorators) {
        this.spanDecorators = new ArrayList<>(decorators);
        return this;
    }

    public TracingHttpClientBuilder disableInjection() {
        this.injectDisabled = true;
        return this;
    }

    @Override
    protected ClientExecChain decorateProtocolExec(final ClientExecChain requestExecutor) {
        return new TracingClientExec(requestExecutor, redirectStrategy,
                redirectHandlingDisabled, injectDisabled, tracer, spanDecorators);
    }
}
