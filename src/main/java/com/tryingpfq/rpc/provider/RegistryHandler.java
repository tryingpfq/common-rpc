package com.tryingpfq.rpc.provider;

import com.tryingpfq.rpc.service.ProviderServiceFacotry;
import com.tryingpfq.rpc.service.RpcInvokerMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Author tryingpfq
 * @Date 2020/3/8
 */
public class RegistryHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result;
        //invoker
        RpcInvokerMsg reqMsg = (RpcInvokerMsg) msg;
        Object bean = ProviderServiceFacotry.getBean(reqMsg.getBeanName());
        if (bean == null) {
            LOGGER.warn("provider is null,provider Name is:{}", reqMsg.getBeanName());
            return;
        }
        Method method = bean.getClass().getMethod(reqMsg.getMethodName(), reqMsg.getParameterTypes());
        result = method.invoke(bean, reqMsg.getValues());

        ctx.channel().writeAndFlush(result);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
