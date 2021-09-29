package me.conclure.cityrp.common.sender;

import com.google.common.reflect.TypeToken;

public interface SenderTransformRegistry {

    <PlatformSender,S extends Sender<? extends PlatformSender>> SenderTranformer<PlatformSender,S> get(
            Class<PlatformSender> platformSenderType,
            TypeToken<S> senderTypeToken
    );

}
