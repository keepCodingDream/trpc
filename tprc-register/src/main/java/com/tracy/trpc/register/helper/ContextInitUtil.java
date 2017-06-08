package com.tracy.trpc.register.helper;

import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.ClassHelper;
import com.tracy.trpc.common.IpHelper;
import com.tracy.trpc.common.annotation.Provider;
import com.tracy.trpc.common.model.NodeInfo;
import com.tracy.trpc.common.zookeeper.ZookeeperOperations;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Created by lurenjie on 2017/6/7
 */
public class ContextInitUtil {

    private String basePackage;
    private String applicationName;
    private ZookeeperOperations zookeeperOperations;

    public ContextInitUtil(String basePackage, String applicationName, String zookeeperAddress, Integer zookeeperTimeout) {
        this.basePackage = basePackage;
        this.applicationName = applicationName;
        zookeeperOperations = new ZookeeperOperations(zookeeperAddress, zookeeperTimeout);
    }

    public void init() throws Exception {
        if (StringUtils.isEmpty(basePackage)) {
            throw new IllegalArgumentException("trpc.context.base.package属性不能为空");
        }
        if (StringUtils.isEmpty(basePackage)) {
            throw new IllegalArgumentException("trpc.context.base.package属性不能为空");
        }
        zookeeperOperations.init();
        String[] appNames = applicationName.split(",");
        //获取扫描包下的所有类
        Set<Class<?>> classes = ClassHelper.getClasses(basePackage);
        //找出定义的接口中的所有实现类
        for (String appNameItem : appNames) {
            for (Class item : classes) {
                Provider provider = (Provider) item.getAnnotation(Provider.class);
                if (provider != null) {
                    Class<?>[] interfaces = item.getInterfaces();
                    for (Class inter : interfaces) {
                        if (appNameItem.equals(inter.getName())) {
                            NodeInfo info = new NodeInfo();
                            info.setApplicationName(applicationName);
                            info.setInterfaceName(appNameItem);
                            info.setImplName(item.getName());
                            if (!StringUtils.isEmpty(provider.ip())) {
                                info.setIp(provider.ip());
                            } else {
                                info.setIp(IpHelper.localIpAddress());
                            }
                            info.setPort(provider.port());
                            info.setProtocol(provider.protocol());
                            info.setLoadBalanceType(provider.loadBalanceType());
                            zookeeperOperations.createdNode(appNameItem, JSON.toJSONString(info));
                        }
                    }
                }
            }
        }
    }
}
