package com.tracy.trpc.consumer.core;


import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.model.InvokeModel;
import com.tracy.trpc.consumer.strategy.IStrategy;
import com.tracy.trpc.protocol.rpc.RpcCall;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 构建动态代理类
 *
 * @author tracy.
 * @create 2018-08-09 16:18
 **/
public class ProxyBuilder implements InvocationHandler {

    private IStrategy strategy;

    private String interfaceName;

    private ProxyBuilder() {
    }


    public ProxyBuilder(IStrategy strategy, String interfaceName) {
        this.strategy = strategy;
        this.interfaceName = interfaceName;
    }

    public <T> T newMapperProxy(Class<T> mapperInterface) {
        ClassLoader classLoader = mapperInterface.getClassLoader();
        Class<?>[] interfaces = new Class[]{mapperInterface};
        return (T) Proxy.newProxyInstance(classLoader, interfaces, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("start call server");
        RpcCall call = strategy.select();
        InvokeModel invokeModel = new InvokeModel();
        invokeModel.setInterfaceName(interfaceName);
        invokeModel.setMethod(method.getName());
        invokeModel.setInvokeParams(args);
        return call.invoke(invokeModel);
    }
}
