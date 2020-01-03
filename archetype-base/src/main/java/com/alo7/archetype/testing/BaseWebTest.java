// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.testing;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Direct web layer will run in separate thread,this cause @Transactional can not rollback in test case.To keep test
 * case stateless with @Sql or @SqlGroup through SqlScriptsTestExecutionListener
 *
 * @author Kelin Tan
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Ignore
public class BaseWebTest {
    @LocalServerPort
    private int serverPort;

    protected String serverPrefix;

    @Before
    public void setUp() {
        serverPrefix = "http://localhost:" + serverPort;
    }
}
