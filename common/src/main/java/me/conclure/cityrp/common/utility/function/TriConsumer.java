package me.conclure.cityrp.common.utility.function;

@FunctionalInterface
public interface TriConsumer<T, T0, T1> {
    void accept(T t, T0 t0, T1 t1);
}
