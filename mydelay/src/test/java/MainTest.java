import cn.ishangchi.mydelay.config.RedisDelayQueueConfig;
import cn.ishangchi.mydelay.delay.MyBaseDelay;
import cn.ishangchi.mydelay.queue.MyRedisDelayQueue;
import cn.ishangchi.mydelay.queue.SimpleErrorProcessRetryStrategy;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 刀锋
 * @date : Create in 9:05 2018/4/19
 * @description:
 */
public class MainTest {
    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxIdle(3);
        jedisPoolConfig.setMaxWaitMillis(100);
        jedisPoolConfig.setTestOnBorrow(false);
        jedisPoolConfig.setTestOnReturn(true);
        Pool<Jedis> jedisPool = new JedisPool(jedisPoolConfig, "192.168.1.1", 6379, 200, "pwd");
        RedisDelayQueueConfig config = new RedisDelayQueueConfig("192.168.1.1", 6379, jedisPool, new SimpleErrorProcessRetryStrategy(3, 10 * 1000L));
        MyRedisDelayQueue delayQueue = new MyRedisDelayQueue(config);

        MyBaseDelay delay = new MyBaseDelay("hello");
        DataTest data = new DataTest();
        data.setAge(10);
        data.setName("baby");
        delay.setCreateTime(new Date());
        //不要使用匿名内部类
        delay.setInvoker(new InvokerTest());
        delay.setSerializableObject(data);
        delayQueue.put(delay, 10, TimeUnit.SECONDS);
    }
}
