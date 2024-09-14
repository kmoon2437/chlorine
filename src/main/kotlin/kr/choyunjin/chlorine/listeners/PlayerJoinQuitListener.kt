package kr.choyunjin.chlorine.listeners;

import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.entity.Player
import org.bukkit.Server
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import kr.choyunjin.chlorine.Chlorine

private fun renderFormat(format: String, player: Player, server: Server): Component {
    return MiniMessage.miniMessage().deserialize(
        format,
        Placeholder.component("name", player.displayName()),
        Placeholder.component("current", Component.text(server.onlinePlayers.size)),
        Placeholder.component("max", Component.text(server.maxPlayers))
    )
}

class PlayerJoinQuitListener(cl: Chlorine) : Listener {
    val joinMsgFormat: String;
    val quitMsgFormat: String;

    init {
        this.joinMsgFormat = cl.config.getString("joinMessageFormats.join") ?: "<green>+=</green> <name> <aqua>[</aqua><yellow><current></yellow> / <yellow><max></yellow><aqua>]</aqua>"
        this.quitMsgFormat = cl.config.getString("quitMessageFormats.join") ?: "<red>-=</red> <name> <aqua>[</aqua><yellow><current></yellow> / <yellow><max></yellow><aqua>]</aqua>"
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage(renderFormat(this.joinMsgFormat, event.player, event.player.server))
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.quitMessage(renderFormat(this.quitMsgFormat, event.player, event.player.server))
    }
}