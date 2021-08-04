// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller;

import com.kelin.archetype.api.client.UserClient;
import com.kelin.archetype.common.beans.RestResponse;
import com.kelin.archetype.database.entity.primary.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Kelin Tan
 */
@RestController
@RequestMapping("/api/v1/tracing")
public class TracingDemoController {
    @Autowired
    private UserClient userClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/rest")
    public Object tracingRestTemplate() {
        return restTemplate.getForObject("http://localhost:8081/api/v1/user/findAll", Object.class);
    }

    @GetMapping("/rpc")
    public RestResponse<List<User>> tracingRpc() {
        return userClient.findAll();
    }
}
