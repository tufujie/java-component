package com.jef.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author Jef
 * @date 2019/8/20
 */
public class CounterUtil {
    private static final AtomicReferenceFieldUpdater<CounterUtil, AtomicInteger> valueUpdater =
            AtomicReferenceFieldUpdater.newUpdater(CounterUtil.class, AtomicInteger.class, "value");
    //保证变量线程间可见
    private volatile String key;
    //保证计数器原子性
    private volatile AtomicInteger value;
    private static final String DATE_PATTERN = "yyyy-MM-dd";


    public CounterUtil() {
        /**
         * key值设定为当前日期是因为计数器每天重置一次，否则数值一天天增大
         */
        this.key = getCurrentDateString() ;
        this.value = new AtomicInteger(0);
    }


    /**
     * 获取计数器加1以后的值
     * @return
     */
    public Integer addAndGet() {
        AtomicInteger oldValue = value;
        AtomicInteger newInteger = new AtomicInteger(0);
        int newVal;
        String newDateStr = getCurrentDateString();
        //日期一致，计数器加1后返回
        if (isDateEquals(newDateStr)) {
            newVal = add(1);
            return newVal;
        }
        // 日期不一致，保证有一个线程重置技术器
        reSet(oldValue, newInteger);
        this.key = newDateStr;
        // 重置后加1返回
        newVal = add(1);
        return newVal;
    }

    /**
     * 获取计数器的当前值
     * @return
     */
    public Integer get() {
        return value.get();
    }

    /**
     * 判断当前日期与老的日期（也即key成员变量记录的值）是否一致
     * @return
     */
    private boolean isDateEquals(String newDateStr) {
        String oldDateStr = key;
        if (!isBlank(oldDateStr) && oldDateStr.equals(newDateStr)) {
            return true;
        }
        return false;
    }


    /**
     * 如果日期发生变化，重置计数器，也即将key设置为当前日期，并将value重置为0，重置后才能接着累加，
     */
    private void reSet(AtomicInteger oldValue, AtomicInteger newValue) {
        if (valueUpdater.compareAndSet(this, oldValue, newValue)) {
            System.out.println("线程" + Thread.currentThread().getName() + "发现日期发生变化");
        }
    }

    /**
     * 获取当前日期字符串
     *
     * @return
     */
    private String getCurrentDateString() {
        Date date = new Date();
        String newDateStr = new SimpleDateFormat(DATE_PATTERN).format(date);
        return newDateStr;
    }

    /**
     * 计数器的值加1
     * @param increment
     */
    private int add(int increment) {
        return value.addAndGet(increment);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if(cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }
}