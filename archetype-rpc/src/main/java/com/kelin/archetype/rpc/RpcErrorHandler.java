// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.rpc;

/**
 * @author Kelin Tan
 */
public interface RpcErrorHandler {
    void handle(int status, String errorResponse, String serviceName, String method);
}
