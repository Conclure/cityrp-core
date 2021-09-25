package me.conclure.cityrp.item.repository;

import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.utility.Key;
import org.jspecify.nullness.Nullable;

public interface ItemRepository {
    @Nullable
    Item addItem(Key key, Item item);

    @Nullable
    Item getItem(Key key);

    @Nullable
    Key getKey(Item item);

    MaterialItemLookup getMaterialLookup();
}
