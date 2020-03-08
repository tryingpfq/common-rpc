package com.tryingpfq.rpc.consumer;

import com.tryingpfq.rpc.RpcCons;
import com.tryingpfq.rpc.provider.RegistryHandler;
import com.tryingpfq.rpc.service.RpcInvokerMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class RpcConsumerProxy {
    private static ConcurrentHashMap<Class, Object> proxyCache = new ConcurrentHashMap<Class, Object>();

    public static Object createProxy(final Class<?> intefaceClass) {
        Object o = proxyCache.computeIfAbsent(intefaceClass,(i) -> {
            return Proxy.newProxyInstance(intefaceClass.getClassLoader(), new Class<?>[]{intefaceClass}, new RpcInvokerHandler(intefaceClass));
        });
        return o;
    }

    public static class RpcInvokerHandler implements InvocationHandler {
        private Class<?> intefaceClass;

        public  RpcInvokerHandler(Class<?> intefaceClass) {
            this.intefaceClass = intefaceClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            }
            return rpcInvoke(method, args);
        }

        public Object rpcInvoke(Method method,Object[] args) {
            EventLoopGroup group = new NioEventLoopGroup();
            RpcInvokerMsg sendMsg = buildMsg(method, args);
            Bootstrap b = new Bootstrap();
            RpcProxyHandler proxyHandler = new RpcProxyHandler();
            try {
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                ChannelPipeline pipeline = socketChannel.pipeline();

                                // 处理拆包，黏包问题
                                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                                pipeline.addLast(new LengthFieldPrepender(4));

                                //jdk 默认序列化
                                pipeline.addLast("encoder", new ObjectEncoder());
                                pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                                //业务handler
                                pipeline.addLast(proxyHandler);
                            }
                        });
                ChannelFuture future = b.connect("localhost",3322).sync();
                future.channel().writeAndFlush(sendMsg).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                group.shutdownGracefully();
            }
            return proxyHandler.getResult();
        }

        private RpcInvokerMsg buildMsg(Method method, Object[] args) {
            RpcInvokerMsg.Builder builder = new RpcInvokerMsg.Builder();
            final RpcInvokerMsg msg = builder.setBeanName(intefaceClass.getName())
                    .setMethodName(method.getName())
                    .setParameterTypes(method.getParameterTypes())
                    .setValues(args)
                    .build();
            return msg;
        }
    }
}
