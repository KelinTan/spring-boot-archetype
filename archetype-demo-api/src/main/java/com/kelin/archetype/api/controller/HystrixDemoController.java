// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller;

import com.kelin.archetype.api.client.UserClient;
import com.kelin.archetype.common.beans.RestResponse;
import com.kelin.archetype.common.beans.RestResponseFactory;
import com.kelin.archetype.database.entity.primary.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author Kelin Tan
 */
@RestController
@RequestMapping("/api/v1/hystrix")
@Slf4j
public class HystrixDemoController {
    @Autowired
    private UserClient userClient;

    @SneakyThrows
    @GetMapping("/demo")
    @HystrixCommand(fallbackMethod = "findAllFallback", groupKey = "hystrix")
    public RestResponse<List<User>> findHystrixAll() {
        return userClient.findAll();
    }

    @SuppressWarnings("unused")
    private RestResponse<List<User>> findAllFallback() {
        log.info("findAll fallback to findAllFallback");
        return RestResponseFactory.success(Collections.singletonList(new User("fallback")));
    }
}
