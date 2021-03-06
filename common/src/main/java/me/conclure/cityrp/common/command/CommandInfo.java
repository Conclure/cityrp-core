package me.conclure.cityrp.common.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import me.conclure.cityrp.common.command.dispatching.CommandDispatcher;
import me.conclure.cityrp.common.sender.Sender;
import org.jspecify.nullness.Nullable;

import java.util.Locale;
import java.util.concurrent.locks.Lock;

public final class CommandInfo<S extends Sender> {
    @Nullable
    private final String permission;
    private final String name;
    private final TypeToken<S> senderType;
    private final ImmutableList<String> aliases;
    private final CommandDispatcher commandDispatcher;
    private final Lock lock;

    private CommandInfo(
            String permission,
            String name,
            TypeToken<S> senderType,
            ImmutableSet<String> aliases,
            CommandDispatcher commandDispatcher,
            Lock lock
    ) {
        Preconditions.checkNotNull(permission);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(senderType);
        Preconditions.checkNotNull(aliases);
        Preconditions.checkNotNull(commandDispatcher);

        this.name = name;
        this.permission = permission;
        this.senderType = senderType;
        this.aliases = ImmutableList.copyOf(aliases);
        this.commandDispatcher = commandDispatcher;
        this.lock = lock;
    }

    public static <S extends Sender> Builder<S> newBuilder(TypeToken<S> senderType) {
        return new Builder<>(senderType);
    }

    @Nullable
    public TypeToken<S> getSenderType() {
        return this.senderType;
    }

    @Nullable
    public String getPermission() {
        return this.permission;
    }

    public boolean requiresPermission() {
        return this.permission != null;
    }

    public ImmutableList<String> getAliases() {
        return this.aliases;
    }

    public CommandDispatcher getCommandDispatcher() {
        return this.commandDispatcher;
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    public Lock getLock() {
        return this.lock;
    }

    public boolean requiresLocking() {
        return this.lock != null;
    }

    public static final class Builder<S extends Sender> {
        private final TypeToken<S> senderType;

        @Nullable
        private String permission;
        private String name;
        private ImmutableSet<String> aliases = ImmutableSet.of();
        private CommandDispatcher commandDispatcher;
        private Lock lock;

        private Builder(TypeToken<S> senderType) {
            this.senderType = senderType;
        }

        public Builder<S> permission(String permission) {
            Preconditions.checkNotNull(permission);
            this.permission = permission.toLowerCase(Locale.ROOT);
            return this;
        }

        public Builder<S> name(String name) {
            this.name = name.toLowerCase(Locale.ROOT);
            return this;
        }

        public Builder<S> aliases(String... aliases) {
            Preconditions.checkNotNull(aliases);

            for (int i = 0; i < aliases.length; i++) {
                aliases[i] = aliases[i].toLowerCase(Locale.ROOT);
            }

            this.aliases = ImmutableSet.copyOf(aliases);
            return this;
        }

        public Builder<S> commandDispatcher(CommandDispatcher commandDispatcher) {
            Preconditions.checkNotNull(commandDispatcher);
            this.commandDispatcher = commandDispatcher;
            return this;
        }

        public Builder<S> lock(Lock lock) {
            this.lock = lock;
            return this;
        }

        public CommandInfo<S> build() {
            return new CommandInfo<>(
                    this.permission,
                    this.name,
                    this.senderType,
                    this.aliases,
                    this.commandDispatcher,
                    this.lock
            );
        }
    }
}
