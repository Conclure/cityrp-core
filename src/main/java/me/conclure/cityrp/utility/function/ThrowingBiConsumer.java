package me.conclure.cityrp.utility.function;

@FunctionalInterface
public interface ThrowingBiConsumer<T, T0, E extends Exception> {
    void accept(T t, T0 t0) throws E;
}
