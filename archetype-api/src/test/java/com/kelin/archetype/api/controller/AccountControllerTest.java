// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kelin.archetype.api.SpringBootArchetypeServer;
import com.kelin.archetype.api.model.constant.BizErrorCode;
import com.kelin.archetype.api.model.request.LoginRequest;
import com.kelin.archetype.base.json.JsonConverter;
import com.kelin.archetype.base.testing.BaseMockMvcTest;
import com.kelin.archetype.base.testing.database.MockDatabase;
import com.kelin.archetype.database.config.DataSourceConfig;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

/**
 * @author Kelin Tan
 */
@MockDatabase(name = DataSourceConfig.BIZ)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringBootArchetypeServer.class
)
public class AccountControllerTest extends BaseMockMvcTest {
    @Test
    public void testLoginBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(HttpStatus.SC_BAD_REQUEST));
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
