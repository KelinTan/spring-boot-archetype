// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.utils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Kelin Tan
 */
public class ProxyUtils {
    private static final Constructor<MethodHandles.Lookup> METHOD_HANDLER_CONSTRUCTOR;

    static {
        try {
            METHOD_HANDLER_CONSTRUCTOR = MethodHandles.Lookup.class
                    .getDeclaredConstructor(Class.class, int.class);
            METHOD_HANDLER_CONSTRUCTOR.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object safeInvokeDefaultMethod(Object proxy, Method method, Object[] args) {
        if (!METHOD_HANDLER_CONSTRUCTOR.isAccessible()) {
            METHOD_HANDLER_CONSTRUCTOR.setAccessible(true);
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        try {
            return METHOD_HANDLER_CONSTRUCTOR.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                    .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable.getLocalizedMessage());
        }
    }
}
