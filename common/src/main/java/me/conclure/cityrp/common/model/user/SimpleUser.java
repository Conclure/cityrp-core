package me.conclure.cityrp.common.model.user;

import me.conclure.cityrp.common.model.DelegatingPlayerSender;
import me.conclure.cityrp.common.sender.PlayerSender;
import me.conclure.cityrp.common.sender.SenderTranformer;
import me.conclure.cityrp.common.utility.PlayerObtainer;

import java.util.UUID;

public class SimpleUser<PlatformPlayer>
        extends DelegatingPlayerSender<PlayerSender<? extends PlatformPlayer>, PlatformPlayer>
        implements PlayerSender<PlatformPlayer> {
    private final UUID uniqueId;
    private final PlayerObtainer<PlatformPlayer> playerObtainer;
    private final SenderTranformer<PlatformPlayer,PlayerSender<? extends PlatformPlayer>> senderTranformer;

    public SimpleUser(
            UUID uniqueId,
            PlayerObtainer<PlatformPlayer> playerObtainer,
            SenderTranformer<PlatformPlayer, PlayerSender<? extends PlatformPlayer>> senderTranformer
    ) {
        this.uniqueId = uniqueId;
        this.playerObtainer = playerObtainer;
        this.senderTranformer = senderTranformer;
    }

    @Override
    protected PlayerSender<? extends PlatformPlayer> getDelegate() {
        PlatformPlayer player = this.playerObtainer.getByUniqueId(this.uniqueId);
        return this.senderTranformer.tranform(player);
    }
}
