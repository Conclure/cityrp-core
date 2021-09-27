package me.conclure.cityrp.command.argument;

import me.conclure.cityrp.position.Position;
import me.conclure.cityrp.sender.Sender;
import net.kyori.adventure.text.Component;

public class PositionArgument<S extends Sender<SS>,SS> extends AbstractArgument<Position, S,SS,ArgumentInfo> {
    public PositionArgument(ArgumentInfo info) {
        super(info);
    }

    @Override
    public ArgumentParseResult<Position, S,SS> parse(String argument) {
        return null;
    }
}
