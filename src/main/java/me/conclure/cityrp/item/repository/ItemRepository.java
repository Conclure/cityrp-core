package me.conclure.cityrp.item.repository;

import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.utility.Key;
import org.jspecify.nullness.Nullable;

import java.util.Iterator;
import java.util.function.Consumer;

public interface ItemRepository<M> {
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

    boolean forEach(Consumer<? super Item> consumer);
}
