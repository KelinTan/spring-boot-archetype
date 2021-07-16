// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.client;

import com.kelin.archetype.common.rest.response.RestResponse;
import com.kelin.archetype.common.rest.response.RestResponseFactory;
import com.kelin.archetype.core.rpc.HttpMethod;
import com.kelin.archetype.core.rpc.RpcClient;
import com.kelin.archetype.database.entity.primary.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

/**
 * @author Kelin Tan
 */
@RpcClient(endpoint = "${client.endpoint.user}")
public interface UserClient {
    @HttpMethod(value = "/api/v1/user/findAll/error")
    RestResponse<List<User>> findAllError();

    @HttpMethod(value = "/api/v1/user/findAll")
    RestResponse<List<User>> findAll() throws Exception;

    @HystrixCommand(groupKey = "user")
    @HttpMethod(value = "/api/v1/user/findAll/error")
    default RestResponse<List<User>> findAllErrorFallback() {
        return RestResponseFactory.success(Collections.singletonList(new User("fallback")));
    }

    @HttpMethod(value = "/api/v1/user/findAll", async = true)
    RestResponse<List<User>> findAllAsync();

    @HttpMethod(value = "/api/v1/user/{id}")
    RestResponse<User> findUser(@PathVariable(value = "id") Long id);

    @HttpMethod(value = "/api/v1/user/findUser", method = RequestMethod.GET)
    RestResponse<User> findUser2(@RequestParam(value = "id") Long id);

    @HttpMethod(value = "/api/v1/user/save", method = RequestMethod.POST)
    RestResponse<User> save(@RequestParam(value = "name") String name);

    @HttpMethod(value = "/api/v1/user/save2", method = RequestMethod.POST)
    RestResponse<User> save2(@RequestBody User user);

    @HttpMethod(value = "/api/v1/user/save3", method = RequestMethod.PUT)
    RestResponse<User> save3(@RequestBody User user);

    @HttpMethod(value = "/api/v1/user/{id}", method = RequestMethod.DELETE)
    void delete(@PathVariable(value = "id") Long id);

    @HttpMethod(value = "/api/v1/user/delete", method = RequestMethod.DELETE)
    void delete2(@RequestParam(value = "id") Long id);
}
