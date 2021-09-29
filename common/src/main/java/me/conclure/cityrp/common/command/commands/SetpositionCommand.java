package me.conclure.cityrp.common.command.commands;

import me.conclure.cityrp.common.command.CommandInfo;
import me.conclure.cityrp.common.command.abstraction.UniAbstractCommand;
import me.conclure.cityrp.common.sender.PlayerSender;
import net.kyori.adventure.text.Component;

import javax.swing.text.Position;

public class SetpositionCommand<PlatformSender> extends UniAbstractCommand<PlayerSender<PlatformSender>, PlatformSender, Position> {
    public SetpositionCommand(CommandInfo<PlayerSender<PlatformSender>, PlatformSender> commandInfo) {
        super(commandInfo, null);
    }

    @Override
    protected void execute(PlayerSender<PlatformSender> sender, Position argument) throws Exception {
        sender.sendMessage(Component.text("hi"));
    }
}
