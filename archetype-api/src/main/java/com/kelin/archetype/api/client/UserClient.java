// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.client;

import com.kelin.archetype.base.rest.response.RestResponse;
import com.kelin.archetype.base.rpc.HttpMethod;
import com.kelin.archetype.base.rpc.RpcClient;
import com.kelin.archetype.common.entity.primary.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Kelin Tan
 */
@RpcClient(endpoint = "${client.endpoint.user}")
public interface UserClient {
    @HttpMethod(value = "/api/v1/user/findAll/error", method = RequestMethod.GET)
    RestResponse<List<User>> findAllError();

    @HttpMethod(value = "/api/v1/user/findAll", method = RequestMethod.GET)
    RestResponse<List<User>> findAll();

    @HttpMethod(value = "/api/v1/user/{id}", method = RequestMethod.GET)
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
