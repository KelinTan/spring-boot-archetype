// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.redis

import com.kelin.archetype.test.KtBaseSpringTest
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Ignore
import redis.embedded.RedisServer

/**
 * @author Kelin Tan
 */
@Ignore
open class KtBaseRedisTest : KtBaseSpringTest() {
    companion object {
        private var redisServer: RedisServer? = null

        @BeforeClass
        @JvmStatic
        fun setUp() {
            redisServer = RedisServer()
            redisServer!!.start()
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            redisServer?.stop()
        }
    }
}