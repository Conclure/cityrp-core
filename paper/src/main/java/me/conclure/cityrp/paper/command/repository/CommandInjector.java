package me.conclure.cityrp.paper.command.repository;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.command.dispatching.CommandDispatcher;
import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.sender.SenderTransformer;
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
    final SenderTransformer<CommandSender, Sender> senderTransformer;

    CommandInjector(PluginManager pluginManager, String fallbackPrefix, SenderTransformer<CommandSender, Sender> senderTransformer) {
        this.senderTransformer = senderTransformer;
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

    <S extends Sender> void inject(Command<S> command) {
        Preconditions.checkNotNull(command);

        this.commandMap.register(this.fallbackPrefix, new InjectedCommand<>(command));
    }

    static class FailedInstantiationException extends RuntimeException {
        FailedInstantiationException(Throwable cause) {
            super(cause);
        }
    }

    class InjectedCommand<S extends Sender> extends org.bukkit.command.Command {
        final Command<S> command;

        InjectedCommand(Command<S> command) {
            super(command.getInfo().getName());
            this.command = command;
        }

        @Override
        public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
            CommandDispatcher dispatcher = this.command.getInfo().getCommandDispatcher();
            Sender sender = CommandInjector.this.senderTransformer.tranform(commandSender);

            dispatcher.dispatch(this.command, sender, strings);
            return true;
        }

        @Override
        public @NotNull List<String> getAliases() {
            return this.command.getInfo().getAliases();
        }
    }
}
