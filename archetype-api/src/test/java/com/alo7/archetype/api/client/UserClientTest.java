// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.alo7.archetype.api.SpringBootArchetypeServer;
import com.alo7.archetype.base.rest.exception.RestException;
import com.alo7.archetype.base.rest.response.RestResponse;
import com.alo7.archetype.base.testing.BaseSpringTest;
import com.alo7.archetype.base.testing.database.MockDatabase;
import com.alo7.archetype.common.entity.primary.User;
import com.alo7.archetype.common.mapper.primary.UserMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Defined port webEnvironment for rpc test
 *
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = SpringBootArchetypeServer.class)
@MockDatabase
public class UserClientTest extends BaseSpringTest {
    @Autowired
    UserClient userClient;

    @Autowired
    UserMapper userMapper;

    @Test
    public void testRpcInit() {
        assertNotNull(userClient);
        assertTrue(Proxy.isProxyClass(userClient.getClass()));
    }

    @Test(expected = RestException.class)
    public void testRpcError() {
        userClient.findAllError();
    }

    @Test
    public void testRpcDeleteWithPathVariable() {
        assertNotNull(userMapper.findOne(3L));
        userClient.delete(3L);
        assertNull(userMapper.findOne(3L));
    }

    @Test
    public void testRpcDeleteWithRequestParam() {
        assertNotNull(userMapper.findOne(4L));
        userClient.delete2(4L);
        assertNull(userMapper.findOne(4L));
    }

    @Test
    public void testRpcGetByIdWithPathVariable() {
        RestResponse<User> response = userClient.findUser(1L);
        assertNotNull(response.getResult());
        assertEquals(response.getResult().getId().intValue(), 1);
        assertEquals(response.getResult().getUserName(), "test1");
    }

    @Test
    public void testRpcGetByIdWithRequestParam() {
        RestResponse<User> response = userClient.findUser2(1L);
        assertNotNull(response.getResult());
        assertEquals(response.getResult().getId().intValue(), 1);
        assertEquals(response.getResult().getUserName(), "test1");
    }

    @Test
    public void testRpcPostWithRequestParam() {
        RestResponse<User> response = userClient.save("rpcSave");
        assertNotNull(response.getResult());
        assertEquals(response.getResult().getUserName(), "rpcSave");
    }

    @Test
    public void testRpcPostWithRequestBody() {
        User data = new User();
        data.setUserName("rpcSave2");
        RestResponse<User> response = userClient.save2(data);
        assertNotNull(response.getResult());
        assertEquals(response.getResult().getUserName(), "rpcSave2");
    }

    @Test
    public void testRpcPutWithRequestBody() {
        User data = new User();
        data.setUserName("rpcSave3");
        RestResponse<User> response = userClient.save3(data);
        assertNotNull(response.getResult());
        assertEquals(response.getResult().getUserName(), "rpcSave3");
    }

    @Test
    public void testRpcGetNoParams() {
        RestResponse<List<User>> response = userClient.findAll();
        assertEquals(response.getResult().get(0).getId().intValue(), 1);
    }
}