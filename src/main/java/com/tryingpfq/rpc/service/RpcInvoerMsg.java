package com.tryingpfq.rpc.service;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class RpcInvoerMsg implements Serializable {
    private String beanName;

    private String methodName;

    private Class<?>[] parameterTypes;

    public RpcInvoerMsg(Builder builder) {
        this.beanName = builder.getBeanName();
        this.methodName = builder.getMethodName();
    }

    public String getBeanName() {
        return beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public static class Builder{
        private String beanName;

        private String methodName;

        private Class<?>[] parameterTypes;

        public Builder setBeanName(String name) {
            this.beanName = name;
            return this;
        }

        public Builder setMethodName(String name) {
            this.methodName = name;
            return this;
        }

        public Builder setParameterTypes(Class<?>[] parameterTypes) {
            this.parameterTypes = parameterTypes;
            return this;
        }

        public RpcInvoerMsg build() {
            if (StringUtils.isEmpty(beanName)
                    || StringUtils.isEmpty(methodName)) {
                System.err.println("argument error");
                return null;
            }
            return new RpcInvoerMsg(this);
        }

        public String getBeanName() {
            return beanName;
        }

        public String getMethodName() {
            return methodName;
        }
    }

    @Override
    public String toString() {
        return "RpcInvoerMsg{" +
                "beanName='" + beanName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                '}';
    }
}
