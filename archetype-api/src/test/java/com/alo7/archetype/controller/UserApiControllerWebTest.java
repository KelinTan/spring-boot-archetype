// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;


import com.alo7.archetype.SpringBootArchetypeServer;
import com.alo7.archetype.entity.User;
import com.alo7.archetype.http.HttpRequest;
import com.alo7.archetype.mapper.UserMapper;
import com.alo7.archetype.rest.response.RestResponse;
import com.alo7.archetype.testing.BaseSpringTest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringBootArchetypeServer.class)
public class UserApiControllerWebTest extends BaseSpringTest {
    @LocalServerPort
    private int serverPort;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testFindAllUsers() {
        RestResponse<List<User>> response = HttpRequest.withPath(
                "http://localhost:" + serverPort + "/api/v1/user/findAll")
                .performGet()
                .json(new TypeReference<RestResponse<List<User>>>() {
                });

        Assert.assertEquals(response.getResult().size(), 4);
        Assert.assertEquals(response.getResult().get(0).getId().intValue(), 1);
    }

    @Test
    public void testFindUser() {
        RestResponse<User> response = HttpRequest.withPath(
                "http://localhost:" + serverPort + "/api/v1/user/1")
                .performGet()
                .json(new TypeReference<RestResponse<User>>() {
                });

        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getId().intValue(), 1);
    }

    @Test
    public void testFindUser2() {
        RestResponse<User> response = HttpRequest.withPath(
                "http://localhost:" + serverPort + "/api/v1/user/1")
                .withParam("id", 1)
                .performGet()
                .json(new TypeReference<RestResponse<User>>() {
                });

        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getId().intValue(), 1);
    }

    @Test
    public void testSaveUser2() {
        RestResponse<User> response = HttpRequest.withPath(
                "http://localhost:" + serverPort + "/api/v1/user/save2")
                .withContent("{\"userName\":\"webSave\"}")
                .performPost()
                .json(new TypeReference<RestResponse<User>>() {
                });

        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getUserName(), "webSave");
    }

    @Test
    public void testDelete() {
        HttpRequest.withPath(
                "http://localhost:" + serverPort + "/api/v1/user/1")
                .performDelete();

        Assert.assertNull(userMapper.findById(1L));
    }
}
