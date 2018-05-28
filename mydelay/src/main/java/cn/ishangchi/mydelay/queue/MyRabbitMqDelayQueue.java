package cn.ishangchi.mydelay.queue;

import cn.ishangchi.mydelay.config.DelayQueueConfig;
import cn.ishangchi.mydelay.delay.MyBaseDelay;

import java.util.List;

/**
 * //todo 尚未实现
 * @author Shirman
 * @date : Create in 15:46 2018/4/4
 * @description:
 */
public class MyRabbitMqDelayQueue extends MyBaseDelayQueue{
    @Override
    public void doPut(MyBaseDelay delay) {

    }

    @Override
    public void putList(List<MyBaseDelay> delays) {

    }

    @Override
    public void remove(MyBaseDelay delay) {

    }

    @Override
    public MyBaseDelay get(String key) {
        return null;
    }

    @Override
    public DelayQueueConfig getConfig() {
        return null;
    }
}
