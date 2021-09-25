package me.conclure.cityrp.item.repository;

import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.utility.Key;
import org.bukkit.Material;
import org.jspecify.nullness.Nullable;

public interface MaterialItemLookup {
    void register(Material material, Key key);

    @Nullable Item lookup(Material material);
}
