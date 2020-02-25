package com.tryingpfq.rpc.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class ProviderServiceFacotry<T> {
    private Class<T> interfactType;

    private static ConcurrentHashMap beans = new ConcurrentHashMap<String,Object>();


    public static void addBeanCache(String name, Object object) {
        beans.put(name, object);
    }

    public static Object getBean(String name) {
        return beans.get(name);
    }
}
