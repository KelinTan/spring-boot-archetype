// Copyright 2019 Alo7 Inc. All rights reserved.

package com.jrs.kelin.mapper;

import com.jrs.kelin.BaseTest;
import com.jrs.kelin.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * @author Kelin Tan
 */
public class UserMapperTest extends BaseTest {

    @Autowired
    private UserMapper userMapper;


    @Test
    public void testFindById() {
        User user = userMapper.findById(1L);

        Assert.notNull(user, "");
        Assert.isTrue(user.getUserName().equals("hehe"), "ID查询失败");
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setUserName("test");
        userMapper.insert(user);
        User insert = userMapper.findById(user.getId());

        Assert.isTrue(insert.getUserName().equals("test"), "数据插入失败");
    }
}
