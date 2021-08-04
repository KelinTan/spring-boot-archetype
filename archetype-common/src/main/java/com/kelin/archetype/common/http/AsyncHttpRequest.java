// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kelin.archetype.common.json.JsonConverter;
import com.kelin.archetype.common.utils.HttpUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.assertj.core.util.Preconditions;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Kelin Tan
 */
public class AsyncHttpRequest {
    private String host;
    private final Map<String, Object> headers = new LinkedHashMap<>();
    private final Map<String, Object> params = new LinkedHashMap<>();
    private String content;
    private HttpConfig config;
    private RequestBuilder request;
    private ListenableFuture<Response> response;

    public AsyncHttpRequest(String host) {
        this.host = host;
    }

    public static AsyncHttpRequest host(String host) {
        return new AsyncHttpRequest(host);
    }

    public AsyncHttpRequest withPath(String path) {
        Preconditions.checkNotNull(host);

        this.host = HttpUtils.concatPath(this.host, path);
        return this;
    }

    public AsyncHttpRequest get() {
        this.request = Dsl.get(this.host);
        return this;
    }

    public AsyncHttpRequest delete() {
        this.request = Dsl.delete(this.host);
        return this;
    }

    public AsyncHttpRequest post() {
        this.request = Dsl.post(this.host);

        buildBody();

        return this;
    }

    public AsyncHttpRequest put() {
        this.request = Dsl.put(this.host);
        buildBody();
        return this;
    }

    public AsyncHttpRequest withParam(String param, Object value) {
        this.params.put(param, value);
        return this;
    }

    public AsyncHttpRequest withParams(Map<String, Object> params) {
        this.params.putAll(params);
        return this;
    }

    public AsyncHttpRequest withHeader(String header, Object value) {
        this.headers.put(header, value);
        return this;
    }

    public AsyncHttpRequest withHeaders(Map<String, Object> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public AsyncHttpRequest withContent(String content) {
        this.content = content;
        return this;
    }

    public AsyncHttpRequest withConfig(HttpConfig config) {
        this.config = config;
        return this;
    }

    public ListenableFuture<Response> response() {
        Preconditions.checkNotNull(this.response);

        return response;
    }

    public Response execute() {
        return execute(HttpClientFactory.getAsyncHttpClient());
    }

    public Response execute(AsyncHttpClient asyncHttpClient) {
        Preconditions.checkNotNull(this.request);

        if (!headers.isEmpty()) {
            headers.forEach((header, value) -> request.addHeader(header, value.toString()));
        }
        if (!params.isEmpty()) {
            this.request.addQueryParams(HttpUtils.buildAsyncParams(params));
        }
        if (config != null) {
            request.setRequestTimeout(config.getConnectionTimeout());
            request.setReadTimeout(config.getReadTimeout());
        }

        return HttpUtils.asyncExecuteResponse(request.build(), asyncHttpClient);
    }

    public Response performGet() {
        return get().execute();
    }

    public Response performPost() {
        return post().execute();
    }

    public Response performPut() {
        return put().execute();
    }

    public Response performDelete() {
        return delete().execute();
    }

    public <T> T json(TypeReference<T> type) {
        Preconditions.checkNotNull(this.response);

        return JsonConverter.deserializeGenerics(HttpUtils.safeAsyncResponseBody(this.response), type);
    }

    private void buildBody() {
        if (content != null) {
            this.request.setHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON);
            this.request.setBody(content);
        }
    }
}
