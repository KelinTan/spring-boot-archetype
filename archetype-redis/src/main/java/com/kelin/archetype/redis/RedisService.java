// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.redis;

import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

/**
 * @author Kelin Tan
 */
public class RedisService {
    private final RedisCommands<String, String> sync;
    private final RedisAsyncCommands<String, String> async;

    public RedisService(RedisCommands<String, String> sync, RedisAsyncCommands<String, String> async) {
        this.sync = sync;
        this.async = async;
    }

    public RedisCommands<String, String> sync() {
        return this.sync;
    }

    public RedisAsyncCommands<String, String> async() {
        return this.async;
    }
}
