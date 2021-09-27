package me.conclure.cityrp.paper.utility;

import me.conclure.cityrp.common.utility.Unconstructable;
import org.bukkit.ChatColor;

public final class ColorUtil extends Unconstructable {
    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
