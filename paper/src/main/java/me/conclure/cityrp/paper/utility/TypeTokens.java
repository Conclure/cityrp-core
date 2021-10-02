package me.conclure.cityrp.paper.utility;

import com.google.common.reflect.TypeToken;
import me.conclure.cityrp.common.sender.PlayerSender;
import me.conclure.cityrp.common.sender.Sender;

public interface TypeTokens {
    TypeToken<PlayerSender> PLAYERSENDER = new TypeToken<>() {
    };
    TypeToken<Sender> SENDER = new TypeToken<>() {
    };
}
