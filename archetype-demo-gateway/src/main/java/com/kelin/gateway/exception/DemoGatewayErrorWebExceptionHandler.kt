// Copyright 2022 Alo7 Inc. All rights reserved.

package com.kelin.gateway.exception

import com.kelin.archetype.beans.rest.RestErrorResponse
import com.kelin.archetype.beans.rest.RestResponseFactory
import com.kelin.archetype.beans.exception.RestException
import org.springframework.boot.autoconfigure.web.ErrorProperties
import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

/**
 * @author Kelin Tan
 */
class DemoGatewayErrorWebExceptionHandler(
    errorAttributes: ErrorAttributes,
    resourceProperties: ResourceProperties,
    errorProperties: ErrorProperties,
    applicationContext: ApplicationContext
) : DefaultErrorWebExceptionHandler(
    errorAttributes, resourceProperties, errorProperties, applicationContext
) {
    override fun renderErrorView(request: ServerRequest): Mono<ServerResponse> {
        return renderErrorResponse(request)
    }

    override fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        val error = this.getError(request)

        val errorStatus = determineHttpStatus(error)
        val errorResponse = determineError(errorStatus, error)
        return ServerResponse.status(errorStatus)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(errorResponse))
            .doOnNext { logError(request, it, error) }
    }

    private fun determineError(httpStatus: HttpStatus, error: Throwable): RestErrorResponse {
        if (error is RestException) {
            return RestResponseFactory.error(error.errorCode, error.message)
        }
        return when {
            httpStatus == HttpStatus.NOT_FOUND -> {
                RestResponseFactory.error(HttpStatus.NOT_FOUND.value(), "Gateway not found")
            }
            httpStatus.is4xxClientError -> {
                RestResponseFactory.error(HttpStatus.BAD_REQUEST.value(), "Gateway bad request")
            }
            httpStatus.is5xxServerError -> {
                RestResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Gateway system error")
            }
            else -> {
                RestResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Gateway unknown error")
            }
        }
    }

    private fun determineHttpStatus(error: Throwable): HttpStatus {
        if (error is ResponseStatusException) {
            return error.status
        }
        val responseStatus = AnnotatedElementUtils
            .findMergedAnnotation(error.javaClass, ResponseStatus::class.java)
        return responseStatus?.code ?: HttpStatus.INTERNAL_SERVER_ERROR
    }
}