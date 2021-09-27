package me.conclure.cityrp.utility.logging;

public class DelegatedSlf4jLogger implements Logger {
    private final org.slf4j.Logger delegatingLogger;

    public DelegatedSlf4jLogger(org.slf4j.Logger delegatingLogger) {
        this.delegatingLogger = delegatingLogger;
    }

    @Override
    public void error(String msg) {
        this.delegatingLogger.error(msg);
    }

    @Override
    public void errorf(String msg, Object... args) {
        this.error(String.format(msg, args));
    }

    @Override
    public void error(Throwable cause) {
        this.delegatingLogger.error(cause.getMessage(), cause);
    }

    @Override
    public void error(Throwable cause, String msg) {
        this.delegatingLogger.error(msg, cause);
    }

    @Override
    public void errorf(Throwable cause, String msg, Object... args) {
        this.error(cause, String.format(msg, args));
    }

    @Override
    public void warn(String msg) {
        this.delegatingLogger.warn(msg);
    }

    @Override
    public void warnf(String msg, Object... args) {
        this.warn(String.format(msg, args));
    }

    @Override
    public void warn(Throwable cause) {
        this.delegatingLogger.warn(cause.getMessage(), cause);
    }

    @Override
    public void warn(Throwable cause, String msg) {
        this.delegatingLogger.warn(msg, cause);
    }

    @Override
    public void warnf(Throwable cause, String msg, Object... args) {
        this.warn(cause, String.format(msg, args));
    }

    @Override
    public void info(String msg) {
        this.delegatingLogger.info(msg);
    }

    @Override
    public void infof(String msg, Object... args) {
        this.info(String.format(msg, args));
    }

    @Override
    public void info(Throwable cause) {
        this.delegatingLogger.info(cause.getMessage(), cause);
    }

    @Override
    public void info(Throwable cause, String msg) {
        this.delegatingLogger.info(msg, cause);
    }

    @Override
    public void infof(Throwable cause, String msg, Object... args) {
        this.info(cause, String.format(msg, args));
    }

    @Override
    public void debug(String msg) {
        this.delegatingLogger.debug(msg);
    }

    @Override
    public void debugf(String msg, Object... args) {
        this.debug(String.format(msg, args));
    }

    @Override
    public void debug(Throwable cause) {
        this.delegatingLogger.debug(cause.getMessage(), cause);
    }

    @Override
    public void debug(Throwable cause, String msg) {
        this.delegatingLogger.debug(msg, cause);
    }

    @Override
    public void debugf(Throwable cause, String msg, Object... args) {
        this.debug(cause, String.format(msg, args));
    }
}
