// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.jwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.alo7.archetype.api.SpringBootArchetypeServer;
import com.alo7.archetype.base.jwt.JwtManager;
import com.alo7.archetype.base.testing.BaseSpringTest;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = SpringBootArchetypeServer.class)
public class JwtTest extends BaseSpringTest {
    @Autowired
    private JwtManager jwtManager;

    @Test
    public void testJwtManagerVerify() {
        String token = jwtManager.generateToken("test");
        assertNotNull(token);

        DecodedJWT jwt = jwtManager.verify(token);

        assertNotNull(jwt);
        assertEquals(jwt.getSubject(), "test");
        assertEquals(jwt.getIssuer(), "archetype.boot");
    }
}
