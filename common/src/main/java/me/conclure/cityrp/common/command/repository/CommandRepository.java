package me.conclure.cityrp.common.command.repository;

import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.sender.Sender;
import org.jspecify.nullness.Nullable;

public interface CommandRepository<PlatformSender> {
    void registerContainedCommands();

    @Nullable
    Command<? extends Sender<PlatformSender>, PlatformSender> getByName(String name);

    @Nullable
    Command<? extends Sender<PlatformSender>, PlatformSender> getByAlias(String alias);
}
