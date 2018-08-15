package com.tracy.trpc.demo.add;

import com.tracy.trpc.common.annotation.Provider;
import com.tracy.trpc.common.demo.Demo;

import java.io.Serializable;

/**
 * Created by lurenjie on 2017/6/8
 */
@Provider
public class DemoClass2 implements Demo, Serializable {
    @Override
    public String sayHello(String name) {
        return "hello2 " + name;
    }
}
