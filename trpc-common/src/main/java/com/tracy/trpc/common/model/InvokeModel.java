package com.tracy.trpc.common.model;

import lombok.Data;

import java.util.Map;

/**
 * Created by lurenjie on 2017/6/12
 */
@Data
public class InvokeModel {
    private TProtocol tProtocol = TProtocol.NETTY;
    private String interfaceName;
    private String method;
    private Object[] invokeParams;
    private Class<?>[] paramsCls;
    private Map<String, String> extraParams;
}
