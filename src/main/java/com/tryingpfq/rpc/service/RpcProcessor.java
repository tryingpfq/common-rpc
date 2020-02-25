package com.tryingpfq.rpc.service;

import com.tryingpfq.rpc.annotion.Consumer;
import com.tryingpfq.rpc.annotion.Provider;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
@Component
public class RpcProcessor implements BeanPostProcessor {


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = AopUtils.getTargetClass(bean);
        Provider provider = clazz.getAnnotation(Provider.class);
        if (provider != null) {
            System.err.println("addCache  " + beanName);
            ProviderServiceFacotry.addBeanCache(beanName, bean);
        }

        for (Field field : clazz.getDeclaredFields()) {
            Consumer consumer = field.getAnnotation(Consumer.class);
            if (consumer == null) {
                continue;
            }
            Class<?> type = field.getType();
            field.setAccessible(true);

            Object object = RpcConsumers.createProxy(type);
            try {
                field.set(bean,object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return bean;
    }
}
