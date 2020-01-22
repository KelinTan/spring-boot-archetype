// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.common.entity.biz;

import com.alo7.archetype.base.mybatis.crud.BasicEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Kelin Tan
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizAccount extends BasicEntity {
    private String account;
    private String password;
    private String token;
    private Integer age;
    private Long height;
    private BigDecimal money;
    private LocalDateTime birthDate;
    private Boolean verify;
}
