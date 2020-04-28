// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.client;

import com.kelin.archetype.base.rpc.RpcErrorHandler;

/**
 * @author Kelin Tan
 */
public class UserClientErrorHandler implements RpcErrorHandler {
    @Override
    public void handle(int status, String errorResponse) {
        //just simply throw exception to check custom error handler
        throw new IllegalArgumentException(errorResponse);
    }
}
