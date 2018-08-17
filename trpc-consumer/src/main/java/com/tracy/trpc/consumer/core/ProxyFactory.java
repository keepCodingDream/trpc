package com.tracy.trpc.consumer.core;

import com.tracy.trpc.consumer.strategy.IStrategy;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author tracy.
 * @create 2018-08-17 16:33
 **/
public class ProxyFactory<T> implements FactoryBean<T> {

    private Class<T> interfaceClass;

    private IStrategy strategy;

    private String interfaceName;

    public ProxyFactory() {

    }

    public ProxyFactory(Class<T> interfaceClass, IStrategy strategy, String interfaceName) {
        this.interfaceClass = interfaceClass;
        this.strategy = strategy;
        this.interfaceName = interfaceName;
    }

    public void setStrategy(IStrategy strategy) {
        this.strategy = strategy;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }


    @Override
    public T getObject() throws Exception {
        return new ProxyBuilder(strategy, interfaceName).newMapperProxy(interfaceClass);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
