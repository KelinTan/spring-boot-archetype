// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.base.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kelin Tan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestErrorResponse {
    private int errorCode;
    private String errorMessage;
}
