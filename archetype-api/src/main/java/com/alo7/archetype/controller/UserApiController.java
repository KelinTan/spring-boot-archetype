// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;

import com.alo7.archetype.persistence.entity.primary.User;
import com.alo7.archetype.persistence.mapper.primary.UserMapper;
import com.alo7.archetype.rest.response.RestResponse;
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
public class UserApiController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/findAll")
    public RestResponse<List<User>> findAll() {
        return RestResponse.<List<User>>builder()
                .result(userMapper.findAll())
                .build();
    }

    @GetMapping("/findUserWithHeader")
    public RestResponse<User> findUserWithHeader(@RequestHeader("id") Long id) {
        return RestResponse.<User>builder()
                .result(userMapper.findById(id))
                .build();
    }

    @GetMapping("/{id}")
    public RestResponse<User> findUser(@PathVariable(value = "id") Long id) {
        return RestResponse.<User>builder()
                .result(userMapper.findById(id))
                .build();
    }

    @GetMapping("/findUser")
    public RestResponse<User> findUser2(@RequestParam(value = "id") Long id) {
        return RestResponse.<User>builder()
                .result(userMapper.findById(id))
                .build();
    }

    @PostMapping("/save")
    public RestResponse<User> save(@RequestParam("name") String name) {
        User user = new User();
        user.setUserName(name);
        userMapper.insert(user);
        return RestResponse.<User>builder()
                .result(userMapper.findById(user.getId()))
                .build();
    }

    @PostMapping("/save2")
    public RestResponse<User> save2(@Valid @RequestBody User user) {
        userMapper.insert(user);

        return RestResponse.<User>builder()
                .result(userMapper.findById(user.getId()))
                .build();
    }

    @PutMapping("/save3")
    public RestResponse<User> save3(@RequestBody User user) {
        userMapper.insert(user);

        return RestResponse.<User>builder()
                .result(userMapper.findById(user.getId()))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long id) {
        userMapper.deleteById(id);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete2(@RequestParam(value = "id") Long id) {
        userMapper.deleteById(id);
    }
}
