// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.api.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kelin Tan
 */
@AllArgsConstructor
@Getter
public enum BizErrorCode {
    ACCOUNT_NOT_FOUND(1000, "Account not found"),
    ACCOUNT_PASSWORD_INVALID(1001, "Account password incorrect"),
    ACCOUNT_SESSION_EXPIRED(1002, "Session expired"),
    ;
    private final int errorCode;
    private final String errorMessage;
}
