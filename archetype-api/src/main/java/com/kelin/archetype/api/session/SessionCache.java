// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.session;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.kelin.archetype.common.entity.biz.BizAccount;

/**
 * @author Kelin Tan
 */
public class SessionCache {
    public static final ThreadLocal<BizAccount> ACCOUNT = new ThreadLocal<>();
    public static final ThreadLocal<DecodedJWT> JWT = new ThreadLocal<>();
}
