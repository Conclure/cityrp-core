package me.conclure.cityrp.item;

import org.bukkit.Material;

public class Items {
    public static final Item PETER;

    static {
        PETER = new Item(new ItemProperties()
                .material(Material.GLISTERING_MELON_SLICE)
        );
    }
}
