package kr.choyunjin.chlorine.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import kr.choyunjin.commands.annotations.Command;
import kr.choyunjin.commands.annotations.Permission;
import kr.choyunjin.commands.annotations.Default;
import kr.choyunjin.commands.annotations.PlayerSender;
import kr.choyunjin.commands.annotations.arg.PlayerArg;
import kr.choyunjin.commands.annotations.arg.TextArg;

@Command(
    name = "tell",
    aliases = { "w", "msg" }
)
@Permission("chlorine.command.tell")
public class TellCommand {
    @Default
    public void run(CommandSender sender) {
        sender.sendMessage("You are CommandSender!");
    }

    @Default
    public void run(Player sender, @PlayerArg Player receiver, @TextArg String message) {
        sender.sendMessage("You are Player!");
        sender.sendMessage(message);
    }
}