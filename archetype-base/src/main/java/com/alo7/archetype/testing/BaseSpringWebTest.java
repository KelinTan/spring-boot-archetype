// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.testing;

import org.junit.Before;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

/**
 * Direct web layer will run in separate thread,this cause @Transactional can not rollback in test case
 *
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
