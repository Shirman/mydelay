package cn.ishangchi.mydelay.queue;


import cn.ishangchi.mydelay.delay.MyBaseDelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shirman
 * @date : Create in 13:25 2022/07/11
 * @description:
 */

public class SimpleLogErrorProcessGiveUpStrategy implements ErrorProcessStrategy {

    private static Logger logger = LoggerFactory.getLogger(SimpleLogErrorProcessGiveUpStrategy.class);

    @Override
    public MyBaseDelay strategy(MyBaseDelay delay) {
        logger.error("SimpleLogErrorProcessGiveUpStrategy error data ---> {}",delay);
        return null;
    }
}
