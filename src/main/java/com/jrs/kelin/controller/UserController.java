// Copyright 2019 Alo7 Inc. All rights reserved.

package com.jrs.kelin.controller;

import com.jrs.kelin.entity.User;
import com.jrs.kelin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Kelin Tan
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/findAll")
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @PostMapping("/save")
    public User save(@RequestParam("name") String name) {
        User user = new User();
        user.setUserName(name);
        userMapper.insert(user);
        return userMapper.findById(user.getId());
    }
}
