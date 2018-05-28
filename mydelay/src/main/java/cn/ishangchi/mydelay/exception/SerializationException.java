package cn.ishangchi.mydelay.exception;

/**
 * @author 刀锋
 * @date : Create in 11:59 2018/4/10
 * @description:
 */
public class SerializationException extends RuntimeException{
    public SerializationException() {
        super();
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }

    protected SerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
