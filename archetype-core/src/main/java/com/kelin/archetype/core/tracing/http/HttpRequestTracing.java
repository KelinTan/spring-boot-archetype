// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing.http;

import lombok.Data;

/**
 * @author Kelin Tan
 */
@Data
public class HttpRequestTracing {
    private String hostName;
    private String method;
    private String url;
    private int port;
}
