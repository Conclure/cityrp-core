package me.conclure.cityrp.item;

import me.conclure.cityrp.registry.Key;
import me.conclure.cityrp.registry.Registries;
import org.bukkit.Material;
import org.jspecify.nullness.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class MaterialItemLookup {
    private final Map<Material, Key> lookUpCache = new EnumMap<>(Material.class);

    public void register(Material material, Key key) {
        this.lookUpCache.put(material,key);
    }

    @Nullable
    public Item lookup(Material material) {
        return Registries.ITEMS.getByKey(this.lookUpCache.get(material));
    }

    @Override
    public String toString() {
        return this.lookUpCache.toString();
    }
}
