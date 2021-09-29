package me.conclure.cityrp.paper.utility;

import com.google.common.reflect.TypeToken;
import me.conclure.cityrp.common.sender.PlayerSender;
import me.conclure.cityrp.common.sender.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface TypeTokens {
    TypeToken<PlayerSender<CommandSender>> PLAYERSENDER_COMMANDSENDER = new TypeToken<>() {};
    TypeToken<PlayerSender<? extends Player>> PLAYERSENDER_PLAYER = new TypeToken<>() {};
    TypeToken<Sender<? extends CommandSender>> SENDER_PLAYER = new TypeToken<>() {};
}
