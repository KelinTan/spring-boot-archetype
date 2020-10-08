// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.cache;

import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author Kelin Tan
 */
@Service
public class DemoServiceImpl implements DemoService {
    private static HashMap<Integer, Demo> dataMap = new HashMap<>();

    static {
        dataMap.put(1, new Demo(1, "name_1"));
        dataMap.put(2, new Demo(1, "name_2"));
    }

    @Override
    public Demo getDemo(int id) {
        return dataMap.get(id);
    }

    @Override
    public Demo insert(Demo demo) {
        dataMap.put(demo.getId(), demo);
        return demo;
    }

    @Override
    public void delete(int id) {
        dataMap.remove(id);
    }
}
