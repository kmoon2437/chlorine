package kr.choyunjin.chlorine.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import kr.choyunjin.chlorine.Chlorine;
import kr.choyunjin.chlorine.textrenderers.ChatMessageRenderer;

public class ChatListener implements Listener {
    private Chlorine cl;
    private ChatMessageRenderer renderer;

    public ChatListener(Chlorine cl) {
        this.cl = cl;
        this.renderer = new ChatMessageRenderer(this.cl);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = false
    )
    public void onChat(AsyncChatEvent event) {
        if (this.cl.getConfig().getBoolean("rewriteChat")) {
            // 채팅 이벤트를 취소함
            event.setCancelled(true);

            Player source = event.getPlayer();
            Component message = this.renderer.render(source, source.displayName(), event.message());
            event.getPlayer().getServer().broadcast(message);
        } else {
            event.renderer(this.renderer);
        }
    }
}