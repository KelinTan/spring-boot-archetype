// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.entity.biz;

import com.kelin.archetype.database.mybatis.crud.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Kelin Tan
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdSharding extends BaseEntity {
    private String data;
}
