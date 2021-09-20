package me.conclure.cityrp.command;

import com.google.common.base.Preconditions;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

public class CommandInjector {
    private final SimpleCommandMap commandMap;
    private final String fallbackPrefix;

    public CommandInjector(PluginManager pluginManager, String fallbackPrefix) {
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

    public <S extends CommandSender> void inject(Command<S> command) {
        Preconditions.checkNotNull(command);
        this.commandMap.register(this.fallbackPrefix, new InjectedCommand<>(command));
    }

    static class InjectedCommand<S extends CommandSender> extends org.bukkit.command.Command {
        final Command<S> command;

        InjectedCommand(Command<S> command) {
            super(command.getInfo().getName());
            this.command = command;
        }

        @Override
        public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
            this.command.getInfo().getCommandDispatcher().dispatch(this.command,commandSender,strings);
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
