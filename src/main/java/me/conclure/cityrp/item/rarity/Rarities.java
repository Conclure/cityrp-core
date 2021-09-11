package me.conclure.cityrp.item.rarity;

import me.conclure.cityrp.registry.Key.Id;
import me.conclure.cityrp.utility.Unconstructable;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class Rarities extends Unconstructable {
    @Id("common")
    public static final Rarity COMMON;
    @Id("uncommon")
    public static final Rarity UNCOMMON;
    @Id("rare")
    public static final Rarity RARE;
    @Id("epic")
    public static final Rarity EPIC;
    @Id("legendary")
    public static final Rarity LEGENDARY;
    @Id("mythic")
    public static final Rarity MYTHIC;
    @Id("administrator")
    public static final Rarity ADMINISTRATOR;

    static {
        COMMON = of(NamedTextColor.GRAY);
        UNCOMMON = of(NamedTextColor.YELLOW);
        RARE = of(NamedTextColor.AQUA);
        EPIC = of(NamedTextColor.LIGHT_PURPLE);
        LEGENDARY = of(NamedTextColor.GOLD);
        MYTHIC = of(NamedTextColor.RED);
        ADMINISTRATOR = of(NamedTextColor.DARK_RED);
    }

    private static Rarity of(TextColor textColor) {
        return new Rarity(new Rarity.Properties().color(textColor));
    }
}
