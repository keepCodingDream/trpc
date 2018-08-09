package com.tracy.trpc.common.model;

import lombok.Data;

import java.util.Map;

/**
 * Created by lurenjie on 2017/6/12
 */
@Data
public class InvokeModel {
    private TProtocol tProtocol = TProtocol.NETTY;
    private String servicePath;
    private Object[] invokeParams;
    private Map<String, String> extraParams;
}
