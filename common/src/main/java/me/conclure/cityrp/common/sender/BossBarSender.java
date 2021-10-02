package me.conclure.cityrp.common.sender;

import net.kyori.adventure.bossbar.BossBar;

public interface BossBarSender {
    void showBossBar(BossBar bossBar);

    void hideBossBar(BossBar bossBar);
}
