package kr.choyunjin.chlorine.textrenderers;

import org.bukkit.entity.Player;
import org.bukkit.Server;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import kr.choyunjin.chlorine.Chlorine;

public class JoinQuitMessageRenderer {
    private String joinMsgFormat;
    private String quitMsgFormat;

    public JoinQuitMessageRenderer(Chlorine cl) {
        this.joinMsgFormat = cl.getConfig().getString("joinMessageFormats.join");
        this.quitMsgFormat = cl.getConfig().getString("joinMessageFormats.quit");
    }

    public Component renderJoinMessage(Player player, Server server) {
        return MiniMessage.miniMessage().deserialize(
            this.joinMsgFormat,
            Placeholder.component("name", player.displayName()),
            Placeholder.component("current", Component.text(server.getOnlinePlayers().size())),
            Placeholder.component("max", Component.text(server.getMaxPlayers()))
        );
    }

    public Component renderQuitMessage(Player player, Server server) {
        return MiniMessage.miniMessage().deserialize(
            this.quitMsgFormat,
            Placeholder.component("name", player.displayName()),
            Placeholder.component("current", Component.text(server.getOnlinePlayers().size() - 1)),
            Placeholder.component("max", Component.text(server.getMaxPlayers()))
        );
    }
}