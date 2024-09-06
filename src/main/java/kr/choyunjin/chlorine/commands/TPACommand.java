package kr.choyunjin.chlorine.commands;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import com.mojang.brigadier.tree.CommandNode;
import kr.choyunjin.commands.BaseCommand;
import kr.choyunjin.commands.CommandNodeBuilder;
import kr.choyunjin.commands.DeclareCommand;
import kr.choyunjin.commands.Permission;
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
    public CommandNode<?> generateCommandNode(CommandNodeBuilder b) {
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

        sender.sendMessage(i18n.tl("command.tpa.requestSent", i18n.param("player", sender.displayName())));
        sender.sendMessage(i18n.tl("command.tpa.howToCancel"));
    }
}