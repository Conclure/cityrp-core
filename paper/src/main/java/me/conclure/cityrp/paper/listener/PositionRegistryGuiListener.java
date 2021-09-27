package me.conclure.cityrp.paper.listener;

import me.conclure.cityrp.common.position.Position;
import me.conclure.cityrp.paper.gui.PositionRegistryGuiManager;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.concurrent.TimeUnit;

public class PositionRegistryGuiListener implements Listener {
    private final PositionRegistryGuiManager registryGui;

    public PositionRegistryGuiListener(PositionRegistryGuiManager registryGui) {
        this.registryGui = registryGui;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) {
            return;
        }
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof PositionRegistryGuiManager.GuiPage)) {
            return;
        }
        PositionRegistryGuiManager.GuiPage guiPage = (PositionRegistryGuiManager.GuiPage) holder;
        event.setCancelled(true);
        int slot = event.getSlot();
        HumanEntity whoClicked = event.getWhoClicked();
        if (slot == PositionRegistryGuiManager.GuiPage.PREVIOUS_SLOT) {
            this.registryGui.openPage(guiPage.getPage() - 1, whoClicked);
            return;
        }
        if (slot == PositionRegistryGuiManager.GuiPage.NEXT_SLOT) {
            this.registryGui.openPage(guiPage.getPage() + 1, whoClicked);
            return;
        }
        if (slot >= guiPage.getPositionAmount()) {
            return;
        }
        Position<Entity, World> position = guiPage.getPosition(slot);
        if (position == null) {
            return;
        }

        if (!position.hasLocation()) {
            return;
        }
        position.teleportAsync(whoClicked)
                .orTimeout(10, TimeUnit.SECONDS)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        whoClicked.sendMessage(Component.text("Failed teleporting you: " + exception.getMessage()));
                        return;
                    }
                    if (result == Boolean.FALSE) {
                        whoClicked.sendMessage(Component.text("Failed teleporting you!"));
                        return;
                    }
                    whoClicked.sendMessage(Component.text("Successfully teleport you!"));
                });
    }
}
