// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alo7.archetype.config.DataSourceConfig;
import com.alo7.archetype.model.constant.BizErrorCode;
import com.alo7.archetype.persistence.mapper.primary.UserMapper;
import com.alo7.archetype.session.testing.FakeSessionService;
import com.alo7.archetype.testing.BaseMockMvcTest;
import com.alo7.archetype.testing.database.MockDatabase;
import com.alo7.archetype.testing.database.MockDatabases;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

/**
 * @author Kelin Tan
 */
@MockDatabases
        ({@MockDatabase(name = DataSourceConfig.PRIMARY, mappers = UserMapper.class),
                @MockDatabase(name = DataSourceConfig.BIZ, tables = "biz_account")})
public class UserApiAuthControllerTest extends BaseMockMvcTest {
    @Autowired
    FakeSessionService fakeSessionService;

    @Test
    public void testAuthFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/user/auth/findAll")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.length()").value("4"))
                .andExpect(jsonPath("$.result[0].id").value("1"))
                .andExpect(jsonPath("$.result[0].userName").value("test1"));
    }

    @Test
    public void testAuthFindPage() throws Exception {
        mockMvc.perform(get("/api/v1/user/auth/findPage")
                .param("pageNo", "0")
                .param("pageSize", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.length()").value("2"))
                .andExpect(jsonPath("$.results[0].id").value("1"))
                .andExpect(jsonPath("$.results[0].userName").value("test1"))
                .andExpect(jsonPath("$.results[1].id").value("2"))
                .andExpect(jsonPath("$.results[1].userName").value("test2"))
        ;
    }

    @Test
    public void testSessionExpired() throws Exception {
        fakeSessionService.setFakeAccount(null);

        mockMvc.perform(get("/api/v1/user/auth/findAll")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.errorCode").value(BizErrorCode.ACCOUNT_SESSION_EXPIRED.getErrorCode()));
        ;
    }
}
