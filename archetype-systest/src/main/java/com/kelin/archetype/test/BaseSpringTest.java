// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.test;

import com.kelin.archetype.common.constants.Profile;
import com.kelin.archetype.test.listener.DatabaseTestExecutionListener;
import com.kelin.archetype.test.listener.SystemPropertyTestExecutionListener;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

/**
 * @author Kelin Tan
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles(Profile.PROFILE_TEST)
@TestExecutionListeners({ServletTestExecutionListener.class, DirtiesContextBeforeModesTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        MockitoTestExecutionListener.class,
        ResetMocksTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class,
        SystemPropertyTestExecutionListener.class,
        DatabaseTestExecutionListener.class,
})
public abstract class BaseSpringTest {
}
