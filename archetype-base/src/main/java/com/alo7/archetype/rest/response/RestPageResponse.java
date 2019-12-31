// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.rest.response;

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
    private int totalCount;
}
