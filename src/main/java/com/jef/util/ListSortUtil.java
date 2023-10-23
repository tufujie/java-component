package com.jef.util;

import com.jef.util.algorithm.sort.ArraySortUtil;

import com.google.common.collect.Lists;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 集合排序工具类
 *
 * @author Jef
 * @date 2018/10/8 14:09
 */
public class ListSortUtil<E> {
    @SuppressWarnings("all")
    public void sort(List<E> list, final String method, final String sort) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object a, Object b) {
                int ret = 0;
                try {
                    Method m1 = ((E) a).getClass().getMethod(method, null);
                    Method m2 = ((E) b).getClass().getMethod(method, null);
                    if (sort != null && "desc".equals(sort)) {
                        // 倒序
                        ret = m2.invoke(((E) b), null).toString().compareTo(m1.invoke(((E) a), null).toString());
                    } else {
                        // 正序
                        ret = m1.invoke(((E) a), null).toString().compareTo(m2.invoke(((E) b), null).toString());
                    }
                } catch (NoSuchMethodException ne) {
                    System.out.println(ne);
                } catch (IllegalAccessException ie) {
                    System.out.println(ie);
                } catch (InvocationTargetException it) {
                    System.out.println(it);
                }
                return ret;
            }
        });
    }

    /**
     * 对map集合的某个属性排序
     */
    @SuppressWarnings("all")
    public void sortMapList(List<Map<String, Object>> list, final String key, final String sort) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                int ret = 0;
                try {
                    Map<String, Object> m1 = (Map<String, Object>) o1;
                    Map<String, Object> m2 = (Map<String, Object>) o2;
                    if (sort != null && "desc".equals(sort)) {
                        // 倒序
                        ret = NumberUtils.compareValue(m2.get(key), m1.get(key));
                    } else {
                        ret = NumberUtils.compareValue(m1.get(key), m2.get(key));
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                return ret;
            }
        });
    }

    /**
     * 添加并排序
     * @param listOne
     * @param listTwo
     * @return
     */
    public static List<Integer> addAndSort(List<Integer> listOne, List<Integer> listTwo) {
        List<Integer> resultList = new ArrayList<>();
        resultList.addAll(listOne);
        resultList.addAll(listTwo);
        Collections.sort(resultList);
        return resultList;
    }

    /**
     * 添加并排序
     *
     * @param
     * @return void
     * @author Jef
     * @date 2021/4/13
     */
    public static int[] addAndSortV2(List<Integer> listOne, List<Integer> listTwo) {
        List<Integer> resultListTemp = Lists.newArrayList();
        resultListTemp.addAll(listOne);
        resultListTemp.addAll(listTwo);
        int[] array = new int[resultListTemp.size()];
        int i = 0;
        for (Integer temp : resultListTemp) {
            array[i++] = temp;
        }
        ArraySortUtil.bubbleSort(array);
        return array;
    }

    /**
     * 添加并排序
     * @author Jef
     * @date 2021/4/13
     * @param
     * @return void
     */
    public static Set<Integer> addAndSortV3(List<Integer> listOne, List<Integer> listTwo) {
        Set<Integer> resultSet = new TreeSet<Integer>();
        resultSet.addAll(listOne);
        resultSet.addAll(listTwo);
        return resultSet;
    }

}
