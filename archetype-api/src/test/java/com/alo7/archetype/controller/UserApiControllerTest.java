// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alo7.archetype.rest.exception.GlobalErrorCode;
import com.alo7.archetype.testing.BaseMockMvcTest;
import com.alo7.archetype.testing.MockDatabase;
import org.junit.Test;
import org.springframework.http.MediaType;

/**
 * @author Kelin Tan
 */
@MockDatabase(dataSource = "primaryDataSource", schema = "schema/primary/*.sql", data = "data/primary/*.sql")
public class UserApiControllerTest extends BaseMockMvcTest {
    @Test
    public void testFindAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/user/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.length()").value("4"))
                .andExpect(jsonPath("$.result[0].id").value("1"))
                .andExpect(jsonPath("$.result[0].userName").value("test1"));
    }

    @Test
    public void testFindUserWithHeader() throws Exception {
        mockMvc.perform(get("/api/v1/user/findUserWithHeader")
                .header("id", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value("1"))
                .andExpect(jsonPath("$.result.userName").value("test1"));
    }

    @Test
    public void testFindUserWithParam() throws Exception {
        mockMvc.perform(get("/api/v1/user/findUser")
                .param("id", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value("1"))
                .andExpect(jsonPath("$.result.userName").value("test1"));
    }

    @Test
    public void testSaveUser() throws Exception {
        mockMvc.perform(post("/api/v1/user/save").param("name", "a boy").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.userName").value("a boy"));
    }

    @Test
    public void testFindUser2MissingParameter() throws Exception {
        mockMvc.perform(get("/api/v1/user/findUser2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(GlobalErrorCode.BAD_REQUEST_ERROR));
    }

    @Test
    public void testSaveUser2ValidException() throws Exception {
        mockMvc.perform(post("/api/v1/user/save2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(GlobalErrorCode.BAD_REQUEST_ERROR));
    }

    @Test
    public void testSaveUser2MediaNotSupported() throws Exception {
        mockMvc.perform(post("/api/v1/user/save2")
                .content("{}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(GlobalErrorCode.BAD_REQUEST_ERROR));
    }

    @Test
    public void testSaveUser3MethodNotSupported() throws Exception {
        mockMvc.perform(post("/api/v1/user/save3")
                .content("{}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.errorCode").value(GlobalErrorCode.BAD_REQUEST_ERROR));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/user/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
