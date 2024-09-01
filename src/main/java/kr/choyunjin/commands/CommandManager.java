package kr.choyunjin.commands;

import java.util.ArrayList;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;

public class CommandManager implements Listener {
    private Plugin plugin;
    private ArrayList<CommandWrapper> commands;

    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerSendCommandsEvent(AsyncPlayerSendCommandsEvent<?> event) {
        for (CommandWrapper command : this.commands) {
            command.applyCommandNode(event.getPlayer(), event.getCommandNode());
        }
    }

    public void run() {}
}