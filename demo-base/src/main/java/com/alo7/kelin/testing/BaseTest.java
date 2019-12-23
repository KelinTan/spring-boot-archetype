// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.testing;

import org.junit.Ignore;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * 添加Transactional注解进行数据回滚，隔离多个单元测试环境
 *
 * @author Kelin Tan
 */
@ActiveProfiles("test")
@Transactional
@Ignore
abstract class BaseTest {
}
