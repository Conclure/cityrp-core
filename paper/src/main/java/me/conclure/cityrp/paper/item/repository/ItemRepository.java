package me.conclure.cityrp.paper.item.repository;

import me.conclure.cityrp.paper.item.Item;
import me.conclure.cityrp.common.utility.Key;
import org.jspecify.nullness.Nullable;

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
