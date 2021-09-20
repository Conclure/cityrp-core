package me.conclure.cityrp.utility.function;

import me.conclure.cityrp.utility.Throwables;

@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> extends Runnable {

    void runExceptionally() throws E;

    @Override
    default void run() {
        try {
            this.runExceptionally();
        } catch (Exception e) {
            throw Throwables.delegateThrow(e);
        }
    }

}
