package me.conclure.cityrp.command.abstraction;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.command.CommandInfo;
import me.conclure.cityrp.command.argument.Argument;
import me.conclure.cityrp.command.argument.ArgumentParseResult;
import me.conclure.cityrp.sender.Sender;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class UniAbstractCommand<S extends Sender<SS>,SS, A0> extends AbstractCommand<S,SS> {
    private static final int ARGUMENT_LENGTH = 1;
    private final List<Argument<?, S>> argumentList;
    private final Argument<A0, S> argument0;

    protected UniAbstractCommand(CommandInfo<S,SS> commandInfo, Argument<A0, S> argument0) {
        super(commandInfo);
        Preconditions.checkNotNull(argument0);
        this.argumentList = new ArrayList<>(ARGUMENT_LENGTH);
        this.argumentList.set(0, this.argument0 = argument0);
    }

    @Override
    public void run(Sender<SS> sender, String[] arguments) {
        if (arguments.length < ARGUMENT_LENGTH) {
            String name = this.getInfo().getName();
            sender.sendMessage(Component.text(name));
            sender.sendMessage(Component.text("Arguments:"));
            for (Argument<?, S> argument : this.argumentList) {
                sender.sendMessage(Component.text()
                        .append(argument.getNameFormatted())
                        .append(Component.text(" - "))
                        .append(argument.getInfo().getDescription())
                        .build());
            }
            return;
        }

        AbstractCommand.run0(this, sender, arguments, castedSender -> {
            ArgumentParseResult<A0, S> argument0ParseResult = this.argument0.parse(arguments[0]);
            Preconditions.checkNotNull(argument0ParseResult);

            if (argument0ParseResult.isFail()) {
                Consumer<? super S> failHandler = argument0ParseResult.getFailHandler();

                Preconditions.checkNotNull(failHandler);

                failHandler.accept(castedSender);
                return;
            }
            this.execute(castedSender, argument0ParseResult.getParsedObject());
        });
    }

    protected abstract void execute(S sender, A0 argument) throws Exception;
}
