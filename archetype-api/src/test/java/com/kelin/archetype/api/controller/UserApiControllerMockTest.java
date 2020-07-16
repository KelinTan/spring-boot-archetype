// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kelin.archetype.base.rest.exception.RestExceptionFactory;
import com.kelin.archetype.base.testing.BaseMockMvcTest;
import com.kelin.archetype.base.testing.database.MockDatabase;
import com.kelin.archetype.common.entity.primary.User;
import com.kelin.archetype.common.mapper.primary.UserMapper;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

/**
 * @author Kelin Tan
 */
@MockDatabase
public class UserApiControllerMockTest extends BaseMockMvcTest {
    @MockBean
    UserMapper userMapper;

    @Test
    public void mockUserMapperTest() throws Exception {
        User mockUser = new User();
        mockUser.setUserName("mock");
        mockUser.setId(2L);
        when(userMapper.findAll()).thenReturn(Lists.newArrayList(mockUser));

        mockMvc.perform(get("/api/v2/user/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.length()").value("1"))
                .andExpect(jsonPath("$.result[0].id").value("2"))
                .andExpect(jsonPath("$.result[0].userName").value("mock"));
    }

    @Test
    public void mockUserMapperException() throws Exception {
        when(userMapper.findAll()).thenThrow(RestExceptionFactory.toBadRequestException(1000, "Mock Bad Request"));

        mockMvc.perform(get("/api/v2/user/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(1000))
                .andExpect(jsonPath("$.errorMessage").value("Mock Bad Request"));
    }
}
