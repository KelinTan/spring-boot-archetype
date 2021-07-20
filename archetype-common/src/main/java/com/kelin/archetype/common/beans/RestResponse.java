// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kelin Tan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {
    private T result;
}
