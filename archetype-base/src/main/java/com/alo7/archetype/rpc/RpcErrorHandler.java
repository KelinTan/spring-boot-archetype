// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.rpc;

/**
 * @author Kelin Tan
 */
public interface RpcErrorHandler {
    void handle(int status, String errorResponse);
}
