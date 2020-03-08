package com.tryingpfq.rpc.consumer;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class RpcClient {

    public <T> T refer(final Class<T> interfaceClass) throws Exception {

        if (interfaceClass == null)
            throw new IllegalArgumentException("Interface class == null");
        if (! interfaceClass.isInterface())
            throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");

        return (T) RpcConsumerProxy.createProxy(interfaceClass);
    }

}
