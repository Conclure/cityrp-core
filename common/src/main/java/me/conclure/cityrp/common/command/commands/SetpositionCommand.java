package me.conclure.cityrp.common.command.commands;

import me.conclure.cityrp.common.command.CommandInfo;
import me.conclure.cityrp.common.command.abstraction.UniAbstractCommand;
import me.conclure.cityrp.common.sender.PlayerSender;
import net.kyori.adventure.text.Component;

import javax.swing.text.Position;

public class SetpositionCommand<SS> extends UniAbstractCommand<PlayerSender<SS>,SS, Position> {
    public SetpositionCommand(CommandInfo<PlayerSender<SS>,SS> commandInfo) {
        super(commandInfo, null);
    }

    @Override
    protected void execute(PlayerSender<SS> sender, Position argument) throws Exception {
        sender.sendMessage(Component.text("hi"));
    }
}
