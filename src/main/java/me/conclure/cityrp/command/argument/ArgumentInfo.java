package me.conclure.cityrp.command.argument;

import me.conclure.cityrp.sender.Sender;
import net.kyori.adventure.text.Component;

import javax.management.Descriptor;
import java.util.Locale;

public class ArgumentInfo {
    private final boolean isOptional;
    private final String name;
    private final Component description;

    protected ArgumentInfo(boolean isOptional, String name, Component description) {
        this.isOptional = isOptional;
        this.name = name;
        this.description = description;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getName() {
        return this.name;
    }

    public Component getDescription() {
        return this.description;
    }

    public boolean isOptional() {
        return this.isOptional;
    }

    public boolean isRequired() {
        return !this.isOptional;
    }

    public static class Builder {
        private boolean isOptional;
        private String name;
        private Component description;

        protected Builder() {

        }

        public Builder description(Component description) {
            this.description = description;
            return this;
        }

        protected Component description() {
            return this.description;
        }

        public Builder name(String name) {
            this.name = name.toLowerCase(Locale.ROOT);
            return this;
        }

        protected String name() {
            return this.name;
        }

        public Builder optional(boolean optional) {
            this.isOptional = optional;
            return this;
        }

        protected boolean isOptional() {
            return this.isOptional;
        }

        public ArgumentInfo build() {
            return new ArgumentInfo(this.isOptional,this.name,this.description);
        }
    }
}
