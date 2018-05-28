package cn.ishangchi.mydelay.lock;

import cn.ishangchi.mydelay.config.DelayQueueConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;

/**
 * @author 刀锋
 * @date : Create in 11:57 2018/4/13
 * @description:
 */
public class JedisSimpleLock {
    private static Logger logger = LoggerFactory.getLogger(JedisSimpleLock.class);
    public static final String KEY_SUFFIX = "_LOCK";
    public static final String LOCK_VALUE = "LOCKED";
    public static final int DEFAULT_RELEASE_TIME = 60;
    public static boolean lock(String key, int timeRelease, BinaryJedis jedis, boolean isDelayShare){
        if (isDelayShare){
            return true;
        }
        boolean result =false;
        try {
            byte[] lockKey = (key + KEY_SUFFIX).getBytes(DelayQueueConfig.DEFAULT_CHARSET);
            synchronized (key.intern()){
                if (jedis.setnx(lockKey, LOCK_VALUE.getBytes(DelayQueueConfig.DEFAULT_CHARSET)) > 0){
                    result = true;
                    jedis.expire(lockKey, timeRelease);
                }
            }
        } catch (UnsupportedEncodingException e) {
            //do nothing
        }
        return result;
    }

    public static void unlock(String key, BinaryJedis jedis){
        try {
            byte[] lockKey = (key + KEY_SUFFIX).getBytes(DelayQueueConfig.DEFAULT_CHARSET);
            jedis.del(lockKey);
        } catch (Exception e) {
            logger.error("jedis 解锁异常", e);
        }
    }



    public static boolean lock(String key, Jedis jedis){
        return lock(key, DEFAULT_RELEASE_TIME, jedis, false);
    }
}
