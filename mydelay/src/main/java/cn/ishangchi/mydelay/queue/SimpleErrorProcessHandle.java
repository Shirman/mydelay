package cn.ishangchi.mydelay.queue;

import cn.ishangchi.mydelay.delay.MyBaseDelay;
import org.apache.log4j.Logger;

/**
 * @author Shirman
 * @date : Create in 9:19 2018/4/9
 * @description:
 */
public class SimpleErrorProcessHandle implements ErrorProcessHandle{
    private static Logger logger = Logger.getLogger(SimpleErrorProcessHandle.class);
    private ErrorProcessStrategy errorProcessStrategy;

    public SimpleErrorProcessHandle(ErrorProcessStrategy errorProcessStrategy) {
        this.errorProcessStrategy = errorProcessStrategy;
    }

    @Override
    public void handle(MyDelayQueue delayQueue, MyBaseDelay baseDelay) {
        MyBaseDelay strategyDelay = this.errorProcessStrategy.strategy(baseDelay);
        if (strategyDelay == null){
            logger.debug("retry delay is null,to empty redis delay");
            delayQueue.remove(baseDelay);
        }
        delayQueue.doPut(strategyDelay);
    }
}
