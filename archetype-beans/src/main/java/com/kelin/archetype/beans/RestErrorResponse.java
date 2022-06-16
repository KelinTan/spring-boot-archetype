// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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
    private Map<String, Object> meta;
}
