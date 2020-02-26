package com.tryingpfq.rpc;

import com.tryingpfq.rpc.data.RpcCons;
import com.tryingpfq.rpc.service.ProviderServiceFacotry;
import com.tryingpfq.rpc.service.RpcInvoerMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    public void start() throws Exception {
        final ServerSocket server = new ServerSocket(RpcCons.PORT);
        LOGGER.info("start server port:[{}]",RpcCons.PORT);
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
                            Object bean = ProviderServiceFacotry.getBean(msg.getBeanName());
                            if(bean == null){
                                System.err.println("bean is null");
                            }
                            LOGGER.info("{}",msg.toString());
                            Object[] arguments = (Object[]) input.readObject();

                            Class<?>[] parameterTypes = (Class<?>[]) input.readObject();

                            // 不知道为什么 msg中的class[] 不能写过来
                            Method method = bean.getClass().getMethod(msg.getMethodName(), parameterTypes);
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
        }
    }
}
