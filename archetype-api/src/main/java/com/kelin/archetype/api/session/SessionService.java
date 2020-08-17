// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.session;

import com.kelin.archetype.database.entity.biz.BizAccount;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kelin Tan
 */
public interface SessionService {
    BizAccount getCurrentAccount(HttpServletRequest request);
}
