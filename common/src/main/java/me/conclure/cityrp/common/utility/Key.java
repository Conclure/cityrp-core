package me.conclure.cityrp.common.utility;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Preconditions;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.regex.Pattern;

public final class Key {
    private static final Pattern PATTERN;
    private static final KeyPool KEY_POOL = new KeyPool();

    static {
        PATTERN = Pattern.compile("[a-z0-9_]+");
    }

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

    public static Key[] fetchStaticKeys(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .map(field -> {
                    try {
                        return field.get(null);
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                })
                .filter(Key.class::isInstance)
                .map(Key.class::cast)
                .toArray(Key[]::new);
    }

    static class KeyPool {
        final LoadingCache<String, Key> keyMap;

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
