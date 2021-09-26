package me.conclure.cityrp.command.repository;


import me.conclure.cityrp.command.Command;
import me.conclure.cityrp.sender.Sender;
import org.bukkit.command.CommandSender;
import org.jspecify.nullness.Nullable;

public interface CommandRepository<SS> {
    void registerContainedCommands();

    @Nullable
    Command<? extends Sender<SS>,SS> getByName(String name);

    @Nullable
    Command<? extends Sender<SS>,SS> getByAlias(String alias);

    <S extends Sender<SS>> void add(Command<S,SS> command);
}
