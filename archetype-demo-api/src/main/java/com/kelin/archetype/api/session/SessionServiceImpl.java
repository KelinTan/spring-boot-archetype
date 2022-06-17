// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.session;

import com.kelin.archetype.beans.constants.GatewayConstant;
import com.kelin.archetype.database.entity.biz.BizAccount;
import com.kelin.archetype.database.mapper.biz.BizAccountMapper;
import com.kelin.archetype.jwt.JwtManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kelin Tan
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {
    private final JwtManager jwtManager;
    private final BizAccountMapper accountMapper;

    @Override
    public BizAccount getCurrentAccount(HttpServletRequest request) {
        String header = request.getHeader(GatewayConstant.GATEWAY_ACCOUNT_HEADER);
        if (StringUtils.isBlank(header)) {
            throw SessionExceptionFactory.toAccountSessionExpired();
        }

        try {
            BizAccount account = accountMapper.findByAccount(header);
            if (account != null) {
                return account;
            }
        } catch (Exception e) {
            log.error("jwt verify error:", e);
        }
        throw SessionExceptionFactory.toAccountSessionExpired();
    }
}
