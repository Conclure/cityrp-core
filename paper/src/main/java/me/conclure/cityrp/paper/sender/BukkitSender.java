package me.conclure.cityrp.paper.sender;

import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.sender.SenderTranformer;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitSender<BukkitPlatformSender extends CommandSender> implements Sender {
    private final BukkitPlatformSender sender;

    BukkitSender(BukkitPlatformSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(Component component) {
        this.sender.sendMessage(component);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    public static class Transformer implements SenderTranformer<CommandSender,Sender> {
        private final BukkitAudiences audiences;

        public Transformer(BukkitAudiences audiences) {
            this.audiences = audiences;
        }

        @Override
        public Sender tranform(CommandSender bukkitPlatformSender) {
            if (bukkitPlatformSender instanceof Player player) {
                return new BukkitPlayerSender(player, this.audiences);
            }
            return new BukkitSender<>(bukkitPlatformSender);
        }
    }
}
