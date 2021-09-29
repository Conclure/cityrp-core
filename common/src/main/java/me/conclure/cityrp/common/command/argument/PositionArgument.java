package me.conclure.cityrp.common.command.argument;


import me.conclure.cityrp.common.model.position.Position;
import me.conclure.cityrp.common.sender.Sender;


public class PositionArgument<S extends Sender<PlatformSender>, PlatformSender,PlatformEntity,PlatformWorld>
        extends AbstractArgument<Position<PlatformEntity,PlatformWorld>, S, PlatformSender,ArgumentInfo> {
    public PositionArgument(ArgumentInfo info) {
        super(info);
    }

    @Override
    public ArgumentParseResult<Position<PlatformEntity,PlatformWorld>, S, PlatformSender> parse(String argument) {
        return null;
    }
}
