package cn.ishangchi.mydelay.exception;

/**
 * @author Shirman
 * @date : Create in 15:25 2018/4/4
 * @description:
 */
public class TimeLessThanZeroException extends RuntimeException{
    public TimeLessThanZeroException() {
        super();
    }

    public TimeLessThanZeroException(String message) {
        super(message);
    }

    public TimeLessThanZeroException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeLessThanZeroException(Throwable cause) {
        super(cause);
    }

    protected TimeLessThanZeroException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
