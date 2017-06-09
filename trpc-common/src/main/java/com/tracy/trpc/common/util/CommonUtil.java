package com.tracy.trpc.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lurenjie on 2017/6/8
 */
public class CommonUtil {

    /**
     * 根据配置文件名字读取配置信息
     *
     * @param path 配置文件路径
     * @return Properties hashTable
     * @throws IOException if read fail
     */
    public static Properties getProperties(String path) throws IOException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        InputStream in = CommonUtil.class.getResourceAsStream(path);
        Properties prop = new Properties();
        try {
            prop.load(in);
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop;
    }

}
