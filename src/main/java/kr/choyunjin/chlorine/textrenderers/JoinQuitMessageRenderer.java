package kr.choyunjin.chlorine.textrenderers;

import org.bukkit.entity.Player;
import org.bukkit.Server;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * 추후 config.yml에서 형식을 바꿀 수 있도록 할 예정
 */
public class JoinQuitMessageRenderer {
    private Component renderPlayersCount(Server server, int playersCountAdjustment) {
        return Component.text("[").color(NamedTextColor.AQUA)
            .append(Component.text(server.getOnlinePlayers().size() + playersCountAdjustment).color(NamedTextColor.YELLOW))
            .append(Component.text(" / ").color(NamedTextColor.WHITE))
            .append(Component.text(server.getMaxPlayers()).color(NamedTextColor.YELLOW))
            .append(Component.text("]").color(NamedTextColor.AQUA));
    }

    public Component renderJoinMessage(Player player, Server server) {
        return Component.text("+= ").color(NamedTextColor.GREEN)
            .append(Component.text("").color(NamedTextColor.WHITE).append(player.displayName()))
            .append(Component.text(" "))
            .append(this.renderPlayersCount(server, 0));
    }

    public Component renderQuitMessage(Player player, Server server) {
        return Component.text("-= ").color(NamedTextColor.RED)
            .append(Component.text("").color(NamedTextColor.WHITE).append(player.displayName()))
            .append(Component.text(" "))
            .append(this.renderPlayersCount(server, -1));
    }
}