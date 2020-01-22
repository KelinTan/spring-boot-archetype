// Copyright 2018 Alo7 Inc. All rights reserved.

package com.alo7.archetype.common.entity.primary;

import com.alo7.archetype.base.mybatis.crud.BasicEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Kelin Tan
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BasicEntity {
    @NotBlank
    private String userName;
}
