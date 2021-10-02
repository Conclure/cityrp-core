package me.conclure.cityrp.paper.utility;

import me.conclure.cityrp.common.utility.PlayerObtainer;
import me.conclure.cityrp.common.utility.PlayerUUIDHelper;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayerUUIDHelper implements PlayerUUIDHelper {
    private final PlayerObtainer<Player> playerObtainer;

    public BukkitPlayerUUIDHelper(PlayerObtainer<Player> playerObtainer) {
        this.playerObtainer = playerObtainer;
    }

    @Override
    public boolean isOnline(UUID uuid) {
        Player player = this.playerObtainer.getByUniqueId(uuid);
        return player != null && player.isOnline();
    }
}
