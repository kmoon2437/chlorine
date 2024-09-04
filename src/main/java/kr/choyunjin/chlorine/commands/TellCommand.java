package kr.choyunjin.chlorine.commands;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import kr.choyunjin.commands.BaseCommand;
import kr.choyunjin.commands.DeclareCommand;
import kr.choyunjin.chlorine.textrenderers.WhisperRenderer;

@DeclareCommand(
    name = "tell",
    aliases = { "w", "msg" }
)
public class TellCommand extends BaseCommand {
    private WhisperRenderer renderer;

    public TellCommand() {
        this.renderer = new WhisperRenderer();
    }

    @Override
    public void run(Server server, Player sender, String label, String[] args) throws Exception {
        if (!sender.hasPermission("chlorine.command.tell")) {
            throw new NoPermissionException();
        }

        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }

        Player receiver = server.getPlayer(args[1]);
        if (receiver == null) {
            throw new CommandException("No such player exists");
        }

        String message = this.getGreedyString(args, 1);
    
        sender.sendMessage(this.renderer.render("나", receiver.displayName(), message));
        receiver.sendMessage(this.renderer.render(sender.displayName(), "나", message));
    }
}