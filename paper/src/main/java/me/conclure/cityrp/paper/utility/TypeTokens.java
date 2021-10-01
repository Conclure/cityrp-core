package me.conclure.cityrp.paper.utility;

import com.google.common.reflect.TypeToken;
import me.conclure.cityrp.common.sender.PlayerSender;
import me.conclure.cityrp.common.sender.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface TypeTokens {
    TypeToken<PlayerSender> PLAYERSENDER = new TypeToken<>() {};
    TypeToken<Sender> SENDER = new TypeToken<>() {};
}
