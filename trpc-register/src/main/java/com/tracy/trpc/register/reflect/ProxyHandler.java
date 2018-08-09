package com.tracy.trpc.register.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by lurenjie on 2017/6/7
 */
@Slf4j
public class ProxyHandler implements InvocationHandler {
    private Object proxied;

    public ProxyHandler(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("ProxyHandler.invoke() before");
        Object result = method.invoke(proxied, args);
        System.out.println("ProxyHandler.invoke() after");
        return result;
    }
}
