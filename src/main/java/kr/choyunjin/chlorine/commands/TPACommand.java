package kr.choyunjin.chlorine.commands;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import kr.choyunjin.commands.BaseCommand;
import kr.choyunjin.commands.DeclareCommand;
import kr.choyunjin.commands.Permission;
import kr.choyunjin.commands.exceptions.NotEnoughArgumentsException;
import kr.choyunjin.chlorine.i18n.I18n;
import kr.choyunjin.chlorine.exceptions.AdventureComponentException;

@DeclareCommand(name = "tpa")
@Permission("chlorine.command.tpa")
public class TPACommand extends BaseCommand {
    private I18n i18n;

    public TPACommand(I18n i18n) {
        this.i18n = i18n;
    }

    @Override
    public void run(Server server, Player sender, String label, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException(this);
        }

        Player receiver = server.getPlayer(args[0]);
        if (receiver == null) {
            throw new AdventureComponentException(i18n.tl("general.playerNotFound"));
        }
    }
}