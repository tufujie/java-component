package com.jef.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jef
 * @dater 2018/5/15 19:18
 */
public class BeanPropertiesCopy {

    /**
     * 从from对象转化为to对象
     *
     * @param from
     * @param to
     * @throws IllegalAccessException
     */
    public static void propertiesCopy(Object from, Object to) throws IllegalAccessException {
        Class fromClass = from.getClass();
        Method[] toMethods = to.getClass().getDeclaredMethods();
        List<String> toMethodNames = Arrays.stream(to.getClass().getDeclaredMethods()).filter(i -> i.getName().startsWith("set")).map(i -> i.getName().substring(3)).collect(Collectors.toList());
        Arrays.stream(fromClass.getDeclaredMethods()).filter(i -> toMethodNames.contains(i.getName().substring(3)) && i.getName().contains("get")).forEach(i -> {
            try {
                System.out.println(i.getName());
                Object value = i.invoke(from);
                if (value != null) {
                    Method method = findMethodByName(toMethods, "set" + i.getName().substring(3));
                    if (method != null)
                        method.invoke(to, value);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 通过方法名称找到方法
     *
     * @param methods
     * @param name
     * @return
     */
    private static Method findMethodByName(Method[] methods, String name) {
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }
}
