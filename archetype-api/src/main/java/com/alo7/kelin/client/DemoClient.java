// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.client;

import com.alo7.kelin.entity.User;
import com.alo7.kelin.rpc.HttpMethod;
import com.alo7.kelin.rpc.RpcClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Kelin Tan
 */
@RpcClient(host = "${client.endpoint.demo}")
public interface DemoClient {
    @HttpMethod(value = "/user/findAll", method = RequestMethod.GET)
    List<User> findAll();

    @HttpMethod(value = "/user/{id}", method = RequestMethod.GET)
    User findUser(@PathVariable(value = "id") Long id);

    @HttpMethod(value = "/user/findUser", method = RequestMethod.GET)
    User findUser2(@RequestParam(value = "id") Long id);

    @HttpMethod(value = "/user/save", method = RequestMethod.POST)
    User save(@RequestParam(value = "name") String name);

    @HttpMethod(value = "/user/save2", method = RequestMethod.POST)
    User save2(@RequestBody User user);

    @HttpMethod(value = "/user/save3", method = RequestMethod.PUT)
    User save3(@RequestBody User user);

    @HttpMethod(value = "/user/{id}", method = RequestMethod.DELETE)
    void delete(@PathVariable(value = "id") Long id);

    @HttpMethod(value = "/user/delete", method = RequestMethod.DELETE)
    void delete2(@RequestParam(value = "id") Long id);
}
