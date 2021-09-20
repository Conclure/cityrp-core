package me.conclure.cityrp.utility.function;

@FunctionalInterface
public interface Obj2IntResultFunction<T> {
    int apply(T t);
}
