// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.test;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.web.server.LocalServerPort;

/**
 * @author Kelin Tan
 */
@AutoConfigureWebMvc
public abstract class BaseSpringWebTest extends BaseSpringTest {
    @LocalServerPort
    private int serverPort;

    protected String serverPrefix;

    @BeforeEach
    public void setUp() {
        serverPrefix = "http://localhost:" + serverPort;
    }
}
