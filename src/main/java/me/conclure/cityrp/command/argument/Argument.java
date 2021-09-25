package me.conclure.cityrp.command.argument;

import me.conclure.cityrp.languange.Locale;
import me.conclure.cityrp.sender.Sender;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public interface Argument<T, S extends Sender<SS>,SS> {
    ArgumentInfo getInfo();

    ArgumentParseResult<T, S> parse(String argument);

    Component getNameFormatted();
}
