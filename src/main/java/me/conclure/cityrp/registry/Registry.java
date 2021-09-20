package me.conclure.cityrp.registry;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.commons.lang.mutable.MutableInt;
import org.jspecify.nullness.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class Registry<E,C extends Registry.RegistryContext<? extends E>> implements Iterable<E> {
    private final BiMap<Key,E> keyMap;
    private final Int2ObjectMap<Key> id2valueMap;
    private final Object2IntMap<Key> value2idMap;
    private final ArrayList<E> list;
    private final MutableInt idTracker;
    private final Class<E> clazz;

    public Registry(Class<E> clazz) {
        this.clazz = clazz;
        this.keyMap = HashBiMap.create();
        this.id2valueMap = new Int2ObjectOpenHashMap<>();
        this.value2idMap = new Object2IntOpenHashMap<>();
        this.list = new ArrayList<>();
        this.idTracker = new MutableInt(0);
    }

    public static <E> RegistryContext<E> context(Key key, E value) {
        return new RegistryContext<>(value,key);
    }

    public OptionalInt getId(E entry) {
        Key key = this.keyMap.inverse().get(entry);
        if (key == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(this.value2idMap.getInt(key));
    }

    public @Nullable E getById(int id) {
        Key key = this.id2valueMap.get(id);
        if (key == null) {
            return null;
        }
        return this.keyMap.get(key);
    }

    public @Nullable E getByKey(Key key) {
        return this.keyMap.get(key);
    }

    public @Nullable Key getKey(E value) {
        return this.keyMap.inverse().get(value);
    }

    public Class<E> getBaseType() {
        return this.clazz;
    }

    public boolean isRegistered(Key key) {
        return this.keyMap.get(key) != null;
    }

    public RegistrationsResult<E> register(C context) {
        Key key = context.getKey();
        E value = context.getValue();
        if (this.isRegistered(key)) {
            E oldValue = this.getByKey(key);
            OptionalInt idOptional = this.getId(oldValue);
            Preconditions.checkArgument(idOptional.isPresent());
            int id = idOptional.getAsInt();

            this.keyMap.put(key,value);
            this.id2valueMap.put(id,key);
            this.value2idMap.put(key,id);
            this.list.set(id,value);

            return new RegistrationsResult<>(oldValue,id, key);
        }

        int id = this.idTracker.intValue();

        this.keyMap.put(key,value);
        this.id2valueMap.put(id,key);
        this.value2idMap.put(key,id);
        this.list.add(value);

        this.idTracker.increment();
        return new RegistrationsResult<>(id, key);
    }

    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.unmodifiableIterator(this.list.iterator());
    }

    public int size() {
        return this.list.size();
    }

    public Stream<E> stream() {
        return this.list.stream();
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        for (Map.Entry<Key, E> entry : this.keyMap.entrySet()) {
            joiner.add(entry.getKey()+"="+entry.getValue());
        }
        return "Registry{"+ joiner +"}";
    }

    public static class RegistryContext<E> {
        final E value;
        final Key key;

        RegistryContext(E value, Key key) {
            this.value = value;
            this.key = key;
        }

        public E getValue() {
            return this.value;
        }


        public Key getKey() {
            return this.key;
        }
    }

    public static class RegistrationsResult<E> {
        final boolean overriddenPrevious;
        @Nullable
        final E previous;
        final int id;
        final Key key;

        RegistrationsResult(int id, Key key) {
            Preconditions.checkArgument(id >= 0);
            Preconditions.checkNotNull(key);
            this.key = key;
            this.overriddenPrevious = false;
            this.id = id;
            this.previous = null;
        }

        RegistrationsResult(E previous, int id, Key key) {
            Preconditions.checkNotNull(previous);
            Preconditions.checkNotNull(key);
            Preconditions.checkArgument(id >= 0);
            this.key = key;
            this.overriddenPrevious = true;
            this.id = id;
            this.previous = previous;
        }

        public boolean hasOverriddenPrevious() {
            return this.overriddenPrevious;
        }

        public int getId() {
            return this.id;
        }

        @Nullable
        public E getPrevious() {
            return this.previous;
        }

        public Key getKey() {
            return this.key;
        }
    }
}
