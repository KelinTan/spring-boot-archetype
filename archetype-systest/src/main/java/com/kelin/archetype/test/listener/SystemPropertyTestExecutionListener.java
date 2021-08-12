// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.test.listener;

import com.kelin.archetype.common.constants.Profile;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * @author Kelin Tan
 */
public class SystemPropertyTestExecutionListener extends AbstractTestExecutionListener {
    @Override
    public void beforeTestClass(@NotNull TestContext testContext) {
        Profile.activeProfile = Profile.PROFILE_TEST;
        //TODO other
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
