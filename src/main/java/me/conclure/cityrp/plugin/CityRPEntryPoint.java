package me.conclure.cityrp.plugin;

import me.conclure.cityrp.gui.ItemRegistryGui;
import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.item.ItemProperties;
import me.conclure.cityrp.item.Items;
import me.conclure.cityrp.item.rarity.Rarities;
import me.conclure.cityrp.registry.Key;
import me.conclure.cityrp.registry.Registries;
import me.conclure.cityrp.registry.Registry;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.ItemAir;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import java.util.concurrent.CountDownLatch;

@Plugin(
        name = "CityRP",
        version = "1.2"
)
@ApiVersion(ApiVersion.Target.v1_16)
public class CityRPEntryPoint extends JavaPlugin {
    private final CountDownLatch countDownLatch;

    public CityRPEntryPoint() {
        this.countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        Registries.registerReflectively(Registries.class,Registries.REGISTRIES);
        Registries.registerReflectively(Rarities.class,Registries.RARITIES);
        this.registerItems();
        PluginManager pluginManager = this.getServer().getPluginManager();
        EventListenerRegistrator eventListenerRegistrator = new EventListenerRegistrator(pluginManager, this);
        new ItemRegistryGui(eventListenerRegistrator);
    }

    private void registerItems() {
        for (net.minecraft.server.v1_16_R3.Item item : IRegistry.ITEM) {
            if (item instanceof ItemAir) {
                continue;
            }

            String name = item.toString();
            Key key = Key.of(name);
            ItemProperties properties = new ItemProperties().material(Material.matchMaterial(name));
            this.getLogger().info(key.toString());
            Registry.RegistryContext<Item> context = Registry.context(key, new Item(properties));
            Registries.ITEMS.register(context);
        }
        Registries.registerReflectively(Items.class,Registries.ITEMS, result -> {
            if (result.hasOverriddenPrevious()) {
                this.getLogger().info(String.format("(item:%s id:%s) was overridden",result.getKey(),result.getId()));
            }
        });
    }
}
