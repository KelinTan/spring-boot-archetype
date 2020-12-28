// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.entity.biz;

import com.kelin.archetype.core.mybatis.crud.BasicEntity;
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
public class IdSharding extends BasicEntity {
    private String data;
}
