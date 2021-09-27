package me.conclure.cityrp.paper.item.rarity;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.format.TextColor;

public class Rarity {
    private final TextColor color;

    public Rarity(Properties properties) {
        Preconditions.checkNotNull(properties);
        Preconditions.checkNotNull(properties.color);

        this.color = properties.color;
    }

    public TextColor getColor() {
        return this.color;
    }

    public static class Properties {
        TextColor color;

        public Properties color(TextColor color) {
            this.color = color;
            return this;
        }
    }
}
