package com.valuestudio.web.utils;

import android.content.Context;

import com.valuestudio.web.R;

import java.io.InputStream;
import java.util.Properties;

/**
 * 读取Properties配置文件工具类
 *
 * @author zuolong
 * @version V1.0
 * @title PropertiesUtil
 * @description 读取Properties配置文件工具类
 */
public class PropertiesUtil {

    private static PropertiesUtil propertiesUtil;
    private static Properties props;

    public PropertiesUtil(Context context) {
        if (props == null) {
            props = new Properties();
        }
        try {
            InputStream in = context.getResources().openRawResource(
                    R.raw.settings);
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PropertiesUtil getInstance(Context context) {
        if (propertiesUtil == null) {
            propertiesUtil = new PropertiesUtil(context);
        }
        return propertiesUtil;
    }

    /**
     * 根据指定名称获取值
     *
     * @param name         名称（大小写全匹配）
     * @param defaultValue 默认值
     * @return
     */
    public String getProperty(String name, String defaultValue) {
        return props.getProperty(name, defaultValue).trim();
    }
}
