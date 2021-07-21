// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.service;

import com.kelin.archetype.database.entity.primary.User;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author Kelin Tan
 */
public interface BizService {
    void updateWithPrimaryTransaction();

    void updateWithBizTransaction();

    void updateWithMultipleTransaction();

    @Cacheable(value = "UserCache", key = "#id")
    User findCacheUserById(Long id);

    User findUserById(Long id);
}
