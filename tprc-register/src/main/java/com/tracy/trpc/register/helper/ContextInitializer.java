package com.tracy.trpc.register.helper;

import com.tracy.trpc.common.ClassHelper;
import com.tracy.trpc.common.IpHelper;
import com.tracy.trpc.common.annotation.Provider;
import com.tracy.trpc.common.model.NodeInfo;
import com.tracy.trpc.register.register.Register;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 用于阅读basePackage下所有包含 @Provider 注解的service信息注册到zookeeper
 * Created by lurenjie on 2017/6/7
 */
public class ContextInitializer {

    private Register register;

    public ContextInitializer(Register register) throws Exception {
        this.register = register;
    }

    /**
     * 初始化Trpc context,扫描基础包，找出服务提供者，注册到zookeeper
     * <p>
     * 1.首先获取所有applicationName(使用","分割)<br/>
     * 2.根据用户提供的basePackage去找到带有@Provider注解的类。<br/>
     * 3.检测被找到的类,去匹匹配实现接口与applicationName同名的，注册到zookeeper
     *
     * @throws Exception if anything need stop
     */
    public void init(Properties properties) throws Exception {
        String basePackage = properties.getProperty("trpc.context.base.package");
        String applicationName = properties.getProperty("trpc.context.application.name");
        String applicationIp = properties.getProperty("trpc.context.application.ip");
        String applicationPort = properties.getProperty("trpc.context.application.port");
        if (StringUtils.isEmpty(basePackage)) {
            throw new IllegalArgumentException("trpc.context.base.package属性不能为空");
        }
        if (StringUtils.isEmpty(applicationName)) {
            throw new IllegalArgumentException("trpc.context.application.name属性不能为空");
        }
        if (StringUtils.isEmpty(applicationPort)) {
            throw new IllegalArgumentException("trpc.context.application.port属性不能为空");
        }
        String[] appNames = applicationName.split(",");
        //获取扫描包下的所有类
        Set<Class<?>> classes = ClassHelper.getClasses(basePackage);
        Map<String, Class> registerMap = new HashMap<>();
        for (String item : appNames) {
            registerMap.put(item, null);
        }
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
                            if (!StringUtils.isEmpty(applicationIp)) {
                                info.setIp(applicationIp);
                            } else {
                                info.setIp(IpHelper.localIpAddress());
                            }
                            info.setPort(applicationPort);
                            info.setProtocol(provider.protocol());
                            info.setVersion(provider.version());
                            info.setLoadBalanceType(provider.loadBalanceType());
                            register.registry(info);
                            if (registerMap.get(appNameItem) != null) {
                                throw new IllegalArgumentException(appNameItem + "has find more then one implements:" + registerMap.get(appNameItem) + "and" + item);
                            } else {
                                registerMap.put(appNameItem, item);
                            }
                            break;
                        }
                    }
                }
            }
        }
        //找出没有实现类的applicationName
        for (String item : appNames) {
            if (registerMap.get(item) == null) {
                throw new IllegalArgumentException("can not find implement of " + item);
            }
        }
    }
}
