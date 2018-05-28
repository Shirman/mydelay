package cn.ishangchi.mydelay.queue;


import cn.ishangchi.mydelay.delay.MyBaseDelay;

/**
 * @author Shirman
 * @date : Create in 9:25 2018/4/9
 * @description:
 */
public class SimpleErrorProcessGiveUpStrategy implements ErrorProcessStrategy {

    @Override
    public MyBaseDelay strategy(MyBaseDelay delay) {
        return null;
    }
}
