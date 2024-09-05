package kr.choyunjin.chlorine;

import java.io.IOException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.tomlj.Toml;
import kr.choyunjin.commands.CommandManager;
import kr.choyunjin.commands.exceptions.CommandException;
import kr.choyunjin.commands.exceptions.NoPermissionException;
import kr.choyunjin.commands.exceptions.NotEnoughArgumentsException;
import kr.choyunjin.chlorine.i18n.I18n;
import kr.choyunjin.chlorine.listeners.*;
import kr.choyunjin.chlorine.commands.*;

public class Chlorine extends JavaPlugin {
    private CommandManager commandManager;
    private I18n i18n;

    @Override
    public void onEnable() {
        // 설정파일 생성
        this.saveDefaultConfig();

        // 번역 파일 세팅
        try {
            this.i18n = new I18n(Toml.parse(this.getResource("translation_ko.toml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // event listener 추가
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ChatListener(this), this);
        pluginManager.registerEvents(new PlayerJoinQuitListener(), this);

        // 명령어 세팅
        this.commandManager = new CommandManager(this);
        this.commandManager.registerCommands(
            new TellCommand(this.i18n)
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command ctx, String label, String[] args) {
        boolean result = true;
        try {
            result = this.commandManager.onCommand(sender, ctx, label, args);
        } catch (NoPermissionException e) {
            sender.sendMessage(i18n.tl("command.noPermission").color(NamedTextColor.RED));
        } catch (NotEnoughArgumentsException e) {
            sender.sendMessage(i18n.tl("command.notEnoughArguments").color(NamedTextColor.RED));
            e.getCommand().showHelp(sender);
        } catch (CommandException e) {
            sender.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
        } catch (Exception e) {
            sender.sendMessage(i18n.tl("command.error").color(NamedTextColor.RED));
            e.printStackTrace();
        } finally {
            return result;
        }
    }
}