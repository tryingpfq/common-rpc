package com.tryingpfq.rpc.service;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class RpcInvokerMsg implements Serializable {
    private String beanName;

    private String methodName;

    private Class<?>[] parameterTypes;  //方法参数

    private Object[] values;    //参数值

    public RpcInvokerMsg(Builder builder) {
        this.beanName = builder.getBeanName();
        this.methodName = builder.getMethodName();
        this.parameterTypes = builder.getParameterTypes();
        this.values = builder.getValues();
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

    public Object[] getValues() {
        return values;
    }

    public static class Builder{
        private String beanName;

        private String methodName;

        private Class<?>[] parameterTypes;

        private Object[] values;

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

        public Builder setValues(Object[] values) {
            this.values = values;
            return this;
        }

        public Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

        public Object[] getValues() {
            return values;
        }

        public RpcInvokerMsg build() {
            if (StringUtils.isEmpty(beanName)
                    || StringUtils.isEmpty(methodName)) {
                System.err.println("argument error");
                return null;
            }
            return new RpcInvokerMsg(this);
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
