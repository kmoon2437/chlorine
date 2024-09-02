package kr.choyunjin.commands;

import org.bukkit.command.CommandSender;

public interface ExceptionHandler {
    void handleNoPermission(CommandSender sender);
    void handleOtherException(CommandSender sender);
}