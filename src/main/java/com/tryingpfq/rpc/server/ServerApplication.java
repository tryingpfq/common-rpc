package com.tryingpfq.rpc.server;

import com.tryingpfq.rpc.RpcServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class ServerApplication {

    private static ApplicationContext context;

    static {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    public static void main(String[] args) {
        RpcServer server = new RpcServer();
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
