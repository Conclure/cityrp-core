package me.conclure.cityrp.common.utility.function;

import me.conclure.cityrp.common.utility.Throwables;

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
