package me.conclure.cityrp.paper.command.repository;

import com.google.common.collect.ImmutableMap;
import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.command.CommandInfo;
import me.conclure.cityrp.common.command.repository.CommandRepository;
import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.sender.SenderTranformer;
import me.conclure.cityrp.common.utility.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jspecify.nullness.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BukkitCommandRepository implements CommandRepository<CommandSender> {
    private final ImmutableMap<String, Command<? extends Sender<CommandSender>, CommandSender>> commandMap;
    private final ImmutableMap<String, Command<? extends Sender<CommandSender>, CommandSender>> aliasMap;
    private final Logger logger;
    private final Supplier<CommandInjector> commandInjector;
    private final SenderTranformer<CommandSender, Sender<? extends CommandSender>> senderTranformer;

    public BukkitCommandRepository(
            Logger logger,
            PluginManager pluginManager,
            Plugin plugin,
            Stream<Command<? extends Sender<CommandSender>, CommandSender>> commands,
            SenderTranformer<CommandSender, Sender<? extends CommandSender>> senderTranformer) {
        this.logger = logger;
        this.senderTranformer = senderTranformer;
        ImmutableMap.Builder<String, Command<? extends Sender<CommandSender>, CommandSender>> commandMapBuilder = ImmutableMap.builder();
        Map<String, Command<? extends Sender<CommandSender>, CommandSender>> tempAliasMap = new HashMap<>();
        commands.forEach(command -> {
            CommandInfo<? extends Sender<CommandSender>, CommandSender> info = command.getInfo();
            String name = info.getName();
            commandMapBuilder.put(name, command);

            for (String alias : info.getAliases()) {
                Command<?, CommandSender> previousCommand = tempAliasMap.get(alias);

                if (previousCommand != null) {
                    String previousCommandName = previousCommand.getInfo().getName();
                    this.logger.warnf("Command alias %s already registered for %s, overriding for %s", alias, previousCommandName, name);
                }

                tempAliasMap.put(alias, command);
            }
        });
        this.commandMap = commandMapBuilder.build();
        this.aliasMap = ImmutableMap.copyOf(tempAliasMap);
        this.commandInjector = () -> new CommandInjector(pluginManager, plugin.getName(), this.senderTranformer);
    }

    @Override
    public void registerContainedCommands() {
        CommandInjector commandInjector = this.commandInjector.get();
        for (Command<? extends Sender<CommandSender>, CommandSender> command : this.commandMap.values()) {
            commandInjector.inject(command);
        }
    }

    @Nullable
    @Override
    public Command<? extends Sender<CommandSender>, CommandSender> getByName(String name) {
        return this.commandMap.get(name.toLowerCase(Locale.ROOT));
    }

    @Nullable
    @Override
    public Command<? extends Sender<CommandSender>, CommandSender> getByAlias(String alias) {
        return this.aliasMap.get(alias.toLowerCase(Locale.ROOT));
    }

    public static class Builder {
        private final Stream.Builder<Command<? extends Sender<CommandSender>, CommandSender>> commands = Stream.builder();

        public Builder() {
        }

        public Builder add(Command<? extends Sender<CommandSender>, CommandSender> command) {
            this.commands.add(command);
            return this;
        }

        public BukkitCommandRepository build(
                Logger logger,
                PluginManager pluginManager,
                Plugin plugin,
                SenderTranformer<CommandSender, Sender<? extends CommandSender>> senderConverter
        ) {
            return new BukkitCommandRepository(logger, pluginManager, plugin, this.commands.build(), senderConverter);
        }
    }
}
