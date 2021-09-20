package me.conclure.cityrp.gui.framework;

import me.conclure.cityrp.utility.function.Functions;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GuiCell {
    private final int slot;
    private final Consumer<? super InventoryClickEvent> action;
    private final Supplier<? extends ItemStack> itemStackFactory;

    GuiCell(int slot, Consumer<? super InventoryClickEvent> action, Supplier<? extends ItemStack> itemStackFactory) {
        this.slot = slot;
        this.action = action;
        this.itemStackFactory = itemStackFactory;
    }

    public int getSlot() {
        return this.slot;
    }

    public void onClickEvent(InventoryClickEvent event) {
        this.action.accept(event);
    }

    public ItemStack newItemStack() {
        return this.itemStackFactory.get();
    }

    public static class Builder {
        Consumer<? super InventoryClickEvent> clickAction = Functions.emptyConsumer();
        Supplier<? extends ItemStack> itemStackFactory = Functions.nullSupplier();

        public Builder clickAction(Consumer<? super InventoryClickEvent> clickAction) {
            this.clickAction = clickAction;
            return this;
        }

        public Builder itemStackFactory(Supplier<? extends ItemStack> itemStackFactory) {
            this.itemStackFactory = itemStackFactory;
            return this;
        }

        GuiCell build(int slot) {
            return new GuiCell(slot,this.clickAction,this.itemStackFactory);
        }
    }
}
