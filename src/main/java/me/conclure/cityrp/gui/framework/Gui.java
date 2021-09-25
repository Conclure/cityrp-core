package me.conclure.cityrp.gui.framework;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class Gui implements OpenableInventoryHolder {
    private final GuiFactory factory;
    private final Inventory inventory;

    Gui(GuiFactory factory, Component title) {

        this.factory = factory;
        this.inventory = Bukkit.createInventory(this, factory.getSize(), title);

        this.render();
    }

    public final void render() {
        for (GuiCell guiCell : this.factory) {
            if (guiCell == null) {
                continue;
            }

            this.inventory.setItem(guiCell.getSlot(), guiCell.newItemStack());
        }
    }

    public final GuiFactory getDerivedFactory() {
        return this.factory;
    }

    @Override
    public final @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
