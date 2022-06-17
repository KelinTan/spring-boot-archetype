// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.beans.exception;

import lombok.Getter;

/**
 * @author Kelin Tan
 */
public class RestException extends RuntimeException {
    /**
     * http status
     */
    @Getter
    private final int status;

    /**
     * biz errorCode
     */
    @Getter
    private final int errorCode;

    public RestException(int httpStatus, String message) {
        super(message);
        this.status = httpStatus;
        this.errorCode = httpStatus;
    }

    public RestException(int httpStatus, String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.status = httpStatus;
    }
}
