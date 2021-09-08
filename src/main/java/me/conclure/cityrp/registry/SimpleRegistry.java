package me.conclure.cityrp.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.commons.lang.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.nullness.Nullable;

import java.util.*;

public class SimpleRegistry<E> implements Registry<E> {
    private final BiMap<Key,E> keyMap;
    private final Int2ObjectMap<E> id2valueMap;
    private final Object2IntMap<E> value2idMap;
    private final ArrayList<E> list;
    private final MutableInt idTracker;
    private final Class<E> clazz;

    public SimpleRegistry(Class<E> clazz) {
        this.clazz = clazz;
        this.keyMap = HashBiMap.create();
        this.id2valueMap = new Int2ObjectOpenHashMap<>();
        this.value2idMap = new Object2IntOpenHashMap<>();
        this.list = new ArrayList<>();
        this.idTracker = new MutableInt(0);
    }

    @Override
    public OptionalInt getId(E entry) {
        return OptionalInt.of(this.value2idMap.getInt(entry));
    }

    @Override
    public @Nullable E fromId(int id) {
        return this.id2valueMap.get(id);
    }

    @Override
    public @Nullable E fromKey(Key key) {
        return this.keyMap.get(key);
    }

    @Override
    public Class<E> getBaseType() {
        return this.clazz;
    }

    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.unmodifiableIterator(this.list.iterator());
    }

    @Override
    public int register(Key key, E entry) {
        this.keyMap.put(key,entry);
        int id = this.idTracker.intValue();
        this.id2valueMap.put(id,entry);
        this.value2idMap.put(entry,id);
        this.list.add(entry);
        this.idTracker.increment();
        return id;
    }
}
