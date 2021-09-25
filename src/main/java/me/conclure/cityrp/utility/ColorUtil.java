package me.conclure.cityrp.utility;

import org.bukkit.ChatColor;

public final class ColorUtil extends Unconstructable {
    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
