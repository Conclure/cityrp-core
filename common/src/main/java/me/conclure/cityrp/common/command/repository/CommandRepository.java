package me.conclure.cityrp.common.command.repository;

import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.sender.Sender;
import org.jspecify.nullness.Nullable;

public interface CommandRepository<SS> {
    void registerContainedCommands();

    @Nullable
    Command<? extends Sender<SS>,SS> getByName(String name);

    @Nullable
    Command<? extends Sender<SS>,SS> getByAlias(String alias);
}
