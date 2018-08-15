package com.tracy.trpc.common.model;

import lombok.Data;

/**
 * 服务提供者model
 * Created by lurenjie on 2017/6/7
 */
@Data
public class NodeInfo {
    private String applicationName;
    private String interfaceName;
    private String implName;
    private String ip;
    private String port;
    private String version;
    private Boolean isProvider;
    private TProtocol protocol;
    private TLoadBalance loadBalanceType;
}
