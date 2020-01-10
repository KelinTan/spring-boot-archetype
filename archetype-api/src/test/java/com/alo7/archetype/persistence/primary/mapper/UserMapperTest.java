// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.persistence.primary.mapper;

import com.alo7.archetype.SpringBootArchetypeServer;
import com.alo7.archetype.config.DataSourceConfig;
import com.alo7.archetype.persistence.entity.primary.User;
import com.alo7.archetype.persistence.mapper.primary.UserMapper;
import com.alo7.archetype.testing.BaseSpringTest;
import com.alo7.archetype.testing.database.MockDatabase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.List;

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = SpringBootArchetypeServer.class)
@MockDatabase(name = DataSourceConfig.PRIMARY)
public class UserMapperTest extends BaseSpringTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testFindUserByName() {
        List<User> list = userMapper.findByName("test1");

        Assert.assertEquals("test1", list.get(0).getUserName());
        Assert.assertEquals(1, list.get(0).getId().intValue());
    }
}
