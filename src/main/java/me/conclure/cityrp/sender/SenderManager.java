package me.conclure.cityrp.sender;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public interface SenderManager<SS> {
    SenderMappingRegistry<SS> getRegistry();

    void message(Sender<? extends SS> sender, Component component);

    void title(TitleSender<? extends SS> sender, Title title);

    void actionBar(ActionBarSender<? extends SS> sender, Component component);

    Sender<SS> asSender(SS sender);

    <R extends Sender<SS>> R asSender(SS sender, Transformer<SS,R> transformer);

    @FunctionalInterface
    interface Transformer<S,R extends Sender<S>> {
        R transform(S sender);
    }
}
