package me.conclure.cityrp.paper.listener;

import me.conclure.cityrp.common.position.Position;
import me.conclure.cityrp.common.position.PositionRegistry;
import me.conclure.cityrp.common.position.Positions;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ConnectionListener implements Listener {
    private final Set<UUID> playersMenuModeSet = new HashSet<>();
    private final PositionRegistry<Entity, World> positionRegistry;

    public ConnectionListener(PositionRegistry<Entity, World> positionRegistry) {
        this.positionRegistry = positionRegistry;
    }

    private <E extends Event & Cancellable> void cancelEventIfContained(E event, Player player) {
        if (this.playersMenuModeSet.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    private <E extends PlayerEvent & Cancellable> void cancelPlayerEventIfContained(E event) {
        this.cancelEventIfContained(event,event.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        this.cancelPlayerEventIfContained(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        this.cancelEventIfContained(event,event.getPlayer());
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        this.playersMenuModeSet.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Position<Entity, World> spawn = this.positionRegistry.getByKey(Positions.SPAWN);
        spawn.teleportAsync(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.playersMenuModeSet.remove(event.getPlayer().getUniqueId());
    }
}
