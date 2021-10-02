package me.conclure.cityrp.common.sender;

import com.google.common.collect.ImmutableTable;
import com.google.common.reflect.TypeToken;

public class SimpleSenderTransformerRegistry implements SenderTransformerRegistry {
    private final ImmutableTable<Class<?>, TypeToken<? extends Sender>, SenderTransformer<?, ?>> map;

    public SimpleSenderTransformerRegistry(ImmutableTable<Class<?>, TypeToken<? extends Sender>, SenderTransformer<?, ?>> map) {
        this.map = map;
    }

    @Override
    public <PlatformSender, S extends Sender> SenderTransformer<PlatformSender, S> get(
            Class<PlatformSender> platformSenderType,
            TypeToken<S> senderTypeToken
    ) {
        return (SenderTransformer<PlatformSender, S>) this.map.get(platformSenderType, senderTypeToken);
    }

    private record SenderTransformerKey<PlatformSender, S extends Sender>(
            Class<PlatformSender> platformSenderType,
            TypeToken<S> senderTypeToken
    ) {
    }

    public static class Builder {
        private final ImmutableTable.Builder<Class<?>, TypeToken<? extends Sender>, SenderTransformer<?, ?>> builder;

        public Builder() {
            this.builder = ImmutableTable.builder();
        }

        public <PlatformSender, S extends Sender> Builder add(
                Class<PlatformSender> platformSenderType,
                TypeToken<S> senderTypeToken,
                SenderTransformer<PlatformSender, S> senderTransformer
        ) {
            this.builder.put(platformSenderType, senderTypeToken, senderTransformer);
            return this;
        }

        public SimpleSenderTransformerRegistry build() {
            return new SimpleSenderTransformerRegistry(this.builder.build());
        }
    }
}
