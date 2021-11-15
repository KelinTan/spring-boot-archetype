// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.redis

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author Kelin Tan
 */
class RedisServiceTest : KtBaseRedisTest() {
    @Autowired
    lateinit var redisService: RedisService

    @BeforeEach
    fun reset() {
        redisService.sync().flushall()
    }

    @Test
    fun `test redis init`() {
        redisService.sync().get("test") eq null
    }

    @Test
    fun `test redis sync get and set`() {
        val key = "key"
        val value = "value"
        redisService.sync().set(key, value)
        redisService.sync().get(key) eq value
    }

    @Test
    fun `test redis sync expire`() {
        val key = "key"
        val value = "value"
        redisService.sync().set(key, value)

        redisService.sync().expire(key, 10)
        redisService.sync().ttl(key) eq 10
    }

    @Test
    fun `test redis async get and set`() {
        val key = "key2"
        val value = "value"
        redisService.async().set(key, value)
        redisService.async().get(key) verify {
            get() eq value
        }
    }
}