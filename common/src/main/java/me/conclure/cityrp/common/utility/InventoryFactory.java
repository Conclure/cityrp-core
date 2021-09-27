package me.conclure.cityrp.common.utility;

import net.kyori.adventure.text.Component;

public interface InventoryFactory<I,H> {
    I create(H holder, int rows, Component title);
}
