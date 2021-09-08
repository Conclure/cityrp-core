package me.conclure.cityrp.rarity;

import me.conclure.cityrp.registry.Key;

public interface Rarities {

    @Key.Id("common")
    Rarity COMMON = new Rarity(new Rarity.Properties());
}
