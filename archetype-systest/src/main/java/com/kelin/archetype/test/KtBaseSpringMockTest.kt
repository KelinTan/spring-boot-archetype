// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.test

import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners

/**
 * @author Kelin Tan
 */
@TestExecutionListeners(
    MockitoTestExecutionListener::class,
    ResetMocksTestExecutionListener::class,
)
@ContextConfiguration(classes = [DefaultMockTestConfiguration::class])
abstract class KtBaseSpringMockTest : KtBaseSpringTest()