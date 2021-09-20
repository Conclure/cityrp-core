package me.conclure.cityrp.plugin;

import me.conclure.cityrp.command.CommandInfo;
import me.conclure.cityrp.command.CommandRepository;
import me.conclure.cityrp.command.SimpleCommandRepository;
import me.conclure.cityrp.command.commands.SetpositionCommand;
import me.conclure.cityrp.command.dispatching.AsynchronousCommandDispatcher;
import me.conclure.cityrp.command.dispatching.CommandDispatcher;
import me.conclure.cityrp.command.dispatching.SimpleCommandDispatcher;
import me.conclure.cityrp.listener.*;
import me.conclure.cityrp.gui.ItemRegistryGuiManager;
import me.conclure.cityrp.gui.PositionRegistryGuiManager;
import me.conclure.cityrp.gui.ProfileGuiManager;
import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.item.ItemProperties;
import me.conclure.cityrp.item.Items;
import me.conclure.cityrp.item.MaterialItemLookup;
import me.conclure.cityrp.item.rarity.Rarities;
import me.conclure.cityrp.position.PositionDataManager;
import me.conclure.cityrp.position.Positions;
import me.conclure.cityrp.registry.Key;
import me.conclure.cityrp.registry.Registries;
import me.conclure.cityrp.registry.Registry;
import me.conclure.cityrp.utility.logging.Logger;
import me.conclure.cityrp.utility.MoreFiles;
import me.conclure.cityrp.utility.logging.DelegatedSlf4jLogger;
import me.conclure.cityrp.utility.concurrent.BukkitTaskCoordinator;
import me.conclure.cityrp.utility.concurrent.TaskCoordinator;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.ItemAir;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

@Plugin(
        name = "CityRP",
        version = "1.2"
)
@ApiVersion(ApiVersion.Target.v1_16)
public class CityRPEntryPoint extends JavaPlugin {
    private final Server server;
    private final PluginManager pluginManager;
    private final BukkitScheduler bukkitScheduler;

    private final Path dataPath;
    private final Path positionDestinationDirectory;
    private final Logger logger;

    private final TaskCoordinator<ExecutorService> taskCoordinator;
    private final BukkitTaskCoordinator bukkitTaskCoordinator;

    private final MaterialItemLookup materialItemLookup;
    private final PositionDataManager positionDataManager;

    private final CommandDispatcher commandDispatcher;
    private final AsynchronousCommandDispatcher<ExecutorService> asyncCommandDispatcher;

    private CommandRepository commandRepository;
    private ItemRegistryGuiManager itemRegistryGuiManager;
    private PositionRegistryGuiManager positionRegistryGuiManager;
    private ProfileGuiManager profileGuiManager;

    public CityRPEntryPoint() {
        this.server = this.getServer();
        this.pluginManager = this.server.getPluginManager();
        this.bukkitScheduler = this.server.getScheduler();

        this.dataPath = this.getDataFolder().toPath().toAbsolutePath();
        this.logger = new DelegatedSlf4jLogger(this.getSLF4JLogger());
        Thread.UncaughtExceptionHandler exceptionHandler = (t, e) -> this.logger.error(e);

        ForkJoinPool.ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory = ForkJoinPool.defaultForkJoinWorkerThreadFactory;
        ForkJoinPool forkJoinPool = new ForkJoinPool(32, defaultForkJoinWorkerThreadFactory, exceptionHandler, true);
        this.taskCoordinator = new TaskCoordinator<>(forkJoinPool);
        this.bukkitTaskCoordinator = new BukkitTaskCoordinator(this, this.bukkitScheduler);

        this.materialItemLookup = new MaterialItemLookup();
        this.positionDestinationDirectory = this.dataPath.resolve("positions");
        this.positionDataManager = new PositionDataManager(this.positionDestinationDirectory, this.taskCoordinator);

        this.commandDispatcher = new SimpleCommandDispatcher(this.logger);
        this.asyncCommandDispatcher = new AsynchronousCommandDispatcher<>(forkJoinPool,this.commandDispatcher);
    }

    @Override
    public void onLoad() {
        try {
            MoreFiles.createDirectoriesIfNotExists(this.dataPath);
            MoreFiles.createDirectoryIfNotExists(this.positionDestinationDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerItemToItemRegistries() {
        for (net.minecraft.server.v1_16_R3.Item item : IRegistry.ITEM) {
            if (item instanceof ItemAir) {
                continue;
            }

            String name = item.toString();
            Key key = Key.of(name);
            Material material = Material.matchMaterial(name);
            ItemProperties properties = new ItemProperties().material(material);
            Item value = new Item(properties);

            Registry.RegistryContext<Item> context = Registry.context(key, value);
            Registries.ITEMS.register(context);
            this.materialItemLookup.register(material,key);
        }
        Registries.registerReflectively(Items.class,Registries.ITEMS, result -> {
            if (result.hasOverriddenPrevious()) {
                this.getLogger().info(String.format("(item:%s id:%s) was overridden",result.getKey(),result.getId()));
            }
        });
    }

    private void registerPositionToPositionRegisitries() {
        Registries.registerReflectively(Positions.class,Registries.POSITIONS);
        this.positionDataManager.loadAll().join();
    }

    private void setupRegistries() {
        Registries.registerReflectively(Registries.class,Registries.REGISTRIES);
        Registries.registerReflectively(Rarities.class,Registries.RARITIES);
        this.registerItemToItemRegistries();
        this.registerPositionToPositionRegisitries();
    }

    private void setupCommands() {
        this.commandRepository = new SimpleCommandRepository(this.logger, this.pluginManager,this);
        this.commandRepository.register(new SetpositionCommand(CommandInfo.<Player>newBuilder()
                .name("setposition")
                .aliases("testd")
                .senderType(Player.class)
                .commandDispatcher(this.asyncCommandDispatcher)
                .permission("crp.admin")
                .build()));
    }

    @Override
    public void onEnable() {
        this.setupRegistries();

        this.itemRegistryGuiManager = new ItemRegistryGuiManager();
        this.positionRegistryGuiManager = new PositionRegistryGuiManager();
        this.profileGuiManager = new ProfileGuiManager(this.itemRegistryGuiManager,this.positionRegistryGuiManager);

        this.setupListeners();
        this.setupCommands();
    }

    private Stream<Listener> streamListeners() {
        return Stream.<Listener>builder()
                .add(new ItemOverrideListener(this.materialItemLookup))
                .add(new PositionRegistryGuiListener(this.positionRegistryGuiManager))
                .add(new ItemRegistryGuiListener(this.itemRegistryGuiManager))
                .add(new ProfileGuiListener(this.profileGuiManager))
                .add(new ConnectionListener())
                .add(new GuiListener())
                .build();
    }

    private void registerEventListener(Listener listener) {
        this.pluginManager.registerEvents(listener,this);
    }

    private void setupListeners() {
        this.streamListeners().forEach(this::registerEventListener);
    }
}
