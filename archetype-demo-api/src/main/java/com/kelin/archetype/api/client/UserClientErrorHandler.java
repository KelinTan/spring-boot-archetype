// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.client;

import com.kelin.archetype.core.rpc.RpcErrorHandler;

/**
 * @author Kelin Tan
 */
public class UserClientErrorHandler implements RpcErrorHandler {
    @Override
    public void handle(int status, String errorResponse, String serviceName, String method) {
        //just simply throw exception to check custom error handler
        throw new IllegalArgumentException(errorResponse);
    }
}
