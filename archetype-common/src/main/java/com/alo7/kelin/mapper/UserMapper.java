// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.mapper;

import com.alo7.kelin.entity.User;

import java.util.List;

/**
 * @author Kelin Tan
 */
public interface UserMapper {
    List<User> findAll();

    Long insert(User user);

    User findById(Long id);

    void deleteById(Long id);
}
