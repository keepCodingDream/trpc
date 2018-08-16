package com.tracy.trpc.common.demo;

import com.tracy.trpc.common.annotation.Consumer;

/**
 * 仅做测试使用
 * Created by lurenjie on 2017/6/8
 */
@Consumer
public interface Demo {
    String sayHello(String name);
}
