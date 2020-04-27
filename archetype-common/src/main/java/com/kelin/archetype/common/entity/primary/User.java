// Copyright 2018 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.entity.primary;

import com.kelin.archetype.base.mybatis.crud.BasicEntity;
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
