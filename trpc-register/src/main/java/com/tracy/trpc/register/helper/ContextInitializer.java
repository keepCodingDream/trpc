package com.tracy.trpc.register.helper;

import com.tracy.trpc.common.ClassHelper;
import com.tracy.trpc.common.Constants;
import com.tracy.trpc.common.IpHelper;
import com.tracy.trpc.common.annotation.Provider;
import com.tracy.trpc.common.context.DefaultProxyContext;
import com.tracy.trpc.common.model.NodeInfo;
import com.tracy.trpc.register.register.Register;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.Set;

/**
 * 用于阅读basePackage下所有包含 @Provider 注解的service信息注册到zookeeper
 *
 * @author lurenjie
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
        String basePackage = properties.getProperty(Constants.BASE_PACKAGE);
        String applicationName = properties.getProperty(Constants.PROVIDER_NAME);
        String applicationIp = properties.getProperty(Constants.PROVIDER_IP);
        String applicationPort = properties.getProperty(Constants.PROVIDER_PORT);
        String applicationVersion = properties.getProperty(Constants.PROVIDER_VERSION);
        if (StringUtils.isEmpty(applicationVersion)) {
            applicationVersion = "0.0.1";
        }

        if (StringUtils.isEmpty(basePackage)) {
            throw new IllegalArgumentException(Constants.BASE_PACKAGE + "属性不能为空");
        }
        if (StringUtils.isEmpty(applicationName)) {
            throw new IllegalArgumentException(Constants.PROVIDER_NAME + "属性不能为空");
        }
        if (StringUtils.isEmpty(applicationPort)) {
            throw new IllegalArgumentException(Constants.PROVIDER_PORT + "属性不能为空");
        }
        //获取扫描包下的所有类
        Set<Class<?>> classes = ClassHelper.getClasses(basePackage);
        //找出定义的接口中的所有实现类
        for (Class item : classes) {
            Provider provider = (Provider) item.getAnnotation(Provider.class);
            if (provider != null) {
                Class<?>[] interfaces = item.getInterfaces();
                boolean flag = true;
                String[] methodArr = item.getName().split("[.]");
                for (Class inter : interfaces) {
                    String[] interArr = inter.getName().split("[.]");
                    if (methodArr[methodArr.length - 1].contains(interArr[interArr.length - 1])) {
                        DefaultProxyContext.getInstants().setBean(item, inter.getName());
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    throw new RuntimeException("Bean:" + item.getName() + "find no interface.Please check the name of your service interface." +
                            "Such as interface name : Demo and service name : DemoImpl");
                }
            }
        }
        NodeInfo info = new NodeInfo();
        info.setApplicationName(applicationName);
        if (!StringUtils.isEmpty(applicationIp)) {
            info.setIp(applicationIp);
        } else {
            info.setIp(IpHelper.localIpAddress());
        }
        info.setPort(applicationPort);
        info.setVersion(applicationVersion);
        info.setIsProvider(true);
        register.registry(info);

    }
}
