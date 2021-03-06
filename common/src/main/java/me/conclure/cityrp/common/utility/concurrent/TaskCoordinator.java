package me.conclure.cityrp.common.utility.concurrent;

import me.conclure.cityrp.common.utility.function.ThrowingRunnable;
import me.conclure.cityrp.common.utility.function.ThrowingSupplier;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class TaskCoordinator<E extends Executor> {
    private final E executor;

    public TaskCoordinator(E executor) {
        this.executor = executor;
    }

    public E executor() {
        return this.executor;
    }

    public CompletableFuture<Void> run(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, this.executor);
    }

    public CompletableFuture<Void> runExceptionally(ThrowingRunnable<?> runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.runExceptionally();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }

                throw new CompletionException(e);
            }
        }, this.executor);
    }

    public <T> CompletableFuture<T> supply(Supplier<? extends T> supplier) {
        return CompletableFuture.supplyAsync(supplier::get, this.executor);
    }

    public <T> CompletableFuture<T> supplyExceptionally(ThrowingSupplier<? extends T, ?> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.getExceptionally();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }

                throw new CompletionException(e);
            }
        }, this.executor);
    }
}
