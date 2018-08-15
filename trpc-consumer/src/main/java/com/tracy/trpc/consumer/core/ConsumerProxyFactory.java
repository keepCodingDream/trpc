package com.tracy.trpc.consumer.core;

import com.tracy.trpc.common.model.NodeInfo;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * @author tracy.
 * @create 2018-08-13 16:32
 **/
public class ConsumerProxyFactory implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void createProxy(String appName, List<NodeInfo> nodeInfo) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
