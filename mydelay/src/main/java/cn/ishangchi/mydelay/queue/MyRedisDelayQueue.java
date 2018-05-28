package cn.ishangchi.mydelay.queue;

import cn.ishangchi.mydelay.config.DelayQueueConfig;
import cn.ishangchi.mydelay.config.RedisDelayQueueConfig;
import cn.ishangchi.mydelay.delay.DelayKeyHandle;
import cn.ishangchi.mydelay.delay.MyBaseDelay;
import cn.ishangchi.mydelay.util.Jackson2JsonRedisSerializer;
import org.apache.log4j.Logger;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Shirman
 * @date : Create in 15:46 2018/4/4
 * @description: 基于redis实现的延迟队列
 * 原理：利用redis键空间通知，监听键的过期消息，实现延迟。
 * 由于redis键一旦过期即无法获取键值，因此设计了如下的数据结构：
 *      key 和 val在redis 中分开存储,原key存储了数据的状态（ACTIVE，PROCESSING，PROCESSING_FAIL）
 *      key_v(数据key) 存储了真实数据，并设置了远远大于原key的有效期
 *  以上数据结构保证了key在过期时，队列能够仍然持有数据
 */
public class MyRedisDelayQueue extends MyBaseDelayQueue{
    private static Logger logger = Logger.getLogger(MyRedisDelayQueue.class);

    private RedisDelayQueueConfig config;

    private BinaryJedisDelayPubSub jedisDelayPubSub;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MyRedisDelayQueue(RedisDelayQueueConfig config) {
        this.config = config;
        jedisDelayPubSub = new BinaryJedisDelayPubSub();
        jedisDelayPubSub.setDelayQueue(this);
        boolean onlyPublish = config.isOnlyPublish();
        if (!onlyPublish){
            startEventExpireListener();
        }
    }

    private void startEventExpireListener(){
        executorService.submit(new DelayExpire(config, jedisDelayPubSub));
    }

    public static class DelayExpire implements Runnable{

        private RedisDelayQueueConfig redisDelayQueueConfig;

        private BinaryJedisDelayPubSub jedisDelayPubSub;

        public DelayExpire(RedisDelayQueueConfig redisDelayQueueConfig, BinaryJedisDelayPubSub jedisDelayPubSub) {
            this.redisDelayQueueConfig = redisDelayQueueConfig;
            this.jedisDelayPubSub = jedisDelayPubSub;
        }

        @Override
        public void run() {
            while(true){
                BinaryJedis jedis = null;
                try {
                    jedis = this.redisDelayQueueConfig.jedis();
                    logger.info("mydelay subscribe redis expired key start...");
                    jedis.subscribe(this.jedisDelayPubSub,
                            BinaryJedisDelayPubSub.EXPIRE_CHANNEL.getBytes(DelayQueueConfig.DEFAULT_CHARSET));
                    logger.info("mydelay subscribe end...");
                } catch (UnsupportedEncodingException e) {
                    logger.error("mydelay subscribe error when listening", e);
                } finally {
                    if (jedis != null){
                        jedis.close();
                    }
                }
            }
        }
    }


    @Override
    public void doPut(MyBaseDelay delay) {
        if (delay == null){
            return;
        }
        byte[] serialize = Jackson2JsonRedisSerializer.serialize(delay);
        BinaryJedis jedis = this.config.jedis();
        try {
            long remainTime = delay.remainingTime();
            if (remainTime < 0){
                remainTime = RedisDelayQueueConfig.DEFAULT_MIN_DELAY;
            }
            String key = getDelayKey(delay);
            logger.debug("set delay reaming time, remaining " + remainTime + " ms");
            /**
             * 设置原key的有效期
             */
            jedis.psetex(key.getBytes(DelayQueueConfig.DEFAULT_CHARSET), remainTime,
                    delayStatus(delay).name().getBytes(DelayQueueConfig.DEFAULT_CHARSET));

            /**
             * 设置数据key存储真实值的有效期
             */
            jedis.psetex(getDelayDataKey(key).getBytes(DelayQueueConfig.DEFAULT_CHARSET),
                    remainTime + RedisDelayQueueConfig.DEFAULT_PROCESS_ERROR_DATA_MAX_KEEP_TIME,
                    serialize);
        } catch (UnsupportedEncodingException e) {
            //nothing
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    @Override
    public void putList(List<MyBaseDelay> delays) {
        BinaryJedis jedis = null;
        Pipeline pipelined = null;
        try {
            jedis = this.config.jedis();
            pipelined = jedis.pipelined();
            if (delays != null && delays.size() > 0) {
                for (int i = 0; i < delays.size(); i++) {
                    MyBaseDelay delay = delays.get(i);
                    byte[] serialize = Jackson2JsonRedisSerializer.serialize(delay);
                    long remainTime = delay.remainingTime();
                    if (remainTime < 0){
                        remainTime = RedisDelayQueueConfig.DEFAULT_MIN_DELAY;
                    }
                    String key = getDelayKey(delay);
                    logger.debug("set delay reaming time, remaining " + remainTime + " ms");
                    /**
                     * 设置原key的有效期
                     */
                    pipelined.psetex(key.getBytes(DelayQueueConfig.DEFAULT_CHARSET), remainTime,
                            delayStatus(delay).name().getBytes(DelayQueueConfig.DEFAULT_CHARSET));

                    /**
                     * 设置数据key存储真实值的有效期
                     */
                    pipelined.psetex(getDelayDataKey(key).getBytes(DelayQueueConfig.DEFAULT_CHARSET),
                            remainTime + RedisDelayQueueConfig.DEFAULT_PROCESS_ERROR_DATA_MAX_KEEP_TIME,
                            serialize);
                }
                pipelined.sync();
            }

        } catch (UnsupportedEncodingException e) {
            //nothing
        } finally {
            if (jedis != null){
                jedis.close();
            }
            if (null != pipelined) {
                try {
                    pipelined.close();
                } catch (IOException e) {
                    logger.error("pipelined close error,批量设置delay元素异常");
                }
            }
        }
    }

    private String getDelayKey(MyBaseDelay delay) {
        DelayKeyHandle delayKeyHandle = config.getDelayKeyHandle();
        return delayKeyHandle.generateKey(delay);
    }

    private String getDelayDataKey(String key) {
        if(key.endsWith(RedisDelayQueueConfig.MYDELAY_VALUE_SUFFIX)){
            return key;
        }
        return key + "_v";
    }

    @Override
    public void remove(MyBaseDelay delay) {
        String key = getDelayKey(delay);
        BinaryJedis jedis = this.config.jedis();
        try {
            logger.debug("remove delay,delay id is " + delay.getId());
            jedis.del(key.getBytes(DelayQueueConfig.DEFAULT_CHARSET));
            jedis.del(getDelayDataKey(key).getBytes(DelayQueueConfig.DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            //do nothing
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    @Override
    public MyBaseDelay get(String key) {
        MyBaseDelay result = null;
        BinaryJedis jedis = this.config.jedis();
        try {
            byte[] bytes = jedis.get(getDelayDataKey(key).getBytes(DelayQueueConfig.DEFAULT_CHARSET));
            if(bytes != null && bytes.length > 0){
                try {
                    result = Jackson2JsonRedisSerializer.deserialize(bytes, MyBaseDelay.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                logger.debug("mydelay get ,but bytes is empty");
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("mydelay get is error", e);
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return result;
    }

    @Override
    public RedisDelayQueueConfig getConfig() {
        return this.config;
    }
}
