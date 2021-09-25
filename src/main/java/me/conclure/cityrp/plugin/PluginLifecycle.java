package me.conclure.cityrp.plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.conclure.cityrp.command.CommandInfo;
import me.conclure.cityrp.command.commands.SetpositionCommand;
import me.conclure.cityrp.command.dispatching.AsynchronousCommandDispatcher;
import me.conclure.cityrp.command.dispatching.CommandDispatcher;
import me.conclure.cityrp.command.dispatching.SimpleCommandDispatcher;
import me.conclure.cityrp.command.repository.CommandRepository;
import me.conclure.cityrp.command.repository.BukkitCommandRepository;
import me.conclure.cityrp.data.paths.PathRegistry;
import me.conclure.cityrp.data.paths.Paths;
import me.conclure.cityrp.data.paths.SimplePathRegistry;
import me.conclure.cityrp.gui.ItemRegistryGuiManager;
import me.conclure.cityrp.gui.PositionRegistryGuiManager;
import me.conclure.cityrp.gui.ProfileGuiManager;
import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.item.repository.ItemRepository;
import me.conclure.cityrp.item.repository.MaterialItemLookup;
import me.conclure.cityrp.item.repository.SimpleItemRepository;
import me.conclure.cityrp.item.repository.SimpleMaterialItemLookup;
import me.conclure.cityrp.listener.ConnectionListener;
import me.conclure.cityrp.listener.GuiListener;
import me.conclure.cityrp.listener.ItemOverrideListener;
import me.conclure.cityrp.listener.ItemRegistryGuiListener;
import me.conclure.cityrp.listener.PositionRegistryGuiListener;
import me.conclure.cityrp.listener.ProfileGuiListener;
import me.conclure.cityrp.sender.PlayerSender;
import me.conclure.cityrp.sender.Sender;
import me.conclure.cityrp.sender.SenderManager;
import me.conclure.cityrp.sender.impl.BukkitSenderManager;
import me.conclure.cityrp.sender.impl.BukkitSenderMappingRegistry;
import me.conclure.cityrp.utility.Key;
import me.conclure.cityrp.utility.MoreFiles;
import me.conclure.cityrp.utility.concurrent.BukkitTaskCoordinator;
import me.conclure.cityrp.utility.concurrent.TaskCoordinator;
import me.conclure.cityrp.utility.logging.DelegatedSlf4jLogger;
import me.conclure.cityrp.utility.logging.Logger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class PluginLifecycle implements Lifecycle {
    private final Plugin plugin;
    private final Server server;
    private final PluginManager pluginManager;
    private final ScoreboardManager scoreboardManager;
    private final ServicesManager servicesManager;
    private final BukkitScheduler bukkitScheduler;

    private final PathRegistry pathRegistry;
    private final Logger logger;

    private final TaskCoordinator<ExecutorService> taskCoordinator;
    private final BukkitTaskCoordinator bukkitTaskCoordinator;

    private final ItemRepository itemRepository;

    private final SenderManager<CommandSender> senderManager;
    private final CommandDispatcher<CommandSender> commandDispatcher;
    private final CommandDispatcher<CommandSender> asyncCommandDispatcher;
    private final CommandRepository<CommandSender> commandRepository;

    private ItemRegistryGuiManager itemRegistryGuiManager;
    private PositionRegistryGuiManager positionRegistryGuiManager;
    private ProfileGuiManager profileGuiManager;

    public PluginLifecycle(
            Plugin plugin,
            Server server,
            PluginManager pluginManager,
            ScoreboardManager scoreboardManager,
            ServicesManager servicesManager,
            BukkitScheduler bukkitScheduler
    ) {
        Preconditions.checkNotNull(plugin);
        Preconditions.checkNotNull(server);
        Preconditions.checkNotNull(pluginManager);
        Preconditions.checkNotNull(scoreboardManager);
        Preconditions.checkNotNull(servicesManager);
        Preconditions.checkNotNull(bukkitScheduler);

        this.plugin = plugin;
        this.server = server;
        this.pluginManager = pluginManager;
        this.scoreboardManager = scoreboardManager;
        this.servicesManager = servicesManager;
        this.bukkitScheduler = bukkitScheduler;

        this.pathRegistry = this.newPathRegistry();
        this.logger = this.newLogger();

        Thread.UncaughtExceptionHandler exceptionHandler = this.newExceptionHandler();
        ForkJoinPool forkJoinPool = this.newForkJoinPool(exceptionHandler);

        this.taskCoordinator = this.newTaskCoordinator(forkJoinPool);
        this.bukkitTaskCoordinator = this.newBukkitTaskCoordinator();
        this.itemRepository = this.newItemRepository();

        this.commandDispatcher = this.newCommandDispatcher(this.logger);
        this.asyncCommandDispatcher = this.newAsynchronousCommandDispatcher(forkJoinPool,this.commandDispatcher);
        this.senderManager = this.newSenderManager();
        this.commandRepository = this.newCommandRepository(this.logger,this.senderManager);
    }

    private ItemRepository newItemRepository() {
        BiMap<Key, Item> map = HashBiMap.create();
        Int2ObjectMap<Key> idMap = new Int2ObjectOpenHashMap<>();
        Object2IntMap<Key> keyMap = new Object2IntOpenHashMap<>();
        MaterialItemLookup materialItemLookup = new SimpleMaterialItemLookup();
        return new SimpleItemRepository(map, idMap, keyMap, materialItemLookup);
    }

    private PathRegistry newPathRegistry() {
        PathRegistry registry = new SimplePathRegistry(new HashMap<>());
        Path pluginFolderPath = this.plugin.getDataFolder().toPath().toAbsolutePath();

        registry.set(Paths.PLUGIN_FOLDER, pluginFolderPath);

        return registry;
    }

    private Logger newLogger() {
        return new DelegatedSlf4jLogger(this.plugin.getSLF4JLogger());
    }

    private Thread.UncaughtExceptionHandler newExceptionHandler() {
        return (t, e) -> this.logger.error(e);
    }

    private ForkJoinPool newForkJoinPool(Thread.UncaughtExceptionHandler exceptionHandler) {
        ForkJoinPool.ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory = ForkJoinPool.defaultForkJoinWorkerThreadFactory;
        return new ForkJoinPool(32, defaultForkJoinWorkerThreadFactory, exceptionHandler, true);
    }

    private TaskCoordinator<ExecutorService> newTaskCoordinator(ExecutorService pool) {
        return new TaskCoordinator<>(pool);
    }

    private BukkitTaskCoordinator newBukkitTaskCoordinator() {
        return new BukkitTaskCoordinator(this.plugin, this.bukkitScheduler);
    }

    private CommandDispatcher<CommandSender> newCommandDispatcher(Logger logger) {
        return new SimpleCommandDispatcher<>(logger);
    }

    private CommandDispatcher<CommandSender> newAsynchronousCommandDispatcher(ExecutorService pool, CommandDispatcher<CommandSender> commandDispatcher) {
        return new AsynchronousCommandDispatcher<>(pool, commandDispatcher);
    }

    private CommandRepository<CommandSender> newCommandRepository(Logger logger, SenderManager<CommandSender> senderManager) {
        return new BukkitCommandRepository(logger, this.pluginManager, this.plugin, senderManager);
    }

    private SenderManager<CommandSender> newSenderManager() {
        BukkitAudiences audiences = BukkitAudiences.create(this.plugin);
        BukkitSenderMappingRegistry mappingRegistry = new BukkitSenderMappingRegistry();
        return new BukkitSenderManager(audiences, mappingRegistry);
    }

    private Path getFolderPath() {
        return this.pathRegistry.get(Paths.PLUGIN_FOLDER);
    }

    private void tryCreateDirectoriesIfNotExist() {
        try {
            for (Path path : this.pathRegistry) {
                MoreFiles.createDirectoriesIfNotExists(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load() {
        this.tryCreateDirectoriesIfNotExist();
    }

    private void setupCommands() {
        TypeToken<PlayerSender<CommandSender>> playerSenderCommandSenderTypeToken = new TypeToken<>() {};

        SetpositionCommand<CommandSender> setpositionCommand = new SetpositionCommand<>(CommandInfo.newBuilder(playerSenderCommandSenderTypeToken)
                .name("setposition")
                .aliases("testd")
                .commandDispatcher(this.asyncCommandDispatcher)
                .permission("crp.admin")
                .build());

        this.commandRepository.register(setpositionCommand);
        this.commandRepository.injectCommands();
    }

    private Stream<Listener> streamListeners() {
        return Stream.<Listener>builder()
                .add(new ItemOverrideListener(this.itemRepository.getMaterialLookup()))
                .add(new PositionRegistryGuiListener(this.positionRegistryGuiManager))
                .add(new ItemRegistryGuiListener(this.itemRegistryGuiManager))
                .add(new ProfileGuiListener(this.profileGuiManager))
                .add(new ConnectionListener())
                .add(new GuiListener())
                .build();
    }

    private void registerEventListener(Listener listener) {
        this.pluginManager.registerEvents(listener, this.plugin);
    }

    private void setupListeners() {
        this.streamListeners().forEach(this::registerEventListener);
    }

    @Override
    public void enable() {
        this.setupCommands();

        this.itemRegistryGuiManager = new ItemRegistryGuiManager();
        this.positionRegistryGuiManager = new PositionRegistryGuiManager();
        this.profileGuiManager = new ProfileGuiManager(this.itemRegistryGuiManager, this.positionRegistryGuiManager);

        this.setupListeners();
    }

    @Override
    public void disable() {

    }

}
