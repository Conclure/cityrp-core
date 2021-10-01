package me.conclure.cityrp.common.sender;

@FunctionalInterface
public interface SenderTranformer<PlatformSender, S extends Sender> {
    S tranform(PlatformSender sender);
}
