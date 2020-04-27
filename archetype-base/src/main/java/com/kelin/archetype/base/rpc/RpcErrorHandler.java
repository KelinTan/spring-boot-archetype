// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.base.rpc;

/**
 * @author Kelin Tan
 */
public interface RpcErrorHandler {
    void handle(int status, String errorResponse);
}
