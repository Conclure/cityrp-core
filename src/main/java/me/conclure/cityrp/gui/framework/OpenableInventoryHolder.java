package me.conclure.cityrp.gui.framework;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

public interface OpenableInventoryHolder extends InventoryHolder {

    default InventoryView open(HumanEntity entity) {
        return entity.openInventory(this.getInventory());
    }
}
