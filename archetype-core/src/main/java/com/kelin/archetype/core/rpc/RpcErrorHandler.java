// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

/**
 * @author Kelin Tan
 */
public interface RpcErrorHandler {
    void handle(int status, String errorResponse, String serviceName, String method);
}
