// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Kelin Tan
 */
@ConfigurationProperties(prefix = "client.endpoint")
@Data
public class ClientConfig {
    private String demo;
}
