package kr.choyunjin.chlorine.listeners

import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.entity.Player
import io.papermc.paper.event.player.AsyncChatEvent
import io.papermc.paper.chat.ChatRenderer
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import kr.choyunjin.chlorine.Chlorine

class ChatListener(val cl: Chlorine) : Listener, ChatRenderer, ChatRenderer.ViewerUnaware {
    val format: String

    init {
        this.format = cl.config.getString("chatFormats.default") ?: "<<name>> <message>"
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = false
    )
    fun onChat(event: AsyncChatEvent) {
        if (this.cl.config.getBoolean("rewriteChat")) {
            // 채팅 이벤트를 취소함
            event.setCancelled(true)

            val message = this.render(event.player, event.player.displayName(), event.message())
            event.player.server.broadcast(message);
        } else {
            event.renderer(this);
        }
    }

    override fun render(source: Player, sourceDisplayName: Component, message: Component, viewer: Audience): Component {
        return this.render(source, sourceDisplayName, message);
    }

    override fun render(source: Player, sourceDisplayName: Component, message: Component): Component {
        return MiniMessage.miniMessage().deserialize(
            this.format,
            Placeholder.component("name", sourceDisplayName),
            Placeholder.component("message", message)
        );
    }
}