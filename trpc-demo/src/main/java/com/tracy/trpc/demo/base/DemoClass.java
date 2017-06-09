package com.tracy.trpc.demo.base;

import com.tracy.trpc.common.annotation.Provider;

import java.io.Serializable;

/**
 * Created by lurenjie on 2017/6/8
 */
@Provider
public class DemoClass implements DemoInterface, Serializable {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
