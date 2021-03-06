// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.exception;

import com.kelin.archetype.common.beans.RestErrorResponse;
import com.kelin.archetype.common.beans.RestResponseFactory;
import com.kelin.archetype.common.log.LogMessageBuilder;
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
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author Kelin Tan
 */
@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RestException.class)
    public ResponseEntity<RestErrorResponse> handleRestException(RestException e) {
        RestErrorResponse errorResponse = RestErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, e.getStatus());
    }

    @ExceptionHandler(value = RpcException.class)
    public ResponseEntity<RestErrorResponse> handleRpcException(RpcException e) {
        log.error(LogMessageBuilder.builder()
                .message("Rpc failed")
                .parameter("serviceName", e.getServiceName())
                .parameter("method", e.getMethod())
                .parameter("status", e.getStatus())
                .parameter("response", e.getResponse())
                .build());
        RestErrorResponse errorResponse = RestErrorResponse.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage("Rpc failed: " + e.getServiceName() + "." + e.getMethod())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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

    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestErrorResponse handleNotFoundException(Exception e) {
        return handleException(e, HttpStatus.NOT_FOUND.value());
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
