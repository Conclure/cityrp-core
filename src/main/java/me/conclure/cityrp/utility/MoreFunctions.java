package me.conclure.cityrp.utility;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class MoreFunctions {
    public static final Consumer EMPTY_CONSUMER = o -> {};
    public static final BiConsumer EMPTY_BI_CONSUMER = (o0,o1) -> {};

    public static <T> Consumer<T> emptyConsumer() {
        return EMPTY_CONSUMER;
    }

    public static <T,U> BiConsumer<T,U> emptyBiConsumer() {
        return EMPTY_BI_CONSUMER;
    }


}
