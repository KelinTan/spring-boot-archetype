// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.cache

import com.github.benmanes.caffeine.cache.Caffeine
import com.kelin.archetype.test.KtTestUtils
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * @author Kelin Tan
 */
class CacheTest : KtTestUtils {
    var cache = Caffeine.newBuilder()
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .initialCapacity(100)
        .maximumSize(1000)
        .build<Int, Demo>()

    @Test
    fun `test caffeine cache`() {
        cache.getIfPresent(1) eq null

        cache.get(1) {
            return@get Demo().apply {
                id = it
                name = "test"
            }
        } verify {
            id eq 1
            name eq "test"
        }

        cache.getIfPresent(1) not null
    }
}