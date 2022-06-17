// Copyright 2022 Kelin Inc. All rights reserved.

package com.kelin.gateway.config

import com.kelin.archetype.client.service.AccountApiService
import com.kelin.gateway.exception.DemoGatewayErrorWebExceptionHandler
import com.kelin.gateway.filter.GatewayGlobalSessionFilter
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import java.util.stream.Collectors


/**
 * @author Kelin Tan
 */
@Configuration
class DemoGatewayConfig {
    @Bean
    fun gatewayRouteLocator(builder: RouteLocatorBuilder, routeConfig: RouteConfig): RouteLocator {
        var routeBuilder = builder.routes()
        routeConfig.commonRoutes.forEach {
            routeBuilder = routeBuilder.route { predicateSpec ->
                predicateSpec.path(it.path)
                    .filters {
                        it.stripPrefix(1)
                    }
                    .uri(it.url)
            }
        }
        return routeBuilder.build()
    }

    @Bean
    fun gatewayGlobalFilter(accountApiService: AccountApiService): GlobalFilter {
        return GatewayGlobalSessionFilter(accountApiService)
    }

    @Bean
    fun corsFilter(): CorsWebFilter {
        return CorsWebFilter(UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues().apply {
                allowCredentials = true
                addAllowedOrigin(CorsConfiguration.ALL)
                addAllowedMethod(CorsConfiguration.ALL)
                addAllowedHeader(CorsConfiguration.ALL)
                maxAge = 3600
            })
        })
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun gatewayErrorHandler(
        errorAttributes: ErrorAttributes,
        resourceProperties: ResourceProperties,
        serverProperties: ServerProperties,
        applicationContext: ApplicationContext,
        serverCodecConfigurer: ServerCodecConfigurer
    ): ErrorWebExceptionHandler {
        return DemoGatewayErrorWebExceptionHandler(
            errorAttributes,
            resourceProperties,
            serverProperties.error,
            applicationContext
        ).apply {
            setMessageWriters(serverCodecConfigurer.writers)
            setMessageReaders(serverCodecConfigurer.readers)
        }
    }

    @Bean
    @ConditionalOnMissingBean
    fun messageConverters(converters: ObjectProvider<HttpMessageConverter<*>>): HttpMessageConverters {
        return HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()))
    }
}