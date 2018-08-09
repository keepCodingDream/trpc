package com.tracy.trpc.protocol.action;

import com.tracy.trpc.common.model.TConfig;
import com.tracy.trpc.common.model.TProtocol;

/**
 * Created by lurenjie on 2017/6/12
 */
public abstract class AbstractProtocol implements Protocol {
    private TProtocol protocol;
    private Runnable stopProtocol;

    public AbstractProtocol(TProtocol protocol) {
        this.protocol = protocol;
    }

    public void export(TConfig config) throws Exception {
        stopProtocol = startService(config);
    }

    public void destroy() {
        new Thread(stopProtocol).start();
    }

    public abstract Runnable startService(TConfig config) throws Exception;

    public TProtocol getProtocolType() {
        return protocol;
    }

}
