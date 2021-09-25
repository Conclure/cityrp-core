package me.conclure.cityrp.gui;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Preconditions;
import me.conclure.cityrp.gui.framework.Gui;
import me.conclure.cityrp.gui.framework.GuiFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class ProfileGuiManager {
    public static final Style STYLE = Style.style()
            .color(NamedTextColor.WHITE)
            .decoration(TextDecoration.BOLD, true)
            .decoration(TextDecoration.ITALIC, false)
            .build();

    private final GuiFactory guiFactory;
    private final LoadingCache<Player, GuiProfile> cache;

    public ProfileGuiManager(
            ItemRegistryGuiManager itemRegistryGuiManager,
            PositionRegistryGuiManager positionRegistryGuiManager
    ) {
        this.guiFactory = GuiFactory.newBuilder(6)
                .cell(0, builder -> builder
                        .itemStackFactory(() -> new ItemStack(Material.BOOK))
                        .clickAction(event -> {
                            event.getWhoClicked().closeInventory();
                            itemRegistryGuiManager.openFirstPage(event.getWhoClicked());
                        })
                )
                .cell(1, builder -> builder
                        .itemStackFactory(() -> new ItemStack(Material.BARRIER))
                        .clickAction(event -> {
                            event.getWhoClicked().closeInventory();
                            positionRegistryGuiManager.openFirstPage(event.getWhoClicked());
                        })
                )
                .build();

        this.cache = Caffeine.newBuilder()
                .softValues()
                .weakKeys()
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .build(player -> new GuiProfile(this.guiFactory, player));
    }

    public void open(Player player) {
        GuiProfile profile = this.cache.get(player);
        Preconditions.checkNotNull(profile);
        profile.gui.open(player);
    }

    static class GuiProfile {
        final Player player;
        final Gui gui;

        GuiProfile(GuiFactory factory, Player player) {
            this.player = player;
            this.gui = factory.newGui(Component.text("Profile Menu " + player.getName()));
        }
    }

}
