package me.conclure.cityrp.common.command.argument;

import me.conclure.cityrp.common.sender.Sender;
import net.kyori.adventure.text.Component;

public interface Argument<T, S extends Sender> {
    ArgumentInfo getInfo();

    ArgumentParseResult<T, S> parse(String argument);

    Component getNameFormatted();
}
