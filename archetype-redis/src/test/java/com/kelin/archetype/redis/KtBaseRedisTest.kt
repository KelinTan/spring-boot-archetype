// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.redis

import com.kelin.archetype.test.KtBaseSpringTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import redis.embedded.RedisServer

/**
 * @author Kelin Tan
 */
abstract class KtBaseRedisTest : KtBaseSpringTest() {
    companion object {
        private var redisServer: RedisServer? = null

        @BeforeAll
        @JvmStatic
        fun setUp() {
            redisServer = RedisServer()
            redisServer!!.start()
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            redisServer?.stop()
        }
    }
}