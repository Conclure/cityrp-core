package me.conclure.cityrp.gui;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Preconditions;
import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.item.ItemCreationOptions;
import me.conclure.cityrp.plugin.EventListenerRegistrator;
import me.conclure.cityrp.registry.Registries;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ItemRegistryGui {
    private static final int ITEMS_PER_PAGE = 45;

    private final LoadingCache<Integer, GuiPage> cache;
    private final int maxPages;

    public ItemRegistryGui(EventListenerRegistrator eventListenerRegistrator) {
        int pages = Registries.ITEMS.size() / ITEMS_PER_PAGE;
        boolean needsExtraPage = Registries.ITEMS.size() % ITEMS_PER_PAGE > 0;
        this.maxPages = needsExtraPage ? pages + 1 : pages;
        this.cache = Caffeine.newBuilder()
                .softValues()
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .build(key -> new GuiPage(key,this.maxPages));

        eventListenerRegistrator.register(new EventListener(this));
    }

    public boolean openPage(int page, HumanEntity entity) {
        if (page > this.maxPages || page < 1) {
            return false;
        }
        GuiPage guiPage = this.cache.get(page);
        Preconditions.checkNotNull(guiPage);
        guiPage.open(entity);
        return true;
    }

    static class GuiPage implements InventoryHolder {
        static final int NEXT_SLOT = 53;
        static final int PREVIOUS_SLOT = 45;

        final Inventory inventory;
        final Item[] itemList;
        final int page;

        GuiPage(int page, int maxPages) {
            this.page = page;
            this.inventory = Bukkit.createInventory(this,6*9, Component.text("Item Registry ("+page+"/"+maxPages+")"));
            this.itemList = new Item[ITEMS_PER_PAGE];
            for (int i = (page-1)*ITEMS_PER_PAGE; i < page*ITEMS_PER_PAGE; i++) {
                int slot = i - (page-1)*ITEMS_PER_PAGE;
                Item item = Registries.ITEMS.getById(i);
                if (item == null) {
                    break;
                }
                this.inventory.setItem(slot,item.newStack());
                this.itemList[slot] = item;
            }
        }

        @Override
        public @NotNull Inventory getInventory() {
            return this.inventory;
        }

        public void open(HumanEntity entity) {
            entity.openInventory(this.inventory);
        }
    }

    static class EventListener implements Listener {
        static final ItemFlag[] ITEM_FLAGS = ItemFlag.values();
        final ItemRegistryGui registryGui;

        EventListener(ItemRegistryGui registryGui) {
            this.registryGui = registryGui;
        }

        @EventHandler public void onClick(InventoryClickEvent event) {
            Inventory inventory = event.getClickedInventory();
            if (inventory == null) {
                return;
            }
            InventoryHolder holder = inventory.getHolder();
            if (!(holder instanceof GuiPage)) {
                return;
            }
            GuiPage guiPage = (GuiPage) holder;
            event.setCancelled(true);
            int slot = event.getSlot();
            HumanEntity whoClicked = event.getWhoClicked();
            if (slot == GuiPage.PREVIOUS_SLOT) {
                this.registryGui.openPage(guiPage.page-1,whoClicked);
                return;
            }
            if (slot == GuiPage.NEXT_SLOT) {
                this.registryGui.openPage(guiPage.page+1,whoClicked);
                return;
            }
            Item[] itemList = guiPage.itemList;
            if (slot >= itemList.length) {
                return;
            }
            Item item = itemList[slot];
            if (item == null) {
                return;
            }
            ItemCreationOptions options = new ItemCreationOptions()
                    .postMetaEdit((item0,meta) -> meta.addItemFlags(ITEM_FLAGS));
            whoClicked.getInventory().addItem(item.newStack(options));
        }

        @EventHandler public void onJump(PlayerJumpEvent event) {
            Player player = event.getPlayer();
            this.registryGui.openPage(1,player);
        }
    }
}
