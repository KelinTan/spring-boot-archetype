// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Kelin Tan
 */
public class RestException extends RuntimeException {
    /**
     * http status {@link org.springframework.http.HttpStatus}
     */
    @Getter
    private final HttpStatus status;
    /**
     * biz errorCode
     */
    @Getter
    private final int errorCode;

    public RestException(HttpStatus httpStatus) {
        super(httpStatus.getReasonPhrase());
        this.status = httpStatus;
        this.errorCode = httpStatus.value();
    }

    public RestException(HttpStatus httpStatus, int errorCode) {
        super(httpStatus.getReasonPhrase());
        this.errorCode = errorCode;
        this.status = httpStatus;
    }

    public RestException(HttpStatus httpStatus, int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = httpStatus;
    }
}
