package me.conclure.cityrp.rarity;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.registry.Key;
import net.kyori.adventure.text.format.TextColor;

public class Rarity {
    private final TextColor color;

    public Rarity(Properties properties) {
        Preconditions.checkNotNull(properties);

        this.color = properties.color;
    }

    public TextColor getColor() {
        return this.color;
    }

    public static class Properties {
        protected TextColor color;

        public Properties color(TextColor color) {
            this.color = color;
            return this;
        }
    }
}
