// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alo7.archetype.config.DataSourceConfig;
import com.alo7.archetype.json.JsonConverter;
import com.alo7.archetype.model.constant.BizErrorCode;
import com.alo7.archetype.model.request.LoginRequest;
import com.alo7.archetype.rest.exception.GlobalErrorCode;
import com.alo7.archetype.testing.BaseMockMvcTest;
import com.alo7.archetype.testing.database.MockDatabase;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.http.MediaType;

/**
 * @author Kelin Tan
 */
@MockDatabase(name = DataSourceConfig.BIZ)
public class AccountControllerTest extends BaseMockMvcTest {
    @Test
    public void testLoginBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(GlobalErrorCode.BAD_REQUEST_ERROR));
    }

    @Test
    public void testLoginInvalidAccount() throws Exception {
        LoginRequest request = new LoginRequest("account", "password");
        mockMvc.perform(post("/api/v1/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConverter.serialize(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.errorCode").value(BizErrorCode.ACCOUNT_NOT_FOUND.getErrorCode()));
    }

    @Test
    public void testLoginInvalidAccountPassword() throws Exception {
        LoginRequest request = new LoginRequest("test1", "password");
        mockMvc.perform(post("/api/v1/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConverter.serialize(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.errorCode").value(BizErrorCode.ACCOUNT_PASSWORD_INVALID.getErrorCode()));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest("test1", "password1");
        mockMvc.perform(post("/api/v1/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConverter.serialize(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.account").value("test1"))
                .andExpect(jsonPath("$.result.token").isNotEmpty());
    }
}
