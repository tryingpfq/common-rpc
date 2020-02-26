package com.tryingpfq;

import com.tryingpfq.rpc.CalculatorService;
import com.tryingpfq.rpc.RpcClient;
import com.tryingpfq.rpc.service.RpcInvokerHandler;

import java.lang.reflect.Proxy;

/**
 * @author tryingpfq
 * @date 2020/2/26
 **/
public class ProxyTest {

    public static void main(String[] args) {
        RpcClient client = new RpcClient();
        try {
            //CalculatorService refer = client.refer(CalculatorService.class);
            //System.err.println(refer);
            Object object = Proxy.newProxyInstance(CalculatorService.class.getClassLoader(), new Class<?>[]{CalculatorService.class}, new RpcInvokerHandler(CalculatorService.class));
            System.err.println(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
