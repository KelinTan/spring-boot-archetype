// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller;

import com.kelin.archetype.beans.RestResponse;
import com.kelin.archetype.beans.RestResponseFactory;
import com.kelin.archetype.common.exception.RestExceptionFactory;
import com.kelin.archetype.database.entity.primary.User;
import com.kelin.archetype.database.mapper.primary.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

/**
 * @author Kelin Tan
 */
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserApiController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/findAll")
    public RestResponse<List<User>> findAll() {
        log.info("Find all user");
        return RestResponseFactory.success(userMapper.findAll());
    }

    @GetMapping("/findUserWithHeader")
    public RestResponse<User> findUserWithHeader(@RequestHeader("id") Long id) {
        return RestResponseFactory.success(userMapper.findOne(id));
    }

    @GetMapping("/{id}")
    public RestResponse<User> findUser(@PathVariable(value = "id") Long id) {
        return RestResponseFactory.success(userMapper.findOne(id));
    }

    @GetMapping("/findUser")
    public RestResponse<User> findUser2(@RequestParam(value = "id") Long id) {
        return RestResponseFactory.success(userMapper.findOne(id));
    }

    @PostMapping("/save")
    public RestResponse<User> save(@RequestParam("name") String name) {
        User user = new User();
        user.setUserName(name);
        userMapper.insert(user);
        return RestResponseFactory.success(userMapper.findOne(user.getId()));
    }

    @PostMapping("/saveError")
    public RestResponse<User> saveError(@RequestParam("name") String name) {
        throw RestExceptionFactory.toSystemException();
    }

    @PostMapping("/save2")
    public RestResponse<User> save2(@Valid @RequestBody User user) {
        userMapper.insert(user);

        return RestResponseFactory.success(userMapper.findOne(user.getId()));
    }

    @PutMapping("/save3")
    public RestResponse<User> save3(@RequestBody User user) {
        userMapper.insert(user);

        return RestResponseFactory.success(userMapper.findOne(user.getId()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long id) {
        userMapper.delete(id);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete2(@RequestParam(value = "id") Long id) {
        userMapper.delete(id);
    }
}
