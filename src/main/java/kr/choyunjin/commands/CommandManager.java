package kr.choyunjin.commands;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import kr.choyunjin.commands.exceptions.NoPermissionException;

// 일단 지금은 runtime에서 annotation을 처리하고 있긴 한데
// 추후 compile time에 처리하도록 바꿀 예정
public class CommandManager implements Listener {
    private JavaPlugin plugin;
    private ArrayList<BaseCommand> commands;
    private HashMap<String, Integer> commandsIndex;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.commands = new ArrayList<>();
        this.commandsIndex = new HashMap<>();
    }

    public void registerCommands(BaseCommand... commands) {
        for (BaseCommand command : commands) {
            this.commands.add(command);
        }
        if (CommodoreProvider.isSupported()) {
            Commodore commodore = CommodoreProvider.getCommodore(plugin);
            for (BaseCommand command : commands) {
                commodore.register(this.plugin.getCommand(command.name()), command.getCommandNode());
            }
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
    public void onAsyncTabComplete(AsyncTabCompleteEvent event) {
        if (!event.isCommand()) return;

        String buf = event.getBuffer();
        if (buf.isEmpty()) return;

        if (buf.charAt(0) == '/') {
            buf = buf.substring(1);
        }

        int firstSpace = buf.indexOf(' ');
        if (firstSpace < 0) return;

        String label = buf.substring(0, firstSpace);
        BaseCommand command = this.getCommand(label);
        if (command == null) return;
        String[] args = buf.substring(firstSpace + 1).split(" ");

        event.setCompletions(this.onTabComplete(event.getSender(), null, label, args));
        event.setHandled(true);
    }

    private BaseCommand getCommand(String label) {
        Integer index = this.commandsIndex.get(label);
        if (index == null) return null;
        return this.commands.get(index);
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