package com.tracy.trpc.register.register;

import com.tracy.trpc.common.model.NodeInfo;

/**
 * Created by lurenjie on 2017/6/8
 */
public interface Register {
    /**
     * 注册服务
     *
     * @param info info of this app
     * @throws Exception if register in fail
     */
    void registry(NodeInfo info) throws Exception;

    /**
     * 暂停服务
     *
     * @throws Exception if set status is fail
     */
    void suspend() throws Exception;

    /**
     * 恢复服务
     *
     * @throws Exception if set status is fail
     */
    void recovery() throws Exception;

    /**
     * 取消注册
     *
     * @throws Exception if operation is fail
     */
    void unRegistry() throws Exception;
}
