package me.conclure.cityrp.common.sender;

@FunctionalInterface
public interface SenderTranformer<PlatformSender, S extends Sender<? extends PlatformSender>> {
    S tranform(PlatformSender sender);
}
