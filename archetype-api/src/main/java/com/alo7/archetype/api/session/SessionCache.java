// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.api.session;

import com.alo7.archetype.common.entity.biz.BizAccount;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author Kelin Tan
 */
public class SessionCache {
    public static final ThreadLocal<BizAccount> ACCOUNT = new ThreadLocal<>();
    public static final ThreadLocal<DecodedJWT> JWT = new ThreadLocal<>();
}
