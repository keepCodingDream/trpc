package com.tracy.trpc.demo.add;

import com.tracy.trpc.common.annotation.Provider;
import com.tracy.trpc.demo.base.DemoInterface;

import java.io.Serializable;

/**
 * Created by lurenjie on 2017/6/8
 */
@Provider
public class DemoClass2 implements DemoInterface, Serializable {
    @Override
    public String sayHello(String name) {
        return "hello2 " + name;
    }
}
