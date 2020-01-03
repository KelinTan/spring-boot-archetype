// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;


import com.alo7.archetype.entity.User;
import com.alo7.archetype.http.HttpRequest;
import com.alo7.archetype.mapper.UserMapper;
import com.alo7.archetype.rest.response.RestResponse;
import com.alo7.archetype.testing.BaseWebTest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Kelin Tan
 */
@SqlGroup({@Sql(value = "classpath:reset/user.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:data/user.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)})
public class UserApiControllerWebTest extends BaseWebTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testFindAllUsersPerformGet() {
        RestResponse<List<User>> response = HttpRequest.withPath(serverPrefix + "/api/v1/user/findAll")
                .performGet()
                .json(new TypeReference<RestResponse<List<User>>>() {
                });

        Assert.assertEquals(response.getResult().size(), 4);
        Assert.assertEquals(response.getResult().get(0).getId().intValue(), 1);
    }

    @Test
    public void testFindUser2PerformGetWithParam() {
        RestResponse<User> response = HttpRequest.withPath(serverPrefix + "/api/v1/user/1")
                .withParam("id", 1)
                .performGet()
                .json(new TypeReference<RestResponse<User>>() {
                });

        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getId().intValue(), 1);
    }

    @Test
    public void testSaveUserPerformPostWithParam() {
        RestResponse<User> response = HttpRequest.withPath(serverPrefix + "/api/v1/user/save")
                .withParam("name", "performPostWithParam")
                .performPost()
                .json(new TypeReference<RestResponse<User>>() {
                });

        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getUserName(), "performPostWithParam");
    }

    @Test
    @Transactional
    public void testSaveUser2PerformPostWithContent() {
        RestResponse<User> response = HttpRequest.withPath(serverPrefix + "/api/v1/user/save2")
                .withContent("{\"userName\":\"performPost\"}")
                .performPost()
                .json(new TypeReference<RestResponse<User>>() {
                });

        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getUserName(), "performPost");
    }

    @Test
    public void testSaveUser3PerformPut() {
        RestResponse<User> response = HttpRequest.withPath(serverPrefix + "/api/v1/user/save3")
                .withContent("{\"userName\":\"performPut\"}")
                .performPut()
                .json(new TypeReference<RestResponse<User>>() {
                });

        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getUserName(), "performPut");
    }

    @Test
    public void testDelete() {
        Assert.assertNotNull(userMapper.findById(3L));

        HttpRequest.withPath(serverPrefix + "/api/v1/user/3")
                .performDelete();

        Assert.assertNull(userMapper.findById(3L));
    }

    @Test
    public void testDelete2WithParam() {
        Assert.assertNotNull(userMapper.findById(4L));

        HttpRequest.withPath(serverPrefix + "/api/v1/user/delete")
                .withParam("id", 4)
                .performDelete();

        Assert.assertNull(userMapper.findById(4L));
    }
}
