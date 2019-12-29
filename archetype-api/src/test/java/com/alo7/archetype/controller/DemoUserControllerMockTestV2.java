// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alo7.archetype.entity.User;
import com.alo7.archetype.mapper.UserMapper;
import com.alo7.archetype.testing.BaseMockMvcTest;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

/**
 * @author Kelin Tan
 */
public class DemoUserControllerMockTestV2 extends BaseMockMvcTest {
    @Mock
    UserMapper userMapper;

    @InjectMocks
    @Autowired
    DemoUserController demoUserController;

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
