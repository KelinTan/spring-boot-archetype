// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller;

import com.kelin.archetype.common.rest.response.RestResponse;
import com.kelin.archetype.common.rest.response.RestResponseFactory;
import com.kelin.archetype.database.entity.primary.User;
import com.kelin.archetype.database.mapper.primary.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Kelin Tan
 */
@RestController
@RequestMapping("/api/v2/user")
public class UserApiControllerV2 {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/findAll")
    public RestResponse<List<User>> findAll() {
        return RestResponseFactory.success(userMapper.findAll());
    }
}
