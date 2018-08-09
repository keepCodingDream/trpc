package com.tracy.trpc.protocol.action;

import com.tracy.trpc.common.model.TProtocol;

/**
 * Created by lurenjie on 2017/6/12
 */
public interface Protocol {
    /**
     * Get the type of protocol
     *
     * @return the type of protocol
     */
    TProtocol getProtocolType();
}
