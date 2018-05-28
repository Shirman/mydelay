package cn.ishangchi.mydelay.queue;

import cn.ishangchi.mydelay.delay.MyBaseDelay;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Shirman
 * @date : Create in 14:04 2018/4/8
 * @description:
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public interface MyDelayInvoker {
    /**
     * 执行回调
     * @param delay
     */
    void invoke(MyBaseDelay delay);

    /**
     *  业务上处理错误
     * @param delay
     */
    void onError(MyBaseDelay delay, Exception e);
}
