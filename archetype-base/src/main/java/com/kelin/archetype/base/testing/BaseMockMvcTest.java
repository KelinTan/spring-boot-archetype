// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.base.testing;

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
}
