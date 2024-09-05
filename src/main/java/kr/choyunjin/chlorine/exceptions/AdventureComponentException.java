package kr.choyunjin.chlorine.exceptions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.format.NamedTextColor;

public class AdventureComponentException extends Exception {
    private Component messageComponent;

    public AdventureComponentException() {
        super();
    }

    public AdventureComponentException(String message) {
        super(message);
        this.messageComponent = Component.text(message).color(NamedTextColor.RED);
    }

    public AdventureComponentException(Component message) {
        super(MiniMessage.miniMessage().serialize(message));
        this.messageComponent = message;
    }

    public Component getMessageComponent() {
        return this.messageComponent;
    }
}