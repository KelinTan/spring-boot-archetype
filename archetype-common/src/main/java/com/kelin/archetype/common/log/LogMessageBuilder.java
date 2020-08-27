// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.log;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kelin Tan
 */
public class LogMessageBuilder {
    private String message;
    private List<String> parameters = new ArrayList<>();

    private LogMessageBuilder() {
    }

    public static LogMessageBuilder builder() {
        return new LogMessageBuilder();
    }

    public LogMessageBuilder message(String message) {
        this.message = message;
        return this;
    }

    public LogMessageBuilder parameter(String name, Object value) {
        parameters.add(String.format("%s=%s", name, value));
        return this;
    }

    public String build() {
        if (parameters.isEmpty()) {
            return message;
        } else {
            return message
                    + " Parameters: "
                    + StringUtils.join(parameters, ",");
        }
    }

    public String toString() {
        return build();
    }
}
