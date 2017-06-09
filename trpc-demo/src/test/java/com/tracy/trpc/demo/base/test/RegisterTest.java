package com.tracy.trpc.demo.base.test;

import com.tracy.trpc.common.util.CommonUtil;
import com.tracy.trpc.register.helper.ContextInitializer;
import com.tracy.trpc.register.register.Register;
import com.tracy.trpc.register.register.ZookeeperRegister;
import org.junit.Test;

import java.util.Properties;

/**
 * Created by lurenjie on 2017/6/8
 */
public class RegisterTest {
    @Test
    public void testRegisterCorrect() throws Exception {
        Register register = new ZookeeperRegister();
        ContextInitializer util = new ContextInitializer(register);
        Properties properties = CommonUtil.getProperties("/trpc.properties");
        util.init(properties);
    }
}
