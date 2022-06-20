// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.tracing.http.asynchttpclient;

import com.kelin.archetype.common.tracing.http.HttpClientSpanDecorator;
import com.kelin.archetype.common.tracing.http.HttpResponseTracing;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.netty.request.NettyRequest;

import javax.net.ssl.SSLSession;

import java.net.InetSocketAddress;
import java.util.List;

public class TracingAsyncHandler<T> implements AsyncHandler<T> {
    private final Tracer tracer;
    private final AsyncHandler<T> handler;
    private final Span span;
    private final List<HttpClientSpanDecorator> decorators;

    public TracingAsyncHandler(final Tracer tracer, final AsyncHandler<T> handler, final Span span,
            List<HttpClientSpanDecorator> decorators) {
        this.tracer = tracer;
        this.handler = handler;
        this.span = span;
        this.decorators = decorators;
    }

    @Override
    public State onStatusReceived(final HttpResponseStatus responseStatus) throws Exception {
        try {
            return handler.onStatusReceived(responseStatus);
        } finally {
            HttpResponseTracing responseTracing = new HttpResponseTracing(responseStatus.getStatusCode());

            decorators.forEach(decorator -> decorator.onResponse(responseTracing, span));
        }
    }

    @Override
    public State onHeadersReceived(final HttpHeaders headers) throws Exception {
        return handler.onHeadersReceived(headers);
    }

    @Override
    public State onBodyPartReceived(final HttpResponseBodyPart bodyPart) throws Exception {
        return handler.onBodyPartReceived(bodyPart);
    }

    @Override
    public State onTrailingHeadersReceived(final HttpHeaders headers) throws Exception {
        return handler.onTrailingHeadersReceived(headers);
    }

    @Override
    public void onThrowable(final Throwable t) {
        try (final Scope ignored = tracer.scopeManager().activate(span)) {
            handler.onThrowable(t);
        } finally {
            decorators.forEach(decorator -> decorator.onError(t, span));
            span.finish();
        }
    }

    @Override
    public T onCompleted() throws Exception {
        try (final Scope ignored = tracer.scopeManager().activate(span)) {
            return handler.onCompleted();
        } finally {
            span.finish();
        }
    }

    @Override
    public void onHostnameResolutionAttempt(final String name) {
        handler.onHostnameResolutionAttempt(name);
    }

    @Override
    public void onHostnameResolutionSuccess(final String name, final List<InetSocketAddress> list) {
        handler.onHostnameResolutionSuccess(name, list);
    }

    @Override
    public void onHostnameResolutionFailure(final String name, final Throwable cause) {
        handler.onHostnameResolutionFailure(name, cause);
    }

    @Override
    public void onTcpConnectAttempt(final InetSocketAddress remoteAddress) {
        handler.onTcpConnectAttempt(remoteAddress);
    }

    @Override
    public void onTcpConnectSuccess(final InetSocketAddress remoteAddress, final Channel connection) {
        handler.onTcpConnectSuccess(remoteAddress, connection);
    }

    @Override
    public void onTcpConnectFailure(final InetSocketAddress remoteAddress, final Throwable cause) {
        handler.onTcpConnectFailure(remoteAddress, cause);
    }

    @Override
    public void onTlsHandshakeAttempt() {
        handler.onTlsHandshakeAttempt();
    }

    @Override
    public void onTlsHandshakeSuccess(final SSLSession sslSession) {
        handler.onTlsHandshakeSuccess(sslSession);
    }

    @Override
    public void onTlsHandshakeFailure(final Throwable cause) {
        handler.onTlsHandshakeFailure(cause);
    }

    @Override
    public void onConnectionPoolAttempt() {
        handler.onConnectionPoolAttempt();
    }

    @Override
    public void onConnectionPooled(final Channel connection) {
        handler.onConnectionPooled(connection);
    }

    @Override
    public void onConnectionOffer(final Channel connection) {
        handler.onConnectionOffer(connection);
    }

    @Override
    public void onRequestSend(final NettyRequest request) {
        handler.onRequestSend(request);
    }

    @Override
    public void onRetry() {
        handler.onRetry();
    }
}
