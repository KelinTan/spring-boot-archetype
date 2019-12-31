// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.rpc;

import com.alo7.archetype.log.LogMessageBuilder;
import com.alo7.archetype.rest.exception.RestExceptionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Kelin Tan
 */
@Slf4j
public class DefaultRpcErrorHandler implements RpcErrorHandler {
    @Override
    public void handle(int status, String errorResponse) {
        log.error(LogMessageBuilder.builder()
                .message("Rpc request error")
                .parameter("status", status)
                .parameter("response", errorResponse)
                .build());
        throw RestExceptionFactory.toRpcException();
    }
}
