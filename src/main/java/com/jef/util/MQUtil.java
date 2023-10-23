package com.jef.util;

import com.jef.entity.MQInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * MQ调用工具类
 *
 * @author Jef
 * @date 2021/12/7
 */
public class MQUtil {

    /**
     * 封装处理业务的方法
     *
     * @param mqInfo
     */
    public static void handlerBill(MQInfo mqInfo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        System.out.println("适配器方法是：" + mqInfo.getMqAdaptor());
        System.out.println("开始进行业务处理");
        String adaptor = mqInfo.getMqAdaptor();
        int index = adaptor.lastIndexOf(".");
        String beanName = adaptor.substring(0, index);
        String methodName = adaptor.substring(index + 1);
        // 如果有容器的话直接通过beanName获取
        Class clazz = Class.forName("com.jef.designpattern.adaptor." + beanName);
        Object object = clazz.newInstance();
        // 反射调用消息处理方法
        Method method = clazz.getDeclaredMethod(methodName, MQInfo.class);
        method.invoke(object, mqInfo);
    }

}