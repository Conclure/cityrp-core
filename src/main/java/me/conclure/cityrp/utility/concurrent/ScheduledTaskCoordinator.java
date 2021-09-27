package me.conclure.cityrp.utility.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledTaskCoordinator<S extends ScheduledExecutorService, E extends Executor> extends TaskCoordinator<E> {
    private final S scheduler;

    public ScheduledTaskCoordinator(S scheduler, E executor) {
        super(executor);
        this.scheduler = scheduler;
    }

    public S scheduler() {
        return this.scheduler;
    }

    public ScheduledFuture<?> later(Runnable runnable, long delay, TimeUnit unit) {
        return this.scheduler.schedule(() -> this.executor().execute(runnable), delay, unit);
    }

    public ScheduledFuture<?> repeat(Runnable runnable, long initialDelay, long interval, TimeUnit unit) {
        return this.scheduler.scheduleWithFixedDelay(() -> this.executor().execute(runnable), initialDelay, interval, unit);
    }

    public ScheduledFuture<?> repeat(Runnable runnable, long interval, TimeUnit unit) {
        return this.scheduler.scheduleWithFixedDelay(() -> this.executor().execute(runnable), interval, interval, unit);
    }
}
