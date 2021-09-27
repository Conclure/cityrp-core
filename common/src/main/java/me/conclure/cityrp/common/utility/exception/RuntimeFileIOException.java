package me.conclure.cityrp.common.utility.exception;

public class RuntimeFileIOException extends RuntimeException {
    public RuntimeFileIOException() {
        super();
    }

    public RuntimeFileIOException(String message) {
        super(message);
    }

    public RuntimeFileIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeFileIOException(Throwable cause) {
        super(cause);
    }

    protected RuntimeFileIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
