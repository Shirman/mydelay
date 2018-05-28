package cn.ishangchi.mydelay.exception;

/**
 * @author Shirman
 * @date : Create in 17:30 2018/4/4
 * @description:
 */
public class TopicNullException extends RuntimeException{
    public TopicNullException() {
        super();
    }

    public TopicNullException(String message) {
        super(message);
    }

    public TopicNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public TopicNullException(Throwable cause) {
        super(cause);
    }

    protected TopicNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
