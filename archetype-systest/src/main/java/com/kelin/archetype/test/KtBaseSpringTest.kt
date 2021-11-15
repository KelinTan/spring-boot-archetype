// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.test

import com.kelin.archetype.common.constants.Profile.PROFILE_TEST
import com.kelin.archetype.test.listener.DatabaseTestExecutionListener
import com.kelin.archetype.test.listener.SystemPropertyTestExecutionListener
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.web.ServletTestExecutionListener

/**
 * @author Kelin Tan
 */
@ExtendWith(SpringExtension::class)
@ActiveProfiles(PROFILE_TEST)
@TestExecutionListeners(
    DependencyInjectionTestExecutionListener::class,
    ServletTestExecutionListener::class,
    SqlScriptsTestExecutionListener::class,
    SystemPropertyTestExecutionListener::class,
    DatabaseTestExecutionListener::class
)
@ContextConfiguration(classes = [DefaultTestConfiguration::class])
abstract class KtBaseSpringTest : KtTestUtils