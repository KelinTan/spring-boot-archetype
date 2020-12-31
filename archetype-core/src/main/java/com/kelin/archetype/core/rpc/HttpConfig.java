// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

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
public class HttpConfig {
    private int connectionTimeout;
    private int readTimeout;
    private int retryTimes;
}
