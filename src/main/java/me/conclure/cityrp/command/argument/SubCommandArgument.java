package me.conclure.cityrp.command.argument;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import me.conclure.cityrp.command.Command;
import me.conclure.cityrp.sender.Sender;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class SubCommandArgument<C extends Command<S,SS>,S extends Sender<SS>,SS> extends AbstractArgument<C,S,SS, SubCommandArgument.Info<C,S,SS>>{
    public SubCommandArgument(SubCommandArgument.Info<C,S, SS> info, Stream<C> stream) {
        super(info);

        stream.forEach(command -> {
            String name = command.getInfo().getName();
            this.getInfo().map.put(name,command);
        });
    }

    @Override
    public ArgumentParseResult<C, S, SS> parse(String argument) {
        Preconditions.checkNotNull(argument);
        C command = this.getInfo().map.get(argument.toLowerCase(Locale.ROOT));

        if (command == null) {
            return ArgumentParseResult.fail(sender -> {
                sender.sendMessage(Component.text("Subcommand unrecognized: "+argument));
            });
        }

        return ArgumentParseResult.success(command);
    }

    public static class Info<C extends Command<S,SS>,S extends Sender<SS>,SS> extends ArgumentInfo {
        private final Map<String,C> map;

        protected Info(boolean isOptional, String name, Component description, Map<String, C> map) {
            super(isOptional, name, description);
            this.map = map;
        }
    }

    public static class InfoBuilder<C extends Command<S,SS>,S extends Sender<SS>,SS> extends ArgumentInfo.Builder {
        private Map<String,C> map = new HashMap<>();

        @Override
        public InfoBuilder<C,S,SS> description(Component description) {
            super.description(description);
            return this;
        }

        @Override
        public InfoBuilder<C,S,SS> name(String name) {
            super.name(name);
            return this;
        }

        @Override
        public InfoBuilder<C,S,SS> optional(boolean optional) {
            super.optional(optional);
            return this;
        }

        public InfoBuilder<C,S,SS> mapImplementation(Map<String, C> map) {
            this.map = map;
            return this;
        }

        @Override
        public Info<C,S,SS> build() {
            return new Info<>(this.isOptional(),this.name(),this.description(),this.map);
        }
    }
}
