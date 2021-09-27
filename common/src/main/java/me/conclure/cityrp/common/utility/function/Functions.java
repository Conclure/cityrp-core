package me.conclure.cityrp.common.utility.function;

import me.conclure.cityrp.common.utility.Unconstructable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Functions extends Unconstructable {
    public static final Consumer EMPTY_CONSUMER = o -> {
    };
    public static final BiConsumer EMPTY_BI_CONSUMER = (o0, o1) -> {
    };
    public static final Supplier NULL_SUPPLIER = () -> null;

    public static <T> Consumer<T> emptyConsumer() {
        return EMPTY_CONSUMER;
    }

    public static <T, U> BiConsumer<T, U> emptyBiConsumer() {
        return EMPTY_BI_CONSUMER;
    }

    public static <T> Supplier<T> nullSupplier() {
        return NULL_SUPPLIER;
    }
}
