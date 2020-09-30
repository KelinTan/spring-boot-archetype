// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.service;

import com.kelin.archetype.database.entity.primary.User;
import com.kelin.archetype.database.mapper.primary.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kelin Tan
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findCacheUserById(Long id) {
        return findUserById(id);
    }

    @Override
    public User findUserById(Long id) {
        return userMapper.findOne(id);
    }
}
