package me.conclure.cityrp.paper.listener;

import me.conclure.cityrp.common.data.user.UserDataStorage;
import me.conclure.cityrp.common.languange.Locale;
import me.conclure.cityrp.common.model.user.User;
import me.conclure.cityrp.common.model.user.UserRepository;
import me.conclure.cityrp.common.model.user.UserScrubber;
import me.conclure.cityrp.common.utility.concurrent.AwaitableLatch;
import me.conclure.cityrp.common.utility.concurrent.TaskCoordinator;
import me.conclure.cityrp.common.utility.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * LuckPerms (MIT License)
 *
 * @author lucko
 */
public class ConnectionListener implements Listener {
    private final Set<UUID> uniqueConnections, deniedAsyncLogin, deniedLogin;
    private final AwaitableLatch enableLatch;
    private final UserScrubber userScrubber;
    private final UserRepository userRepository;
    private final UserDataStorage userDataStorage;
    private final Logger logger;
    private final TaskCoordinator<? extends Executor> taskCoordinator;

    public ConnectionListener(
            AwaitableLatch enableLatch,
            UserScrubber userScrubber,
            UserRepository userRepository,
            UserDataStorage userDataStorage,
            Logger logger,
            TaskCoordinator<? extends Executor> taskCoordinator
    ) {
        this.enableLatch = enableLatch;
        this.userScrubber = userScrubber;
        this.userRepository = userRepository;
        this.userDataStorage = userDataStorage;
        this.logger = logger;
        this.taskCoordinator = taskCoordinator;

        this.uniqueConnections = ConcurrentHashMap.newKeySet();
        this.deniedAsyncLogin = Collections.synchronizedSet(new HashSet<>());
        this.deniedLogin = Collections.synchronizedSet(new HashSet<>());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        try {
            this.enableLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UUID uniqueId = event.getUniqueId();

        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            this.deniedAsyncLogin.add(uniqueId);
            return;
        }

        try {
            this.userScrubber.registerUsage(uniqueId);
            User user = this.userRepository.getOrCreate(uniqueId);
            user.setName(event.getName());
            this.userDataStorage.load(user).join();
            this.uniqueConnections.add(uniqueId);
        } catch (Exception e) {
            this.logger.error(e);
            this.deniedAsyncLogin.add(uniqueId);;
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Locale.DATA_LOAD_ERROR.build());
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLoginMonitor(AsyncPlayerPreLoginEvent event) {

        UUID uniqueId = event.getUniqueId();
        boolean wasDeniedAtLowPriority = this.deniedAsyncLogin.remove(uniqueId);
        boolean wasNotDeniedAtLowPriority = !wasDeniedAtLowPriority;
        if (wasNotDeniedAtLowPriority) {
            return;
        }

        boolean wasDeniedAtLowButNowAllowed = event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED;
        boolean wasNotDeniedAtLowButNowAllowed = !wasDeniedAtLowButNowAllowed;
        if (wasNotDeniedAtLowButNowAllowed) {
            return;
        }

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.empty());
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        boolean userIsLoaded = this.userRepository.contains(uniqueId);

        if (userIsLoaded) {
            return;
        }

        this.deniedLogin.add(uniqueId);
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER,Locale.DATA_LOAD_ERROR.build());
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onLoginMonitor(PlayerLoginEvent event) {
        UUID uniqueId = event.getPlayer().getUniqueId();

        if (!this.deniedAsyncLogin.remove(uniqueId)) {
            return;
        }

        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        event.disallow(PlayerLoginEvent.Result.KICK_OTHER,Component.empty());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        this.userScrubber.registerUsage(uniqueId);
    }
}
