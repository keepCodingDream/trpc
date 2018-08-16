package com.tracy.trpc.consumer.core;


import com.tracy.trpc.common.context.DefaultCallBackFilter;
import com.tracy.trpc.common.model.InvokeModel;
import com.tracy.trpc.consumer.strategy.IStrategy;
import com.tracy.trpc.protocol.rpc.RpcCall;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author tracy.
 * @create 2018-08-09 16:18
 **/
public class ProxyFactory implements MethodInterceptor {

    private IStrategy strategy;

    private String interfaceName;

    private ProxyFactory() {
    }


    public ProxyFactory(IStrategy strategy, String interfaceName) {
        this.strategy = strategy;
        this.interfaceName = interfaceName;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxyInstance(Class<T> target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target);
        enhancer.setCallbacks(new Callback[]{this});
        //这里可以添加多个filter提供扩展
        enhancer.setCallbackFilter(new DefaultCallBackFilter());
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object object, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        System.out.println("start call server");
        RpcCall call = strategy.select();
        InvokeModel invokeModel = new InvokeModel();
        invokeModel.setInterfaceName(interfaceName);
        invokeModel.setMethod(method.getName());
        invokeModel.setInvokeParams(params);
        return call.invoke(invokeModel);
    }
}
