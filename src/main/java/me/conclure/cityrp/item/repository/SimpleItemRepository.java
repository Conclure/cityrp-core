package me.conclure.cityrp.item.repository;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.utility.Key;
import org.apache.commons.lang.mutable.MutableInt;
import org.jspecify.nullness.Nullable;

public class SimpleItemRepository implements ItemRepository {
    private final BiMap<Key, Item> map;
    private final Int2ObjectMap<Key> idMap;
    private final Object2IntMap<Key> keyMap;
    private final MutableInt idTracker;
    private final MaterialItemLookup materialItemLookup;

    public SimpleItemRepository(
            BiMap<Key, Item> map,
            Int2ObjectMap<Key> idMap,
            Object2IntMap<Key> keyMap,
            MaterialItemLookup materialItemLookup
    ) {
        this.materialItemLookup = materialItemLookup;
        Preconditions.checkNotNull(map);
        Preconditions.checkNotNull(idMap);
        Preconditions.checkNotNull(keyMap);

        this.idMap = idMap;
        this.keyMap = keyMap;
        this.map = map;
        this.idTracker = new MutableInt();
    }

    @Override
    public Item addItem(Key key, Item item) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(item);

        return this.map.put(key, item);
    }

    @Override
    public @Nullable Item getItem(Key key) {
        Preconditions.checkNotNull(key);

        return this.map.get(key);
    }

    @Override
    public @Nullable Key getKey(Item item) {
        Preconditions.checkNotNull(item);

        return this.map.inverse().get(item);
    }

    @Override
    public MaterialItemLookup getMaterialLookup() {
        return this.materialItemLookup;
    }
}
