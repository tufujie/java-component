package com.jef.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Spring属性工具
 *
 * @author Jef
 * @create 2018/6/12 19:46
 */
public class SpringPropertiesUtil extends PropertyPlaceholderConfigurer {

    private static Map<String, String> propertiesMap;
    private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

    @Override
    protected void processProperties(
            ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {

        super.processProperties(beanFactory, props);

        propertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
            propertiesMap.put(keyStr, valueStr);
        }
    }

    public static String getProperty(String name) {
        if (propertiesMap == null) {
            return null;
        }
        if (propertiesMap.get(name) == null) {
            return null;
        }

        return propertiesMap.get(name).toString();
    }

    public static void setProperty(String keyStr, String valueStr) {
        if (propertiesMap == null) {
            propertiesMap = new HashMap<String, String>();
        }
        propertiesMap.put(keyStr, valueStr);
    }

}
