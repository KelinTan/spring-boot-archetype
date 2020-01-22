// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.base.rest.exception;

import com.google.common.base.Preconditions;
import org.springframework.http.HttpStatus;

/**
 * @author Kelin Tan
 */
public class RestExceptionFactory {
    private RestExceptionFactory() {
    }

    public static RestException toBadRequestException(int errorCode, String message) {
        return toException(HttpStatus.BAD_REQUEST, errorCode, message);
    }

    public static RestException toBadRequestException(int errorCode) {
        return toException(errorCode, HttpStatus.BAD_REQUEST);
    }

    public static RestException toSystemException(int errorCode, String message) {
        return toException(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, message);
    }

    public static RestException toSystemException(int errorCode) {
        return toException(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static RestException toSystemException() {
        return toException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static RestException toNotFoundException(int errorCode, String message) {
        return toException(HttpStatus.NOT_FOUND, errorCode, message);
    }

    public static RestException toNotFoundException(int errorCode) {
        return toException(errorCode, HttpStatus.NOT_FOUND);
    }

    public static RestException toForbiddenException(int errorCode, String message) {
        return toException(HttpStatus.FORBIDDEN, errorCode, message);
    }

    public static RestException toForbiddenException(int errorCode) {
        return toException(errorCode, HttpStatus.FORBIDDEN);
    }

    public static RestException toUnAuthorizedException(int errorCode, String message) {
        return toException(HttpStatus.UNAUTHORIZED, errorCode, message);
    }

    public static RestException toUnAuthorizedException(int errorCode) {
        return toException(errorCode, HttpStatus.UNAUTHORIZED);
    }

    public static RestException toMethodNotAllowedException(int errorCode, String message) {
        return toException(HttpStatus.METHOD_NOT_ALLOWED, errorCode, message);
    }

    public static RestException toMethodNotAllowedException(int errorCode) {
        return toException(errorCode, HttpStatus.METHOD_NOT_ALLOWED);
    }

    public static RestException toConflictException(int errorCode, String message) {
        return toException(HttpStatus.CONFLICT, errorCode, message);
    }

    public static RestException toConflictException(int errorCode) {
        return toException(errorCode, HttpStatus.CONFLICT);
    }

    public static RestException toRpcException() {
        return toException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Rpc Request Error");
    }

    public static RestException toException(int errorCode, HttpStatus httpStatus) {
        Preconditions.checkNotNull(httpStatus);

        return new RestException(httpStatus, errorCode);
    }

    public static RestException toException(HttpStatus httpStatus) {
        Preconditions.checkNotNull(httpStatus);

        return new RestException(httpStatus);
    }

    public static RestException toException(HttpStatus status, int errorCode, String message) {
        Preconditions.checkNotNull(status);

        return new RestException(status, errorCode, message);
    }
}
