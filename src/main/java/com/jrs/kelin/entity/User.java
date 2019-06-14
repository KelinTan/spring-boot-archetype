// Copyright 2018 Alo7 Inc. All rights reserved.
package com.jrs.kelin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kelin on 2018/9/30.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userName;
    private Long id;
}
