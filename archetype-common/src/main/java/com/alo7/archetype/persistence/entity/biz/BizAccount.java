// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.persistence.entity.biz;

import lombok.Data;

/**
 * @author Kelin Tan
 */
@Data
public class BizAccount {
    private Long id;
    private String account;
    private String password;
    private String token;
}
