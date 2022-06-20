// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.rpc;

import lombok.Getter;

/**
 * @author Kelin Tan
 */
public class RpcException extends RuntimeException {
    @Getter
    private String serviceName;
    @Getter
    private String method;
    @Getter
    private int status;
    @Getter
    private String response;

    public RpcException(String serviceName, String method, int status, String response, String message) {
        super(message);
        this.serviceName = serviceName;
        this.method = method;
        this.status = status;
        this.response = response;
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(Throwable throwable) {
        super(throwable);
    }

    public RpcException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
