package com.jef.util;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * @author Jef
 * @date 2020/4/20
 */
public class LambdaUtil {

    /**
     * 场景：Collectors.toMap 时，key冲突时，保持原来的值
     * @DEMO Map<String, String> record = voList.stream()
     *                 .collect(Collectors.toMap(BaseVo::getId, BaseVo::getName, LambdaUtils.remain()));
     * @author Jef
     * @date 2020/3/31
     * @param
     * @return java.util.function.BinaryOperator<T>
     */
    public static <T> BinaryOperator<T> remain() {
        return (t1, t2) -> t1;
    }

    /**
     * 场景：Collectors.toMap 时，key冲突时，取新的值
     * @DEMO Map<String, String> record = voList.stream()
     *                 .collect(Collectors.toMap(BaseVo::getId, BaseVo::getName, LambdaUtils.merge()));
     * @author Jef
     * @date 2020/4/8
     * @param
     * @return java.util.function.BinaryOperator<T>
     */
    public static <T> BinaryOperator<T> merge() {
        return (t1, t2) -> t2;
    }

    /**
     * 场景：Stream.map 时，从Map中根据key获取value
     * @DEMO  List<String> idList = listMap.stream().map(LambdaUtils.mapValue("id")).collect(Collectors.toList)
     * @author Jef
     * @date 2020/3/31
     * @param key
     * @return java.util.function.Function<java.util.Map, T>
     */
    public static <T> Function<Map, T> mapValue(String key) {
        return t -> (T) t.get(key);
    }

    /**
     * Collectors.toMap 时，key冲突时，求和
     * @DEMO Map<String, BigDecial> record = voList.stream()
     *                 .collect(Collectors.toMap(BaseVo::getId, BaseVo::getAmount, LambdaUtils.sum()));
     * @author Jef
     * @date 2020/4/15
     * @return java.util.function.BinaryOperator<T>
     */
    public static <T extends Number> BinaryOperator<T> sum(){
        return (num1,num2) -> NumberUtils.sum(num1, num2);
    }

}