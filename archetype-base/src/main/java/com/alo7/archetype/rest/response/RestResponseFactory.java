// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.rest.response;

import java.util.List;

/**
 * @author Kelin Tan
 */
public class RestResponseFactory {
    private RestResponseFactory() {
    }

    public static RestResponse emptyResponse() {
        return success(null);
    }

    public static RestErrorResponse error(int errorCode, String message) {
        return RestErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(message)
                .build();
    }

    public static <T> RestResponse<T> success(T result) {
        return RestResponse.<T>builder()
                .result(result)
                .build();
    }

    public static <T> RestPageResponse<T> success(int pageNo, int pageSize, int totalCount, List<T> results) {
        return RestPageResponse.<T>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalCount(totalCount)
                .results(results)
                .build();
    }
}
