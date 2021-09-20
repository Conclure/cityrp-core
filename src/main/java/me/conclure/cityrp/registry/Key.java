package me.conclure.cityrp.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.checkerframework.checker.units.qual.K;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class Key {
    static final Pattern PATTERN;

    static {
        PATTERN = Pattern.compile("[a-z0-9_]+");
    }

    private static final KeyPool KEY_POOL = new KeyPool();
    private final String key;

    private Key(String key) {
        this.key = key;
    }

    public static Key of(String key) {
        Preconditions.checkArgument(PATTERN.matcher(key).matches());
        return KEY_POOL.get(key);
    }

    @Override
    public String toString() {
        return this.key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o.getClass() != Key.class) return false;
        return this.key.equals(((Key) o).key);
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Id {
        String value();
    }

    static class KeyPool {
        final LoadingCache<String,Key> keyMap;

        KeyPool() {
            this.keyMap = Caffeine.newBuilder()
                    .weakKeys()
                    .softValues()
                    .build(Key::new);
        }

        Key get(String str) {
            return this.keyMap.get(str.intern());
        }
    }
}
