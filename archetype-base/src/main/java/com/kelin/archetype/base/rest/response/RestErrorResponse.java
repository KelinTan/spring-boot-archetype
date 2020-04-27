// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.base.rest.response;

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
