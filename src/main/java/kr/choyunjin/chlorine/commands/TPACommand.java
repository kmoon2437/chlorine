package kr.choyunjin.chlorine.commands;

import java.util.List;
import java.util.Collections;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import com.mojang.brigadier.tree.LiteralCommandNode;
import kr.choyunjin.commands.BaseCommand;
import kr.choyunjin.commands.CommandNodeBuilder;
import kr.choyunjin.commands.exceptions.NotEnoughArgumentsException;
import kr.choyunjin.chlorine.Chlorine;
import kr.choyunjin.chlorine.i18n.I18n;
import kr.choyunjin.chlorine.models.ChlorinePlayer;
import kr.choyunjin.chlorine.exceptions.AdventureComponentException;

public class TPACommand extends BaseCommand {
    private Chlorine cl;
    private I18n i18n;

    public TPACommand(Chlorine cl, I18n i18n) {
        super("tpa");
        permission("chlorine.command.tpa");

        this.cl = cl;
        this.i18n = i18n;
    }

    @Override
    public LiteralCommandNode<?> generateCommandNode(CommandNodeBuilder b) {
        return b.literal(this.name())
        .then(
            b.argument("player", b.wordArg())
        ).build();
    }

    @Override
    public void run(Server server, Player sender, String label, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException(this);
        }

        ChlorinePlayer receiver = this.cl.players().getPlayer(args[0]);
        if (receiver == null) {
            throw new AdventureComponentException(i18n.tl("general.playerNotFound"));
        }

        receiver.addTPARequest(sender.getUniqueId());
        receiver.sendMessage(i18n.tl("command.tpa.tpaReceived", i18n.param("player", sender.displayName())));
        receiver.sendMessage(i18n.tl("command.tpa.acceptOrDeny"));

        sender.sendMessage(i18n.tl("command.tpa.requestSent", i18n.param("player", receiver.displayName())));
        sender.sendMessage(i18n.tl("command.tpa.howToCancel"));
    }

    @Override
    public List<String> getTabCompleteOptions(Server server, CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            return getAllPlayerNames(server);
        }
        return Collections.emptyList();
    }
}