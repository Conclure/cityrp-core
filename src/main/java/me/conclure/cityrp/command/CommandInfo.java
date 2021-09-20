package me.conclure.cityrp.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import me.conclure.cityrp.command.dispatching.CommandDispatcher;
import org.bukkit.command.CommandSender;
import org.jspecify.nullness.Nullable;

import java.util.Locale;

public final class CommandInfo<S extends CommandSender> {
    @Nullable
    private final String permission;
    private final String name;
    private final Class<S> senderType;
    private final ImmutableList<String> aliases;
    private final CommandDispatcher commandDispatcher;

    CommandInfo(String permission, String name, Class<S> senderType, ImmutableSet<String> aliases, CommandDispatcher commandDispatcher) {
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
    }

    public static <S extends CommandSender> Builder<S> newBuilder() {
        return new Builder<>();
    }

    @Nullable
    public Class<S> getSenderType() {
        return this.senderType;
    }

    @Nullable
    public String getPermission() {
        return this.permission;
    }

    public boolean requiresPermission() {
        return this.permission == null;
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

    public static final class Builder<S extends CommandSender> {
        @Nullable
        private String permission;
        private String name;
        private Class<S> senderType;
        private ImmutableSet<String> aliases = ImmutableSet.of();
        private CommandDispatcher commandDispatcher;

        Builder() {

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

        public Builder<S> senderType(Class<S> senderType) {
            Preconditions.checkNotNull(senderType);
            this.senderType = senderType;
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

        public CommandInfo<S> build() {
            return new CommandInfo<>(this.permission, this.name, this.senderType, this.aliases, this.commandDispatcher);
        }
    }
}
