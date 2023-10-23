package com.jef.redis;

import com.jef.util.MD5Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 封装redis 缓存服务器服务接口
 * @author Jef
 * @create 2018/7/13 10:26
 */
//@Service(value = "redisService")
public class RedisServiceImpl implements RedisService {
    private static final Logger logger = LogManager.getLogger(RedisServiceImpl.class);
    private static String redisCode = "utf-8";
    private RedisTemplate redisTemplate;

    // 区分数据库
    private String db;

    private StringRedisTemplate stringRedisTemplate;

    /**
     * @param key
     */
    @Override
    public long del(final byte[] key) {
        return (Long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.del(key);
            }
        });
    }

    @Override
    public long del(final String... keys) {
        return (Long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    result = connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }

    @Override
    public void set(final byte[] key, final byte[] value, final long liveTime) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }

    @Override
    public void set(String key, String value, long liveTime) {
        this.set(key.getBytes(), value.getBytes(), liveTime);
    }

    @Override
    public void set(String key, String value) {
        this.set(key, value, 0L);
    }

    @Override
    public void set(byte[] key, byte[] value) {
        this.set(key, value, 0L);
    }

    @Override
    public String get(final String key) {
        return this.getStringByByte(key.getBytes());
    }

    @Override
    public byte[] get(final byte[] key){
        String value = this.getStringByByte(key);
        if (value!=null) {
            return value.getBytes();
        }
        return null;
    }

    @Override
    public String getStringByByte(byte[] key) {
        String value = (String)redisTemplate.execute(new RedisCallback() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    if(connection.get(key)!=null){
                        return new String(connection.get(key), redisCode);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        return value;
    }

    @Override
    public Set setkeys(String pattern) {
        return redisTemplate.keys(pattern);

    }

    @Override
    public boolean exists(final String key) {
        return (Boolean) redisTemplate.execute(new RedisCallback() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(key.getBytes());
            }
        });
    }

    @Override
    public String flushDB() {
        return (String) redisTemplate.execute(new RedisCallback() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    @Override
    public long dbSize() {
        return (Long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    @Override
    public String ping() {
        return (String) redisTemplate.execute(new RedisCallback() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ping();
            }
        });
    }

    public RedisServiceImpl(RedisTemplate redisTemplate, StringRedisTemplate stringRedisTemplate, String jdbc ) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        if (jdbc != null && !"".equals(jdbc)) {
            this.db = MD5Utils.getMD5Code(jdbc);
        } else {
            this.db = "";
        }

    }

    @Override
    public void putObject(String objectKey, String key, Object obj, boolean limitSize) {
        try{
            if (key == null || "".equals(key) || objectKey == null || obj == null) {
                return ;
            }
            if (limitSize) {
                // 计算obj大小，超过10M不允许存储
            }
            redisTemplate.opsForHash().put(this.db + objectKey, key, obj);
        } catch(Exception e) {
            logger.error("objectKey:" + objectKey+" key:"+ key + "obj:" + obj);
            logger.error("error",e);
        }
    }

    @Override
    public Object getObject(String objKey, String key) {
        try{
            if (key!=null) {
                return (Object) redisTemplate.opsForHash().get(this.db + objKey, key);
            }
        } catch(Exception e){
            logger.error("objKey:"+objKey+" key:"+key);
            logger.error("error",e);
            //	if(objKey!=null && key!=null){
            //	//如果更新版本，反序列化错误，则清理key
            //	this.deleteObject(this.db+objKey, key);
            //	}
        }

        return null;
    }

    @Override
    public void deleteObject(String objectKey, String key) {
        try{
            redisTemplate.opsForHash().delete(this.db + objectKey, key);
        } catch(Exception e) {
            logger.error("objectKey:" + objectKey + " key:" + key);
            logger.error("error",e);
        }
    }

    @Override
    public Set getKeys(String objKey) {
        return redisTemplate.opsForHash().keys(this.db+objKey);
    }

    @Override
    public Long size(String objKey) {
        try {
            return redisTemplate.opsForHash().size(this.db+objKey);
        }catch (Exception e) {
            logger.error("objKey:"+objKey );
            logger.error("error",e);

            return Long.valueOf("0");
        }
    }

    @Override
    public void deleteAll(String objKey) {
        Set keys = this.getKeys(objKey);
        if (keys != null) {
            Iterator it = keys.iterator();
            while(it.hasNext()){
                String key = (String) it.next();
                try {
                    redisTemplate.opsForHash().delete(this.db+objKey,key);
                } catch (Exception e) {
                    logger.error("objKey:"+objKey+" key:"+key);
                    logger.error("error",e);
                }
            }
        }
    }

    @Override
    public Object setIfNotExist(final String key, final String value, final long liveTime) {
        final String script = "if redis.call('get', KEYS[1]) then return nil " +
                " else redis.call('set', KEYS[1], ARGV[1]) ; redis.call('expire', KEYS[1], ARGV[2]) return 1 end";
        Object result = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                List<String> strings = new ArrayList<String>();
                strings.add(String.valueOf(value));
                strings.add(String.valueOf(liveTime));
                if (nativeConnection instanceof Jedis) {
                    return ((Jedis) nativeConnection).eval(script, Collections.singletonList(key), strings);
                }
                return null;
            }
        });
        return result;
    }

    @Override
    public boolean setIfNotExistAndExpire(String key, String value, long expireTime) {
        Object result = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                if (key == null ) {
                    return false;
                }
                Object nativeConnection = connection.getNativeConnection();
                if (!(nativeConnection instanceof Jedis)) {
                    return false;
                }
                Jedis jedis = ((Jedis) nativeConnection);
                String setResult = jedis.set(key, value, SetParams.setParams().nx().px(expireTime));
                if (setResult != null && "OK".equals(setResult) ) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        if (result != null && (boolean)result) {
            return true;
        }
        return false;
    }

    @Override
    public Object delByLua(final String key, final String value) {
        final String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // 单点
                if (nativeConnection instanceof Jedis) {
                    return ((Jedis) nativeConnection).eval(script, Collections.singletonList(key), Collections.singletonList(value));
                }
                return 0;
            }
        });
        return result;
    }

    @Override
    public Integer getIncrCache(String redisKey, long liveTime) {
        Objects.requireNonNull(redisKey, "redisKey must be not null");
        RedisAtomicInteger redisAtomicInteger = new RedisAtomicInteger(redisKey, redisTemplate.getConnectionFactory());
        Integer value = redisAtomicInteger.getAndIncrement();
        // 初始设置过期时间
        if (liveTime > 0) {
            redisAtomicInteger.expire(liveTime, TimeUnit.MILLISECONDS);
        }
        return value;
    }

    @Override
    public Integer getDecrCache(String key, long liveTime) {
        Objects.requireNonNull(key, "redisKey must be not null");
        RedisAtomicInteger redisAtomicInteger = new RedisAtomicInteger(key, redisTemplate.getConnectionFactory());
        Integer value = redisAtomicInteger.getAndDecrement();
        // 初始设置过期时间
        if (liveTime > 0) {
            redisAtomicInteger.expire(liveTime, TimeUnit.MILLISECONDS);
        }
        return value;
    }

    @Override
    public boolean setZset(String setKey, String value, Double score) {
        Boolean add = redisTemplate.opsForZSet().add(setKey, value, score);
        return add;
    }

    @Override
    public Set<String> getZset(String setKey) {
        // 按照排名先后(从大到小)打印指定区间内的元素, -1为打印全部
        Set<String> range = redisTemplate.opsForZSet().reverseRange(setKey, 0, -1);
        return range;
    }

    @Override
    public Long removeRangeSet(String key, int start, int end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    @Override
    public Long zsetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    @Override
    public String getList(String key, int index) {
        Object first = redisTemplate.opsForList().index(key, index);
        if (first != null) {
            return first.toString();
        }
        return null;
    }

    @Override
    public boolean removeRpushListFirst(String key) {
        Object o = redisTemplate.opsForList().leftPop(key);
        return o != null ? true : false;
    }

    @Override
    public boolean rpushList(String key, String value) {
        Long aLong = redisTemplate.opsForList().rightPush(key, value);
        if (aLong == null) {
            return false;
        }
        return aLong >= 1 ? true : false;
    }

    @Override
    public boolean lpushList(String key, String value) {
        Long aLong = redisTemplate.opsForList().leftPush(key, value);
        if (aLong == null) {
            return false;
        }
        return aLong >= 1 ? true : false;
    }

    @Override
    public boolean setListExpire(String key, long milliseconds) {
        Boolean expire = redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);
        return expire;
    }

    @Override
    public boolean removeListSameValues(String key, int count, String value) {
        Long remove = redisTemplate.opsForList().remove(key, count, value);
        return remove == count ? true : false;
    }

    @Override
    public String leftPop(String key) {
        Object o = redisTemplate.opsForList().leftPop(key);
        return o != null ? (String)o : null;
    }

    @Override
    public long decrby(String key, long productQty, long liveTime) {
        Long result = redisTemplate.opsForValue().decrement(key, productQty);
        redisTemplate.expire(key, liveTime, TimeUnit.SECONDS);
        return result;
    }

    @Override
    public long decrby(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.decrementAndGet();
        return increment;
    }

    @Override
    public long incrby(String key, Integer productQty, long liveTime) {
        Long result = redisTemplate.opsForValue().increment(key, productQty);
        redisTemplate.expire(key, liveTime, TimeUnit.SECONDS);
        return result;
    }

    @Override
    public List<String> rangeList(String key, int start, int end) {
        List range = redisTemplate.opsForList().range(key, start, end);
        return range;
    }

    /**模糊查询所有的key值
     * @param pattern
     * @return
     * @return
     */
    @Override
    public Set getkeysPattern(String pattern) {
        //生产redis是集群的，需要按集群查询。暂时不可用，不支持集群
        return stringRedisTemplate.keys("*" + pattern + "*");
    }

    /**
     * @Description get increment by Redis Hash
     * @author Jef
     * @date 2020/4/27
     * @param objKey
     * @param key
     * @param liveTime
     * @return java.lang.Integer
     */
    public Integer getIncrCache(String objKey, String key, long liveTime) {
        Objects.requireNonNull(objKey, "objKey must be not null");
        Objects.requireNonNull(key, "key must be not null");
        RedisHashAtomicIntegerHelper redisHashAtomicIntegerHelper = new RedisHashAtomicIntegerHelper(objKey, key, redisTemplate.getConnectionFactory());
        redisHashAtomicIntegerHelper.setExpireTime(liveTime);
        return redisHashAtomicIntegerHelper.getAndIncrement();
    }
}