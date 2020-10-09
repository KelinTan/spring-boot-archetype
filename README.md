[English](./README_EN.md) | 简体中文


[![CI](https://github.com/KelinTan/spring-boot-archetype/workflows/Java%20CI/badge.svg)](https://github.com/KelinTan/spring-boot-archetype)
[![codecov](https://codecov.io/gh/KelinTan/spring-boot-archetype/branch/master/graph/badge.svg)](https://codecov.io/gh/KelinTan/spring-boot-archetype)

# Spring Boot 脚手架

1. `Gradle5` + `Spring Boot 2.0.9.RELEASE` + `Mybatis 3.5` + `Kotlin 1.4` 搭建的Web服务脚手架

2. `@MybatisDatabase` 注入Mybatis SqlSession & DataSource && ...支持多数据源

3. `@RpcClient`代理HttpClient, 通过接口调用外部Http请求

4. `@MockDatabase`用于单元测试 & 集成测试，采用内存数据库H2进行数据库测试

5. 集成`Checkstyle`用于代码静态检查

6. 集成`Jacoco`的单元测试覆盖率

7. `JWT`支持

8. Mybatis `BasicCrudMapper` 用于单表的CRUD

9. 统一异常处理，统一Rest请求返回格式

10. `Kotlin`用于单元测试，更简洁，可读性更好

11. 集成`lettuce` & `Redisson`的Redis客户端，支持 `sync|async`的redis操作及分布式锁


