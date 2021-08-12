// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing;

import com.kelin.archetype.common.constants.Profile;
import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;

/**
 * @author Kelin Tan
 */
public class CurrentTracer {
    private CurrentTracer() {
    }

    public static Tracer get() {
        if (Profile.isTest()) {
            return new MockTracer();
        } else {
            return GlobalTracer.get();
        }
    }
}
