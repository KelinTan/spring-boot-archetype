// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.client;

import com.alo7.archetype.SpringBootArchetypeServer;
import com.alo7.archetype.persistence.entity.primary.User;
import com.alo7.archetype.persistence.mapper.primary.UserMapper;
import com.alo7.archetype.rest.response.RestResponse;
import com.alo7.archetype.testing.BaseSpringTest;
import com.alo7.archetype.testing.database.MockDatabase;
import org.junit.Assert;
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
        Assert.assertNotNull(userClient);
        Assert.assertTrue(Proxy.isProxyClass(userClient.getClass()));
    }

    @Test
    public void testRpcDeleteWithPathVariable() {
        Assert.assertNotNull(userMapper.findOne(3L));
        userClient.delete(3L);
        Assert.assertNull(userMapper.findOne(3L));
    }

    @Test
    public void testRpcDeleteWithRequestParam() {
        Assert.assertNotNull(userMapper.findOne(4L));
        userClient.delete2(4L);
        Assert.assertNull(userMapper.findOne(4L));
    }

    @Test
    public void testRpcGetByIdWithPathVariable() {
        RestResponse<User> response = userClient.findUser(1L);
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getId().intValue(), 1);
        Assert.assertEquals(response.getResult().getUserName(), "test1");
    }

    @Test
    public void testRpcGetByIdWithRequestParam() {
        RestResponse<User> response = userClient.findUser2(1L);
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getId().intValue(), 1);
        Assert.assertEquals(response.getResult().getUserName(), "test1");
    }

    @Test
    public void testRpcPostWithRequestParam() {
        RestResponse<User> response = userClient.save("rpcSave");
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getUserName(), "rpcSave");
    }

    @Test
    public void testRpcPostWithRequestBody() {
        User data = new User();
        data.setUserName("rpcSave2");
        RestResponse<User> response = userClient.save2(data);
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getUserName(), "rpcSave2");
    }

    @Test
    public void testRpcPutWithRequestBody() {
        User data = new User();
        data.setUserName("rpcSave3");
        RestResponse<User> response = userClient.save3(data);
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getUserName(), "rpcSave3");
    }

    @Test
    public void testRpcGetNoParams() {
        RestResponse<List<User>> response = userClient.findAll();
        Assert.assertEquals(response.getResult().get(0).getId().intValue(), 1);
    }
}
