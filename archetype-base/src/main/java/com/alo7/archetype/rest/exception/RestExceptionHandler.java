// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.rest.exception;

import com.alo7.archetype.rest.response.RestErrorResponse;
import com.alo7.archetype.rest.response.RestResponseFactory;
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
        return handleException(e, GlobalErrorCode.BAD_REQUEST_ERROR);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public RestErrorResponse handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return handleException(e, GlobalErrorCode.BAD_REQUEST_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse defaultExceptionHandler(Exception e) {
        return handleException(e, GlobalErrorCode.SYSTEM_ERROR);
    }

    private RestErrorResponse handleException(Exception e, int errorCode) {
        log.error("Handle Exception : ", e);

        return RestResponseFactory.error(errorCode, e.getLocalizedMessage());
    }
}
