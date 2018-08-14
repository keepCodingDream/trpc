package com.tracy.trpc.common.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tracy.
 * @create 2018-08-09 16:27
 **/
public class DefaultProxyContext implements TContext {

    private Map<String, Object> context = new ConcurrentHashMap<>(16);

    private static volatile DefaultProxyContext INSTANTS = null;

    private DefaultProxyContext() {
    }

    public static DefaultProxyContext getInstants() {
        if (INSTANTS == null) {
            synchronized (DefaultProxyContext.class) {
                if (INSTANTS == null) {
                    INSTANTS = new DefaultProxyContext();
                }
            }
        }
        return INSTANTS;
    }

    @Override
    public <T> void setBean(Class<T> cls, String interfaceName) {
        context.put(interfaceName, CglibProxy.getInstants().getProxyInstance(cls));
    }

    @Override
    public Object getBean(String interfaceName) {
        return context.get(interfaceName);
    }
}
