package me.conclure.cityrp.common.sender;

import com.google.common.reflect.TypeToken;

public interface SenderTransformerRegistry {

    <PlatformSender, S extends Sender> SenderTransformer<PlatformSender, S> get(
            Class<PlatformSender> platformSenderType,
            TypeToken<S> senderTypeToken
    );

}
