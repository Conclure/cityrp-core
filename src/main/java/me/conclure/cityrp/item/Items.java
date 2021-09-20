package me.conclure.cityrp.item;

import me.conclure.cityrp.item.rarity.Rarities;
import me.conclure.cityrp.registry.Key.Id;
import org.bukkit.Material;

import java.util.function.UnaryOperator;

public class Items {

    @Id("bedrock")
    public static final Item BEDROCK;
    @Id("repeating_command_block")
    public static final Item REPEATING_COMMAND_BLOCK;

    static {
        BEDROCK = of(props -> props
                .material(Material.BEDROCK)
                .rarity(Rarities.ADMINISTRATOR));
        REPEATING_COMMAND_BLOCK = of(props -> props
                .material(Material.REPEATING_COMMAND_BLOCK)
                .rarity(Rarities.ADMINISTRATOR));
    }

    private static Item of(UnaryOperator<ItemProperties> propertiesConfigurer) {
        return new Item(propertiesConfigurer.apply(new ItemProperties()));
    }
}
