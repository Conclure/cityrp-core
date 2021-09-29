package me.conclure.cityrp.common.sender;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.reflect.TypeToken;

public class SimpleSenderTranformerRegistry implements SenderTransformRegistry {
    private final ImmutableTable<Class<?>,TypeToken<? extends Sender<?>>,SenderTranformer<?,?>> map;

    public SimpleSenderTranformerRegistry(ImmutableTable<Class<?>,TypeToken<? extends Sender<?>>,SenderTranformer<?,?>> map) {
        this.map = map;
    }

    private record SenderTransformerKey<PlatformSender, S extends Sender<PlatformSender>>(
            Class<PlatformSender> platformSenderType,
            TypeToken<S> senderTypeToken
    ) {}

    @Override
    public <PlatformSender, S extends Sender<? extends PlatformSender>> SenderTranformer<PlatformSender, S> get(
            Class<PlatformSender> platformSenderType,
            TypeToken<S> senderTypeToken
    ) {
        return (SenderTranformer<PlatformSender, S>) this.map.get(platformSenderType,senderTypeToken);
    }

    public static class Builder {
        private final ImmutableTable.Builder<Class<?>,TypeToken<? extends Sender<?>>,SenderTranformer<?,?>> builder;

        public Builder() {
            this.builder = ImmutableTable.builder();
        }

        public <PlatformSender, S extends Sender<? extends PlatformSender>> Builder add(
                Class<PlatformSender> platformSenderType,
                TypeToken<S> senderTypeToken,
                SenderTranformer<PlatformSender, S> senderTranformer
        ) {
            this.builder.put(platformSenderType,senderTypeToken,senderTranformer);
            return this;
        }

        public SimpleSenderTranformerRegistry build() {
            return new SimpleSenderTranformerRegistry(this.builder.build());
        }
    }
}
