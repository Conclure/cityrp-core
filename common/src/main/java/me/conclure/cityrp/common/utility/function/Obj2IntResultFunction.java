package me.conclure.cityrp.common.utility.function;

@FunctionalInterface
public interface Obj2IntResultFunction<T> {
    int apply(T t);
}
