package kr.choyunjin.chlorine.commands;

import org.bukkit.entity.Player;
import kr.choyunjin.commands.annotations.Command;
import kr.choyunjin.commands.annotations.Permission;
import kr.choyunjin.commands.annotations.Default;
import kr.choyunjin.commands.annotations.arg.PlayerArg;
import kr.choyunjin.commands.annotations.arg.StringArgGreedy;

@Command(
    name = "tell",
    aliases = { "w", "msg" }
)
@Permission("chlorine.command.tell")
public class TellCommand {
    @Default
    public void run(Player sender, @PlayerArg Player receiver, @StringArgGreedy String message) {
        sender.sendMessage(message);
    }
}