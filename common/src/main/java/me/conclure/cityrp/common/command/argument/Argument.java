package me.conclure.cityrp.common.command.argument;

import me.conclure.cityrp.common.sender.Sender;
import net.kyori.adventure.text.Component;

public interface Argument<T, S extends Sender<SS>,SS> {
    ArgumentInfo getInfo();

    ArgumentParseResult<T, S,SS> parse(String argument);

    Component getNameFormatted();
}
