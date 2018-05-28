package cn.ishangchi.mydelay.queue;


import cn.ishangchi.mydelay.delay.MyBaseDelay;

/**
 * @author Shirman
 * @date : Create in 9:23 2018/4/9
 * @description:
 */
public interface ErrorProcessStrategy {
    /**
     * 异常的delay处理
     * @param delay
     */
    MyBaseDelay strategy(MyBaseDelay delay);
}
