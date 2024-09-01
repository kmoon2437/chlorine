package kr.choyunjin.chlorine.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.text.Component;
import kr.choyunjin.chlorine.Chlorine;
import kr.choyunjin.chlorine.textrenderers.ChatMessageRenderer;

public class ChatListener implements Listener {
    private Chlorine chlorine;
    private ChatMessageRenderer renderer;

    public ChatListener(Chlorine chlorine) {
        this.chlorine = chlorine;
        this.renderer = new ChatMessageRenderer();
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = false
    )
    public void onChat(AsyncChatEvent event) {
        if (this.chlorine.getConfig().getBoolean("rewrite-chat")) {
            // 채팅 이벤트를 취소함
            event.setCancelled(true);

            Player source = event.getPlayer();
            Component message = this.renderer.render(source, source.displayName(), event.message());
            event.getPlayer().getServer().broadcast(message);
        } else {
            event.renderer(renderer);
        }
    }
}