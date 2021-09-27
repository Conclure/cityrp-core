package me.conclure.cityrp.common.command.argument;

import me.conclure.cityrp.common.languange.Locale;
import me.conclure.cityrp.common.sender.Sender;
import net.kyori.adventure.text.Component;

public abstract class AbstractArgument<T, S extends Sender<SS>,SS,A extends ArgumentInfo> implements Argument<T, S,SS> {
    private final A info;

    protected AbstractArgument(A info) {
        this.info = info;
    }

    @Override
    public A getInfo() {
        return this.info;
    }

    @Override
    public Component getNameFormatted() {
        if (this.info.isOptional()) {
            return Locale.OPTIONAL_ARGUMENT.build(this.info.getName());
        }

        return Locale.REQUIRED_ARGUMENT.build(this.info.getName());
    }
}
