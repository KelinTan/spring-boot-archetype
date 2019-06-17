// Copyright 2019 Alo7 Inc. All rights reserved.

package com.jrs.kelin;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Kelin Tan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test") //test环境
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional //添加Transactional注解进行数据回滚，隔离多个单元测试环境
@Ignore
public class BaseTest {
    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Before
    public void setUp() {
        //注入mockMvc用于rest api测试
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //初始化mock注解支持
        MockitoAnnotations.initMocks(this);
    }
}
