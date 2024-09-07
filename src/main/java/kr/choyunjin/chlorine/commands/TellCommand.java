package kr.choyunjin.chlorine.commands;

import java.util.List;
import java.util.Collections;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import com.mojang.brigadier.tree.CommandNode;
import kr.choyunjin.commands.BaseCommand;
import kr.choyunjin.commands.CommandNodeBuilder;
import kr.choyunjin.commands.exceptions.NotEnoughArgumentsException;
import kr.choyunjin.chlorine.i18n.I18n;
import kr.choyunjin.chlorine.exceptions.AdventureComponentException;

public class TellCommand extends BaseCommand {
    private I18n i18n;

    public TellCommand(I18n i18n) {
        super("tell", "w", "msg");
        permission("chlorine.command.tell");

        this.i18n = i18n;
    }

    @Override
    public CommandNode<?> generateCommandNode(CommandNodeBuilder b) {
        return b.literal(this.name())
        .then(
            b.argument("player", b.wordArg())
            .then(
                b.argument("message", b.greedyStringArg())
            )
        ).build();
    }

    @Override
    public void run(Server server, Player sender, String label, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException(this);
        }

        Player receiver = server.getPlayer(args[0]);
        if (receiver == null) {
            throw new AdventureComponentException(i18n.tl("general.playerNotFound"));
        }

        String message = this.getGreedyString(args, 1);
    
        sender.sendMessage(i18n.tl(
            "command.tell.sent",
            i18n.param("player", receiver.displayName()),
            i18n.param("message", message)
        ));
        receiver.sendMessage(i18n.tl(
            "command.tell.received",
            i18n.param("player", sender.displayName()),
            i18n.param("message", message)
        ));
    }

    @Override
    public List<String> getTabCompleteOptions(Server server, CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            return getAllPlayerNames(server);
        }
        return Collections.emptyList();
    }
}