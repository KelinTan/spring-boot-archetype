// Copyright 2018 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.entity.primary;

import com.kelin.archetype.database.mybatis.crud.BaseEntity;
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
public class User extends BaseEntity {
    @NotBlank
    private String userName;
}
