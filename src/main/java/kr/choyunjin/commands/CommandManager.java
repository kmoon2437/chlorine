package kr.choyunjin.commands;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import kr.choyunjin.commands.exceptions.NoPermissionException;

// 일단 지금은 runtime에서 annotation을 처리하고 있긴 한데
// 추후 compile time에 처리하도록 바꿀 예정
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
        this.commandsIndex = new HashMap<>();
        int i = 0;
        for (BaseCommand command : this.commands) {
            this.commandsIndex.put(command.name(), i);
            for (String alias : command.aliases()) {
                this.commandsIndex.put(alias, i);
            }
            i++;
        }
    }

    @EventHandler
    public void onPlayerSendCommandsEvent(AsyncPlayerSendCommandsEvent<?> event) {
        Player sender = event.getPlayer();
        for (BaseCommand command : this.commands) {
            String permission = command.permission();
            if (permission == null || sender.hasPermission(permission)) {
                command.applyCommandNode(sender, event.getCommandNode());
            }
        }
    }

    private BaseCommand getCommand(String label) {
        return this.commands.get(this.commandsIndex.get(label));
    }

    public boolean onCommand(CommandSender sender, Command ctx, String label, String[] args) throws Exception {
        BaseCommand command = this.getCommand(label);

        String permission = command.permission();
        if (permission == null || sender.hasPermission(permission)) {
            if (sender instanceof Player) {
                command.run(sender.getServer(), (Player)sender, label, args);
            } else {
                command.run(sender.getServer(), sender, label, args);
            }
        } else {
            throw new NoPermissionException();
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command ctx, String label, String[] args) {
        BaseCommand command = this.getCommand(label);

        if (command == null) {
            return Collections.emptyList();
        }

        String permission = command.permission();
        if (permission == null || sender.hasPermission(permission)) {
            return command.getTabCompleteOptions(sender.getServer(), sender, label, args);
        } else {
            return Collections.emptyList();
        }
    }
}