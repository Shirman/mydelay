package cn.ishangchi.mydelay.queue;


import cn.ishangchi.mydelay.delay.MyBaseDelay;
import cn.ishangchi.mydelay.exception.TimeLessThanZeroException;
import cn.ishangchi.mydelay.exception.TriggerTimeNotConfigException;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Shirman
 * @date : Create in 15:05 2018/4/4
 * @description:
 */
public abstract class MyBaseDelayQueue implements MyDelayQueue{
    public enum STATUS{
        //未过期
        ACTIVE,
        //处理时发生错误，未被正确消费的
        PROCESSING_FAIL,
    }
    private static final int DELAY_MIN = 0;

    public void put(MyBaseDelay delay, long time, TimeUnit timeUnit) {
        if(time < DELAY_MIN){
            throw new TimeLessThanZeroException("延迟时间小于0");
        }
        TimeUnit ms = TimeUnit.MILLISECONDS;
        long convert = ms.convert(time, timeUnit) + System.currentTimeMillis();
        Date triggerTime = new Date(convert);
        put(delay, triggerTime);
    }

    public void put(MyBaseDelay delay, Date triggerTime) {
        if (triggerTime == null) {
            throw new TriggerTimeNotConfigException("未设置延时时间");
        }
        delay.setTriggerTime(triggerTime);
        put(delay);
    }

    public void put(MyBaseDelay delay) {
        Date triggerTime = delay.getTriggerTime();
        if (triggerTime == null) {
            throw new TriggerTimeNotConfigException("未设置延时时间");
        }
        delay.setCreateTime(new Date());
        doPut(delay);
    }


    @Override
    public long remainingTime(MyBaseDelay delay, TimeUnit timeUnit) {
        return timeUnit.convert(delay.remainingTime(), TimeUnit.MICROSECONDS);
    }

    @Override
    public STATUS delayStatus(MyBaseDelay delay) {
        int retryNum = delay.getRetryNum();
        if(retryNum > 0){
            return STATUS.PROCESSING_FAIL;
        }else{
            return STATUS.ACTIVE;
        }
    }
}
