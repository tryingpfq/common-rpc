package com.tryingpfq.rpc;

import com.tryingpfq.rpc.data.RpcCons;
import com.tryingpfq.rpc.service.ProviderServiceFacotry;
import com.tryingpfq.rpc.service.RpcInvoerMsg;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class RpcServer {
    public void start() throws Exception {
        final ServerSocket server = new ServerSocket(RpcCons.PORT);
        while (true) {
            final Socket socket = server.accept();
            new Thread(new Thread(){
                public void run() {
                    try {
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream()) ;
                        try {
                            Object obj = input.readObject();
                            if (!(obj instanceof RpcInvoerMsg)) {
                                System.err.println("err the obj is not msg");
                            }
                            RpcInvoerMsg msg = (RpcInvoerMsg) obj;
                            System.out.println("beanName");
                            Object bean = ProviderServiceFacotry.getBean(msg.getBeanName());
                            if(bean == null){
                                System.err.println("bean is null");
                            }
                            Object[] arguments = (Object[]) input.readObject();
                            Method method = bean.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
                            Object result = method.invoke(bean, arguments);
                            output.writeObject(result);
                        } catch (Throwable t) {
                            output.writeObject(t);
                        } finally {
                            output.close();
                            input.close();
                            socket.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            System.err.println("server start port " + RpcCons.PORT);
        }
    }
}
