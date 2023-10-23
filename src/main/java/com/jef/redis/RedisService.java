package com.jef.redis;

import java.util.List;
import java.util.Set;

/**
 * redis 的操作开放接口
 * @create 20180713
 */
public interface RedisService {

    /**
     * 添加key value 并且设置存活时间
     * @param key 实际key
     * @param value 实际值
     * @param liveTime 存放时间 单位秒
     */
    void set(String key, String value, long liveTime);

    /**
     * 添加key value 并且设置存活时间(byte)
     * @param key 实际key
     * @param value 实际值
     * @param liveTime 存放时间
     */
    void set(byte[] key, byte[] value, long liveTime);

    /**
     * 添加key value
     * @param key 实际key
     * @param value 实际值
     */
    void set(String key, String value);

    /**
     * 添加key value (字节)(序列化)
     * @param key 实际key
     * @param value 实际值
     */
    void set(byte[] key, byte[] value);

    /**
     * 通过key删除
     * @param keys 需要删除的键
     */
    long del(String... keys);

    /**
     * @param key
     */
    long del(final byte[] key);

    /**
     * 获取redis value (String)
     * @param key 实际key
     */
    String get(String key);

    /**
     * 添加key value (字节)(序列化)
     * @param key 实际key
     */
    byte[] get(byte[] key);

    /**
     * 添加key value (字节)(序列化)
     * @param key 实际key
     */
    String getStringByByte(byte[] key);

    /**
     * 通过正则匹配keys
     * @param pattern
     * @return
     * @return
     */
    Set setkeys(String pattern);

    /**
     * 检查key是否已经存在
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * 清空redis 所有数据
     * @return
     */
    String flushDB();

    /**
     * 查看redis里有多少数据
     */
    long dbSize();

    /**
     * 检查是否连接成功
     * @return
     */
    String ping();

    /**
     * 存放对象
     * @param objectKey 对象key
     * @param key 实际key
     * @param obj 对象内容
     * @param limitSize 限制大小
     */
    void putObject(String objectKey, String key, Object obj, boolean limitSize);

    /**
     * 获取对象
     * @param key
     * @return
     */
    Object getObject(String objKey, String key);

    /**
     * 删除对象
     * @param objectKey 对象key
     * @param key 实际key
     */
    void deleteObject(String objectKey, String key) ;

    /**
     * 获取所有key
     * @param objectKey 对象key
     */
    Set getKeys(String objectKey) ;

    //获得某个objKey size
    Long size(String objKey) ;

    //删除所有key
    void deleteAll(String objKey) ;

    /**
     * 如果key不存在，则进行set
     * @date 2018/12/19 14:24
     * @param key key
     * @param value value
     * @param liveTime 过期时间值 秒
     * @return java.lang.Object
     * @throws
     */
    Object setIfNotExist(String key, String value, long liveTime);
    /**
     * @date 2019/1/28 9:55
     * @param key
     * @param value
     * @param expireTime 过期时间 单位毫秒
     * @return boolean 设置成功true,否则fasle
     * @throws
     */
    boolean setIfNotExistAndExpire(String key, String value, long expireTime);
    
    /**
     * 使用lua脚本 进行删除key操作，且需要value相等的才能够删除--用户分布式锁的解锁操作
     * @date 2018/12/19 16:38
     * @param key
     * @param value
     * @return java.lang.Object
     * @throws
     */
    Object delByLua(String key, String value);

    /**
     * 获取递增缓存
     * @param key
     * @param liveTime
     * @return
     */
    Integer getIncrCache(String key, long liveTime);

    /**
     * 获取递减缓存
     * @param key
     * @param liveTime
     * @return
     */
    Integer getDecrCache(String key, long liveTime);

    /**
     *  向有序集合增加值
     * @date 2019/12/17 15:12
     * @param setKey
     * @param value
     * @param score
     * @return boolean
     * @throws
     */
    boolean setZset(String setKey, String value, Double score);

    /**
     *  获取有序集合
     * @date 2019/12/17 15:13
     * @param setKey
     * @return java.util.Set<java.lang.String>
     * @throws
     */
    Set<String> getZset(String setKey);

    /**
     *  删除有序集合（start , end 倒序的：[1,2,3,4] 0~1 表示删除 3 4）
     * @date 2019/12/17 15:13
     * @param key
     * @param start
     * @param end
     * @return 删除成功数
     * @throws
     */
    Long removeRangeSet(String key, int start, int end);
    /**
     *  有序集合大小
     * @date 2019/12/17 15:32
     * @param key
     * @return java.lang.Long
     * @throws
     */
    Long zsetSize(String key);

    /**
     *  查询list元素
     * @date 2020/3/3 10:31
     * @param key
     * @param index
     * @return java.lang.String
     * @throws
     */
    String getList(String key, int index);

    /**
     *  删除rpushlist队头元素
     * @date 2020/3/3 10:37
     * @param key
     * @return boolean
     * @throws
     */
    boolean removeRpushListFirst(String key);

    /**
     *  从右边压入列表
     * @date 2020/3/3 10:47
     * @param key
     * @param value
     * @return boolean
     * @throws
     */
    boolean rpushList(String key, String value);

    /**
     *  从左边压入队列
     * @date 2020/3/11 18:13
     * @return boolean
     * @throws
     */
    boolean lpushList(String key, String value);

    /**
     *  设置list的过期时间
     * @date 2020/3/3 10:56
     * @param key
     * @param milliseconds 毫秒
     * @return boolean
     * @throws
     */
    boolean setListExpire(String key, long milliseconds);

    /**
     *  删除list里面key对应的所有value
     * @date 2020/3/3 11:06
     * @param key
     * @param count 需要移除的数量
     * @param value
     * @return boolean
     * @throws
     */
    boolean removeListSameValues(String key, int count, String value);

    /**
     *  向左弹出一个元素
     * @date 2020/3/11 13:58
     * @param key
     * @return java.lang.String
     * @throws
     */
    String leftPop(String key);

    /**
     *  减去decryNum
     *
     * @author yanghua
     * @date 2020/3/11 17:00
     * @param key
     * @param productQty
     * @param liveTime 秒
     * @return long
     * @throws
     */
    long decrby(String key, long productQty, long liveTime);

    /**
     *  减去decryNum
     *
     * @date 2020/8/18 19:56
     * @param key
     * @return long
     * @throws
     */
    long decrby(String key);

    /**
     * 加上decryNum
     * @date 2020/3/11 17:29
     * @param key
     * @param productQty
     * @param liveTime
     * @return long
     * @throws
     */
    long incrby(String key, Integer productQty, long liveTime);

    /**
     *  查询列表
     * @date 2020/3/12 17:17
     * @param key
     * @param start
     * @param end
     * @return java.util.List<java.lang.String>
     * @throws
     */
    List<String> rangeList(String key, int start, int end);

    /**
     * 模糊查询keys
     * @param pattern
     * @return
     * @return
     */
    Set getkeysPattern(String pattern);

}