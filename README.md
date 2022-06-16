[English](./README_EN.md) | 简体中文


[![CI](https://github.com/KelinTan/spring-boot-archetype/workflows/Java%20CI/badge.svg)](https://github.com/KelinTan/spring-boot-archetype)
[![codecov](https://codecov.io/gh/KelinTan/spring-boot-archetype/branch/master/graph/badge.svg)](https://codecov.io/gh/KelinTan/spring-boot-archetype)

# Spring Boot 脚手架

[TOC]

## 简介
spring-boot-archetype基于`Spring Boot`,`Mybatis`,`Kotlin`搭建，在此基础上优化改进，提供数据库、Rpc接口、Redis、缓存、单元测试等的自动注入，集成`CheckStyle`代码静态检查、单元测试、集成测试,简化开发

## 数据库

### 配置

基于注解 `MybatisDatabase` 配置相关数据库连接信息,`MybatisDatabaseRegistrar` 自动注册`DataSource`、`SqlSession`  、`TransactionalManager`、`SqlSessionTemplate`  等相关类
例如：
```java
/**
 * @author Kelin Tan
 */
@Configuration
@MybatisDatabase(name = NAME,
        databaseUrl = "${spring.datasource.biz.jdbc-url}",
        databaseUserName = "${spring.datasource.biz.username}",
        databasePassword = "${spring.datasource.biz.password}",
        mapperPackages = "com.kelin.archetype.database.mapper.biz",
        mapperXmlLocation = "classpath:mappers/biz/*.xml",
        typeAliasesPackage = "com.kelin.archetype.database.entity.biz",
        schemaLocation = "classpath:schema/biz/*.sql",
        dataLocation = "classpath:data/biz/*.sql"
)
public class BizDatabase {
    public static final String NAME = "Biz";
}
```

属性配置如下：

| 配置 | 说明  |
| --- | --- |
| name | 数据源名称，多个数据源名称不能重复 |
| databaseUrl | 数据库连接地址，支持Spring EL表达式  |
| databaseUserName | 数据库连接账号，支持Spring El表达式  |
| databasePassword | 数据库连接密码，支持Spring El表达式  |
| mapperPackages | Mybatis需要扫描的mapper包路径  |
| mapperXmlLocation | Mybatis需要扫描的xml路径  |
| typeAliasesPackage | Mybatis需要扫描的别名路径  |
| schemaLocation | 单元测试初始化的数据库schema SQL  |
| dataLocation | 单元测试初始化的数据库data SQL  |

### MapperTable

提供 `MapperTable` 注解替换Mybatis自带的`Mapper`注解

```java
/**
 * @author Kelin Tan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MapperTable {
    /**
     * Database table name
     */
    String value() default "";

    /**
     * Database table select columns,default * but not recommended
     */
    String columns() default "*";

    /**
     * Table sharding
     */
    boolean sharding() default false;

    /**
     * Table sharding key default id
     */
    String shardingKey() default "id";

    /**
     * Sharding Table count only when sharding = true
     */
    int count() default 0;

    /**
     * Sharding strategy
     */
    Class<? extends ShardingStrategy> shardingStrategy() default DefaultShardingStrategy.class;
}
```
主要提供以下功能

* 定义表名和相关列
* 根据定义的表和相关列提供 `BaseCrudMapper`的单表增删改查，基于Mybatis相关Provider实现，无需写简单的单表增删改查SQL
* 根据定义的表,提供单元测试中初始化和清理相关表数据的功能
* 支持分表,目前提供基于特定KEY取模和按年划分的分表策略，更多策略支持自定义扩展，实现 `ShardingStrategy` 即可


## RPC
基于注解 `RpcClient`,`RpcScan`,`HttpMethod` 实现通过接口访问远程服务,代码更简洁，支持统一的错误处理和HTTP配置

### 注入

`RpcClientScan` 指定package扫描,通过 `RpcClientRegistrar`注册相关`RpcClient`接口的代理到Spring容器
例如：
```java
@SpringBootApplication
@RpcClientScan(RPC_CLIENT_SCAN_PACKAGE)
public class SpringBootArchetypeApplication {
    public static void main(String[] args) {
        //to make GlobalException to handle NoHandlerFoundException
        System.setProperty("spring.mvc.throwExceptionIfNoHandlerFound", "true");
        System.setProperty("spring.mvc.staticPathPattern", "/swagger-ui.html");
        SpringApplication.run(SpringBootArchetypeApplication.class, args);
    }
}
```

### HTTP远程访问

`RpcClient` 标记相关类为Rpc的客户端

属性配置如下

| 配置 | 说明  |
| --- | --- |
| endpoint | 远程服务器地址，支持Spring EL表达式 |
| errorHandler | 错误处理，支持自定义实现处理4xx和5xx的请求  |

`HttpMethod`作用接口的方法，标记为一个远程请求,支持Spring MVC的相关Request注解，如：`PathVariable`,`RequestParam`,`RequestHeader`,`RequestBody`
支持`@HystrixCommand`注解提供熔断、降级等能力，详情见 Hystrix 相关文档
```java
// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kelin Tan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HttpMethod {
    /**
     * alias for path
     */
    @AliasFor("path")
    String value() default "";

    /**
     * Http path,join with {@link RpcClient#endpoint()}
     */
    @AliasFor("value")
    String path() default "";

    /**
     * @see org.springframework.web.bind.annotation.RequestMethod
     */
    RequestMethod method() default RequestMethod.GET;

    /**
     * Http request connect timeout milliseconds default 5000
     */
    int connectionTimeout() default 5000;

    /**
     * Http request read timeout milliseconds default 5000
     */
    int readTimeout() default 5000;

    /**
     * Http Request retry times
     */
    int retryTimes() default 1;

    /**
     * Use AsyncHttpClient to perform http request
     */
    boolean async() default false;
}

```
属性配置如下:

| 配置 | 说明  |
| --- | --- |
| value | 远程服务器路径，和endpoint组合为完整的请求地址 |
| path | value的别名  |
| method | HTTP请求方法  |
| connectionTimeout | 连接超时，单位ms  |
| readTimeout | 读取超时，单位ms  |
| retryTimes | 重试次数，默认不重试  |
| async | 是否异步调用  |

## 单测

### MockDatabase

`MockDatabase`注解用于相关单元测试类，基于`DatabaseTestExecutionListener` 可以初始化和清理相关数据源的数据，保证单元测试的独立和可重复执行，可指定特定的mapper或table来初始化提升单测速度，默认会对数据源整个数据库做初始化和清理,

### BaseSpringTest

继承`BaseSpringTest`提供基础的Spring容器测试能力,支持数据库的重置

### BaseSpringWebTest

继承`BaseSpringWebTest`提供基础的Spring Web容器测试能力,支持数据库的重置,可利用RPC的基础`HttpRequest`来实现对API的集成测试

### Kotlin支持

基于`KtTestUtils`的`KtBaseSpringTest`和`KtBaseSpringWebTest`的Kotlin单测基类，单测更简单，可读性更好
例如：
```kotlin
/**
 * @author Kelin Tan
 */
@MockDatabase(name = PrimaryDatabase.NAME)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [SpringBootArchetypeApplication::class]
)
class KtUserApiControllerWebTest : KtBaseSpringWebTest() {
    @Test
    fun `find all users with header`() {
        Http.withPath("$API_PREFIX/findAll")
            .performHeader()
            .status() eq HttpStatus.SC_OK
    }

    @Test
    fun `test find all users`() {
        Http.withPath("$API_PREFIX/findAll")
            .performGet()
            .json() verify {
            -"result" verify {
                size eq 4
                item(0) verify {
                    -"id" eq 1
                    -"id" not 2
                    "nId".node.isNullOrNone
                    +"userName" startsWith "test"
                    -"userName" endsWith "1"
                }
            }
        }
    }
```

## 其他

### Redis
集成`lettuce` & `Redisson`的Redis客户端，支持 `sync|async`的redis操作及分布式锁，内嵌`embedded-redis`提供集成测试,参见：`archetype-redis`

### Cache

集成`CaffeineCacheManager`提供更强性能的缓存工具

### JWT

集成`JWT`提供统一认证授权 参见：`archetype-jwt`

### Gateway

基于`spring-cloud-gateway`搭建的服务网关，支持全局异常处理，通用路由支持，参见：`archetype-demo-gateway`
