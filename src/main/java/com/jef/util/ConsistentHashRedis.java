package com.jef.util;

import redis.clients.jedis.Jedis;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 一致性hash取key对应的节点
 */
public class ConsistentHashRedis {

    // 用跳表模拟一致性hash环，即使在节点很多的情况下，也可以有不错的性能
    private final ConcurrentSkipListMap circle;

    // 虚拟节点数量
    private final int virtualSize;

    public ConsistentHashRedis(String configs) {
        this.circle = new ConcurrentSkipListMap<>();
        String[] host = configs.split(",");
        this.virtualSize = getVirtualSize(host.length);
        for (String c : host) {
            this.add(c);
        }
    }

    /**
     * 将每个节点添加进环中，并且添加对应数量的虚拟节点
     */
    private void add(String host) {
        if (host == null) return;
        int hash = getHash(host);
        circle.put(hash, host);
        for (int i = 0; i < virtualSize; ++i) {
            String virtual = getVirtualHost(host, i);
            hash = getHash(virtual);
            circle.put(hash, virtual);
        }
    }

    /**
     * 根据字符串获取hash值，这里使用简单粗暴的绝对值
     */
    private int getHash(String s) {
        return Math.abs(s.hashCode());

    }

    // 计算当前需要多少个虚拟节点，这里没有计算，直接使用了5
    private int getVirtualSize(int length) {
        return 5;

    }

    /**
     * 对外提供的set方法
     */
    public void set(String key, String v) {
        getJedisFromCircle(key).set(key, v);

    }

    public String get(String k) {
        return getJedisFromCircle(k).get(k);

    }

    /**
     * 从环中取到适合当前key的jedis.
     */
    private Jedis getJedisFromCircle(String key) {
        int keyHash = getHash(key);
        ConcurrentNavigableMap tailMap = circle.tailMap(keyHash);
        String config = (String) (tailMap.isEmpty() ? circle.firstEntry().getValue() : tailMap.firstEntry().getValue());
        // 注意，由于使用了虚拟节点，所以这里要做 虚拟节点 -> 真实节点的映射
        String[] hosts = config.split("-");
        String host = hosts[0];
        System.out.println("key=" + key + ",middleNode=" + config + ",useNode=" + host);
        return new Jedis(host);
    }

    /**
     * 对外暴露的添加节点接口
     */
    public boolean addJedis(String host) {
        add(host);
        return true;

    }

    /**
     * 对外暴露的删除节点节点
     */
    public boolean deleteJedis(String host) {
        delete(host);
        return true;

    }

    /**
     * 从环中删除一个节点极其虚拟节点
     */
    private void delete(String host) {
        if (host == null) return;
        for (int i = 0; i < virtualSize; ++i) {
            String virtual = getVirtualHost(host, i);
            int hash = getHash(virtual);
            circle.remove(hash, virtual);
        }
    }

    private String getVirtualHost(String host, int i) {
        return host + "-V" + i;
    }

}
