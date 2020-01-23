// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.base.http;

import com.alo7.archetype.base.json.JsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.assertj.core.util.Preconditions;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kelin Tan
 */
public class HttpRequest {
    private URIBuilder uriBuilder;
    private Map<String, Object> headers = new LinkedHashMap<>();
    private String content;
    private HttpRequestBase request;
    private CloseableHttpResponse response;

    private HttpRequest(String path) {
        this.uriBuilder = HttpUtils.safeURIBuilder(path);
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

    public HttpRequest get() {
        this.request = new HttpGet(build());
        return this;
    }

    public HttpRequest post() {
        HttpPost post = new HttpPost(build());

        buildEntity(post);

        this.request = post;
        return this;
    }

    public HttpRequest delete() {
        this.request = new HttpDelete(HttpUtils.safeURI(this.uriBuilder));
        return this;
    }

    public HttpRequest put() {
        HttpPut put = new HttpPut(build());

        buildEntity(put);

        this.request = put;
        return this;
    }

    public HttpRequest patch() {
        HttpPatch patch = new HttpPatch(build());

        buildEntity(patch);

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
        Preconditions.checkNotNull(this.request);

        if (!headers.isEmpty()) {
            headers.forEach((header, value) -> request.addHeader(header, value.toString()));
        }
        this.response = HttpUtils.safeExecute(this.request);
        return this;
    }

    public CloseableHttpResponse response() {
        Preconditions.checkNotNull(this.response);

        return response;
    }

    public int status() {
        Preconditions.checkNotNull(this.response);

        return response.getStatusLine().getStatusCode();
    }

    public boolean isOk() {
        Preconditions.checkNotNull(this.response);

        return HttpUtils.isHttpOk(status());
    }

    public boolean isBadRequest() {
        Preconditions.checkNotNull(this.response);

        return HttpUtils.isHttpBadRequest(status());
    }

    public boolean isErrorRequest() {
        Preconditions.checkNotNull(this.response);

        return HttpUtils.isHttpErrorRequest(status());
    }

    public JsonNode json() {
        Preconditions.checkNotNull(this.response);

        String entity = HttpUtils.safeEntityToString(this.response.getEntity());
        return JsonConverter.readTree(entity);
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

    private void buildEntity(HttpEntityEnclosingRequestBase entityRequest) {
        if (content != null) {
            entityRequest.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
        }
    }

    private URI build() {
        return HttpUtils.safeURI(this.uriBuilder);
    }
}
