// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;

import com.alo7.archetype.persistence.entity.primary.User;
import com.alo7.archetype.persistence.mapper.primary.UserMapper;
import com.alo7.archetype.rest.response.RestPageResponse;
import com.alo7.archetype.rest.response.RestResponse;
import com.alo7.archetype.rest.response.RestResponseFactory;
import com.alo7.archetype.session.SessionAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return RestResponseFactory.success(userMapper.findAll());
    }

    @GetMapping("/findPage")
    @SessionAuth
    public RestPageResponse<User> findPage(@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<User> page = userMapper.findPage(null, PageRequest.of(pageNo, pageSize));
        return RestResponseFactory.successPage(page);
    }
}
