// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Kelin Tan
 */
@ConfigurationProperties(prefix = "client.endpoint")
@Data
public class ClientConfig {
    private String user;
}
