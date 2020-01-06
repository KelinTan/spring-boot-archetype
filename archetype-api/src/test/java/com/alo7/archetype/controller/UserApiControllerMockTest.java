// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alo7.archetype.persistence.entity.primary.User;
import com.alo7.archetype.persistence.mapper.primary.UserMapper;
import com.alo7.archetype.rest.exception.RestExceptionFactory;
import com.alo7.archetype.testing.BaseMockMvcTest;
import com.alo7.archetype.testing.MockDatabase;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kelin Tan
 */
@MockDatabase(dataSource = "primaryDataSource", schema = "schema/primary/*.sql", data = "data/primary/*.sql")
public class UserApiControllerMockTest extends BaseMockMvcTest {
    @MockBean
    UserMapper userMapper;

    @Test
    public void mockUserMapperTest() throws Exception {
        when(userMapper.findAll()).thenReturn(Lists.newArrayList(new User("mock", 2L)));

        mockMvc.perform(get("/api/v1/user/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.length()").value("1"))
                .andExpect(jsonPath("$.result[0].id").value("2"))
                .andExpect(jsonPath("$.result[0].userName").value("mock"));
    }

    @Test
    public void mockUserMapperException() throws Exception {
        when(userMapper.findAll()).thenThrow(RestExceptionFactory.toBadRequestException(1000, "Mock Bad Request"));

        mockMvc.perform(get("/api/v1/user/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(1000))
                .andExpect(jsonPath("$.errorMessage").value("Mock Bad Request"));
    }
}
