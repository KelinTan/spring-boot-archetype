// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.mapper;

import com.alo7.archetype.entity.User;
import com.alo7.archetype.testing.BaseSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.util.Assert;

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class UserMapperTest extends BaseSpringTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testFindById() {
        User user = userMapper.findById(1L);

        Assert.notNull(user, "");
        Assert.isTrue(user.getUserName().equals("test1"), "ID查询失败");
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setUserName("test");
        userMapper.insert(user);
        User insert = userMapper.findById(user.getId());

        Assert.isTrue(insert.getUserName().equals("test"), "数据插入失败");
    }

    @Test
    public void testDelete() {
        userMapper.deleteById(3L);

        Assert.isNull(userMapper.findById(3L), "数据删除失败");
    }
}
