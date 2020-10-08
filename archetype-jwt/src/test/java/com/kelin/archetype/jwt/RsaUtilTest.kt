// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.jwt

import com.kelin.archetype.test.KtTestUtils
import org.junit.Test

/**
 * @author Kelin Tan
 */
class RsaUtilTest : KtTestUtils {
    @Test
    @Throws(Exception::class)
    fun testRsa() {
        val src = "test"
        val keyPair = RsaUtil.generatorKeyPair()
        val privateKey = RsaUtil.encodePrivateKey(keyPair)
        val publicKey = RsaUtil.encodePublicKey(keyPair)
        val encrypt = RsaUtil.encrypt(src, publicKey)
        val decrypt = RsaUtil.decrypt(encrypt, privateKey)
        decrypt eq src
    }
}