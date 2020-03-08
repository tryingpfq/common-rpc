## common-rpc

> 一个简单的rpcdemo,主要用来练习一下，顺便用到jdk基于接口的代理模式，和一个build模式。



### 通信

 是基于Netty通信，协议是通过传输java对象（Msg：包含服务名字，方法名称，方法参数，参数值）然后用默认的jdk 序列化和反序列化。

~~~java
 InvokerMsg
     private String beanName;
     private String methodName;
     private Class<?>[] parameterTypes;  //方法参数
     private Object[] values;    //参数值
~~~

### 代理

服务由服务端进行注册，然后服务端启动一个端口，专门监听消费端的请求，其业务处理是在RegisterHandler中。

~~~java
 Object result;
        //invoker
        RpcInvokerMsg reqMsg = (RpcInvokerMsg) msg;
        Object bean = ProviderServiceFacotry.getBean(reqMsg.getBeanName());
        if (bean == null) {
            LOGGER.warn("provider is null,provider Name is:{}", 		                            reqMsg.getBeanName());
            return;
        }
        Method method = bean.getClass().getMethod(reqMsg.getMethodName(),                               reqMsg.getParameterTypes());
        result = method.invoke(bean, reqMsg.getValues());

        ctx.channel().writeAndFlush(result);
~~~

客户端是通过给每个远程接口生成伪代理，然后调用是在InvokeHander中实现，启动netty客户端，用builder模式构建一个sendMsg,
把接口信息发送到远程，并监听返回，最后把结果给上层业务。



### TODO

还有很多地方需要处理，这个以后慢慢完善吧。

* 比如客户端并不是每次调用的时后，都去启动一个新的channel，而是根据远程地址进行缓存（这又会有一个问题，当这个链接关闭后，如何进行重连，是通过心跳还是怎么去判断）,同样服务端也需要。
* 协议
* 消费端接口缓存注解进行注册。
* .......

