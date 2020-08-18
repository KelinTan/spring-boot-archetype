// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rest.exception;

import com.kelin.archetype.core.rest.response.RestErrorResponse;
import com.kelin.archetype.core.rest.response.RestResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author Kelin Tan
 */
@ControllerAdvice
@Slf4j
@ResponseBody
public class RestExceptionHandler {
    @ExceptionHandler(value = RestException.class)
    public ResponseEntity<RestErrorResponse> handleRestException(RestException e) {
        RestErrorResponse errorResponse = RestErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, e.getStatus());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
            HttpMediaTypeNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestErrorResponse handleBadRequestException(Exception e) {
        return handleException(e, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public RestErrorResponse handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return handleException(e, HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse defaultExceptionHandler(Exception e) {
        return handleException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private RestErrorResponse handleException(Exception e, int errorCode) {
        log.error("Handle Exception : ", e);

        return RestResponseFactory.error(errorCode, e.getLocalizedMessage());
    }
}
