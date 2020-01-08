// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.jwt;

import com.auth0.jwt.interfaces.RSAKeyProvider;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Kelin Tan
 */
@Data
@Slf4j
public class CustomRSAKeyProvider implements RSAKeyProvider {
    @Value("${jwt.key.public}")
    private String publicKeyString;
    @Value("${jwt.key.private}")
    private String privateKeyString;

    @Override
    public RSAPublicKey getPublicKeyById(String keyId) {
        return buildRSAPublicKey(publicKeyString);
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return buildRSAPrivateKey(privateKeyString);
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }

    private RSAPublicKey buildRSAPublicKey(String publicKey) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        try {
            return (RSAPublicKey) getKeyFactoryInstance().generatePublic(x509EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            log.error("invalid public key: ", e);
            return null;
        }
    }

    private RSAPrivateKey buildRSAPrivateKey(String privateKey) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        try {
            return (RSAPrivateKey) getKeyFactoryInstance().generatePrivate(pkcs8EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            log.error("invalid private key: ", e);
            return null;
        }
    }

    private static KeyFactory getKeyFactoryInstance() {
        try {
            return KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            log.error("key factory: ", e);
            return null;
        }
    }
}
