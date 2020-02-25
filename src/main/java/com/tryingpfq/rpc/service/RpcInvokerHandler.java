package com.tryingpfq.rpc.service;

import com.tryingpfq.rpc.data.RpcCons;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class RpcInvokerHandler implements InvocationHandler {
    private Class<?> intefaceClass;

    public RpcInvokerHandler(Class<?> intefaceClass) {
        this.intefaceClass = intefaceClass;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Socket socket = new Socket(RpcCons.HOST_NAME, RpcCons.PORT);
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            try {
                String beanName = intefaceClass.getName();
                RpcInvoerMsg msg = new RpcInvoerMsg.Builder().setBeanName(beanName)
                        .setMethodName(method.getName())
                        .setParameterTypes(method.getParameterTypes()).build();
                output.writeObject(msg);
                output.writeObject(args);

                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                try {
                    Object result = input.readObject();
                    if (result instanceof Throwable) {
                        throw (Throwable) result;
                    }
                    return result;
                } finally {
                    input.close();
                }
            } finally {
                output.close();
            }
        } finally {
            socket.close();
        }
    }
}
