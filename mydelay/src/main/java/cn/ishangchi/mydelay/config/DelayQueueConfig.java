package cn.ishangchi.mydelay.config;

import cn.ishangchi.mydelay.delay.DelayKeyHandle;
import cn.ishangchi.mydelay.queue.ErrorProcessHandle;

/**
 * @author Shirman
 * @date : Create in 10:56 2018/4/8
 * @description:
 */
public abstract class DelayQueueConfig {
    public static final String DEFAULT_CHARSET = "utf-8";
    public static final int DEFAULT_RETRY_NUM = 3;
    public static final long DEFAULT_DELAY_TIME = 60 * 1000;

    /**
     * 消息是否共享,默认不共享消息，即分布式客户端只有一个能够处理消息
     */
    private boolean isDelayShare = false;

    /**
     * 只发布消息，不接收消息
     */
    private boolean onlyPublish = false;

    public boolean isOnlyPublish() {
        return onlyPublish;
    }

    public void setOnlyPublish(boolean onlyPublish) {
        this.onlyPublish = onlyPublish;
    }

    public boolean isDelayShare() {
        return isDelayShare;
    }

    public void setDelayShare(boolean delayShare) {
        isDelayShare = delayShare;
    }

    private DelayKeyHandle delayKeyHandle;
    private ErrorProcessHandle errorProcessHandle;
    private Integer maxRetryNum;
    public Object getLock(String key){return null;}
    /**
     * 错误消息延时时间
     */
    private Long delayTime;

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getMaxRetryNum() {
        return maxRetryNum;
    }

    public void setMaxRetryNum(Integer maxRetryNum) {
        this.maxRetryNum = maxRetryNum;
    }

    public ErrorProcessHandle getErrorProcessHandle() {
        return errorProcessHandle;
    }

    public void setErrorProcessHandle(ErrorProcessHandle errorProcessHandle) {
        this.errorProcessHandle = errorProcessHandle;
    }

    public DelayKeyHandle getDelayKeyHandle() {
        return delayKeyHandle;
    }

    public void setDelayKeyHandle(DelayKeyHandle delayKeyHandle) {
        this.delayKeyHandle = delayKeyHandle;
    }
}
