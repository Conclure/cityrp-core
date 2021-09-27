package me.conclure.cityrp.listener;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.item.repository.MaterialItemLookup;
import me.conclure.cityrp.utility.ItemStacks;
import me.conclure.cityrp.utility.Key;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemOverrideListener implements Listener {
    private final MaterialItemLookup materialItemLookup;

    public ItemOverrideListener(MaterialItemLookup materialItemLookup) {
        this.materialItemLookup = materialItemLookup;
    }

    private me.conclure.cityrp.item.Item processItem(
            ItemStack itemStack,
            Consumer<? super ItemStack> ifTagAbsent,
            Runnable ifNotRegistered
    ) {
        Material material = itemStack.getType();

        if (material.isAir()) {
            return null;
        }

        NBTTagCompound tag = ItemStacks.getNbtTag(itemStack);

        String bypassKey = me.conclure.cityrp.item.Item.BYPASS_KEY;
        if (tag.hasKey(bypassKey)) {
            return null;
        }

        String uniqueKey = me.conclure.cityrp.item.Item.UNIQUE_KEY;

        if (!tag.hasKey(uniqueKey)) {
            ifTagAbsent.accept(this.materialItemLookup.lookup(material).newStack(itemStack.getAmount()));
            return null;
        }

        Key key = Key.of(tag.getString(uniqueKey));

        if (!Registries.ITEMS.isRegistered(key)) {
            ifNotRegistered.run();
            return null;
        }

        me.conclure.cityrp.item.Item item = Registries.ITEMS.getByKey(key);
        Preconditions.checkNotNull(item);
        return item;
    }

    private void handleItemDrop(Item itemDrop) {
        ItemStack itemStack = itemDrop.getItemStack();
        me.conclure.cityrp.item.Item item = this.processItem(itemStack, itemDrop::setItemStack, itemDrop::remove);

        if (item == null) {
            return;
        }

        itemDrop.setItemStack(item.newStack(itemStack.getAmount()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrop(PlayerDropItemEvent event) {
        this.handleItemDrop(event.getItemDrop());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPickup(EntityPickupItemEvent event) {
        this.handleItemDrop(event.getItem());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreative(InventoryCreativeEvent event) {
        ItemStack itemStack = event.getCursor();
        me.conclure.cityrp.item.Item item = this.processItem(itemStack, event::setCursor, () -> event.setCursor(ItemStacks.AIR_STACK));

        if (item == null) {
            return;
        }

        event.setCursor(item.newStack(itemStack.getAmount()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(InventoryMoveItemEvent event) {
        ItemStack itemStack = event.getItem();
        me.conclure.cityrp.item.Item item = this.processItem(itemStack, event::setItem, () -> event.setItem(ItemStacks.AIR_STACK));

        if (item == null) {
            return;
        }

        event.setItem(item.newStack(itemStack.getAmount()));
    }
}
