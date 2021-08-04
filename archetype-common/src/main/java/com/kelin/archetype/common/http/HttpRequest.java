// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kelin.archetype.common.json.JsonConverter;
import com.kelin.archetype.common.utils.HttpUtils;
import lombok.Getter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.Preconditions;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kelin Tan
 */
@Getter
public class HttpRequest {
    private final URIBuilder uriBuilder;
    private final Map<String, Object> headers = new LinkedHashMap<>();
    private String content;
    private HttpConfig config;
    private HttpRequestBase request;
    private CloseableHttpResponse response;

    public HttpRequest(String host) {
        this.uriBuilder = HttpUtils.safeURIBuilder(host);
    }

    public static HttpRequest host(String host) {
        return new HttpRequest(host);
    }

    public HttpRequest withPath(String path) {
        Preconditions.checkNotNull(uriBuilder);

        this.uriBuilder.setPath(path);
        return this;
    }

    public HttpRequest withParam(String param, Object value) {
        Preconditions.checkNotNull(uriBuilder);

        this.uriBuilder.addParameter(param, value.toString());
        return this;
    }

    public HttpRequest withParams(Map<String, Object> params) {
        Preconditions.checkNotNull(this.uriBuilder);

        this.uriBuilder.addParameters(HttpUtils.buildNameValuePairs(params));
        return this;
    }

    public HttpRequest withHeader(String header, Object value) {
        this.headers.put(header, value);
        return this;
    }

    public HttpRequest withHeaders(Map<String, Object> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public HttpRequest withContent(String content) {
        this.content = content;
        return this;
    }

    public HttpRequest withConfig(HttpConfig config) {
        this.config = config;
        return this;
    }

    public HttpRequest get() {
        this.request = new HttpGet(build());
        return this;
    }

    public HttpRequest post() {
        HttpPost post = new HttpPost(build());

        buildBody(post);

        this.request = post;
        return this;
    }

    public HttpRequest delete() {
        this.request = new HttpDelete(HttpUtils.safeURI(this.uriBuilder));
        return this;
    }

    public HttpRequest put() {
        HttpPut put = new HttpPut(build());

        buildBody(put);

        this.request = put;
        return this;
    }

    public HttpRequest patch() {
        HttpPatch patch = new HttpPatch(build());

        buildBody(patch);

        this.request = patch;
        return this;
    }

    public HttpRequest options() {
        this.request = new HttpOptions(build());
        return this;
    }

    public HttpRequest header() {
        this.request = new HttpHead(build());
        return this;
    }

    public HttpRequest execute() {
        return execute(HttpClientFactory.getDefaultHttpClient());
    }

    public HttpRequest execute(CloseableHttpClient client) {
        Preconditions.checkNotNull(this.request);

        if (!headers.isEmpty()) {
            headers.forEach((header, value) -> request.addHeader(header, value.toString()));
        }
        if (config != null) {
            request.setConfig(RequestConfig.custom()
                    .setConnectTimeout(config.getConnectionTimeout())
                    .setSocketTimeout(config.getReadTimeout())
                    .build());
        }
        this.response = HttpUtils.safeExecute(this.request, client);
        return this;
    }

    public CloseableHttpResponse response() {
        Preconditions.checkNotNull(this.response);

        return response;
    }

    public int status() {
        Preconditions.checkNotNull(this.response);
        EntityUtils.consumeQuietly(this.response.getEntity());
        return response.getStatusLine().getStatusCode();
    }

    public boolean isOk() {
        return HttpUtils.isOk(status());
    }

    public boolean isBadRequest() {
        return HttpUtils.isBadRequest(status());
    }

    public boolean isErrorRequest() {
        return HttpUtils.isError(status());
    }

    public <T> T json(Class<T> type) {
        Preconditions.checkNotNull(this.response);

        String entity = HttpUtils.safeEntityToString(this.response.getEntity());
        return JsonConverter.deserialize(entity, type);
    }

    public <T> T json(TypeReference<T> type) {
        Preconditions.checkNotNull(this.response);

        return JsonConverter.deserializeGenerics(HttpUtils.safeEntityToString(this.response.getEntity()), type);
    }

    public <T> List<T> jsonArray(Class<T> type) {
        Preconditions.checkNotNull(this.response);

        return JsonConverter.deserializeList(HttpUtils.safeEntityToString(this.response.getEntity()), type);
    }

    public <T, E> Map<T, E> jsonMap(Class<T> key, Class<E> value) {
        Preconditions.checkNotNull(this.response);

        return JsonConverter.deserializeMap(HttpUtils.safeEntityToString(this.response.getEntity()), key, value);
    }

    public HttpRequest performPut() {
        return put().execute();
    }

    public HttpRequest performGet() {
        return get().execute();
    }

    public HttpRequest performPost() {
        return post().execute();
    }

    public HttpRequest performDelete() {
        return delete().execute();
    }

    public HttpRequest performHeader() {
        return header().execute();
    }

    public HttpRequest performOptions() {
        return options().execute();
    }

    public HttpRequest performPatch() {
        return patch().execute();
    }

    private void buildBody(HttpEntityEnclosingRequestBase entityRequest) {
        if (content != null) {
            entityRequest.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
        }
    }

    private URI build() {
        return HttpUtils.safeURI(this.uriBuilder);
    }
}
