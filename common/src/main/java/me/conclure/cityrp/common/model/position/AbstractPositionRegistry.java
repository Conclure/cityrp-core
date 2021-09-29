package me.conclure.cityrp.common.model.position;

import com.google.common.collect.ImmutableMap;
import me.conclure.cityrp.common.utility.Key;

import java.util.OptionalInt;
import java.util.stream.Stream;

public abstract class AbstractPositionRegistry<PlatformEntity, PlatformWorld>
        implements PositionRegistry<PlatformEntity, PlatformWorld> {
    private final ImmutableMap<Key,Position<PlatformEntity, PlatformWorld>> map;
    private final ImmutableMap<Integer,Key> id2KeyMap;
    private final ImmutableMap<Key,Integer> key2IdMap;

    protected AbstractPositionRegistry(Stream<Position<PlatformEntity, PlatformWorld>> positions) {
        ImmutableMap.Builder<Key,Position<PlatformEntity, PlatformWorld>> mapBuilder = ImmutableMap.builder();

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
    public Position<PlatformEntity, PlatformWorld> getByKey(Key key) {
        return this.map.get(key);
    }

    @Override
    public Stream<Position<PlatformEntity, PlatformWorld>> stream() {
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
    public Position<PlatformEntity, PlatformWorld> getById(int id) {
        Key key = this.id2KeyMap.get(id);
        if (key == null) {
            return null;
        }
        return this.getByKey(key);
    }
}
