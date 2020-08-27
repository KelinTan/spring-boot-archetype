// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.session;

import com.kelin.archetype.api.model.constant.BizErrorCode;
import com.kelin.archetype.common.rest.exception.RestException;
import com.kelin.archetype.common.rest.exception.RestExceptionFactory;

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
