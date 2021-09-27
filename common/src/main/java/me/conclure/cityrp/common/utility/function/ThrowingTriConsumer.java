package me.conclure.cityrp.common.utility.function;

@FunctionalInterface
public interface ThrowingTriConsumer<T, T0, T1, E extends Exception> {
    void accept(T t, T0 t0, T1 t1) throws E;
}
