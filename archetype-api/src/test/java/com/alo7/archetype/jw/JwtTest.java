// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.jw;

import com.alo7.archetype.jwt.JwtManager;
import com.alo7.archetype.testing.BaseSpringTest;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class JwtTest extends BaseSpringTest {
    @Autowired
    private JwtManager jwtManager;

    @Test
    public void testJwtManagerVerify() {
        String token = jwtManager.generateToken("test");
        Assert.assertNotNull(token);

        DecodedJWT jwt = jwtManager.verify(token);

        Assert.assertNotNull(jwt);
        Assert.assertEquals(jwt.getSubject(), "test");
        Assert.assertEquals(jwt.getIssuer(), "archetype.boot");
    }
}
