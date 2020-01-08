// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.persistence.entity.biz;

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
public class BizAccount {
    private Long id;
    private String account;
    private String password;
    private String token;
}
