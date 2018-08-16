package com.tracy.trpc.consumer.core;

import com.tracy.trpc.common.ClassHelper;
import com.tracy.trpc.common.Constants;
import com.tracy.trpc.common.annotation.Consumer;
import com.tracy.trpc.consumer.strategy.IStrategy;
import com.tracy.trpc.consumer.strategy.PollingStrategy;
import com.tracy.trpc.protocol.rpc.RpcCall;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author tracy.
 * @create 2018-08-13 16:32
 **/
public class ConsumerProxyFactory implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    /**
     * @param properties 基本配置信息
     * @param appName    应用名称
     * @param rpcCalls   远程调用句柄
     */
    public void createProxy(Properties properties, String appName, List<RpcCall> rpcCalls) {
        String basePackage = properties.getProperty(Constants.BASE_PACKAGE);
        Set<Class<?>> classes = ClassHelper.getClasses(basePackage);
        for (Class item : classes) {
            Consumer consumer = (Consumer) item.getAnnotation(Consumer.class);
            if (consumer != null && item.isInterface()) {
                String[] midArr = item.getName().split("[.]");
                String beanName = midArr[midArr.length - 1];
                IStrategy strategy = new PollingStrategy(rpcCalls);
                ProxyFactory factory = new ProxyFactory(strategy, item.getName());
                Object object = factory.getProxyInstance(item);
                //将applicationContext转换为ConfigurableApplicationContext
                ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
                // 获取bean工厂并转换为DefaultListableBeanFactory
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
                // 通过BeanDefinitionBuilder创建bean定义
                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(item);
                // 注册bean
                defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
            }
        }
    }


    private Object createProxy0(Properties properties, List<RpcCall> rpcCalls) {
        return null;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
