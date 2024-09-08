package kr.choyunjin.chlorine.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import kr.choyunjin.chlorine.Chlorine;
import kr.choyunjin.chlorine.textrenderers.JoinQuitMessageRenderer;

public class PlayerJoinQuitListener implements Listener {
    private JoinQuitMessageRenderer renderer;

    public PlayerJoinQuitListener(Chlorine cl) {
        this.renderer = new JoinQuitMessageRenderer(cl);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(this.renderer.renderJoinMessage(player, player.getServer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage(this.renderer.renderQuitMessage(player, player.getServer()));
    }
}