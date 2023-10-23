package com.jef.redis.cache;

import com.jef.redis.RedisService;
import com.jef.redis.RedisServiceFactory;

import org.apache.ibatis.cache.Cache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Jef
 * @create 2018/7/13 10:33
 */
public class RedisCache implements Cache {
    private static Logger logger = LogManager.getLogger(RedisCache.class);

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private String objectKey;

    public RedisCache(final String objectKey) {
        if (objectKey == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.debug("MybatisRedisCache:id=" + objectKey);
        this.objectKey = objectKey;
    }

    @Override
    public String getId() {
        return this.objectKey;
    }

    @Override
    public int getSize() {
        return (int) RedisService().dbSize();
    }

    @Override
    public void putObject(Object key, Object value) {
        logger.debug("RedisCache putObject:" + key + "=" + value);
        if (key != null) {
            RedisService().putObject(this.objectKey, key.toString(), value, false);
        }
    }

    @Override
    public Object getObject(Object key) {
        if (key != null) {
            return RedisService().getObject(this.objectKey, key.toString());
        } else {
            return null;
        }
    }

    @Override
    public Object removeObject(Object key) {
        logger.debug("RedisCache removeObject:" + key);
        if (key != null) {
            RedisService().deleteObject(this.objectKey, key.toString());
            return null;
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        // 清理相关key
        RedisService().deleteAll(this.objectKey);
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    public RedisService RedisService() {
        // 工厂返回实例
        return RedisServiceFactory.getInstance();
    }

}
