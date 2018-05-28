package cn.ishangchi.mydelay.queue;

import cn.ishangchi.mydelay.config.DelayQueueConfig;
import cn.ishangchi.mydelay.delay.MyBaseDelay;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Shirman
 * @date : Create in 14:45 2018/4/4
 * @description: 延时队列任务
 */
public interface MyDelayQueue {
    /**
     * 加入延时元素
     * @param delay
     */
    void doPut(MyBaseDelay delay);

    /**
     * 批量
     * @param delays
     */
    void putList(List<MyBaseDelay> delays);

    /**
     * 移除在延时队列中的元素
     * @param delay
     */
    void remove(MyBaseDelay delay);

    /**
     * 获取指定元素触发事件剩余的时间
     * @param delay
     * @return
     */
    long remainingTime(MyBaseDelay delay, TimeUnit timeUnit);

    /**
     * 获取延时元素，需要保证延时通知达到时，仍然能够根据key获取数据
     * 保证被用户消费之前，数据不会丢失
     * @param key
     * @return
     */
    MyBaseDelay get(String key);

    /**
     * 获取队列的配置
     * @return
     */
    DelayQueueConfig getConfig();

    /**
     * 获取delay状态
     * @param delay
     * @return
     */
    MyBaseDelayQueue.STATUS delayStatus(MyBaseDelay delay);
}
