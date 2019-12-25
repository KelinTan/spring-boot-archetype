// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.controller;

import com.alo7.kelin.entity.User;
import com.alo7.kelin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Kelin Tan
 */
@RestController
@RequestMapping("/user")
public class DemoUserController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/findAll")
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @RequestMapping("/{id}")
    public User findUser(@PathVariable(value = "id") Long id) {
        return userMapper.findById(id);
    }

    @RequestMapping("/findUser")
    public User findUser2(@RequestParam(value = "id") Long id) {
        return userMapper.findById(id);
    }

    @PostMapping("/save")
    public User save(@RequestParam("name") String name) {
        User user = new User();
        user.setUserName(name);
        userMapper.insert(user);
        return userMapper.findById(user.getId());
    }

    @PostMapping("/save2")
    public User save2(@Validated @RequestBody User user) {
        userMapper.insert(user);

        return userMapper.findById(user.getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        userMapper.deleteById(id);
    }

    @DeleteMapping("/delete")
    public void delete2(@RequestParam(value = "id") Long id) {
        userMapper.deleteById(id);
    }
}
