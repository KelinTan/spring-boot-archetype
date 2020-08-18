// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller;

import com.kelin.archetype.api.model.request.LoginRequest;
import com.kelin.archetype.api.model.response.AccountResponse;
import com.kelin.archetype.api.session.SessionExceptionFactory;
import com.kelin.archetype.core.rest.response.RestResponse;
import com.kelin.archetype.core.rest.response.RestResponseFactory;
import com.kelin.archetype.database.entity.biz.BizAccount;
import com.kelin.archetype.database.mapper.biz.BizAccountMapper;
import com.kelin.archetype.jwt.JwtManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Kelin Tan
 */
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    @Autowired
    private BizAccountMapper accountMapper;
    @Autowired
    private JwtManager jwtManager;

    @PostMapping("/login")
    public RestResponse<AccountResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        BizAccount account = accountMapper.findByAccount(loginRequest.getAccount());
        if (account == null) {
            throw SessionExceptionFactory.toAccountNotFound();
        }

        if (!StringUtils.equals(loginRequest.getPassword(), account.getPassword())) {
            throw SessionExceptionFactory.toAccountInvalidPassword();
        }

        String token = jwtManager.generateToken(account.getAccount());
        //may store jwt id and find jwt token by jwt id in redis
        accountMapper.updateToken(account.getId(), token);

        return RestResponseFactory.success(new AccountResponse(account.getAccount(), token));
    }
}
