// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.base.jwt;

import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.kelin.archetype.base.utils.RsaUtil;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author Kelin Tan
 */
@Data
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class JwtRSAKeyProvider implements RSAKeyProvider {
    private final JwtProperties jwtProperties;

    @SneakyThrows
    @Override
    public RSAPublicKey getPublicKeyById(String keyId) {
        return (RSAPublicKey) RsaUtil.decode2PublicKey(jwtProperties.getPublicKey());
    }

    @SneakyThrows
    @Override
    public RSAPrivateKey getPrivateKey() {
        return (RSAPrivateKey) RsaUtil.decode2PrivateKey(jwtProperties.getPrivateKey());
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}
