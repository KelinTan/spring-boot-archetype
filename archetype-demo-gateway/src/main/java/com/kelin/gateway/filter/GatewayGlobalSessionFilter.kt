// Copyright 2022 Kelin Alo7 Inc. All rights reserved.

package com.kelin.gateway.filter

import com.kelin.archetype.beans.constants.GatewayConstant
import com.kelin.archetype.beans.exception.RestException
import com.kelin.archetype.client.service.AccountApiService
import com.kelin.gateway.exception.GatewayErrorCode
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * @author Kelin Tan
 */
class GatewayGlobalSessionFilter(
    private val accountApiService: AccountApiService,
) : GlobalFilter, Ordered {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val path = exchange.request.path.pathWithinApplication().value()
        log.info("$path: gateway session filter")
        if (SESSION_BLACK.contains(path)) {
            return chain.filter(exchange).then()
        }
        val authorizationHeader = exchange.request.headers.getFirst(AUTHORIZATION_HEADER_NAME)
        if (authorizationHeader != null && authorizationHeader.isNotEmpty() && authorizationHeader.startsWith(
                AUTHORIZATION_BEARER
            )
        ) {
            val rawToken = authorizationHeader.replace(AUTHORIZATION_BEARER, "")

            try {
                val account = accountApiService.verifyToken(rawToken)
                val request = exchange.request
                    .mutate()
                    .header(GatewayConstant.GATEWAY_ACCOUNT_HEADER, account.account)
                    .build()
                val modifyExchange = exchange.mutate().request(request).build()
                return chain.filter(modifyExchange).then()
            } catch (e: Exception) {
                log.error("jwt verify error", e)
            }
        }

        throw RestException(
            HttpStatus.UNAUTHORIZED.value(),
            GatewayErrorCode.SESSION_EXPIRE.errorMessage,
            GatewayErrorCode.SESSION_EXPIRE.errorCode
        )
    }

    override fun getOrder(): Int {
        return HIGHEST_PRECEDENCE
    }

    companion object {
        const val AUTHORIZATION_BEARER = "Bearer "
        const val AUTHORIZATION_HEADER_NAME = "Authorization"
        val SESSION_BLACK = setOf(
            "/demo/api/v1/account/login"
        )

        private val log = LoggerFactory.getLogger(this::class.java)
    }
}