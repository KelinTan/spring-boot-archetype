// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;

import com.alo7.archetype.persistence.entity.primary.User;
import com.alo7.archetype.persistence.mapper.primary.UserMapper;
import com.alo7.archetype.rest.response.RestResponse;
import com.alo7.archetype.session.SessionAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Kelin Tan
 */
@RestController
@RequestMapping("/api/v1/user/auth/")
public class UserApiAuthController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/findAll")
    @SessionAuth
    public RestResponse<List<User>> findAll() {
        return RestResponse.<List<User>>builder()
                .result(userMapper.findAll())
                .build();
    }
}
