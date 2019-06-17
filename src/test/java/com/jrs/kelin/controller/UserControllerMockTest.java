// Copyright 2019 Alo7 Inc. All rights reserved.

package com.jrs.kelin.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jrs.kelin.BaseTest;
import com.jrs.kelin.entity.User;
import com.jrs.kelin.mapper.UserMapper;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

/**
 * @author Kelin Tan
 */
public class UserControllerMockTest extends BaseTest {
    @Mock
    UserMapper userMapper;

    @InjectMocks
    @Autowired
    UserController userController;

    @Test
    public void mockUserMapperTest() throws Exception {
        when(userMapper.findAll()).thenReturn(Lists.newArrayList(new User("mock", 2L)));

        mockMvc.perform(get("/user/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value("1"))
                .andExpect(jsonPath("$[0].id").value("2"))
                .andExpect(jsonPath("$[0].userName").value("mock"));
    }
}
