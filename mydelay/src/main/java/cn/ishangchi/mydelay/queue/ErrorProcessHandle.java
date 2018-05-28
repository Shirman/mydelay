package cn.ishangchi.mydelay.queue;


import cn.ishangchi.mydelay.delay.MyBaseDelay;

/**
 * @author Shirman
 * @date : Create in 15:12 2018/4/8
 * @description:
 */
public interface ErrorProcessHandle {
    /**
     * 错误消息发生时处理错误
     * @param delayQueue
     * @param baseDelay
     */
    void handle(MyDelayQueue delayQueue, MyBaseDelay baseDelay);
}
