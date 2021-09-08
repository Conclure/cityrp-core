package me.conclure.cityrp;

import org.bukkit.ChatColor;

public class ColorUtil {
    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&',str);
    }
}
