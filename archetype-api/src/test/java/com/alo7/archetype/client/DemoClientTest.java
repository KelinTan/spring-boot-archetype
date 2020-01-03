// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.client;

import com.alo7.archetype.SpringBootArchetypeServer;
import com.alo7.archetype.entity.User;
import com.alo7.archetype.mapper.UserMapper;
import com.alo7.archetype.rest.response.RestResponse;
import com.alo7.archetype.testing.BaseWebTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Defined port webEnvironment for rpc test
 *
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = SpringBootArchetypeServer.class)
@SqlGroup({@Sql(value = "classpath:reset/user.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:data/user.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)})
public class DemoClientTest extends BaseWebTest {
    @Autowired
    DemoClient demoClient;

    @Autowired
    UserMapper userMapper;

    @Test
    public void testRpcInit() {
        Assert.assertNotNull(demoClient);
        Assert.assertTrue(Proxy.isProxyClass(demoClient.getClass()));
    }

    @Test
    public void testRpcDeleteWithPathVariable() {
        Assert.assertNotNull(userMapper.findById(3L));
        demoClient.delete(3L);
        Assert.assertNull(userMapper.findById(3L));
    }

    @Test
    public void testRpcDeleteWithRequestParam() {
        Assert.assertNotNull(userMapper.findById(4L));
        demoClient.delete2(4L);
        Assert.assertNull(userMapper.findById(4L));
    }

    @Test
    public void testRpcGetByIdWithPathVariable() {
        RestResponse<User> response = demoClient.findUser(1L);
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getId().intValue(), 1);
        Assert.assertEquals(response.getResult().getUserName(), "test1");
    }

    @Test
    public void testRpcGetByIdWithRequestParam() {
        RestResponse<User> response = demoClient.findUser2(1L);
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getId().intValue(), 1);
        Assert.assertEquals(response.getResult().getUserName(), "test1");
    }

    @Test
    public void testRpcPostWithRequestParam() {
        RestResponse<User> response = demoClient.save("rpcSave");
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getUserName(), "rpcSave");
    }

    @Test
    public void testRpcPostWithRequestBody() {
        User data = new User();
        data.setUserName("rpcSave2");
        RestResponse<User> response = demoClient.save2(data);
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getUserName(), "rpcSave2");
    }

    @Test
    public void testRpcPutWithRequestBody() {
        User data = new User();
        data.setUserName("rpcSave3");
        RestResponse<User> response = demoClient.save3(data);
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getUserName(), "rpcSave3");
    }

    @Test
    public void testRpcGetNoParams() {
        RestResponse<List<User>> response = demoClient.findAll();
        Assert.assertEquals(response.getResult().get(0).getId().intValue(), 1);
    }
}
