package me.conclure.cityrp.common.command.commands;

import me.conclure.cityrp.common.command.CommandInfo;
import me.conclure.cityrp.common.command.abstraction.UniAbstractCommand;
import me.conclure.cityrp.common.sender.PlayerSender;
import net.kyori.adventure.text.Component;

import javax.swing.text.Position;

public class SetpositionCommand extends UniAbstractCommand<PlayerSender, Position> {
    public SetpositionCommand(CommandInfo<PlayerSender> commandInfo) {
        super(commandInfo, null);
    }

    @Override
    protected void execute(PlayerSender sender, Position argument) throws Exception {
        sender.sendMessage(Component.text("hi"));
    }
}
