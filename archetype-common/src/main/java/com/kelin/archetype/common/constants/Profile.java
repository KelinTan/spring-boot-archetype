// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.constants;

/**
 * @author Kelin Tan
 */
public class Profile {
    public static final String PROFILE_TEST = "test";
    public static final String PROFILE_DEV = "dev";
    public static final String PROFILE_BETA = "beta";
    public static final String PROFILE_STAGING = "staging";
    public static final String PROFILE_PROD = "prod";

    public static volatile String activeProfile = "dev";

    public static boolean isTest() {
        return activeProfile.equals(PROFILE_TEST);
    }

    public static boolean isProd() {
        return activeProfile.equals(PROFILE_PROD);
    }
}
