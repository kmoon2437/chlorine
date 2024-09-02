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
    private ArrayList<CommandWrapper> commands;
    private HashMap<String, Integer> commandsIndex;
    private ExceptionHandler exceptionHandler;

    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.commands = new ArrayList<>();
        this.commandsIndex = new HashMap<>();
        this.exceptionHandler = new DefaultExceptionHandler();
    }

    public void registerCommands(Object... commands) {
        for (Object command : commands) {
            this.commands.add(new CommandWrapper(command));
        }
        this.generateIndex();
    }

    private void generateIndex() {
        this.commandsIndex = new HashMap<>();
        int i = 0;
        for (CommandWrapper command : this.commands) {
            this.commandsIndex.put(command.name, i);
            for (String alias : command.aliases) {
                this.commandsIndex.put(alias, i);
            }
            i++;
        }
    }

    @EventHandler
    public void onPlayerSendCommandsEvent(AsyncPlayerSendCommandsEvent<?> event) {
        for (CommandWrapper command : this.commands) {
            command.applyCommandNode(event.getPlayer(), event.getCommandNode());
        }
    }

    private CommandWrapper getCommand(String label) {
        return this.commands.get(this.commandsIndex.get(label));
    }

    public boolean onCommand(CommandSender sender, Command ctx, String label, String[] args) {
        CommandWrapper command = this.getCommand(label);

        try {
            command.run(sender, label, args);
        } catch (NoPermissionException e) {
            this.exceptionHandler.handleNoPermission(sender);
        } catch (Exception e) {
            this.exceptionHandler.handleOtherException(sender);
            e.printStackTrace();
        }

        return true;
    }

    // todo: 추후 구현 예정
    public List<String> onTabComplete() {
        return new ArrayList<>();
    }
}