// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Kelin Tan
 */
@AutoConfigureMockMvc
public class BaseWebTest extends BaseSpringTest {
    @Autowired
    protected MockMvc mockMvc;
}
