// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.jwt;

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
