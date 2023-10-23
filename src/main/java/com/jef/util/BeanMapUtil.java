package com.jef.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Bean封装出Map，可用于Myabtis交互时传递map条件作为参数时
 * @author Jef
 * @date 2018/8/7 19:29
 */
public class BeanMapUtil {
    /**
     * javaBean转map
     * @param javaBean
     * @return
     */
    public static void mapBuild(Object javaBean, Map map) {
        Class clazz = javaBean.getClass();
        // 反射获取
        Field[] fields= clazz.getDeclaredFields();
        for (Field field : fields) {
            // 将非null的JavaBean属性值装配到map中
            Object object = getMethodValue(javaBean, field.getName());
            if (object != null) {
                map.put(field.getName(), object);
            }
        }
    }
    /**
     * 根据属性，获取get方法的值
     * @param javaBean
     * @param field
     */
    private static Object getMethodValue(Object javaBean, String field) {
        Method[] methods = javaBean.getClass().getMethods();
        try {
            for (Method method : methods) {
                if (("get" + field).toLowerCase().equals(method.getName().toLowerCase())) {
                    return method.invoke(javaBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
