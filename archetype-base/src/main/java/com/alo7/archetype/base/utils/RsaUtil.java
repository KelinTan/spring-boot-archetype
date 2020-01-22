// Copyright 2020 Alo7 Inc. All rights reserved.

package com.alo7.archetype.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Kelin Tan
 */
@Slf4j
public class RsaUtil {
    private static final String RSA = "RSA";

    public static KeyPair generatorKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(RSA);
        keyGenerator.initialize(1024);
        return keyGenerator.genKeyPair();
    }

    public static String encodePublicKey(KeyPair keyPair) {
        return base64Encode(keyPair.getPublic().getEncoded());
    }

    public static String encodePrivateKey(KeyPair keyPair) {
        return base64Encode(keyPair.getPrivate().getEncoded());
    }

    public static PublicKey decode2PublicKey(String publicKeyString) throws InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
        return getKeyFactoryInstance().generatePublic(keySpec);
    }

    public static PrivateKey decode2PrivateKey(String privateKeyString) throws InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));

        return getKeyFactoryInstance().generatePrivate(keySpec);
    }

    public static String encrypt(String src, String publicKeyString) throws Exception {
        return encrypt(src, decode2PublicKey(publicKeyString));
    }

    public static String encrypt(String src, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(src.getBytes(Charsets.UTF_8)));
    }

    public static String decrypt(String encryptContent, String privateKeyString) throws Exception {
        return decrypt(encryptContent, decode2PrivateKey(privateKeyString));
    }

    public static String decrypt(String encryptContent, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return StringUtils.toEncodedString(cipher.doFinal(base64decode(encryptContent)), Charsets.UTF_8);
    }

    private static byte[] base64decode(String src) {
        return Base64.getDecoder().decode(src);
    }

    private static String base64Encode(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

    private static KeyFactory getKeyFactoryInstance() {
        try {
            return KeyFactory.getInstance(RSA);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
