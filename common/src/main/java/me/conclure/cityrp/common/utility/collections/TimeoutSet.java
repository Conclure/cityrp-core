package me.conclure.cityrp.common.utility.collections;

import com.github.benmanes.caffeine.cache.Cache;
import me.conclure.cityrp.common.utility.CaffeineFactory;

import java.util.concurrent.TimeUnit;

/**
 * LuckPerms (MIT License)
 *
 * @author lucko
 */
public class TimeoutSet<E> {
    private final Cache<E, Long> cache;
    private final long lifetime;

    public TimeoutSet(long duration, TimeUnit unit) {
        this.cache = CaffeineFactory.newBuilder().expireAfterWrite(duration, unit).build();
        this.lifetime = unit.toMillis(duration);
    }

    public boolean add(E item) {
        boolean present = this.contains(item);
        this.cache.put(item, System.currentTimeMillis() + this.lifetime);
        return !present;
    }

    public boolean contains(E item) {
        Long timeout = this.cache.getIfPresent(item);
        return timeout != null && timeout > System.currentTimeMillis();
    }

    public void remove(E item) {
        this.cache.invalidate(item);
    }
}
