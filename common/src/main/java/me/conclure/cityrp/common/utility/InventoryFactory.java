package me.conclure.cityrp.common.utility;

import net.kyori.adventure.text.Component;

public interface InventoryFactory<I,H> {
    I createInventory(H holder, int rows, Component title);
}
