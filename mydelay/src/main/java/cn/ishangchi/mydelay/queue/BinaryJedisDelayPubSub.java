package cn.ishangchi.mydelay.queue;

import cn.ishangchi.mydelay.config.DelayQueueConfig;
import cn.ishangchi.mydelay.config.RedisDelayQueueConfig;
import cn.ishangchi.mydelay.delay.MyBaseDelay;
import cn.ishangchi.mydelay.lock.JedisSimpleLock;
import org.apache.log4j.Logger;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.BinaryJedisPubSub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Shirman
 * @date : Create in 13:45 2018/4/8
 * @description: 监听redis 键的过期消息
 */
public class BinaryJedisDelayPubSub extends BinaryJedisPubSub{
    private static Logger logger = Logger.getLogger(BinaryJedisDelayPubSub.class);
    private MyRedisDelayQueue delayQueue;
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public MyRedisDelayQueue getDelayQueue() {
        return delayQueue;
    }

    public void setDelayQueue(MyRedisDelayQueue delayQueue) {
        this.delayQueue = delayQueue;
    }

    public static final String EXPIRE_CHANNEL = "__keyevent@0__:expired";
    @Override
    public void onMessage(byte[] channel, byte[] message) {
        super.onMessage(channel, message);
        String cn = new String(channel);
        String key = new String(message);
        if(cn.equals(EXPIRE_CHANNEL) && key.startsWith(RedisDelayQueueConfig.MYDELAY_PREFIX)){
           logger.debug("mydelay expired msg come...");
            MyBaseDelay myBaseDelay = delayQueue.get(key);
            if (myBaseDelay != null){
                /**
                 * 异步处理消息，防止某个消息处理耗时，阻塞其他消息
                 */
                DelayTask delayTask = new DelayTask(key, myBaseDelay, delayQueue);
                executorService.submit(delayTask);
            }else {
                delayQueue.remove(myBaseDelay);
               logger.debug("mydelay msg is null...");
           }
       }
    }

    private static class DelayTask implements Runnable{
        private String key;
        private MyBaseDelay delay;
        private MyDelayQueue delayQueue;
        public static final double RELEASE_TIME_XS = 0.5;

        public DelayTask(String key, MyBaseDelay delay, MyDelayQueue delayQueue) {
            this.key = key;
            this.delay = delay;
            this.delayQueue = delayQueue;
        }

        @Override
        public void run() {
            if (null != delay && null != delayQueue) {
                RedisDelayQueueConfig config = (RedisDelayQueueConfig) delayQueue.getConfig();
                BinaryJedis jedis = config.jedis();
                boolean delayShare = config.isDelayShare();
                try {
                    if (JedisSimpleLock.lock(key, new Double(DelayQueueConfig.DEFAULT_DELAY_TIME * RELEASE_TIME_XS).intValue(), jedis, delayShare)){
                        MyBaseDelay delayInRedis = delayQueue.get(key);
                        if (delayInRedis != null){
                            this.delay.invoke(delayQueue);
                        }
                    }else{
                        logger.debug("delay has been processed");
                    }
                } catch (Exception e) {
                    logger.error("执行delay任务异常", e);
                }finally {
                    JedisSimpleLock.unlock(key, jedis);
                    jedis.close();
                }

            }
        }
    }
}
