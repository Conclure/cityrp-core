package me.conclure.cityrp.listener;

import me.conclure.cityrp.gui.framework.Gui;
import me.conclure.cityrp.gui.framework.GuiCell;
import me.conclure.cityrp.gui.framework.GuiFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

public class GuiListener implements Listener {

    private static void acceptInventoryIfIsGui(Inventory inventory, Consumer<? super Gui> consumer) {
        if (inventory == null) {
            return;
        }

        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof Gui)) {
            return;
        }

        consumer.accept((Gui) holder);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(InventoryClickEvent event) {
        acceptInventoryIfIsGui(event.getClickedInventory(), gui -> {
            int slot = event.getSlot();
            GuiFactory factory = gui.getDerivedFactory();
            GuiCell guiCell = factory.getCell(slot);
            if (guiCell != null) {
                guiCell.onClickEvent(event);
            }

            event.setCancelled(true);
        });
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onOpen(InventoryOpenEvent event) {
        acceptInventoryIfIsGui(event.getInventory(), gui -> gui.getDerivedFactory().onOpen(event));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onClose(InventoryCloseEvent event) {
        acceptInventoryIfIsGui(event.getInventory(), gui -> gui.getDerivedFactory().onClose(event));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDrag(InventoryDragEvent event) {
        acceptInventoryIfIsGui(event.getInventory(), gui -> gui.getDerivedFactory().onDrag(event));
    }
}
