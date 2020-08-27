// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.model.response;

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
