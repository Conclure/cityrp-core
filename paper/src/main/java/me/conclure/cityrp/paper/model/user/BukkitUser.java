package me.conclure.cityrp.paper.model.user;

import me.conclure.cityrp.common.model.user.AbstractUser;
import me.conclure.cityrp.common.sender.PlayerSender;
import me.conclure.cityrp.common.sender.SenderTransformer;
import me.conclure.cityrp.common.utility.PlayerObtainer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitUser extends AbstractUser {
    private final SenderTransformer<Player, PlayerSender> senderTransformer;
    private final PlayerObtainer<Player> playerObtainer;

    public BukkitUser(UUID uniqueId, SenderTransformer<Player, PlayerSender> senderTransformer, PlayerObtainer<Player> playerObtainer) {
        super(uniqueId);
        this.senderTransformer = senderTransformer;
        this.playerObtainer = playerObtainer;
    }

    @Override
    protected PlayerSender delegate() {
        Player player = this.playerObtainer.getByUniqueId(this.getUniqueId());
        return this.senderTransformer.tranform(player);
    }
}
