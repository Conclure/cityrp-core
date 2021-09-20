package me.conclure.cityrp.utility.function;

import me.conclure.cityrp.utility.Throwables;
import org.jspecify.nullness.Nullable;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T,E extends Exception> extends Supplier<T> {
    T getExceptionally() throws E;

    @Nullable
    @Override
    default T get() {
        try {
            return this.getExceptionally();
        } catch (Exception e) {
            throw Throwables.delegateThrow(e);
        }
    }
}
