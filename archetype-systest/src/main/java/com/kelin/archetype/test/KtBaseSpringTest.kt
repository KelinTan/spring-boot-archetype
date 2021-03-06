// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.test

import com.kelin.archetype.common.constants.Profile.PROFILE_TEST
import com.kelin.archetype.test.database.DatabaseTestExecutionListener
import org.junit.runner.RunWith
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.context.web.ServletTestExecutionListener

/**
 * @author Kelin Tan
 */
@RunWith(SpringRunner::class)
@ActiveProfiles(PROFILE_TEST)
@TestExecutionListeners(
    ServletTestExecutionListener::class,
    DirtiesContextBeforeModesTestExecutionListener::class,
    DependencyInjectionTestExecutionListener::class,
    DirtiesContextTestExecutionListener::class,
    MockitoTestExecutionListener::class,
    SqlScriptsTestExecutionListener::class,
    DatabaseTestExecutionListener::class
)
@ContextConfiguration(classes = [DefaultTestConfiguration::class])
open class KtBaseSpringTest : KtTestUtils