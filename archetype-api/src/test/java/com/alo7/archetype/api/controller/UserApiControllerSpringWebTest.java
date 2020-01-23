// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.api.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.alo7.archetype.base.http.HttpRequest;
import com.alo7.archetype.base.http.HttpUtils;
import com.alo7.archetype.base.rest.response.RestResponse;
import com.alo7.archetype.base.testing.BaseSpringWebTest;
import com.alo7.archetype.base.testing.database.MockDatabase;
import com.alo7.archetype.common.entity.primary.User;
import com.alo7.archetype.common.mapper.primary.UserMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Kelin Tan
 */
@MockDatabase
public class UserApiControllerSpringWebTest extends BaseSpringWebTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testCrossOriginOptions() {
        CloseableHttpResponse response = HttpRequest.host(serverPrefix + "/api/v1/user/findAll")
                .withHeader(HttpHeaders.ORIGIN, "*")
                .withHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                .performOptions().response();

        assertTrue(HttpUtils.isHttpOk(response.getStatusLine().getStatusCode()));
        assertEquals(response.getFirstHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN).getValue(), "*");
        assertTrue(response.getFirstHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS).getValue()
                .contains("GET"));
        assertTrue(BooleanUtils
                .toBoolean(response.getFirstHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS).getValue()));
    }

    @Test
    public void testFindAllUsersPerformGet() {
        RestResponse<List<User>> response = HttpRequest.host(serverPrefix + "/api/v1/user/findAll")
                .performGet()
                .json(new TypeReference<RestResponse<List<User>>>() {
                });

        assertEquals(response.getResult().size(), 4);
        assertEquals(response.getResult().get(0).getId().intValue(), 1);
    }

    @Test
    public void testFindUserWithHeader() {
        RestResponse<User> response = HttpRequest.host(serverPrefix + "/api/v1/user/findUserWithHeader")
                .withHeader("id", 1)
                .performGet()
                .json(new TypeReference<RestResponse<User>>() {
                });

        assertNotNull(response.getResult());
        assertEquals(response.getResult().getId().intValue(), 1);
    }

    @Test
    public void testFindUser2PerformGetBadRequest() {
        assertTrue(HttpRequest.host(serverPrefix + "/api/v1/user/findUser").performGet().isBadRequest());
    }

    @Test
    public void testFindUser2PerformGetWithParam() {
        RestResponse<User> response = HttpRequest.host(serverPrefix + "/api/v1/user/findUser")
                .withParam("id", 1)
                .performGet()
                .json(new TypeReference<RestResponse<User>>() {
                });

        assertNotNull(response.getResult());
        assertEquals(response.getResult().getId().intValue(), 1);
    }

    @Test
    public void testSaveUserPerformPostWithParam() {
        RestResponse<User> response = HttpRequest.host(serverPrefix + "/api/v1/user/save")
                .withParam("name", "performPostWithParam")
                .performPost()
                .json(new TypeReference<RestResponse<User>>() {
                });

        assertNotNull(response.getResult());
        assertEquals(response.getResult().getUserName(), "performPostWithParam");
    }

    @Test
    @Transactional
    public void testSaveUser2PerformPostWithContent() {
        RestResponse<User> response = HttpRequest.host(serverPrefix + "/api/v1/user/save2")
                .withContent("{\"userName\":\"performPost\"}")
                .performPost()
                .json(new TypeReference<RestResponse<User>>() {
                });

        assertNotNull(response.getResult());
        assertEquals(response.getResult().getUserName(), "performPost");
    }

    @Test
    public void testSaveUser3PerformPut() {
        RestResponse<User> response = HttpRequest.host(serverPrefix + "/api/v1/user/save3")
                .withContent("{\"userName\":\"performPut\"}")
                .performPut()
                .json(new TypeReference<RestResponse<User>>() {
                });

        assertNotNull(response.getResult());
        assertEquals(response.getResult().getUserName(), "performPut");
    }

    @Test
    public void testDelete() {
        assertNotNull(userMapper.findOne(3L));

        HttpRequest.host(serverPrefix + "/api/v1/user/3")
                .performDelete();

        assertNull(userMapper.findOne(3L));
    }

    @Test
    public void testDelete2WithParam() {
        assertNotNull(userMapper.findOne(4L));

        HttpRequest.host(serverPrefix + "/api/v1/user/delete")
                .withParam("id", 4)
                .performDelete();

        assertNull(userMapper.findOne(4L));
    }
}
