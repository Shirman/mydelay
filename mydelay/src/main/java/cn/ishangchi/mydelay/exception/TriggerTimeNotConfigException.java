package cn.ishangchi.mydelay.exception;

/**
 * @author Shirman
 * @date : Create in 15:13 2018/4/4
 * @description:
 */
public class TriggerTimeNotConfigException extends RuntimeException{
    public TriggerTimeNotConfigException() {
        super();
    }

    public TriggerTimeNotConfigException(String message) {
        super(message);
    }

    public TriggerTimeNotConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public TriggerTimeNotConfigException(Throwable cause) {
        super(cause);
    }

    protected TriggerTimeNotConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
