package me.conclure.cityrp.utility;

import com.google.common.reflect.TypeToken;
import me.conclure.cityrp.sender.PlayerSender;
import org.bukkit.command.CommandSender;

public interface TypeTokens {
    TypeToken<PlayerSender<CommandSender>> PLAYER_SENDER_COMMAND_SENDER = new TypeToken<>() {};
}
