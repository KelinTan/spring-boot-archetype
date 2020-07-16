// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.kelin.archetype.base.rest.response.RestResponse;
import com.kelin.archetype.base.testing.BaseMockTest;
import com.kelin.archetype.base.testing.database.MockDatabase;
import com.kelin.archetype.common.entity.primary.User;
import com.kelin.archetype.common.mapper.primary.UserMapper;
import org.assertj.core.util.Lists;
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
    UserApiControllerV2 userApiController;

    @Test
    public void mockUserMapperTest() {
        User mockUser = new User();
        mockUser.setUserName("mock");
        mockUser.setId(2L);
        when(userMapper.findAll()).thenReturn(Lists.newArrayList(mockUser));

        RestResponse<List<User>> response = userApiController.findAll();
        assertEquals(1, response.getResult().size());
        assertEquals(2L, (long) response.getResult().get(0).getId());
    }
}
