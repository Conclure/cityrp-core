package me.conclure.cityrp.common.utility;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public final class CaffeineFactory extends Unconstructable {
    private static final ForkJoinPool LOADER = new ForkJoinPool();

    public static Caffeine<Object, Object> newBuilder() {
        return Caffeine.newBuilder().executor(LOADER);
    }

    public static Executor executor() {
        return LOADER;
    }
}
