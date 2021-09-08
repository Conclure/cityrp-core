package me.conclure.cityrp;

import me.conclure.cityrp.rarity.Rarities;
import me.conclure.cityrp.registry.Registries;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CountDownLatch;

public class CityRPEntryPoint extends JavaPlugin {
    private final CountDownLatch countDownLatch;

    public CityRPEntryPoint() {
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        Registries.registerReflectively(Registries.class,Registries.TOP_REGISTRY);
        Registries.registerReflectively(Rarities.class,Registries.RARITY_REGISTRY);
    }
}
