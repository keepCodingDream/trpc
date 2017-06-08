package com.tracy.trpc.common.annotation;

import com.tracy.trpc.common.model.TLoadBalance;
import com.tracy.trpc.common.model.TProtocol;

import java.lang.annotation.*;

/**
 * 该注解需定义在接口上,我们会自动寻找它的实现类
 * Created by lurenjie on 2017/6/7
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Provider {
    /**
     * 自定义Provider的名字,默认是接口名
     */
    String value() default "";

    String ip() default "";

    String port() default "9200";

    /**
     * 服务提供的协议
     */
    TProtocol protocol() default TProtocol.NETTY;

    /**
     * 负载均衡算法
     */
    TLoadBalance loadBalanceType() default TLoadBalance.ROLLING;
}
