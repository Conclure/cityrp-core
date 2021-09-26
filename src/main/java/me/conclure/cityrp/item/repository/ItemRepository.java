package me.conclure.cityrp.item.repository;

import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.utility.Key;
import org.jspecify.nullness.Nullable;

import java.util.Iterator;

public interface ItemRepository<M> extends Iterable<Item> {
    @Nullable
    Item add(Key key, Item item);

    @Nullable
    Item get(Key key);

    @Nullable
    Item replace(Key key, Item item);

    boolean contains(Key key);

    boolean contains(Item key);

    void remove(Key key);

    void remove(Item item);

    @Nullable
    Key getKey(Item item);

    MaterialItemLookup<M> getMaterialLookup();

    int size();

    interface Iterator extends java.util.Iterator<Item> {
        void replace(Item item);
    }
}
