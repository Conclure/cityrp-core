package me.conclure.cityrp.common.sender;

@FunctionalInterface
public interface SenderTransformer<PlatformSender, S extends Sender> {
    S tranform(PlatformSender sender);
}
