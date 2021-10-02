package me.conclure.cityrp.common.languange;

import me.conclure.cityrp.common.sender.Sender;
import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

/**
 * LuckPerms (MIT License)
 *
 * @author lucko
 */
public interface Locale {
    Component NEW_LINE = Component.newline();
    Component SPACE = Component.space();

    UniComponent<Sender, String> REQUIRED_ARGUMENT = argument -> text()
            .color(DARK_GRAY)
            .append(text("<"))
            .append(text(argument, GRAY))
            .append(text(">"))
            .build();

    UniComponent<Sender, String> OPTIONAL_ARGUMENT = argument -> text()
            .color(DARK_GRAY)
            .append(text("["))
            .append(text(argument, GRAY))
            .append(text("]"))
            .build();

    NullComponent<Sender> FAILED_INITIAL_TELEPORTING = () -> text("Failed teleporting you.");

    NullComponent<Sender> DATA_LOAD_ERROR = () -> text("Failed loading data.");

    @FunctionalInterface
    interface NullComponent<S extends Sender> {
        Component build();

        default void send(S sender) {
            sender.sendMessage(this.build());
        }
    }

    @FunctionalInterface
    interface UniComponent<S extends Sender, A0> {
        Component build(A0 arg0);

        default void send(S sender, A0 arg0) {
            sender.sendMessage(this.build(arg0));
        }
    }

    @FunctionalInterface
    interface BiComponent<S extends Sender, A0, A1> {
        Component build(A0 arg0, A1 arg1);

        default void send(S sender, A0 arg0, A1 arg1) {
            sender.sendMessage(this.build(arg0, arg1));
        }
    }

    @FunctionalInterface
    interface TriComponent<S extends Sender, A0, A1, A2> {
        Component build(A0 arg0, A1 arg1, A2 arg2);

        default void send(S sender, A0 arg0, A1 arg1, A2 arg2) {
            sender.sendMessage(this.build(arg0, arg1, arg2));
        }
    }

    @FunctionalInterface
    interface QuadComponent<S extends Sender, A0, A1, A2, A3> {
        Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3);

        default void send(S sender, A0 arg0, A1 arg1, A2 arg2, A3 arg3) {
            sender.sendMessage(this.build(arg0, arg1, arg2, arg3));
        }
    }

    @FunctionalInterface
    interface PentaComponent<S extends Sender, A0, A1, A2, A3, A4> {
        Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4);

        default void send(S sender, A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4) {
            sender.sendMessage(this.build(arg0, arg1, arg2, arg3, arg4));
        }
    }

    @FunctionalInterface
    interface HexaComponent<S extends Sender, A0, A1, A2, A3, A4, A5> {
        Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5);

        default void send(S sender, A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5) {
            sender.sendMessage(this.build(arg0, arg1, arg2, arg3, arg4, arg5));
        }
    }
}
