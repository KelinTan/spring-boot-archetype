// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.client;

import com.kelin.archetype.api.SpringBootArchetypeServer;
import com.kelin.archetype.base.testing.BaseSpringTest;
import com.kelin.archetype.base.testing.database.MockDatabase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * Defined port webEnvironment for rpc test
 *
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = SpringBootArchetypeServer.class)
@MockDatabase
public class UserClient2Test extends BaseSpringTest {
    @Autowired
    UserClient2 userClient;

    @Test(expected = IllegalArgumentException.class)
    public void testRpcError() {
        userClient.findAllError();
    }
}
