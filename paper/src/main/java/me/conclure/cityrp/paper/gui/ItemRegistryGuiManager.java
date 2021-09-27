package me.conclure.cityrp.paper.gui;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Preconditions;
import me.conclure.cityrp.paper.gui.framework.OpenableInventoryHolder;
import me.conclure.cityrp.paper.item.Item;
import me.conclure.cityrp.registry.Registries;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class ItemRegistryGuiManager {
    private static final int ITEMS_PER_PAGE = 45;

    private final LoadingCache<Integer, GuiPage> cache;
    private final int maxPages;

    public ItemRegistryGuiManager() {
        int pages = Registries.ITEMS.size() / ITEMS_PER_PAGE;
        boolean needsExtraPage = Registries.ITEMS.size() % ITEMS_PER_PAGE > 0;
        this.maxPages = needsExtraPage ? pages + 1 : pages;
        this.cache = Caffeine.newBuilder()
                .softValues()
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .build(key -> new GuiPage(key, this.maxPages));
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

    public boolean openFirstPage(HumanEntity entity) {
        if (this.maxPages < 1) {
            return false;
        }
        this.openPage(1, entity);
        return true;
    }

    public boolean openLastPage(HumanEntity entity) {
        if (this.maxPages < 1) {
            return false;
        }
        this.openPage(this.maxPages, entity);
        return true;
    }

    public static class GuiPage implements OpenableInventoryHolder {
        public static final int NEXT_SLOT = 53;
        public static final int PREVIOUS_SLOT = 45;

        final Inventory inventory;
        final Item[] itemList;
        final int page;

        GuiPage(int page, int maxPages) {
            this.page = page;
            this.inventory = Bukkit.createInventory(this, 6 * 9, Component.text("Item Registry (" + page + "/" + maxPages + ")"));
            this.itemList = new Item[ITEMS_PER_PAGE];
            for (int i = (page - 1) * ITEMS_PER_PAGE; i < page * ITEMS_PER_PAGE; i++) {
                int slot = i - (page - 1) * ITEMS_PER_PAGE;
                Item item = Registries.ITEMS.getById(i);
                if (item == null) {
                    break;
                }
                this.inventory.setItem(slot, item.newStack());
                this.itemList[slot] = item;
            }
        }

        public int getPage() {
            return this.page;
        }

        public Item getItem(int slot) {
            return this.itemList[slot];
        }

        public int getItemAmount() {
            return this.itemList.length;
        }

        @Override
        public @NotNull Inventory getInventory() {
            return this.inventory;
        }
    }

}
