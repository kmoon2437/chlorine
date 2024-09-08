package kr.choyunjin.chlorine.textrenderers;

import org.bukkit.entity.Player;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import kr.choyunjin.chlorine.Chlorine;

public class ChatMessageRenderer implements ChatRenderer, ChatRenderer.ViewerUnaware {
    private String format;

    public ChatMessageRenderer(Chlorine cl) {
        this.format = cl.getConfig().getString("chatFormats.default");
    }

    @Override
    public Component render(Player source, Component sourceDisplayName, Component message, Audience viewer) {
        return this.render(source, sourceDisplayName, message);
    }

    @Override
    public Component render(Player source, Component sourceDisplayName, Component message) {
        return MiniMessage.miniMessage().deserialize(
            this.format,
            Placeholder.component("name", sourceDisplayName),
            Placeholder.component("message", message)
        );
    }
}