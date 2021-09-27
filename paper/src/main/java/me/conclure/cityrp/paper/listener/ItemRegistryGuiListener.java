package me.conclure.cityrp.paper.listener;

import me.conclure.cityrp.paper.gui.ItemRegistryGuiManager;
import me.conclure.cityrp.paper.item.Item;
import me.conclure.cityrp.paper.item.ItemCreationOptions;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;

public class ItemRegistryGuiListener implements Listener {
    public static final ItemFlag[] ITEM_FLAGS = ItemFlag.values();
    private final ItemRegistryGuiManager registryGui;

    public ItemRegistryGuiListener(ItemRegistryGuiManager registryGui) {
        this.registryGui = registryGui;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) {
            return;
        }
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ItemRegistryGuiManager.GuiPage)) {
            return;
        }
        ItemRegistryGuiManager.GuiPage guiPage = (ItemRegistryGuiManager.GuiPage) holder;
        event.setCancelled(true);
        int slot = event.getSlot();
        HumanEntity whoClicked = event.getWhoClicked();
        if (slot == ItemRegistryGuiManager.GuiPage.PREVIOUS_SLOT) {
            this.registryGui.openPage(guiPage.getPage() - 1, whoClicked);
            return;
        }
        if (slot == ItemRegistryGuiManager.GuiPage.NEXT_SLOT) {
            this.registryGui.openPage(guiPage.getPage() + 1, whoClicked);
            return;
        }
        if (slot >= guiPage.getItemAmount()) {
            return;
        }
        Item item = guiPage.getItem(slot);
        if (item == null) {
            return;
        }
        ItemCreationOptions options = new ItemCreationOptions()
                .postMetaEdit((item0, meta) -> meta.addItemFlags(ITEM_FLAGS));
        whoClicked.getInventory().addItem(item.newStack(options));
    }
}
