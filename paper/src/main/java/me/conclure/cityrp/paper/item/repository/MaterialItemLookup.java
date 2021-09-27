package me.conclure.cityrp.paper.item.repository;

import me.conclure.cityrp.paper.item.Item;
import me.conclure.cityrp.common.utility.Key;
import org.jspecify.nullness.Nullable;

public interface MaterialItemLookup<M> {
    void register(M material, Key key);

    @Nullable Item lookup(M material);
}
