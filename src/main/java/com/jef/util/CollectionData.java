package com.jef.util;

import java.util.ArrayList;

/**
 * 使用generator对象填充集合
 * @author Jef
 * @date 20180728
 */
public class CollectionData<T> extends ArrayList<T> {
    CollectionData(Generator<T> gen, int quantity) {
        for (int i = 0; i< quantity; i++) {
            add(gen.next());
        }
    }

    /**
     * 开放调用方法
     * @param gen
     * @param quantity
     * @param <T>
     * @return
     */
    public static <T> CollectionData list(Generator<T> gen, int quantity) {
        return new CollectionData(gen, quantity);
    }
}
