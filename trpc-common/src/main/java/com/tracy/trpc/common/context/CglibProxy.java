package com.tracy.trpc.common.context;


import net.sf.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * @author tracy.
 * @create 2018-08-09 16:18
 **/
public class CglibProxy implements MethodInterceptor {

    private CglibProxy() {
    }

    private volatile static CglibProxy context;

    public static CglibProxy getInstants() {
        if (context == null) {
            synchronized (CglibProxy.class) {
                if (context == null) {
                    context = new CglibProxy();
                }
            }
        }
        return context;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxyInstance(Class<T> target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target);
        /**
         * Set the different callback instance. When CallbackFilter return 1
         * will execute the index 1 of Callback[]……
         * <p>
         * actually the method will execute any index of Callback[]'s instance
         * whatever AdminSayCallBackFilter returned
         * <p>
         * 'NoOp.INSTANCE' means do noting
         */
        enhancer.setCallbacks(new Callback[]{this, NoOp.INSTANCE});
        //这里可以添加多个filter提供扩展
        enhancer.setCallbackFilter(new DefaultCallBackFilter());
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object object, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        System.out.println("call method name:" + method.getName());
        Object result = methodProxy.invokeSuper(object, params);
        System.out.println("Invoke done");
        return result;
    }
}
