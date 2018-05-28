package cn.ishangchi.mydelay.config;

import cn.ishangchi.mydelay.delay.DelayKeyHandle;
import cn.ishangchi.mydelay.delay.MyBaseDelay;
import cn.ishangchi.mydelay.queue.ErrorProcessStrategy;
import cn.ishangchi.mydelay.queue.SimpleErrorProcessGiveUpStrategy;
import cn.ishangchi.mydelay.queue.SimpleErrorProcessHandle;
import cn.ishangchi.mydelay.topic.Topic;
import org.apache.log4j.Logger;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

/**
 * @author Shirman
 * @date : Create in 10:11 2018/4/8
 * @description:
 */
public class RedisDelayQueueConfig extends DelayQueueConfig{
    private static Logger logger = Logger.getLogger(RedisDelayQueueConfig.class);
    public static final String MYDELAY_PREFIX = "cn:ishangchi:mydelay";
    public static final String MYDELAY_VALUE_SUFFIX = "_v";
    private Pool<Jedis> jedisPool;
    private String host;
    private int port;
    private final int DEFAULT_MAX_TOTAL_JEDIS_NUM = 10;
    private ErrorProcessStrategy DEFAULT_ERROR_PROCESS_STRATEGY = new SimpleErrorProcessGiveUpStrategy();
    /**
     * redis数据键默认延时5分钟
     */
    public static final int DEFAULT_PROCESS_ERROR_DATA_MAX_KEEP_TIME = 24 * 60 * 60 * 1000;
    public static final int DEFAULT_MIN_DELAY = 5 * 1000;


    public BinaryJedis jedis(){
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            jedisExt(jedis);
            return jedis;
        } catch (Exception e) {
            logger.error("获取jedis实例异常", e);
        }
        return jedis;
    }

    /**
     * 用户扩展jedis配置
     * @param jedis
     */
    protected void jedisExt(Jedis jedis){
        //for user setting
    }

    public RedisDelayQueueConfig(String host, int port, Pool<Jedis> jedisPool) {
        this.host = host;
        this.port = port;
        this.jedisPool = jedisPool;
        this.setErrorProcessHandle(new SimpleErrorProcessHandle(DEFAULT_ERROR_PROCESS_STRATEGY));
        this.setDelayKeyHandle(new RedisDelayKeyHandle());
    }

    public RedisDelayQueueConfig(String host, int port, Pool<Jedis> jedisPool, ErrorProcessStrategy errorProcessStrategy) {
        this.host = host;
        this.port = port;
        this.jedisPool = jedisPool;
        this.setErrorProcessHandle(new SimpleErrorProcessHandle(errorProcessStrategy));
        this.setDelayKeyHandle(new RedisDelayKeyHandle());
    }

    public RedisDelayQueueConfig(String host, int port) {
        this.host = host;
        this.port = port;
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(this.DEFAULT_MAX_TOTAL_JEDIS_NUM);
        this.jedisPool = new JedisPool(jedisPoolConfig, host, port);

        this.setErrorProcessHandle(new SimpleErrorProcessHandle(DEFAULT_ERROR_PROCESS_STRATEGY));
        this.setDelayKeyHandle(new RedisDelayKeyHandle());
    }

    public RedisDelayQueueConfig(String host, int port, ErrorProcessStrategy errorProcessStrategy) {
        this.host = host;
        this.port = port;
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(this.DEFAULT_MAX_TOTAL_JEDIS_NUM);
        this.jedisPool = new JedisPool(jedisPoolConfig, host, port);

        this.setErrorProcessHandle(new SimpleErrorProcessHandle(errorProcessStrategy));
        this.setDelayKeyHandle(new RedisDelayKeyHandle());
    }

    public static class RedisDelayKeyHandle implements DelayKeyHandle {
        @Override
        public String generateKey(MyBaseDelay ob) {
            Topic topic = ob.getTopic();
            StringBuilder sb = new StringBuilder()
                    .append(MYDELAY_PREFIX)
                    .append(":")
                    .append(topic.getName())
                    .append(":")
                    .append(ob.getId())
                    .append(":$");
            return sb.toString();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
