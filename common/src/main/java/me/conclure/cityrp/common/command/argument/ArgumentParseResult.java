package me.conclure.cityrp.common.command.argument;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.common.sender.Sender;
import org.jspecify.nullness.Nullable;

import java.util.function.Consumer;

public final class ArgumentParseResult<T, S extends Sender<PlatformSender>, PlatformSender> {
    @Nullable
    private final T parsedObject;
    private final boolean isFail;
    @Nullable
    private final Consumer<? super S> failHandler;

    private ArgumentParseResult(
            @Nullable T parsedObject,
            boolean isFail,
            @Nullable Consumer<? super S> failHandler
    ) {
        this.parsedObject = parsedObject;
        this.isFail = isFail;
        this.failHandler = failHandler;
    }

    public static <T, S extends Sender<SS>,SS> ArgumentParseResult<T, S, SS> success(T parsedObject) {
        Preconditions.checkNotNull(parsedObject);

        return new ArgumentParseResult<>(parsedObject, false, null);
    }

    public static <T, S extends Sender<SS>,SS> ArgumentParseResult<T, S, SS> fail(Consumer<? super S> failHandler) {
        Preconditions.checkNotNull(failHandler);

        return new ArgumentParseResult<>(null, true, failHandler);
    }

    public boolean isFail() {
        return this.isFail;
    }

    public boolean isSuccess() {
        return !this.isFail;
    }

    @Nullable
    public T getParsedObject() {
        return this.parsedObject;
    }

    @Nullable
    public Consumer<? super S> getFailHandler() {
        return this.failHandler;
    }
}
