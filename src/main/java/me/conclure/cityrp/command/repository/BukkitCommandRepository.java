package me.conclure.cityrp.command.repository;

import me.conclure.cityrp.command.Command;
import me.conclure.cityrp.command.CommandInfo;
import me.conclure.cityrp.sender.Sender;
import me.conclure.cityrp.sender.SenderManager;
import me.conclure.cityrp.utility.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jspecify.nullness.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class BukkitCommandRepository implements CommandRepository<CommandSender> {
    private final Map<String, Command<? extends Sender<CommandSender>,CommandSender>> commandMap;
    private final Map<String, Command<? extends Sender<CommandSender>,CommandSender>> aliasMap;
    private final Logger logger;
    private final Supplier<CommandInjector> commandInjector;
    private final SenderManager<CommandSender> senderManager;

    public BukkitCommandRepository(Logger logger, PluginManager pluginManager, Plugin plugin, SenderManager<CommandSender> senderManager) {
        this.logger = logger;
        this.senderManager = senderManager;
        this.commandMap = new HashMap<>();
        this.aliasMap = new HashMap<>();
        this.commandInjector = () -> new CommandInjector(pluginManager, plugin.getName(), this.senderManager);
    }

    @Override
    public void injectCommands() {
        CommandInjector commandInjector = this.commandInjector.get();
        for (Command<? extends Sender<CommandSender>,CommandSender> command : this.commandMap.values()) {
            commandInjector.inject(command);
        }
    }

    @Nullable
    @Override
    public Command<? extends Sender<CommandSender>,CommandSender> getByName(String name) {
        return this.commandMap.get(name.toLowerCase(Locale.ROOT));
    }

    @Nullable
    @Override
    public Command<? extends Sender<CommandSender>,CommandSender> getByAlias(String alias) {
        return this.aliasMap.get(alias.toLowerCase(Locale.ROOT));
    }

    @Override
    public <S extends Sender<CommandSender>> void register(Command<S,CommandSender> command) {
        CommandInfo<S,CommandSender> info = command.getInfo();
        String name = info.getName();
        this.commandMap.put(name, command);

        for (String alias : info.getAliases()) {
            Command<?,CommandSender> previousCommand = this.aliasMap.get(alias);

            if (previousCommand != null) {
                String previousCommandName = previousCommand.getInfo().getName();
                this.logger.warnf("Command alias %s already registered for %s, overriding for %s", alias, previousCommandName, name);
            }

            this.aliasMap.put(alias, command);
        }
    }
}
