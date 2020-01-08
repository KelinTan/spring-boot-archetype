// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.jwt;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kelin Tan
 */
@Slf4j
public class KeyPairHelper {
    /**
     * generate rsa public & private key
     */
    public static Map<String, String> generatorRSA() {
        KeyPairGenerator keyGenerator;
        try {
            keyGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            log.error("no such algorithm error:", e);
            return Collections.emptyMap();
        }
        keyGenerator.initialize(1024);

        KeyPair keyPair = keyGenerator.genKeyPair();
        Map<String, String> map = new HashMap<>();
        map.put("public", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        map.put("private", Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return map;
    }
}
