package me.conclure.cityrp.command;


import org.bukkit.command.CommandSender;
import org.jspecify.nullness.Nullable;

public interface CommandRepository {
    @Nullable
    Command<?> getByName(String name);

    @Nullable
    Command<?> getByAlias(String alias);

    <S extends CommandSender> void register(Command<S> command);
}
