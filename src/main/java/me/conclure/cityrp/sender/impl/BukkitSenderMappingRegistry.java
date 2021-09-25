package me.conclure.cityrp.sender.impl;

import me.conclure.cityrp.sender.Sender;
import me.conclure.cityrp.sender.SenderMappingRegistry;
import me.conclure.cityrp.sender.SenderManager.Transformer;
import org.bukkit.command.CommandSender;
import org.jspecify.nullness.Nullable;

import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.function.Function;

public class BukkitSenderMappingRegistry implements SenderMappingRegistry<CommandSender> {
    private final IdentityHashMap<Class<? extends CommandSender>, Transformer<CommandSender,Sender<CommandSender>>> map;

    public BukkitSenderMappingRegistry() {
        this.map = new IdentityHashMap<>();
    }

    @Override
    public <T extends CommandSender, R extends Sender<T>> void add(Class<T> type, Transformer<T, R> tranformer) {
        this.map.put(type, (Transformer<CommandSender, Sender<CommandSender>>) tranformer);
    }

    @Override
    public @Nullable <T extends CommandSender, R extends Sender<T>> Transformer<T, R> getTransformer(Class<? extends T> type) {
        return (Transformer<T, R>) this.map.get(type);
    }

    @Override
    public <T extends CommandSender, R extends Sender<T>> @Nullable R get(Class<? extends T> type, T sender) {
        Transformer<T,R> transformer = this.getTransformer(type);

        if (transformer == null) {
            return null;
        }

        return transformer.transform(sender);
    }
}
