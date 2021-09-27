package me.conclure.cityrp.paper.item.repository;

import me.conclure.cityrp.common.utility.Key;
import me.conclure.cityrp.paper.item.Item;
import org.bukkit.Material;
import org.jspecify.nullness.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class BukkitMaterialItemLookup implements MaterialItemLookup<Material> {
    private final Map<Material, Key> lookUpCache;
    private final ItemRepository<Material> itemRepository;

    public BukkitMaterialItemLookup(ItemRepository<Material> itemRepository) {
        this.itemRepository = itemRepository;
        this.lookUpCache = new EnumMap<>(Material.class);
    }

    @Override
    public void register(Material material, Key key) {
        this.lookUpCache.put(material, key);
    }

    @Override
    @Nullable
    public Item lookup(Material material) {
        return this.itemRepository.get(this.lookUpCache.get(material));
    }
}
