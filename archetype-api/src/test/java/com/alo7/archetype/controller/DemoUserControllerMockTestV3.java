// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;

import static org.mockito.Mockito.when;

import com.alo7.archetype.entity.User;
import com.alo7.archetype.mapper.UserMapper;
import com.alo7.archetype.testing.BaseMockTest;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Kelin Tan
 */
public class DemoUserControllerMockTestV3 extends BaseMockTest {
    @Mock
    UserMapper userMapper;

    @InjectMocks
    @Autowired
    DemoUserController demoUserController;

    @Test
    public void mockUserMapperTest() {
        when(userMapper.findAll()).thenReturn(Lists.newArrayList(new User("mock", 2L)));

        List<User> users = demoUserController.findAll();
        Assert.assertEquals(1, users.size());
        Assert.assertEquals(2L, (long) users.get(0).getId());
    }
}
