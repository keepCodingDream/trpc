package com.tracy.trpc.consumer.core;

import org.springframework.context.ApplicationContext;

/**
 * @author tracy.
 * @create 2018-08-17 10:33
 **/
public class SpringContext {
    private static ApplicationContext applicationContext;

    public static void set(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
