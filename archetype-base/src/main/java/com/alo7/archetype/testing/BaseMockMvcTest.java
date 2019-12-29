// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.testing;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Kelin Tan
 */
@AutoConfigureMockMvc
public class BaseMockMvcTest extends BaseSpringTest {
    @Autowired
    protected MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}
