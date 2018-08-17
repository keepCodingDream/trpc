package com.tracy.trpc.demo.base;

import com.tracy.trpc.common.annotation.Provider;
import com.tracy.trpc.common.demo.Demo;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by lurenjie on 2017/6/8
 */
@Service
@Provider
public class DemoImpl implements Demo, Serializable {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
