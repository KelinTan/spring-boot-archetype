// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.tracing.http;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Kelin Tan
 */
@Data
@AllArgsConstructor
public class HttpResponseTracing {
    private int status;
}
