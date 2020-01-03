// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.rest.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Kelin Tan
 */
public class RestException extends RuntimeException {
    /**
     * http status {@link org.springframework.http.HttpStatus}
     */
    @Getter
    @Setter
    private HttpStatus status;
    /**
     * biz errorCode
     */
    @Getter
    @Setter
    private int errorCode;

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
