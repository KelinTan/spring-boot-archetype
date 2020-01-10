// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.controller;

import com.alo7.archetype.jwt.JwtManager;
import com.alo7.archetype.model.request.LoginRequest;
import com.alo7.archetype.model.response.AccountResponse;
import com.alo7.archetype.persistence.entity.biz.BizAccount;
import com.alo7.archetype.persistence.mapper.biz.BizAccountMapper;
import com.alo7.archetype.rest.response.RestResponse;
import com.alo7.archetype.rest.response.RestResponseFactory;
import com.alo7.archetype.session.SessionExceptionFactory;
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
