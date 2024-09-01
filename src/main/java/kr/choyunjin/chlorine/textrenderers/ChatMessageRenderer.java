package kr.choyunjin.chlorine.textrenderers;

import org.bukkit.entity.Player;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * 추후 config.yml에서 형식을 바꿀 수 있도록 할 예정
 */
public class ChatMessageRenderer implements ChatRenderer, ChatRenderer.ViewerUnaware {
    @Override
    public Component render(Player source, Component sourceDisplayName, Component message, Audience viewer) {
        return this.render(source, sourceDisplayName, message);
    }

    @Override
    public Component render(Player source, Component sourceDisplayName, Component message) {
        return sourceDisplayName
            .append(Component.text(" > ").color(NamedTextColor.GRAY))
            .append(message);
    }
}