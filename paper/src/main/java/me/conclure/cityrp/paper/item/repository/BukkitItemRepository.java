package me.conclure.cityrp.paper.item.repository;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import me.conclure.cityrp.common.utility.Key;
import me.conclure.cityrp.paper.item.Item;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Material;
import org.jspecify.nullness.Nullable;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class BukkitItemRepository implements ItemRepository<Material> {
    private final BiMap<Key, Item> map;
    private final ConcurrentMap<Integer, Key> idMap;
    private final ConcurrentMap<Key, Integer> keyMap;
    private final MutableInt idTracker;
    private final MaterialItemLookup<Material> materialItemLookup;
    private final AtomicBoolean isOperating;

    public BukkitItemRepository(
            BiMap<Key, Item> map,
            ConcurrentMap<Integer, Key> idMap,
            ConcurrentMap<Key, Integer> keyMap
    ) {
        Preconditions.checkNotNull(map);
        Preconditions.checkNotNull(idMap);
        Preconditions.checkNotNull(keyMap);

        this.idMap = idMap;
        this.keyMap = keyMap;
        this.map = map;
        this.materialItemLookup = new BukkitMaterialItemLookup(this);
        this.idTracker = new MutableInt();
        this.isOperating = new AtomicBoolean();
    }

    @Override
    public Item add(Key key, Item item) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(item);

        Item oldItem = this.map.put(key, item);

        int id = this.idTracker.intValue();
        this.idMap.put(id, key);
        this.keyMap.put(key, id);

        return oldItem;
    }

    @Override
    public @Nullable Item get(Key key) {
        Preconditions.checkNotNull(key);

        return this.map.get(key);
    }

    @Override
    public @Nullable Item replace(Key key, Item item) {
        return null;
    }

    @Override
    public boolean contains(Key key) {
        return false;
    }

    @Override
    public boolean contains(Item key) {
        return false;
    }

    @Override
    public void remove(Key key) {

    }

    @Override
    public void remove(Item item) {

    }

    @Override
    public @Nullable Key getKey(Item item) {
        Preconditions.checkNotNull(item);

        return this.map.inverse().get(item);
    }

    @Override
    public MaterialItemLookup<Material> getMaterialLookup() {
        return this.materialItemLookup;
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public void forEach(Consumer<? super Item> consumer) {
        if (this.isOperating.get()) {
            return;
        }
        this.isOperating.set(true);
    }
}
