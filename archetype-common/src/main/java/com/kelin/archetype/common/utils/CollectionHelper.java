// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Kelin Tan
 */
public class CollectionHelper {
    private static final Integer DEFAULT_BATCH_SIZE = 100;

    public static <T, R> List<R> batchCall(List<T> list, Function<List<T>, R> caller) {
        return batchCall(list, DEFAULT_BATCH_SIZE, caller);
    }

    public static <T, R> List<R> batchCall(List<T> list, int batchSize, Function<List<T>, R> caller) {
        if (CollectionUtils.isEmpty(list) || batchSize <= 0) {
            return Collections.emptyList();
        }

        List<R> result = new ArrayList<>();
        if (list.size() <= batchSize) {
            result.add(caller.apply(list));
            return result;
        }

        int fromIndex = 0;
        while (fromIndex < list.size()) {
            //copy new list,do not operate sub list
            int endIndex = fromIndex + batchSize;
            List<T> subList = Lists.newArrayList(list.subList(fromIndex, Math.min(endIndex, list.size())));
            result.add(caller.apply(subList));
            fromIndex = fromIndex + batchSize;
        }
        return result;
    }

    public static <T> void batchConsume(List<T> list, int batchSize, Consumer<List<T>> consumer) {
        if (CollectionUtils.isEmpty(list) || batchSize <= 0) {
            return;
        }

        if (list.size() <= batchSize) {
            consumer.accept(list);
        }

        int fromIndex = 0;
        while (fromIndex < list.size()) {
            int endIndex = fromIndex + batchSize;
            //copy new list,do not operate sub list
            List<T> subList = Lists.newArrayList(list.subList(fromIndex, Math.min(endIndex, list.size())));
            consumer.accept(subList);
            fromIndex = fromIndex + batchSize;
        }
    }

    public static <T> void batchConsume(List<T> list, Consumer<List<T>> consumer) {
        batchConsume(list, DEFAULT_BATCH_SIZE, consumer);
    }
}
