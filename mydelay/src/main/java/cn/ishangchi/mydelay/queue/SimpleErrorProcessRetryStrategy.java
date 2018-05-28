package cn.ishangchi.mydelay.queue;


import cn.ishangchi.mydelay.config.DelayQueueConfig;
import cn.ishangchi.mydelay.delay.MyBaseDelay;

import java.util.Date;

/**
 * @author Shirman
 * @date : Create in 9:28 2018/4/9
 * @description:
 */
public class SimpleErrorProcessRetryStrategy implements ErrorProcessStrategy{
    private int maxTryNum;
    private long delayTime;

    public SimpleErrorProcessRetryStrategy(Integer maxTryNum, Long nextMillisTimeTrigger) {
        if (maxTryNum == null){
            this.maxTryNum = DelayQueueConfig.DEFAULT_RETRY_NUM;
        }else{
            this.maxTryNum = maxTryNum;
        }

        if (nextMillisTimeTrigger == null){
            this.delayTime = DelayQueueConfig.DEFAULT_DELAY_TIME;
        }else{
            this.delayTime = nextMillisTimeTrigger;
        }
    }

    @Override
    public MyBaseDelay strategy(MyBaseDelay delay) {
        int retryNum = delay.getRetryNum();
        if (retryNum < maxTryNum){
            delay.addRetryNum();
            Date now = new Date();
            long l = now.getTime() + this.delayTime;
            Date newTriggerTime = new Date(l);
            delay.setTriggerTime(newTriggerTime);
        }else{
            delay = null;
        }
        return delay;
    }
}
