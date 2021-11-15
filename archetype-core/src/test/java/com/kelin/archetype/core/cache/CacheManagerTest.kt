// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.cache

import com.kelin.archetype.core.cache.DemoService.CACHE_NAME
import com.kelin.archetype.test.KtBaseSpringTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Import

/**
 * @author Kelin Tan
 */
@Import(DemoServiceImpl::class)
class CacheManagerTest : KtBaseSpringTest() {
    @Autowired
    lateinit var cacheManager: CacheManager

    @Autowired
    lateinit var demoService: DemoService

    @AfterEach
    fun clear() {
        cacheManager.getCache(CACHE_NAME)?.clear()
    }

    @Test
    fun `test caffeine manager autowired`() {
        cacheManager not null

        cacheManager as CaffeineCacheManager not null
    }

    @Test
    fun `test cache max size`() {
        (0 until 2001).map {
            demoService.insert(Demo().apply {
                id = it
            })
        }

        cacheManager.getCache(CACHE_NAME)?.let {
            it.get(1999) not null
        }

        demoService.insert(Demo().apply {
            id = 2000
        })

        cacheManager.getCache(CACHE_NAME)?.let {
            it.get(2000) not null
        }

    }

    @Test
    fun `test cache annotation`() {
        demoService.getDemo(9999) eq null

        demoService.getDemo(1) verify {
            id eq 1
        }

        cacheManager.getCache(CACHE_NAME)?.let {
            it.get(1) not null
        }

        demoService.delete(1)

        cacheManager.getCache(CACHE_NAME)?.let {
            it.get(1) eq null
        }

        cacheManager.getCache(CACHE_NAME)?.let {
            it.get(2) eq null
        }

        demoService.insert(Demo().apply {
            id = 2
            name = "name_2"
        })

        cacheManager.getCache(CACHE_NAME)?.let {
            it.get(2) not null
        }
    }
}