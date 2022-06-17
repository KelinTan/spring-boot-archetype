// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller;

import com.kelin.archetype.api.session.SessionAuth;
import com.kelin.archetype.beans.rest.RestPageResponse;
import com.kelin.archetype.beans.rest.RestResponse;
import com.kelin.archetype.beans.rest.RestResponseFactory;
import com.kelin.archetype.database.entity.primary.User;
import com.kelin.archetype.database.mapper.primary.UserMapper;
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
        return RestResponseFactory.successPage(page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getContent());
    }
}
