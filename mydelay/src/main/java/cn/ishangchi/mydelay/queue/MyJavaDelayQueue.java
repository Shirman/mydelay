package cn.ishangchi.mydelay.queue;


import cn.ishangchi.mydelay.config.DelayQueueConfig;
import cn.ishangchi.mydelay.delay.MyBaseDelay;

import java.util.List;

/**
 * //todo 尚未实现
 * @author Shirman
 * @date : Create in 15:34 2018/4/4
 * @description: 使用java 自带的delayQueue实现延时队列。适合单机，集群请选择redis、或者rabbit版本
 */
public class MyJavaDelayQueue extends MyBaseDelayQueue{
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
