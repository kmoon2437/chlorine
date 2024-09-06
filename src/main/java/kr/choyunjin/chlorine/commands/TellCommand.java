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
import kr.choyunjin.chlorine.textrenderers.WhisperRenderer;
import kr.choyunjin.chlorine.i18n.I18n;
import kr.choyunjin.chlorine.exceptions.AdventureComponentException;

public class TellCommand extends BaseCommand {
    private I18n i18n;
    private WhisperRenderer renderer;

    public TellCommand(I18n i18n) {
        super("tell", "w", "msg");
        permission("chlorine.command.tell");

        this.i18n = i18n;
        this.renderer = new WhisperRenderer();
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
    
        sender.sendMessage(this.renderer.render(i18n.tl("command.tell.you"), receiver.displayName(), message));
        receiver.sendMessage(this.renderer.render(sender.displayName(), i18n.tl("command.tell.you"), message));
    }

    @Override
    public List<String> getTabCompleteOptions(Server server, CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            return getAllPlayerNames(server);
        }
        return Collections.emptyList();
    }
}