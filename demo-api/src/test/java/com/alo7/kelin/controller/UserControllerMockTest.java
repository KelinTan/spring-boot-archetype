// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alo7.kelin.entity.User;
import com.alo7.kelin.mapper.UserMapper;
import com.alo7.kelin.testing.BaseMockWebTest;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

/**
 * @author Kelin Tan
 */
public class UserControllerMockTest extends BaseMockWebTest {
    @MockBean
    UserMapper userMapper;

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
