// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.client;

import com.alo7.archetype.ServerApplication;
import com.alo7.archetype.entity.User;
import com.alo7.archetype.mapper.UserMapper;
import com.alo7.archetype.testing.BaseSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 启动真实的web容器测试rpc
 *
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ServerApplication.class)
public class DemoClientTest extends BaseSpringTest {
    @Autowired
    DemoClient demoClient;

    @Autowired
    UserMapper userMapper;

    @Test
    public void testRpcInit() {
        assert demoClient != null;
        assert Proxy.isProxyClass(demoClient.getClass());
    }

    @Test
    public void testRpcDeleteWithPathVariable() {
        assert userMapper.findById(3L) != null;
        demoClient.delete(3L);
        assert userMapper.findById(3L) == null;
    }

    @Test
    public void testRpcDeleteWithRequestParam() {
        assert userMapper.findById(4L) != null;
        demoClient.delete2(4L);
        assert userMapper.findById(4L) == null;
    }

    @Test
    public void testRpcGetNoParams() {
        List<User> users = demoClient.findAll();
        assert users.get(0).getId() == 1;
    }

    @Test
    public void testRpcGetByIdWithPathVariable() {
        User user = demoClient.findUser(1L);
        assert user != null;
        assert user.getId() == 1;
        assert user.getUserName().equals("hehe");
    }

    @Test
    public void testRpcGetByIdWithRequestParam() {
        User user = demoClient.findUser2(1L);
        assert user != null;
        assert user.getId() == 1;
        assert user.getUserName().equals("hehe");
    }

    @Test
    public void testRpcPostWithRequestParam() {
        User user = demoClient.save("rpcSave");
        assert user != null;
        assert user.getUserName().equals("rpcSave");
    }

    @Test
    public void testRpcPostWithRequestBody() {
        User data = new User();
        data.setUserName("rpcSave2");
        User user = demoClient.save2(data);
        assert user != null;
        assert user.getUserName().equals("rpcSave2");
    }

    @Test
    public void testRpcPutWithRequestBody() {
        User data = new User();
        data.setUserName("rpcSave3");
        User user = demoClient.save3(data);
        assert user != null;
        assert user.getUserName().equals("rpcSave3");
    }
}
