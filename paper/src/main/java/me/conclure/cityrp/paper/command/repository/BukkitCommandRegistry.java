package me.conclure.cityrp.paper.command.repository;

import com.google.common.collect.ImmutableMap;
import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.command.CommandInfo;
import me.conclure.cityrp.common.command.repository.CommandRegistry;
import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.sender.SenderTransformer;
import me.conclure.cityrp.common.utility.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jspecify.nullness.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BukkitCommandRegistry implements CommandRegistry {
    private final ImmutableMap<String, Command<? extends Sender>> commandMap;
    private final ImmutableMap<String, Command<? extends Sender>> aliasMap;
    private final Logger logger;
    private final Supplier<CommandInjector> commandInjector;
    private final SenderTransformer<CommandSender, Sender> senderTransformer;

    public BukkitCommandRegistry(
            Logger logger,
            PluginManager pluginManager,
            Plugin plugin,
            Stream<Command<? extends Sender>> commands,
            SenderTransformer<CommandSender, Sender> senderTransformer) {
        this.logger = logger;
        this.senderTransformer = senderTransformer;
        ImmutableMap.Builder<String, Command<? extends Sender>> commandMapBuilder = ImmutableMap.builder();
        Map<String, Command<? extends Sender>> tempAliasMap = new HashMap<>();
        commands.forEach(command -> {
            CommandInfo<? extends Sender> info = command.getInfo();
            String name = info.getName();
            commandMapBuilder.put(name, command);

            for (String alias : info.getAliases()) {
                Command<?> previousCommand = tempAliasMap.get(alias);

                if (previousCommand != null) {
                    String previousCommandName = previousCommand.getInfo().getName();
                    this.logger.warnf("Command alias %s already registered for %s, overriding for %s", alias, previousCommandName, name);
                }

                tempAliasMap.put(alias, command);
            }
        });
        this.commandMap = commandMapBuilder.build();
        this.aliasMap = ImmutableMap.copyOf(tempAliasMap);
        this.commandInjector = () -> new CommandInjector(pluginManager, plugin.getName(), this.senderTransformer);
    }

    @Override
    public void registerContainedCommands() {
        CommandInjector commandInjector = this.commandInjector.get();
        for (Command<? extends Sender> command : this.commandMap.values()) {
            commandInjector.inject(command);
        }
    }

    @Nullable
    @Override
    public Command<? extends Sender> getByName(String name) {
        return this.commandMap.get(name.toLowerCase(Locale.ROOT));
    }

    @Nullable
    @Override
    public Command<? extends Sender> getByAlias(String alias) {
        return this.aliasMap.get(alias.toLowerCase(Locale.ROOT));
    }

    public static class Builder {
        private final Stream.Builder<Command<? extends Sender>> commands = Stream.builder();

        public Builder() {
        }

        public Builder add(Command<? extends Sender> command) {
            this.commands.add(command);
            return this;
        }

        public BukkitCommandRegistry build(
                Logger logger,
                PluginManager pluginManager,
                Plugin plugin,
                SenderTransformer<CommandSender, Sender> senderConverter
        ) {
            return new BukkitCommandRegistry(logger, pluginManager, plugin, this.commands.build(), senderConverter);
        }
    }
}
