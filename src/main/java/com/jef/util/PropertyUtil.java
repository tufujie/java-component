package com.jef.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 转换对象输出，避免过多对象属性输出
 * @date 2018/10/29 18:44
 */
public class PropertyUtil {

    /**
     * List<实体> 转换为 List<Map>
     * @date:Dec 7, 2017 10:25:22 PM
     * @param fields
     * @param list
     * @return
     */
    public static List transToMap(String[] fields, List list) {
        List newList = new ArrayList();
        for(int i = 0; i<list.size(); i++){
            newList.add(transToMap(fields, list.get(i)));
        }
        return newList;
    }


    /**
     * 实体 转换为 Map
     * @param fields
     * @param o
     * @return
     */
    public static Object transToMap(String[] fields, Object o) {
        Map infoMap = new HashMap();
        for(int i = 0; i < fields.length; i++){
            infoMap.put(fields[i], getFieldValueByName(fields[i], o));
        }
        return infoMap;
    }

    /**
     * 根据属性名获取属性值
     **/
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将source的属性复制给target的对象里边去
     * 主要用于vo和base的相互转换
     * 注意属性名相同才能复制
     *
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T transform(Object source, Class<T> target) {
        try {
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanCreationException("无法创建对象：" + target.getName(), e);
        }
    }

    /**
     * 将source的属性复制给target的对象里边去
     * 主要用于vo和base的相互转换
     * 注意属性名相同才能复制
     *
     * 如果有Set或者其他集合类型的需求请添加以Set为参数的方法
     *
     * @param sources
     * @param target
     * @param <T>
     * @return
     */
    public static <T> List<T> transform(List<?> sources, Class<T> target) {
        List<T> result = new ArrayList<>();
        for (Object source : sources) {
            result.add(transform(source, target));
        }
        return result;
    }

    /**
     * list vo 转换为 list map
     * 推荐使用此方法
     *
     * @param list
     * @param fields 可变参数
     * @return
     */
    public static List<Map<String, Object>> transToMap(Iterable<?> list, String... fields) {
        Objects.requireNonNull(list);
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();

        for (Object o : list) {
            newList.add(transToMap(o, fields));
        }

        return newList;
    }

    /**
     * 将name相同的id进行合并(使用前提是list集合以根据name排序，也就是相同的那么必须前后相邻才可以合并)
     * map里存了name和id两个键，将name相同的map的id合并到一个map中，id用逗号分隔
     * @param mapList
     * @return
     */
    public static List<Map<String,Object>> getCombineIdByName(List<Map<String,Object>> mapList){
        int len = mapList.size();
        String lastName = null;
        StringBuilder lastId = new StringBuilder();
        Map<String,Object> lastMap = new HashMap<>();
        List<Map<String,Object>> result = new ArrayList<>();
        boolean flag = false;
        for (int i=0;i<len;i++) {
            String tempId = mapList.get(i).get("id").toString();
            String tempName = StringUtils.isEmpty(mapList.get(i).get("name"))?"":mapList.get(i).get("name").toString();
            if (tempName.equals(lastName)) {
                lastId.append("," + tempId);
                if (i == len - 1) {
                    lastMap.put("name",lastName);
                    lastMap.put("id", lastId.toString());
                    result.add(lastMap);
                }
            } else {
                if (flag) {
                    lastMap.put("id",lastId);
                    result.add(lastMap);
                    lastMap = new HashMap<>();
                }
                lastName = tempName;
                lastId = new StringBuilder(tempId);
                lastMap.put("name",lastName);
                lastMap.put("id", lastId.toString());
                flag = true;
                if (i == len - 1) {
                    result.add(lastMap);
                }
            }
        }
        return result;
    }

    /**
     * 指定源属性字段，按目标属性字段将list vo 转换为 list map
     * @author fuqiang_wen 2019.4.17
     * @param list
     * @param srcFields 源属性字段
     * @param targetFields 目标属性字段
     * @return
     */
    public static List<Map<String, Object>> transToMap(Iterable<?> list, String[] srcFields, String[] targetFields) {
        Objects.requireNonNull(list);
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();

        for (Object o : list) {
            newList.add(transToMap(o, srcFields, targetFields));
        }

        return newList;
    }

    /**
     * vo 转换为 map
     * 推荐使用此方法
     *
     * @param o
     * @param fields 字段，可变参数
     * @return vo为null，每个字段将返回null
     */
    public  static Map<String, Object> transToMap(Object o, String ... fields) {
        Map<String, Object> infoMap = new HashMap<String, Object>(fields.length);
        for (String field : fields) {
            infoMap.put(field, getProperty(o, field));
        }
        return infoMap;
    }

    /**
     * 指定源属性字段，按目标属性字段将 vo 转换为 map
     * @author fuqiang_wen 2019.4.17
     * @param vo vo对象
     * @param srcFields 源属性字段
     * @param targetFields 目标属性字段
     * @return 转换后的map
     */
    public  static Map<String, Object> transToMap(Object vo, String[] srcFields, String[] targetFields) {
        if(srcFields.length != targetFields.length){
            throw new IllegalArgumentException("srcFields's length not equals targetFields's length");
        }

        Map<String, Object> infoMap = new HashMap<String, Object>(srcFields.length);
        for (int i = 0; i < srcFields.length; i++) {
            infoMap.put(targetFields[i], getProperty(vo, srcFields[i]));
        }
        return infoMap;
    }

    /**
     * 根据属性名获取属性值
     * */
    private static Object getProperty(Object o, String fieldName) {
        Objects.requireNonNull(o);
        if (o instanceof Map) {
            Map map = (Map) o;
            return map.get(fieldName);
        }

        try {
			return org.apache.commons.beanutils.PropertyUtils.getProperty(o, fieldName);
        } catch (Exception e) {
            return null;
        }
    }

}
