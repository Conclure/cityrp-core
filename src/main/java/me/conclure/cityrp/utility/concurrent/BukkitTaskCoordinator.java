package me.conclure.cityrp.utility.concurrent;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BukkitTaskCoordinator extends ScheduledTaskCoordinator<ScheduledExecutorService, Executor> {
    private final BukkitScheduler bukkitScheduler;
    private final Plugin plugin;

    public BukkitTaskCoordinator(Plugin plugin, BukkitScheduler bukkitScheduler) {
        super(((Supplier<ScheduledExecutorService>) () -> {
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            executor.setRemoveOnCancelPolicy(true);
            return executor;
        }).get(), bukkitScheduler.getMainThreadExecutor(plugin));
        this.bukkitScheduler = bukkitScheduler;
        this.plugin = plugin;
    }

    public BukkitTask repeat(Runnable command, long intialDelayInTicks, long intervalInTicks) {
        return this.bukkitScheduler.runTaskTimer(this.plugin, command, intialDelayInTicks, intervalInTicks);
    }

    public BukkitTask repeat(Consumer<? super BukkitRunnable> command, long intialDelayInTicks, long intervalInTicks) {
        return new BukkitRunnableImpl(command).runTaskTimer(this.plugin, intialDelayInTicks, intervalInTicks);
    }

    public BukkitTask repeat(Runnable command, long intervalInTicks) {
        return this.bukkitScheduler.runTaskTimer(this.plugin, command, intervalInTicks, intervalInTicks);
    }

    public BukkitTask repeat(Consumer<? super BukkitRunnable> command, long intervalInTicks) {
        return new BukkitRunnableImpl(command).runTaskTimer(this.plugin, intervalInTicks, intervalInTicks);
    }

    public BukkitTask later(Runnable command, long delayInTicks) {
        return this.bukkitScheduler.runTaskLater(this.plugin, command, delayInTicks);
    }

    public BukkitTask later(Consumer<? super BukkitRunnable> command, long delayInTicks) {
        return new BukkitRunnableImpl(command).runTaskLater(this.plugin, delayInTicks);
    }

    public BukkitTask execute(Runnable command) {
        return this.bukkitScheduler.runTask(this.plugin, command);
    }

    public BukkitTask execute(Consumer<? super BukkitRunnable> command) {
        return new BukkitRunnableImpl(command).runTask(this.plugin);
    }

    private static class BukkitRunnableImpl extends BukkitRunnable {
        final Consumer<? super BukkitRunnable> consumer;

        BukkitRunnableImpl(Consumer<? super BukkitRunnable> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            this.consumer.accept(this);
        }
    }
}
