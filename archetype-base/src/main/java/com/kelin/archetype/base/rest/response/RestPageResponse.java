// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.base.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Kelin Tan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestPageResponse<T> {
    private List<T> results;
    private int pageNo;
    private int pageSize;
    private long totalCount;
}
