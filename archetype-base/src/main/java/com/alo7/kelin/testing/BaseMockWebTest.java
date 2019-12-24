// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.testing;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * @author Kelin Tan
 */
public class BaseMockWebTest extends BaseWebTest {
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}
