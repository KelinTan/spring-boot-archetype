// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.consants;

import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * @author Kelin Tan
 */
public class Profile {
    public static final String PROFILE_TEST = "test";
    public static final String PROFILE_DEV = "dev";
    public static final String PROFILE_STAGING = "staging";
    public static final String PROFILE_PROD = "prod";

    public static boolean isTest(Environment environment) {
        return Arrays.stream(environment.getActiveProfiles()).anyMatch(it -> it.equalsIgnoreCase(PROFILE_TEST));
    }
}
