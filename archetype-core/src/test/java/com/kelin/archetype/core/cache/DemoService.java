// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author Kelin Tan
 */
public interface DemoService {
    String CACHE_NAME = "DemoCache";

    @Cacheable(value = CACHE_NAME, key = "#id")
    Demo getDemo(int id);

    @CachePut(value = CACHE_NAME, key = "#demo.id")
    Demo insert(Demo demo);

    @CacheEvict(value = CACHE_NAME, key = "#id")
    void delete(int id);
}
