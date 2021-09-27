package me.conclure.cityrp.paper.command.repository;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.command.dispatching.CommandDispatcher;
import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.sender.SenderManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

final class CommandInjector {
    final SimpleCommandMap commandMap;
    final String fallbackPrefix;
    final SenderManager<CommandSender> senderManager;

    CommandInjector(PluginManager pluginManager, String fallbackPrefix, SenderManager<CommandSender> senderManager) {
        this.senderManager = senderManager;
        Preconditions.checkNotNull(pluginManager);
        Preconditions.checkArgument(pluginManager instanceof SimplePluginManager);
        Preconditions.checkNotNull(fallbackPrefix);

        this.fallbackPrefix = fallbackPrefix;
        SimplePluginManager manager = (SimplePluginManager) pluginManager;

        Field commandMapField;

        try {
            commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
        } catch (NoSuchFieldException e) {
            throw new FailedInstantiationException(e);
        }

        commandMapField.setAccessible(true);

        try {
            this.commandMap = (SimpleCommandMap) commandMapField.get(manager);
        } catch (IllegalAccessException | ClassCastException e) {
            throw new FailedInstantiationException(e);
        }
    }

    <S extends Sender<CommandSender>> void inject(Command<S,CommandSender> command) {
        Preconditions.checkNotNull(command);

        this.commandMap.register(this.fallbackPrefix, new InjectedCommand<>(command));
    }

    class InjectedCommand<S extends Sender<CommandSender>> extends org.bukkit.command.Command {
        final Command<S,CommandSender> command;

        InjectedCommand(Command<S,CommandSender> command) {
            super(command.getInfo().getName());
            this.command = command;
        }

        @Override
        public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
            CommandDispatcher<CommandSender> dispatcher = this.command.getInfo().getCommandDispatcher();
            Sender<? extends CommandSender> sender = CommandInjector.this.senderManager.asSender(commandSender);

            dispatcher.dispatch(this.command, sender, strings);
            return true;
        }

        @Override
        public @NotNull List<String> getAliases() {
            return this.command.getInfo().getAliases();
        }
    }

    static class FailedInstantiationException extends RuntimeException {
        FailedInstantiationException(Throwable cause) {
            super(cause);
        }
    }
}
