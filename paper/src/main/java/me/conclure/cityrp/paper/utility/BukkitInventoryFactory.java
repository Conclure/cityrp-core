package me.conclure.cityrp.paper.utility;

import me.conclure.cityrp.common.utility.InventoryFactory;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BukkitInventoryFactory implements InventoryFactory<Inventory, InventoryHolder> {
    private final Server server;

    public BukkitInventoryFactory(Server server) {
        this.server = server;
    }

    @Override
    public Inventory create(InventoryHolder holder, int rows, Component title) {
        return this.server.createInventory(holder,rows*9,title);
    }
}
