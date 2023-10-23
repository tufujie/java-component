package com.jef.redis.cache;

import com.jef.entity.RedisVo;
import com.jef.redis.RedisService;
import com.jef.redis.RedisServiceFactory;

import java.util.Date;

/**
 * 对象缓存
 * @author Jef
 * @create 2018/7/13 11:13
 */
public class ObjectCache {
    // 需要销毁的keys
    public static final String DELETE_OBJECT_KEY = "delete_them";

    /**
     * 设置缓存
     * @param objectKey 对象key
     * @param key 实际key
     * @param obj 内容
     * @param timeOut 超时是否清理
     */
    public static void setCache(String objectKey, String key, Object obj, boolean timeOut) {
        setCache(objectKey, key, obj, timeOut, false);
    }

    /**
     * 设置缓存
     * @param objectKey 对象key
     * @param key 实际key
     * @param obj 内容
     * @param timeOut 超时是否清理
     * @param limitSize 是否限制大小
     */
    public static void setCache(String objectKey, String key, Object obj, boolean timeOut, boolean limitSize) {
        RedisService redisService = RedisServiceFactory.getInstance();
        if (obj == null) {
            redisService.deleteObject(objectKey, key);
            return ;
        }
        redisService.putObject(objectKey, key, obj, limitSize);
        if (timeOut) {
            // 该对象超时则需要销毁，定时任务每2个小时检查一次
            RedisVo vo = new RedisVo();
            vo.setObjectKey(objectKey);
            vo.setKey(key);
            vo.setCreateTime(new Date());
            // 需要反解析
            redisService.putObject(DELETE_OBJECT_KEY, getObjectKeyWithKey(objectKey, key), vo, limitSize);

        }
    }

    /**
     * 设置带有效期的参数缓存，注意该方法不需要ObjectKey
     * @param key 实际key
     * @param value 实际值
     * @param liveTime 有效期，单位秒
     */
    public static void setCache(String key, String value, long liveTime) {
        RedisService redisService = RedisServiceFactory.getInstance();
        if (value == null) {
            redisService.del(key);
            return;
        }
        redisService.set(key, value, liveTime);
    }

    /**
     * 清理缓存
     * @param objectKey 对象key
     * @param key 实际key
     */
    public static void clearCache(String objectKey, String key) {
        if (key == null || "".equals(key) || objectKey == null) {
            return ;
        }
        RedisService redisService = RedisServiceFactory.getInstance();
        redisService.deleteObject(objectKey, key);
        // 同时清理超时缓存
        String timoutKey = getObjectKeyWithKey(objectKey, key);
        Object obj = redisService.getObject(ObjectCache.DELETE_OBJECT_KEY, timoutKey);
        if (obj != null) {
            redisService.deleteObject(ObjectCache.DELETE_OBJECT_KEY, timoutKey);
        }
    }

    /**
     * 获取缓存
     * @author Jef
     * @date 2020/9/23
     * @param objectKey 对象key
     * @param key 实际key
     * @return java.lang.Object
     */
    public static Object getCache(String objectKey, String key) {
        RedisService redisService = RedisServiceFactory.getInstance();
        return redisService.getObject(objectKey, key);
    }

    /**
     * 获得缓存参数值，注意该方法不需要ObjectKey
     * @param key 实际key
     * @return 缓存参数值
     */
    public static String getCache(String key) {
        RedisService redisService = RedisServiceFactory.getInstance();
        String value;
        try {
            value = redisService.get(key);
        } catch (Exception e) {
            return null;
        }
        return value;
    }

    /**
     * 获取objectKey和key的连接
     * @author Jef
     * @date 2020/9/23
     * @param objectKey 对象key
     * @param key 实际key
     * @return java.lang.String
     */
    private static String getObjectKeyWithKey(String objectKey, String key) {
        return objectKey + ";" + key;
    }

    public static RedisService getRedisService() {
        return RedisServiceFactory.getInstance();
    }

    /**
     * 查看当前的缓存是否存在且未过期，默认15秒
     *
     * @param key
     * @param value
     * @return boolean 设置成功true,否则fasle
     * @author Jef
     */
    public static boolean checkCacheExistAndNotExpire(String key, String value) {
        return !getRedisService().setIfNotExistAndExpire(key, value, 15000);
    }

    /**
     * 查看当前的缓存是否存在且未过期
     *
     * @param key
     * @param value
     * @param expireMillis 超时时间 单位毫秒
     * @return boolean 设置成功true,否则fasle
     * @author Jef
     */
    public static boolean checkCacheExistAndNotExpire(String key, String value, long expireMillis) {
        return !getRedisService().setIfNotExistAndExpire(key, value, expireMillis);
    }
}
