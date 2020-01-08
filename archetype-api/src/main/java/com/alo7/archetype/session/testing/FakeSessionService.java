// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.session.testing;

import com.alo7.archetype.persistence.entity.biz.BizAccount;
import com.alo7.archetype.session.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kelin Tan
 */
@Profile("test")
@Service
@Primary
@RequiredArgsConstructor
public class FakeSessionService implements SessionService {
    @Override
    public BizAccount getCurrentAccount(HttpServletRequest request) {
        return BizAccount.builder()
                .account("fake")
                .password("fake")
                .id(1L)
                .build();
    }
}
