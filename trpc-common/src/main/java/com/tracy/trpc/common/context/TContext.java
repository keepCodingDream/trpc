package com.tracy.trpc.common.context;

/**
 * @author tracy.
 * @create 2018-08-09 16:11
 **/
public interface TContext {

    /**
     * 缓存动态代理对象
     *
     * @param cls           被代理类的class类型
     * @param interfaceName 接口名
     */
    <T> void setBean(Class<T> cls, String interfaceName);

    /**
     * 获取Bean的名字
     *
     * @param cls           被代理类的class类型
     * @param interfaceName 接口名
     * @return 代理类对象
     */
    <T> T getBean(Class<T> cls, String interfaceName);
}
