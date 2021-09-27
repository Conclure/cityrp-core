package me.conclure.cityrp.common.position;

import com.google.common.collect.ImmutableMap;
import me.conclure.cityrp.common.utility.Key;

import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Stream;

public abstract class AbstractPositionRegistry<E,W> implements PositionRegistry<E,W> {
    private final ImmutableMap<Key,Position<E,W>> map;
    private final ImmutableMap<Integer,Key> id2KeyMap;
    private final ImmutableMap<Key,Integer> key2IdMap;

    protected AbstractPositionRegistry(Stream<Position<E,W>> positions) {
        ImmutableMap.Builder<Key,Position<E,W>> mapBuilder = ImmutableMap.builder();

        positions.forEach(position -> mapBuilder.put(position.getKey(),position));

        this.map = mapBuilder.build();

        int id = 0;
        ImmutableMap.Builder<Integer,Key> id2KeyMapBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<Key,Integer> key2idMapBuilder = ImmutableMap.builder();

        for (Key key : this.map.keySet()) {
            int currentId = id++;
            id2KeyMapBuilder.put(currentId,key);
            key2idMapBuilder.put(key,currentId);
        }

        this.id2KeyMap = id2KeyMapBuilder.build();
        this.key2IdMap = key2idMapBuilder.build();
    }

    @Override
    public Position<E, W> getByKey(Key key) {
        return this.map.get(key);
    }

    @Override
    public Stream<Position<E, W>> stream() {
        return this.map.values().stream();
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public OptionalInt getId(Key key) {
        Integer id = this.key2IdMap.get(key);

        if (id == null) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(id);
    }

    @Override
    public Position<E, W> getById(int id) {
        Key key = this.id2KeyMap.get(id);
        if (key == null) {
            return null;
        }
        return this.getByKey(key);
    }
}
