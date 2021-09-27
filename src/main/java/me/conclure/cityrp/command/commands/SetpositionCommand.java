package me.conclure.cityrp.command.commands;

import me.conclure.cityrp.command.CommandInfo;
import me.conclure.cityrp.command.abstraction.UniAbstractCommand;
import me.conclure.cityrp.position.Position;
import me.conclure.cityrp.sender.PlayerSender;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class SetpositionCommand<SS> extends UniAbstractCommand<PlayerSender<SS>,SS, Position> {
    public SetpositionCommand(CommandInfo<PlayerSender<SS>,SS> commandInfo) {
        super(commandInfo, null);
    }

    @Override
    protected void execute(PlayerSender<SS> sender, Position argument) throws Exception {
        sender.sendMessage(Component.text("hi"));
    }
}
