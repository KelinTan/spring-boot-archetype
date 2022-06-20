// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.rpc;

import org.apache.http.HttpStatus;

/**
 * @author Kelin Tan
 */
public class RpcExceptionFactory {
    private RpcExceptionFactory() {
    }

    public static RpcException toException(String serviceName, String method, String message) {
        return toException(serviceName, method, HttpStatus.SC_INTERNAL_SERVER_ERROR, "", message);
    }

    public static RpcException toException(String service, String method, int status, String response,
            String message) {
        return new RpcException(service, method, status, response, message);
    }
}
