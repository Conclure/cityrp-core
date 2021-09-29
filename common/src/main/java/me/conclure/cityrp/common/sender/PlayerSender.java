package me.conclure.cityrp.common.sender;

public interface PlayerSender<PlatformPlayer>
        extends Sender<PlatformPlayer>,
        TitleSender<PlatformPlayer>,
        ActionBarSender<PlatformPlayer>,
        BossBarSender<PlatformPlayer>,
        BookSender<PlatformPlayer> {
}
