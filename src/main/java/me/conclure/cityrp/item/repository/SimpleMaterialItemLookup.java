package me.conclure.cityrp.item.repository;

import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.registry.Registries;
import me.conclure.cityrp.utility.Key;
import org.bukkit.Material;
import org.jspecify.nullness.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class SimpleMaterialItemLookup implements MaterialItemLookup {
    private final Map<Material, Key> lookUpCache = new EnumMap<>(Material.class);

    @Override
    public void register(Material material, Key key) {
        this.lookUpCache.put(material, key);
    }

    @Override
    @Nullable
    public Item lookup(Material material) {
        return Registries.ITEMS.getByKey(this.lookUpCache.get(material));
    }
}
