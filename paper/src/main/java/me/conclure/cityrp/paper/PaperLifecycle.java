package me.conclure.cityrp.paper;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.conclure.cityrp.common.command.CommandInfo;
import me.conclure.cityrp.common.command.commands.SetpositionCommand;
import me.conclure.cityrp.common.command.dispatching.AsynchronousCommandDispatcher;
import me.conclure.cityrp.common.command.dispatching.CommandDispatcher;
import me.conclure.cityrp.common.command.dispatching.SimpleCommandDispatcher;
import me.conclure.cityrp.common.command.repository.CommandRegistry;
import me.conclure.cityrp.common.data.position.JsonPositionDataManager;
import me.conclure.cityrp.common.data.position.PositionDataManager;
import me.conclure.cityrp.common.data.user.ConfigurateUserSerializer;
import me.conclure.cityrp.common.data.user.DelegatingUserDataStorage;
import me.conclure.cityrp.common.data.user.JsonUserDataController;
import me.conclure.cityrp.common.data.user.SimpleConfigurateUserSerializer;
import me.conclure.cityrp.common.data.user.UserDataController;
import me.conclure.cityrp.common.data.user.UserDataStorage;
import me.conclure.cityrp.common.model.position.Position;
import me.conclure.cityrp.common.model.position.PositionInfo;
import me.conclure.cityrp.common.model.position.PositionRegistry;
import me.conclure.cityrp.common.model.position.Positions;
import me.conclure.cityrp.common.model.user.SimpleUserRepository;
import me.conclure.cityrp.common.model.user.SimpleUserScrubber;
import me.conclure.cityrp.common.model.user.User;
import me.conclure.cityrp.common.model.user.UserFactory;
import me.conclure.cityrp.common.model.user.UserRepository;
import me.conclure.cityrp.common.model.user.UserScrubber;
import me.conclure.cityrp.common.plugin.PluginLifecycle;
import me.conclure.cityrp.common.sender.PlayerSender;
import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.sender.SenderTransformer;
import me.conclure.cityrp.common.sender.SenderTransformerRegistry;
import me.conclure.cityrp.common.sender.SimpleSenderTransformerRegistry;
import me.conclure.cityrp.common.utility.InventoryFactory;
import me.conclure.cityrp.common.utility.Key;
import me.conclure.cityrp.common.utility.MoreFiles;
import me.conclure.cityrp.common.utility.Permissions;
import me.conclure.cityrp.common.utility.PlayerObtainer;
import me.conclure.cityrp.common.utility.PlayerUUIDHelper;
import me.conclure.cityrp.common.utility.WorldObtainer;
import me.conclure.cityrp.common.utility.concurrent.AwaitableLatch;
import me.conclure.cityrp.common.utility.concurrent.ScheduledTaskCoordinator;
import me.conclure.cityrp.common.utility.concurrent.TaskCoordinator;
import me.conclure.cityrp.common.utility.logging.Logger;
import me.conclure.cityrp.common.utility.paths.PathRegistry;
import me.conclure.cityrp.common.utility.paths.Paths;
import me.conclure.cityrp.common.utility.paths.SimplePathRegistry;
import me.conclure.cityrp.paper.command.repository.BukkitCommandRegistry;
import me.conclure.cityrp.paper.gui.ItemRegistryGuiManager;
import me.conclure.cityrp.paper.gui.PositionRegistryGuiManager;
import me.conclure.cityrp.paper.gui.ProfileGuiManager;
import me.conclure.cityrp.paper.item.Item;
import me.conclure.cityrp.paper.item.repository.BukkitItemRepository;
import me.conclure.cityrp.paper.item.repository.ItemRepository;
import me.conclure.cityrp.paper.listener.ConnectionListener;
import me.conclure.cityrp.paper.listener.GuiListener;
import me.conclure.cityrp.paper.listener.ItemOverrideListener;
import me.conclure.cityrp.paper.listener.ItemRegistryGuiListener;
import me.conclure.cityrp.paper.listener.LoginListener;
import me.conclure.cityrp.paper.listener.PositionRegistryGuiListener;
import me.conclure.cityrp.paper.listener.ProfileGuiListener;
import me.conclure.cityrp.paper.model.user.BukkitUser;
import me.conclure.cityrp.paper.position.BukkitPosition;
import me.conclure.cityrp.paper.position.BukkitPositionRegistry;
import me.conclure.cityrp.paper.sender.BukkitPlayerSender;
import me.conclure.cityrp.paper.sender.BukkitSender;
import me.conclure.cityrp.paper.utility.BukkitInventoryFactory;
import me.conclure.cityrp.paper.utility.BukkitPlayerObtainer;
import me.conclure.cityrp.paper.utility.BukkitPlayerUUIDHelper;
import me.conclure.cityrp.paper.utility.BukkitWorldObtainer;
import me.conclure.cityrp.paper.utility.TypeTokens;
import me.conclure.cityrp.paper.utility.concurrent.BukkitTaskCoordinator;
import me.conclure.cityrp.paper.utility.logger.DelegatedSlf4jLogger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@SuppressWarnings("FieldCanBeLocal")
public class PaperLifecycle implements PluginLifecycle {
    private final AwaitableLatch enableLatch;

    private final Plugin plugin;
    private final Server server;
    private final PluginManager pluginManager;
    private final ScoreboardManager scoreboardManager;
    private final ServicesManager servicesManager;
    private final BukkitScheduler bukkitScheduler;

    private final PathRegistry pathRegistry;
    private final Logger logger;

    private final ScheduledTaskCoordinator<ScheduledExecutorService,Executor> scheduledTaskCoordinator;
    private final BukkitTaskCoordinator bukkitTaskCoordinator;
    private final TaskCoordinator<ExecutorService> taskCoordinator;

    private final BukkitAudiences bukkitAudiences;
    private final WorldObtainer<World> worldObtainer;
    private final PlayerObtainer<Player> playerObtainer;
    private final PlayerUUIDHelper playerUUIDHelper;
    private final InventoryFactory<Inventory, InventoryHolder> inventoryFactory;

    private final ItemRepository<Material> itemRepository;

    private final SenderTransformerRegistry senderTransformerRegistry;

    private final UserRepository userRepository;
    private final UserDataStorage userDataStorage;
    private final UserScrubber userScrubber;

    private final CommandDispatcher commandDispatcher;
    private final CommandDispatcher asyncCommandDispatcher;
    private CommandRegistry commandRegistry;

    private PositionDataManager<Entity, World> positionDataManager;
    private PositionRegistry<Entity, World> positionRegistry;

    private ItemRegistryGuiManager itemRegistryGuiManager;
    private PositionRegistryGuiManager positionRegistryGuiManager;
    private ProfileGuiManager profileGuiManager;

    public PaperLifecycle(
            AwaitableLatch enableLatch,
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

        this.enableLatch = enableLatch;
        this.plugin = plugin;
        this.server = server;
        this.pluginManager = pluginManager;
        this.scoreboardManager = scoreboardManager;
        this.servicesManager = servicesManager;
        this.bukkitScheduler = bukkitScheduler;


        this.pathRegistry = this.newPathRegistry();
        this.logger = this.newLogger();

        Thread.UncaughtExceptionHandler exceptionHandler = this.newExceptionHandler();
        ExecutorService forkJoinPool = this.newForkJoinPool(exceptionHandler);

        this.senderTransformerRegistry = this.newSenderTransformRegistry();

        this.taskCoordinator = this.newTaskCoordinator(forkJoinPool);
        this.bukkitTaskCoordinator = this.newBukkitTaskCoordinator();
        this.scheduledTaskCoordinator = this.newScheduledTaskCoordinator(exceptionHandler);

        this.bukkitAudiences = this.newBukkitAudiences();
        this.worldObtainer = this.newWorldObtainer();
        this.playerObtainer = this.newPlayerObtainer();
        this.playerUUIDHelper = this.newPlayerUUIDHelper(this.playerObtainer);
        this.inventoryFactory = this.newInventoryFactory();
        this.itemRepository = this.newItemRepository();


        this.commandDispatcher = this.newCommandDispatcher(this.logger);
        this.asyncCommandDispatcher = this.newAsynchronousCommandDispatcher(this.taskCoordinator, this.commandDispatcher);

        this.userRepository = this.newUserRepository(this.senderTransformerRegistry, this.playerObtainer);
        this.userDataStorage = this.newUserDataStorage(this.taskCoordinator, this.pathRegistry);
        this.userScrubber = this.newUserScrubber(this.userRepository, this.playerUUIDHelper);
    }

    private ScheduledTaskCoordinator<ScheduledExecutorService, Executor> newScheduledTaskCoordinator(
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler
    ) {
        ScheduledExecutorService scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
                1,
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("cityrp-scheduler")
                        .build()
        );
        ExecutorService threadPool = Executors.newCachedThreadPool(new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("cityrp-scheduler-worker-%d")
                .setUncaughtExceptionHandler(uncaughtExceptionHandler)
                .build()
        );
        return new ScheduledTaskCoordinator<>(scheduledThreadPoolExecutor,threadPool);
    }

    private BukkitAudiences newBukkitAudiences() {
        return BukkitAudiences.create(this.plugin);
    }

    private SenderTransformerRegistry newSenderTransformRegistry() {
        return new SimpleSenderTransformerRegistry.Builder()
                .add(CommandSender.class, TypeTokens.SENDER, new BukkitSender.Transformer(this.bukkitAudiences))
                .add(Player.class, TypeTokens.PLAYERSENDER, new BukkitPlayerSender.Tranformer(this.bukkitAudiences))
                .build();
    }

    private WorldObtainer<World> newWorldObtainer() {
        return new BukkitWorldObtainer(this.server);
    }

    private PlayerObtainer<Player> newPlayerObtainer() {
        return new BukkitPlayerObtainer(this.server);
    }

    private PlayerUUIDHelper newPlayerUUIDHelper(PlayerObtainer<Player> playerObtainer) {
        return new BukkitPlayerUUIDHelper(playerObtainer);
    }

    private InventoryFactory<Inventory, InventoryHolder> newInventoryFactory() {
        return new BukkitInventoryFactory(this.server);
    }

    private UserScrubber newUserScrubber(UserRepository userRepository, PlayerUUIDHelper playerUUIDHelper) {
        return new SimpleUserScrubber(userRepository, playerUUIDHelper, TimeUnit.MINUTES, 1);
    }

    private UserDataStorage newUserDataStorage(
            TaskCoordinator<? extends Executor> taskCoordinator,
            PathRegistry pathRegistry
    ) {
        ConfigurateUserSerializer configurateUserSerializer = new SimpleConfigurateUserSerializer();
        Path destinationDirectory = pathRegistry.get(Paths.USERS);
        UserDataController userDataController = new JsonUserDataController(destinationDirectory, configurateUserSerializer);
        return new DelegatingUserDataStorage(taskCoordinator, userDataController);
    }

    private UserRepository newUserRepository(
            SenderTransformerRegistry senderTransformerRegistry,
            PlayerObtainer<Player> playerObtainer
    ) {
        ConcurrentHashMap<UUID, User> map = new ConcurrentHashMap<>();
        SenderTransformer<Player, PlayerSender> senderTransformer = senderTransformerRegistry.get(Player.class, TypeTokens.PLAYERSENDER);
        UserFactory userFactory = uniqueId -> {
            return new BukkitUser(uniqueId, senderTransformer, playerObtainer);
        };
        return new SimpleUserRepository(map, userFactory);
    }

    private ItemRepository<Material> newItemRepository() {
        BiMap<Key, Item> map = HashBiMap.create();
        ConcurrentMap<Key, Integer> keyMap = new ConcurrentHashMap<>();
        ConcurrentMap<Integer, Key> idMap = new ConcurrentHashMap<>();
        return new BukkitItemRepository(map, idMap, keyMap);
    }

    private PathRegistry newPathRegistry() {
        Path pluginFolder = this.plugin.getDataFolder().toPath().toAbsolutePath();
        Path users = pluginFolder.resolve("users");
        Path positions = pluginFolder.resolve("positions");
        Path characters = pluginFolder.resolve("characters");

        return new SimplePathRegistry.Builder()
                .add(Paths.PLUGIN_FOLDER, pluginFolder)
                .add(Paths.POSITIONS, positions)
                .add(Paths.CHARACTERS, characters)
                .add(Paths.USERS, users)
                .build();
    }

    private Logger newLogger() {
        return new DelegatedSlf4jLogger(this.plugin.getSLF4JLogger());
    }

    private Thread.UncaughtExceptionHandler newExceptionHandler() {
        return (t, e) -> this.logger.error(e);
    }

    private ExecutorService newForkJoinPool(Thread.UncaughtExceptionHandler exceptionHandler) {
        ForkJoinPool.ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory = ForkJoinPool.defaultForkJoinWorkerThreadFactory;
        int parallelism = Runtime.getRuntime().availableProcessors() * 2;
        return new ForkJoinPool(parallelism, defaultForkJoinWorkerThreadFactory, exceptionHandler, true);
    }

    private TaskCoordinator<ExecutorService> newTaskCoordinator(ExecutorService pool) {
        return new TaskCoordinator<>(pool);
    }

    private BukkitTaskCoordinator newBukkitTaskCoordinator() {
        return new BukkitTaskCoordinator(this.plugin, this.bukkitScheduler);
    }

    private CommandDispatcher newCommandDispatcher(Logger logger) {
        return new SimpleCommandDispatcher(logger);
    }

    private CommandDispatcher newAsynchronousCommandDispatcher(
            TaskCoordinator<? extends Executor> taskCoordinator,
            CommandDispatcher commandDispatcher
    ) {
        return new AsynchronousCommandDispatcher(commandDispatcher, taskCoordinator);
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

    private void tryLoadPositions() {
        try {
            this.positionDataManager.loadAll().join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupPositions() {
        Position<Entity, World> spawnPosition = new BukkitPosition(Positions.SPAWN, new PositionInfo.Builder()
                .permission(Permissions.POSITION_SPAWN)
                .build());

        this.positionRegistry = new BukkitPositionRegistry.Builder()
                .add(spawnPosition)
                .build();

        this.positionDataManager = new JsonPositionDataManager<>(
                this.positionRegistry,
                this.worldObtainer,
                this.pathRegistry.get(Paths.POSITIONS),
                this.taskCoordinator
        );

        this.tryLoadPositions();
    }

    private void setupCommands() {

        SetpositionCommand setpositionCommand = new SetpositionCommand(
                CommandInfo.newBuilder(TypeTokens.PLAYERSENDER)
                        .aliases("testd")
                        .commandDispatcher(this.asyncCommandDispatcher)
                        .permission("crp.admin")
                        .build()
        );

        SenderTransformer<CommandSender, Sender> senderConverter = this.senderTransformerRegistry.get(CommandSender.class, TypeTokens.SENDER);
        this.commandRegistry = new BukkitCommandRegistry.Builder()
                .add(setpositionCommand)
                .build(this.logger, this.pluginManager, this.plugin, senderConverter);


        this.commandRegistry.registerContainedCommands();
    }

    private Stream<Listener> streamListeners() {
        ConnectionListener connectionListener = new ConnectionListener(
                this.enableLatch,
                this.userScrubber,
                this.userRepository,
                this.userDataStorage,
                this.logger,
                this.taskCoordinator
        );
        return Stream.<Listener>builder()
                .add(new ItemOverrideListener(this.itemRepository.getMaterialLookup()))
                .add(new PositionRegistryGuiListener(this.positionRegistryGuiManager))
                .add(new ItemRegistryGuiListener(this.itemRegistryGuiManager))
                .add(new ProfileGuiListener(this.profileGuiManager))
                .add(new LoginListener(this.positionRegistry))
                .add(new GuiListener())
                .add(connectionListener)
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
        this.setupPositions();
        this.setupCommands();
        this.scheduledTaskCoordinator.repeat(this.userScrubber.getTask(),30,TimeUnit.SECONDS);

        this.itemRegistryGuiManager = new ItemRegistryGuiManager();
        this.positionRegistryGuiManager = new PositionRegistryGuiManager(this.positionRegistry, this.inventoryFactory);
        this.profileGuiManager = new ProfileGuiManager(this.itemRegistryGuiManager, this.positionRegistryGuiManager);

        this.setupListeners();
    }

    @Override
    public void disable() {
        this.positionDataManager.saveAll().join();
    }
}
