package kr.choyunjin.chlorine.commands;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import kr.choyunjin.commands.BaseCommand;
import kr.choyunjin.commands.DeclareCommand;
import kr.choyunjin.commands.Permission;
import kr.choyunjin.chlorine.textrenderers.WhisperRenderer;
import kr.choyunjin.commands.exceptions.NotEnoughArgumentsException;
import kr.choyunjin.commands.exceptions.CommandException;
import kr.choyunjin.chlorine.i18n.I18n;

@DeclareCommand(
    name = "tell",
    aliases = { "w", "msg" }
)
@Permission("chlorine.command.tell")
public class TellCommand extends BaseCommand {
    private I18n i18n;
    private WhisperRenderer renderer;

    public TellCommand(I18n i18n) {
        this.i18n = i18n;
        this.renderer = new WhisperRenderer();
    }

    @Override
    public void run(Server server, Player sender, String label, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException(this);
        }

        Player receiver = server.getPlayer(args[0]);
        if (receiver == null) {
            throw new CommandException(i18n.tl("general.playerNotFound"));
        }

        String message = this.getGreedyString(args, 1);
    
        sender.sendMessage(this.renderer.render(i18n.tl("command.tell.you"), receiver.displayName(), message));
        receiver.sendMessage(this.renderer.render(sender.displayName(), i18n.tl("command.tell.you"), message));
    }
}