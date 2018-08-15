package com.tracy.trpc.common.context;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * @author tracy.
 * @create 2018-08-14 15:23
 **/
public class DefaultCallBackFilter implements CallbackFilter {
    @Override
    public int accept(Method method) {
        return 0;
    }
}
