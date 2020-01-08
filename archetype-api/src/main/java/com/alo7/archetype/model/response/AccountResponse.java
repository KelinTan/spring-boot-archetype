// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kelin Tan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private String account;
    private String token;
}
