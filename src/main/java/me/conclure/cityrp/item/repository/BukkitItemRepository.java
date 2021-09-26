package me.conclure.cityrp.item.repository;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.utility.Key;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jspecify.nullness.Nullable;

import java.util.Iterator;
import java.util.function.Consumer;

public class BukkitItemRepository implements ItemRepository<Material> {
    private final BiMap<Key, Item> map;
    private final Int2ObjectMap<Key> idMap;
    private final Object2IntMap<Key> keyMap;
    private final MutableInt idTracker;
    private final MaterialItemLookup<Material> materialItemLookup;

    public BukkitItemRepository(
            BiMap<Key, Item> map,
            Int2ObjectMap<Key> idMap,
            Object2IntMap<Key> keyMap
    ) {
        Preconditions.checkNotNull(map);
        Preconditions.checkNotNull(idMap);
        Preconditions.checkNotNull(keyMap);

        this.idMap = idMap;
        this.keyMap = keyMap;
        this.map = map;
        this.materialItemLookup = new BukkitMaterialItemLookup(this);
        this.idTracker = new MutableInt();
    }

    @Override
    public Item add(Key key, Item item) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(item);

        Item oldItem = this.map.put(key, item);

        int id = this.idTracker.intValue();
        this.idMap.put(id,key);
        this.keyMap.put(key,id);

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

    @NotNull
    @Override
    public Iterator iterator() {
        return new IteratorImpl();
    }

    class IteratorImpl implements Iterator {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Item next() {
            return null;
        }

        @Override
        public void remove() {
            Iterator.super.remove();
        }

        @Override
        public void replace(Item item) {

        }
    }
}
