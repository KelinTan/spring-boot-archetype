// Copyright 2020 Alo7 Inc. All rights reserved.

package com.alo7.archetype.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Kelin Tan
 */
@Data
@ConfigurationProperties("jwt.key")
public class JwtProperties {
    private String publicKey;
    private String privateKey;
}
