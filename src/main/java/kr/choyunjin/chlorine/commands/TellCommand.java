package kr.choyunjin.chlorine.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import kr.choyunjin.commands.annotations.Command;
import kr.choyunjin.commands.annotations.Permission;
import kr.choyunjin.commands.annotations.Default;
import kr.choyunjin.commands.annotations.arg.PlayerArg;
import kr.choyunjin.commands.annotations.arg.TextArg;
import kr.choyunjin.chlorine.textrenderers.WhisperRenderer;

@Command(
    name = "tell",
    aliases = { "w", "msg" }
)
@Permission("chlorine.command.tell")
public class TellCommand {
    private WhisperRenderer renderer;

    public TellCommand() {
        this.renderer = new WhisperRenderer();
    }

    @Default
    public void run(CommandSender sender, @PlayerArg Player receiver, @TextArg String message) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(this.renderer.render("나", receiver.displayName(), message));
            receiver.sendMessage(this.renderer.render("(Server)", "나", message));
        } else if (sender instanceof RemoteConsoleCommandSender) {
            sender.sendMessage(this.renderer.render("나", receiver.displayName(), message));
            receiver.sendMessage(this.renderer.render("(Server)", "나", message));
        } else {
            sender.sendMessage("Failed to send whisper");
        }
    }

    @Default
    public void run(Player sender, @PlayerArg Player receiver, @TextArg String message) {
        sender.sendMessage(this.renderer.render("나", receiver.displayName(), message));
        receiver.sendMessage(this.renderer.render(sender.displayName(), "나", message));
    }
}
/*
b0.literal("tell").then(b1 -> {
    b1.argument("player", b1.wordArg()).then(b2 -> {
        b2.argument("message", b2.textArg());
    });
});

b0.literal("tell", b1 -> {
    b1.argument("player", b1.wordArg(), b2 -> {
        b2.argument("message", b2.textArg());
    });
});

LiteralArgumentBuilder.literal("tell")
    .then(
        RequiredArgumentBuilder.argument("player", StringArgumentType.word())
            .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString()))
    )
.build();

literal("tell")
    .then(argument("player", word())
        .then(argument("message", greedyString()))
    )
.build();
*/