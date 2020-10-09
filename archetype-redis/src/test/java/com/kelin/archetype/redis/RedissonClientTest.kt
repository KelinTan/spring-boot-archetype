// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.redis

import org.junit.Test
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * @author Kelin Tan
 */
class RedissonClientTest : KtBaseRedisTest() {
    @Autowired
    lateinit var redissonClient: RedissonClient

    @Test
    fun `test lock`() {
        val lockName = "lock"
        val lock = redissonClient.getLock(lockName)
        lock.lock()
        lock.unlock()
    }

    @Test
    fun `test try lock`() {
        val lockName = "lock2"
        val lock = redissonClient.getLock(lockName)

        lock.tryLock(5, TimeUnit.SECONDS) eq true

        thread {
            val lock2 = redissonClient.getLock(lockName)
            lock2.tryLock() eq false
        }

        val lock3 = redissonClient.getLock(lockName)
        lock3.tryLock() eq true
    }

    @Test
    fun `test lock wait`() {
        val lockName = "lock2"
        val lock = redissonClient.getLock(lockName)

        lock.tryLock(5, 5, TimeUnit.SECONDS) eq true

        thread {
            val lock2 = redissonClient.getLock(lockName)
            lock2.tryLock(1, 10, TimeUnit.SECONDS) eq false

            sleep(5000)

            lock2.tryLock(1, 10, TimeUnit.SECONDS) eq true
        }
    }
}