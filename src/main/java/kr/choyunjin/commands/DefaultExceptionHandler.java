package kr.choyunjin.commands;

import org.bukkit.command.CommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class DefaultExceptionHandler implements ExceptionHandler {
    public void handleNoPermission(CommandSender sender) {
        sender.sendMessage(Component.text("You don't have permission for running this command.").color(NamedTextColor.RED));
    }

    public void handleOtherException(CommandSender sender) {
        sender.sendMessage(Component.text("Error occured during executing command. Please contact the server administrator.").color(NamedTextColor.RED));
    }
}