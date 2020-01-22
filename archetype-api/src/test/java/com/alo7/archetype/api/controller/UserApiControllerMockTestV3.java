// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.api.controller;

import static org.mockito.Mockito.when;

import com.alo7.archetype.base.rest.response.RestResponse;
import com.alo7.archetype.base.testing.BaseMockTest;
import com.alo7.archetype.base.testing.database.MockDatabase;
import com.alo7.archetype.common.entity.primary.User;
import com.alo7.archetype.common.mapper.primary.UserMapper;
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
@MockDatabase
public class UserApiControllerMockTestV3 extends BaseMockTest {
    @Mock
    UserMapper userMapper;

    @InjectMocks
    @Autowired
    UserApiController userApiController;

    @Test
    public void mockUserMapperTest() {
        User mockUser = new User();
        mockUser.setUserName("mock");
        mockUser.setId(2L);
        when(userMapper.findAll()).thenReturn(Lists.newArrayList(mockUser));

        RestResponse<List<User>> response = userApiController.findAll();
        Assert.assertEquals(1, response.getResult().size());
        Assert.assertEquals(2L, (long) response.getResult().get(0).getId());
    }
}
