package me.conclure.cityrp.command.commands;

import me.conclure.cityrp.command.AbstractCommand;
import me.conclure.cityrp.command.CommandInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class SetpositionCommand extends AbstractCommand<Player> {
    public SetpositionCommand(CommandInfo<Player> commandInfo) {
        super(commandInfo);
    }

    @Override
    protected void execute(Player sender, String[] arguments) throws Exception {
        sender.sendMessage(Component.text("hi"));
    }
}
