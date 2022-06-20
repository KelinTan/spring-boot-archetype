// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.exception;

import com.kelin.archetype.beans.exception.RestException;
import org.apache.http.HttpStatus;

/**
 * @author Kelin Tan
 */
public class RestExceptionFactory {
    private RestExceptionFactory() {
    }

    public static RestException toBadRequestException(int errorCode, String message) {
        return toException(HttpStatus.SC_BAD_REQUEST, errorCode, message);
    }

    public static RestException toBadRequestException(int errorCode) {
        return toException(errorCode, HttpStatus.SC_BAD_REQUEST, "");
    }

    public static RestException toSystemException(int errorCode, String message) {
        return toException(HttpStatus.SC_INTERNAL_SERVER_ERROR, errorCode, message);
    }

    public static RestException toSystemException(String message) {
        return toException(HttpStatus.SC_INTERNAL_SERVER_ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR, message);
    }

    public static RestException toSystemException(int errorCode) {
        return toException(errorCode, HttpStatus.SC_INTERNAL_SERVER_ERROR, "");
    }

    public static RestException toSystemException() {
        return toSystemException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    public static RestException toNotFoundException(int errorCode, String message) {
        return toException(HttpStatus.SC_NOT_FOUND, errorCode, message);
    }

    public static RestException toNotFoundException(int errorCode) {
        return toException(errorCode, HttpStatus.SC_NOT_FOUND, "");
    }

    public static RestException toForbiddenException(int errorCode, String message) {
        return toException(HttpStatus.SC_FORBIDDEN, errorCode, message);
    }

    public static RestException toForbiddenException(int errorCode) {
        return toException(errorCode, HttpStatus.SC_FORBIDDEN, "");
    }

    public static RestException toUnAuthorizedException(int errorCode, String message) {
        return toException(HttpStatus.SC_UNAUTHORIZED, errorCode, message);
    }

    public static RestException toUnAuthorizedException(int errorCode) {
        return toException(errorCode, HttpStatus.SC_UNAUTHORIZED, "");
    }

    public static RestException toMethodNotAllowedException(int errorCode, String message) {
        return toException(HttpStatus.SC_METHOD_NOT_ALLOWED, errorCode, message);
    }

    public static RestException toMethodNotAllowedException(int errorCode) {
        return toException(errorCode, HttpStatus.SC_METHOD_NOT_ALLOWED, "");
    }

    public static RestException toConflictException(int errorCode, String message) {
        return toException(HttpStatus.SC_CONFLICT, errorCode, message);
    }

    public static RestException toConflictException(int errorCode) {
        return toException(errorCode, HttpStatus.SC_CONFLICT, "");
    }

    public static RestException toRpcException() {
        return toException(HttpStatus.SC_INTERNAL_SERVER_ERROR, HttpStatus.SC_SERVICE_UNAVAILABLE,
                "Rpc Request Error");
    }

    public static RestException toException(int httpStatus, int errorCode, String errorMessage) {
        return new RestException(httpStatus, errorMessage, errorCode);
    }
}
