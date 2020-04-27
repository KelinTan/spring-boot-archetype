// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.base.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.security.KeyPair;

/**
 * @author Kelin Tan
 */
public class RsaUtilTest {
    @Test
    public void testRsa() throws Exception {
        String src = "test";

        KeyPair keyPair = RsaUtil.generatorKeyPair();

        String privateKey = RsaUtil.encodePrivateKey(keyPair);
        String publicKey = RsaUtil.encodePublicKey(keyPair);

        String encrypt = RsaUtil.encrypt(src, publicKey);

        String decrypt = RsaUtil.decrypt(encrypt, privateKey);

        assertEquals(decrypt, src);
    }
}
