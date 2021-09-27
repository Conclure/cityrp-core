package me.conclure.cityrp.paper.listener;

import me.conclure.cityrp.paper.gui.ProfileGuiManager;
import me.conclure.cityrp.paper.item.Item;
import me.conclure.cityrp.paper.utility.ItemStacks;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

public class ProfileGuiListener implements Listener {
    private final ProfileGuiManager profileGuiManager;

    public ProfileGuiListener(ProfileGuiManager profileGuiManager) {
        this.profileGuiManager = profileGuiManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack.editMeta(meta -> {
            SkullMeta skullMeta = (SkullMeta) meta;
            skullMeta.setPlayerProfile(event.getPlayer().getPlayerProfile());
            meta.displayName(Component.text("Profile", ProfileGuiManager.STYLE));
        });
        itemStack = ItemStacks.copyAndEditNbt(itemStack, tag -> tag.setInt(Item.BYPASS_KEY, 1));

        inventory.setItem(8, itemStack);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        boolean isProfileItem = ItemStacks.testNbt(item, tag -> tag.getInt(Item.BYPASS_KEY) == 1);
        if (!isProfileItem) {
            return;
        }

        event.setCancelled(true);
        Player player = event.getPlayer();
        this.profileGuiManager.open(player);
    }
}
