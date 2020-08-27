// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

import com.kelin.archetype.common.log.LogMessageBuilder;
import com.kelin.archetype.common.rest.exception.RestExceptionFactory;
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
