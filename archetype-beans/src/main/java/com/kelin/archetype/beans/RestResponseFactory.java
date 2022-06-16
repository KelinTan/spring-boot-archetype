// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.beans;

import java.util.List;
import java.util.Map;

/**
 * @author Kelin Tan
 */
public class RestResponseFactory {
    private RestResponseFactory() {
    }

    public static <T> RestResponse<T> emptyResponse() {
        return success(null);
    }

    public static RestErrorResponse error(int errorCode, String message) {
        return RestErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(message)
                .build();
    }

    public static RestErrorResponse error(int errorCode, String message, Map<String, Object> meta) {
        return RestErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(message)
                .meta(meta)
                .build();
    }

    public static <T> RestResponse<T> success(T result) {
        return RestResponse.<T>builder()
                .result(result)
                .build();
    }

    public static <T> RestPageResponse<T> successPage(int pageNo, int pageSize, long totalCount, List<T> results) {
        return RestPageResponse.<T>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalCount(totalCount)
                .results(results)
                .build();
    }
}
