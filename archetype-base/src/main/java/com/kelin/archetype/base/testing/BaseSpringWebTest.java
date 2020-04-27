// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.base.testing;

import org.junit.Before;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BaseSpringWebTest extends BaseSpringTest {
    @LocalServerPort
    private int serverPort;

    protected String serverPrefix;

    @Before
    public void setUp() {
        serverPrefix = "http://localhost:" + serverPort;
    }
}
