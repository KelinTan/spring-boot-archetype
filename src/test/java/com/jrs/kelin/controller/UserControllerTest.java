// Copyright 2019 Alo7 Inc. All rights reserved.

package com.jrs.kelin.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jrs.kelin.BaseTest;
import org.junit.Test;
import org.springframework.http.MediaType;

/**
 * @author Kelin Tan
 */
public class UserControllerTest extends BaseTest {
    @Test
    public void testFindAllUsers() throws Exception {
        mockMvc.perform(get("/user/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value("1"))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].userName").value("hehe"));
    }

    @Test
    public void testSaveUser() throws Exception {
        mockMvc.perform(post("/user/save").param("name", "a boy").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("a boy"));
    }
}
