// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.api.session;

import com.alo7.archetype.common.entity.biz.BizAccount;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kelin Tan
 */
public interface SessionService {
    BizAccount getCurrentAccount(HttpServletRequest request);
}
