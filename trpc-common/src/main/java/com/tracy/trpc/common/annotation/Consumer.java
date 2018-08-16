package com.tracy.trpc.common.annotation;

import java.lang.annotation.*;

/**
 * 该注解需定义在接口上,我们会自动寻找它的实现类
 * Created by lurenjie on 2017/6/7
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Consumer {
    /**
     * 自定义Provider的名字,默认是接口名
     */
    String value() default "";

    /**
     * 服务版本
     */
    String version() default "0.0.1";
}
