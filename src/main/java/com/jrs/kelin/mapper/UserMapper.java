// Copyright 2019 Alo7 Inc. All rights reserved.

package com.jrs.kelin.mapper;

import com.jrs.kelin.entity.User;

import java.util.List;

/**
 * @author Kelin Tan
 */
public interface UserMapper {
    List<User> findAll();

    Long insert(User user);

    User findById(Long id);
}
