package com.tracy.trpc.core;

import com.tracy.trpc.common.Constants;
import com.tracy.trpc.common.util.CommonUtil;
import com.tracy.trpc.consumer.core.SpringContext;
import com.tracy.trpc.core.consumer.ConsumerInitializer;
import com.tracy.trpc.core.provider.ProviderInitializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author tracy.
 * @create 2018-08-17 16:02
 **/
@Component
public class ContextInitializer implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Properties properties;
        try {
            properties = CommonUtil.getProperties("/trpc.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!StringUtils.isEmpty(properties.getProperty(Constants.PROVIDER_NAME))) {
            Initializer initializer = new ProviderInitializer();
            initializer.start(properties);
        }
        if (!StringUtils.isEmpty(properties.getProperty(Constants.CONSUMER_NAME))) {
            //这里先初始化一下SpringContext获取spring context
            Initializer initializer = new ConsumerInitializer();
            initializer.start(properties);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.set(applicationContext);
    }
}
