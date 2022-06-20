// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.rpc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Kelin Tan
 */
@Slf4j
public class DefaultRpcErrorHandler implements RpcErrorHandler {
    @Override
    public void handle(int status, String errorResponse, String serviceName, String method) {
        throw RpcExceptionFactory.toException(serviceName, method, status, errorResponse, "");
    }
}
