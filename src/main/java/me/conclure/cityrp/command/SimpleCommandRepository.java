package me.conclure.cityrp.command;

import me.conclure.cityrp.utility.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jspecify.nullness.Nullable;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;

public class SimpleCommandRepository implements CommandRepository {
    private final Map<String,Command<?>> commandMap;
    private final Map<String,Command<?>> aliasMap;
    private final Logger logger;
    private final CommandInjector commandInjector;

    public SimpleCommandRepository(Logger logger, PluginManager pluginManager, Plugin plugin) {
        this.logger = logger;
        this.commandMap = new HashMap<>();
        this.aliasMap = new HashMap<>();
        this.commandInjector = new CommandInjector(pluginManager,plugin.getName());
    }

    @Nullable
    @Override
    public Command<?> getByName(String name) {
        return this.commandMap.get(name.toLowerCase(Locale.ROOT));
    }

    @Nullable
    @Override
    public Command<?> getByAlias(String alias) {
        return this.aliasMap.get(alias.toLowerCase(Locale.ROOT));
    }

    @Override
    public <S extends CommandSender> void register(Command<S> command) {
        CommandInfo<S> info = command.getInfo();
        String name = info.getName();
        this.commandMap.put(name,command);

        for (String alias : info.getAliases()) {
            Command<?> previousCommand = this.aliasMap.get(alias);

            if (previousCommand != null) {
                String previousCommandName = previousCommand.getInfo().getName();
                this.logger.warnf("Command alias %s already registered for %s, overriding for %s",alias, previousCommandName, name);
            }

            this.aliasMap.put(alias,command);
        }

        this.commandInjector.inject(command);
    }
}
