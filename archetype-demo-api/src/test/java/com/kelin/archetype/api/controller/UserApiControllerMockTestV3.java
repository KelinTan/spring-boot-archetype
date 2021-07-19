// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.kelin.archetype.api.ApiApplication;
import com.kelin.archetype.common.rest.response.RestResponse;
import com.kelin.archetype.database.config.PrimaryDatabase;
import com.kelin.archetype.database.entity.primary.User;
import com.kelin.archetype.database.mapper.primary.UserMapper;
import com.kelin.archetype.test.BaseSpringTest;
import com.kelin.archetype.test.database.MockDatabase;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Kelin Tan
 */
@MockDatabase(name = PrimaryDatabase.NAME)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ApiApplication.class
)
public class UserApiControllerMockTestV3 extends BaseSpringTest {
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
