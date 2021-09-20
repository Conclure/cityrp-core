package me.conclure.cityrp.listener;

import com.google.common.collect.Sets;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Set;

public class ConnectionListener implements Listener {
    private final Set<Player> playersMenuModeSet = Sets.newIdentityHashSet();

    public ConnectionListener() {
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!this.playersMenuModeSet.contains(event.getPlayer())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //this.playersMenuModeSet.add(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.playersMenuModeSet.remove(event.getPlayer());
    }
}
