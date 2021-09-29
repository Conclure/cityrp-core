package me.conclure.cityrp.paper.sender;

import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.sender.SenderManager;
import me.conclure.cityrp.common.sender.SenderTranformer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitSender<BukkitPlatformSender extends CommandSender> implements Sender<BukkitPlatformSender> {
    private final BukkitPlatformSender sender;

    BukkitSender(BukkitPlatformSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(Component component) {
        this.sender.sendMessage(component);
    }

    @Override
    public BukkitPlatformSender delegate() {
        return this.sender;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    public static class Transformer
            implements SenderTranformer<CommandSender,Sender<? extends CommandSender>> {
        private final SenderManager<CommandSender> senderManager;

        public Transformer(SenderManager<CommandSender> senderManager) {
            this.senderManager = senderManager;
        }

        @Override
        public Sender<? extends CommandSender> tranform(CommandSender bukkitPlatformSender) {
            if (bukkitPlatformSender instanceof Player player) {
                return new BukkitPlayerSender<>(player, this.senderManager);
            }
            return new BukkitSender<>(bukkitPlatformSender);
        }
    }
}
