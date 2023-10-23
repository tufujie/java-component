package com.jef.redis;

import com.jef.common.context.SpringContextHolder;
import com.jef.util.SpringPropertiesUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author Jef
 * @create 2018/7/13 10:24
 */
public class RedisServiceFactory {
    private static final Logger logger = LogManager.getLogger(RedisServiceFactory.class);

    // 单例工厂
    private static RedisServiceFactory fatory;
    // redis服务
    private RedisService redisService;

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private RedisServiceFactory() {

    }

    /**
     * 返回redis实例
     *
     * @return
     */
    public static RedisService getInstance() {
        return getSingleton().getRedisService();
    }

    /**
     * 如果fatory未创建，则创建单例
     *
     * @return com.jef.redis.RedisServiceFactory
     * @author Jef
     * @date 2020/9/23
     */
    public static RedisServiceFactory getSingleton() {
        if (fatory == null) {
            creatFactory();
        }
        return fatory;
    }

    /**
     * 一个时间内只能有一个线程得到执行
     *
     * @author Jef
     * @date 2020/9/23
     */
    private synchronized static void creatFactory() {
        if (fatory == null) {
            fatory = new RedisServiceFactory();
        }
    }

    /**
     * 返回redis实例
     */
    private RedisService getRedisService() {
        if (redisTemplate == null) {
            createRedisTemplate();
        }
        if (stringRedisTemplate == null) {
            createStringRedisTemplate();
        }
        if (redisService == null) {
            createRedisServiceImpl();
        }
        return redisService;
    }

    /**
     * 一个时间内只能有一个线程得到执行
     *
     * @author Jef
     * @date 2020/9/23
     */
    private synchronized void createRedisTemplate() {
        if (this.redisTemplate == null) {
            logger.info("redis初始化");
            // 从Spring获取redisTemplate
            this.redisTemplate = SpringContextHolder.getBean("redisTemplate");
        }
    }

    /**
     * 一个时间内只能有一个线程得到执行
     *
     * @author Jef
     * @date 2020/9/23
     */
    private synchronized void createStringRedisTemplate() {
        if (this.stringRedisTemplate == null) {
            logger.info("stringRedis初始化");
            // 从Spring获取stringRedisTemplate
            this.stringRedisTemplate = SpringContextHolder.getBean("stringRedisTemplate");
        }
    }

    /**
     * 一个时间内只能有一个线程得到执行
     *
     * @author Jef
     * @date 2020/9/23
     */
    private synchronized void createRedisServiceImpl() {
        if (redisService == null) {
            String db = this.diffdb();
            redisService = new RedisServiceImpl(redisTemplate, stringRedisTemplate, db);
            logger.info("redis初始化完成,db:" + db);
        }
    }

    /**
     * 是否区分数据库缓存
     *
     * @return java.lang.String
     * @author Jef
     * @date 2020/9/23
     */
    private String diffdb() {
        String difdb = SpringPropertiesUtil.getProperty("redis.diffdb");
        String db = "";
        if (difdb != null && Boolean.valueOf(difdb)) {
            db = SpringPropertiesUtil.getProperty("jdbc.url");
        }
        return db;
    }


}