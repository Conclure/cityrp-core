package me.conclure.cityrp.common.utility;

import net.kyori.adventure.text.Component;

public interface InventoryFactory<PlatformInventory, PlatformInventoryHolder> {
    PlatformInventory create(PlatformInventoryHolder holder, int rows, Component title);
}
