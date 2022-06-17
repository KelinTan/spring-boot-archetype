// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.session;

import com.kelin.archetype.database.entity.biz.BizAccount;

/**
 * @author Kelin Tan
 */
public class SessionCache {
    public static final ThreadLocal<BizAccount> ACCOUNT = new ThreadLocal<>();
}
