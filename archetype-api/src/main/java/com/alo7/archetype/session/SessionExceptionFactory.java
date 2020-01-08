// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.session;

import com.alo7.archetype.model.constant.BizErrorCode;
import com.alo7.archetype.rest.exception.RestException;
import com.alo7.archetype.rest.exception.RestExceptionFactory;

/**
 * @author Kelin Tan
 */
public class SessionExceptionFactory {
    public static RestException toAccountNotFound() {
        return toUnauthorizedException(BizErrorCode.ACCOUNT_NOT_FOUND);
    }

    public static RestException toAccountInvalidPassword() {
        return toUnauthorizedException(BizErrorCode.ACCOUNT_PASSWORD_INVALID);
    }

    public static RestException toAccountSessionExpired() {
        return toUnauthorizedException(BizErrorCode.ACCOUNT_SESSION_EXPIRED);
    }

    private static RestException toUnauthorizedException(BizErrorCode errorCode) {
        return RestExceptionFactory.toUnAuthorizedException(errorCode.getErrorCode(), errorCode.getErrorMessage());
    }
}
