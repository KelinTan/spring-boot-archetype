// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.http;

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
    private boolean async;
}
