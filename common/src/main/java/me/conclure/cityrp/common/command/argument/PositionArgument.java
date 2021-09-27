package me.conclure.cityrp.common.command.argument;


import me.conclure.cityrp.common.position.Position;
import me.conclure.cityrp.common.sender.Sender;


public class PositionArgument<S extends Sender<SS>,SS> extends AbstractArgument<Position, S,SS,ArgumentInfo> {
    public PositionArgument(ArgumentInfo info) {
        super(info);
    }

    @Override
    public ArgumentParseResult<Position, S,SS> parse(String argument) {
        return null;
    }
}
