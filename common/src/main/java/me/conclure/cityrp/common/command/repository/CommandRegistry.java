package me.conclure.cityrp.common.command.repository;

import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.sender.Sender;
import org.jspecify.nullness.Nullable;

public interface CommandRegistry {
    void registerContainedCommands();

    @Nullable
    Command<? extends Sender> getByName(String name);

    @Nullable
    Command<? extends Sender> getByAlias(String alias);
}
