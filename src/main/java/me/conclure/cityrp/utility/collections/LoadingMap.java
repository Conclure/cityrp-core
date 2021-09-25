package me.conclure.cityrp.utility.collections;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMap;
import org.jspecify.nullness.Nullable;

import java.util.Map;
import java.util.function.Function;

public class LoadingMap<K, V> extends ForwardingMap<K, V> {
    private final Map<K, V> map;
    private final Function<? super K, ? extends V> loadingFunction;

    public LoadingMap(Map<K, V> map, Function<? super K, ? extends V> loadingFunction) {
        this.map = map;
        this.loadingFunction = loadingFunction;
    }

    @Override
    protected Map<K, V> delegate() {
        return this.map;
    }

    @Nullable
    public V getOrNull(Object key) {
        Preconditions.checkNotNull(key);
        return this.map.get(key);
    }

    @Override
    public V get(Object key) {
        Preconditions.checkNotNull(key);
        V value = this.map.get(key);

        if (value != null) {
            return value;
        }

        return this.map.computeIfAbsent((K) key, this.loadingFunction);
    }


}
