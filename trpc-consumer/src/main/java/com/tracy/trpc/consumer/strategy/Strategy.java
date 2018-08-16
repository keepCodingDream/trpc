package com.tracy.trpc.consumer.strategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义负载均衡策略类型
 *
 * @author tracy.
 * @create 2018-08-16 10:25
 **/
@Target(ElementType.TYPE)//表示只能给类添加该注解
@Retention(RetentionPolicy.RUNTIME)
public @interface Strategy {
    String value() default "";
}
