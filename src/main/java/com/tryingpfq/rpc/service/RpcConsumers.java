package com.tryingpfq.rpc.service;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class RpcConsumers {
    private static ConcurrentHashMap<Class, Object> proxyCache = new ConcurrentHashMap<Class, Object>();

    public static Object createProxy(final Class<?> intefaceClass) {
        Object o = proxyCache.computeIfAbsent(intefaceClass,(i) -> {
            return Proxy.newProxyInstance(intefaceClass.getClassLoader(), new Class<?>[]{intefaceClass}, new RpcInvokerHandler(intefaceClass));
        });
        return o;
    }
}
