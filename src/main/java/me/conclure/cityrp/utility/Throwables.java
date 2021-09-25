package me.conclure.cityrp.utility;

import java.io.PrintStream;
import java.io.PrintWriter;

public final class Throwables extends Unconstructable {

    public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

    public static RuntimeException delegateThrow(Exception exception) {
        return new RuntimeException(exception);
    }

    static class DelegateException extends RuntimeException {
        final Exception exception;

        DelegateException(Exception exception) {
            this.exception = exception;
        }

        @Override
        public String getMessage() {
            return this.exception.getMessage();
        }

        @Override
        public String getLocalizedMessage() {
            return this.exception.getLocalizedMessage();
        }

        @Override
        public synchronized Throwable getCause() {
            return this.exception.getCause();
        }

        @Override
        public synchronized Throwable initCause(Throwable cause) {
            return this.exception.initCause(cause);
        }

        @Override
        public String toString() {
            return this.exception.toString();
        }

        @Override
        public void printStackTrace() {
            this.exception.printStackTrace();
        }

        @Override
        public void printStackTrace(PrintStream s) {
            this.exception.printStackTrace(s);
        }

        @Override
        public void printStackTrace(PrintWriter s) {
            this.exception.printStackTrace(s);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this.exception.fillInStackTrace();
        }

        @Override
        public StackTraceElement[] getStackTrace() {
            return this.exception.getStackTrace();
        }

        @Override
        public void setStackTrace(StackTraceElement[] stackTrace) {
            this.exception.setStackTrace(stackTrace);
        }

        @Override
        public int hashCode() {
            return this.exception.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return this.exception.equals(obj);
        }
    }
}
