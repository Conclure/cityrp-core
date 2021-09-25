package me.conclure.cityrp.sender;

import me.conclure.cityrp.sender.SenderManager.Transformer;
import org.jspecify.nullness.Nullable;

public interface SenderMappingRegistry<SS> {
    <T extends SS,R extends Sender<T>> void add(Class<T> type, Transformer<T,R> tranformer);

    @Nullable
    <T extends SS,R extends Sender<T>> Transformer<T,R> getTransformer(Class<? extends T> type);

    @Nullable
    <T extends SS,R extends Sender<T>> R get(Class<? extends T> type, T sender);
}
