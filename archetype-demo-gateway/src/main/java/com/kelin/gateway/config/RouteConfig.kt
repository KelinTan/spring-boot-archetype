// Copyright 2022 Kelin Inc. All rights reserved.

package com.kelin.gateway.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * @author Kelin Tan
 */
@ConfigurationProperties(prefix = "route")
@Configuration
class RouteConfig {
    var commonRoutes: List<CommonRouteConfig> = listOf()
}