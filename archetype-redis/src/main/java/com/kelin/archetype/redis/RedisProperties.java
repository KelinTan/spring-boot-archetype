// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Kelin Tan
 */
@Data
@ConfigurationProperties("redis")
public class RedisProperties {
    private String host = "127.0.0.1";
    private int port = 6379;
    private int timeoutSeconds = 10;
}
