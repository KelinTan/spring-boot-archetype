// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.jwt;

import com.auth0.jwt.interfaces.RSAKeyProvider;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author Kelin Tan
 */
@EnableConfigurationProperties(JwtProperties.class)
public class JwtRSAKeyProvider implements RSAKeyProvider {
    @Getter
    private final JwtProperties jwtProperties;

    public JwtRSAKeyProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

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
