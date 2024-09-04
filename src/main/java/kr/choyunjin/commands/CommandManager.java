package kr.choyunjin.commands;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import kr.choyunjin.commands.exceptions.NoPermissionException;

public class CommandManager implements Listener {
    private Plugin plugin;
    private ArrayList<BaseCommand> commands;
    private HashMap<String, Integer> commandsIndex;

    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.commands = new ArrayList<>();
        this.commandsIndex = new HashMap<>();
    }

    public void registerCommands(BaseCommand... commands) {
        for (BaseCommand command : commands) {
            this.commands.add(command);
        }
        this.generateIndex();
    }

    private void generateIndex() {
        /*this.commandsIndex = new HashMap<>();
        int i = 0;
        for (BaseCommand command : this.commands) {
            this.commandsIndex.put(command.name, i);
            for (String alias : command.aliases) {
                this.commandsIndex.put(alias, i);
            }
            i++;
        }*/
    }

    @EventHandler
    public void onPlayerSendCommandsEvent(AsyncPlayerSendCommandsEvent<?> event) {
        for (BaseCommand command : this.commands) {
            //command.applyCommandNode(event.getPlayer(), event.getCommandNode());
        }
    }

    private BaseCommand getCommand(String label) {
        return this.commands.get(this.commandsIndex.get(label));
    }

    public boolean onCommand(CommandSender sender, Command ctx, String label, String[] args) throws Exception {
        BaseCommand command = this.getCommand(label);
        command.run(sender.getServer(), sender, label, args);
        return true;
    }

    // todo: 추후 구현 예정
    public List<String> onTabComplete() {
        return new ArrayList<>();
    }
}