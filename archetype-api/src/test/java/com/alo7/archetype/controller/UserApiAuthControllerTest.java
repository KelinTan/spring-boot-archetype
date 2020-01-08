// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alo7.archetype.testing.BaseMockMvcTest;
import com.alo7.archetype.testing.MockDatabase;
import com.alo7.archetype.testing.MockDatabases;
import org.junit.Test;
import org.springframework.http.MediaType;

/**
 * @author Kelin Tan
 */
@MockDatabases
        ({@MockDatabase(dataSource = "bizDataSource", schema = "schema/biz/*.sql", data = "data/biz/*.sql", table =
                "user"),
                @MockDatabase(dataSource = "primaryDataSource", schema = "schema/primary/*.sql", data = "data/primary/*"
                        + ".sql", table = "user")})
public class UserApiAuthControllerTest extends BaseMockMvcTest {
    @Test
    public void testFakeAuth() throws Exception {
        mockMvc.perform(get("/api/v1/user/auth/findAll")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.length()").value("4"))
                .andExpect(jsonPath("$.result[0].id").value("1"))
                .andExpect(jsonPath("$.result[0].userName").value("test1"));
    }
}
