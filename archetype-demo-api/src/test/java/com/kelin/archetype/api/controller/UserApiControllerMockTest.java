// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kelin.archetype.api.ApiApplication;
import com.kelin.archetype.common.exception.RestExceptionFactory;
import com.kelin.archetype.database.entity.primary.User;
import com.kelin.archetype.database.mapper.primary.UserMapper;
import com.kelin.archetype.test.BaseMockMvcTest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

/**
 * @author Kelin Tan
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ApiApplication.class
)
public class UserApiControllerMockTest extends BaseMockMvcTest {
    @Spy
    private UserMapper userMapper;

    @InjectMocks
    @Autowired
    private UserApiControllerV2 userApiControllerV2;

    @Test
    public void mockUserMapperTest() throws Exception {
        User mockUser = new User();
        mockUser.setUserName("mockV1");
        mockUser.setId(2L);
        when(userMapper.findAll()).thenReturn(Lists.newArrayList(mockUser));

        mockMvc.perform(get("/api/v2/user/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.length()").value("1"))
                .andExpect(jsonPath("$.result[0].id").value("2"))
                .andExpect(jsonPath("$.result[0].userName").value("mockV1"));
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
