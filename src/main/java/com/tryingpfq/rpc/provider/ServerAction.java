package com.tryingpfq.rpc.provider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class ServerAction {

    private static ApplicationContext context;

    static {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    public static void main(String[] args) {
        RpcServer server = new RpcServer();
        try {
            server.start(3322);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
