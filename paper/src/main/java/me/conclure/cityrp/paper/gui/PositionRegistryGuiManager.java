package me.conclure.cityrp.paper.gui;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Preconditions;
import me.conclure.cityrp.common.position.Position;
import me.conclure.cityrp.common.position.PositionRegistry;
import me.conclure.cityrp.common.utility.InventoryFactory;
import me.conclure.cityrp.paper.gui.framework.OpenableInventoryHolder;
import me.conclure.cityrp.paper.item.Items;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class PositionRegistryGuiManager {
    private static final int ITEMS_PER_PAGE = 45;

    private final LoadingCache<Integer, GuiPage> cache;
    private final int maxPages;
    private final PositionRegistry<Entity, World> positionRegistry;
    private final InventoryFactory<Inventory, InventoryHolder> inventoryFactory;

    public PositionRegistryGuiManager(
            PositionRegistry<Entity, World> positionRegistry,
            InventoryFactory<Inventory, InventoryHolder> inventoryFactory
    ) {
        this.positionRegistry = positionRegistry;
        this.inventoryFactory = inventoryFactory;
        int pages = positionRegistry.size() / ITEMS_PER_PAGE;
        boolean needsExtraPage = positionRegistry.size() % ITEMS_PER_PAGE > 0;
        this.maxPages = needsExtraPage ? pages + 1 : pages;
        this.cache = Caffeine.newBuilder()
                .softValues()
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .build(GuiPage::new);
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

    public class GuiPage implements OpenableInventoryHolder {
        public static final int NEXT_SLOT = 53;
        public static final int PREVIOUS_SLOT = 45;

        final Inventory inventory;
        final Position<Entity, World>[] itemList;
        final int page;

        GuiPage(int page) {
            this.page = page;
            InventoryFactory<Inventory, InventoryHolder> inventoryFactory = PositionRegistryGuiManager.this.inventoryFactory;
            int maxPages = PositionRegistryGuiManager.this.maxPages;
            PositionRegistry<Entity, World> positionRegistry = PositionRegistryGuiManager.this.positionRegistry;

            String content = "Position Registry (" + page + "/" + maxPages + ")";
            this.inventory = inventoryFactory.create(this, 6, Component.text(content));
            this.itemList = new Position[ITEMS_PER_PAGE];

            for (int i = (page - 1) * ITEMS_PER_PAGE; i < page * ITEMS_PER_PAGE; i++) {
                int slot = i - (page - 1) * ITEMS_PER_PAGE;
                Position<Entity, World> item = positionRegistry.getById(i);
                if (item == null) {
                    break;
                }
                this.inventory.setItem(slot, Items.BEDROCK.newStack());
                this.itemList[slot] = item;
            }
        }

        public int getPage() {
            return this.page;
        }

        public Position<Entity, World> getPosition(int slot) {
            return this.itemList[slot];
        }

        public int getPositionAmount() {
            return this.itemList.length;
        }

        @Override
        public @NotNull Inventory getInventory() {
            return this.inventory;
        }
    }
}
