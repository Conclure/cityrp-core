package me.conclure.cityrp.sender;

public interface PlayerSender<SS>
        extends Sender<SS>,
        TitleSender<SS>,
        ActionBarSender<SS>,
        BossBarSender<SS>,
        BookSender<SS> {
}
