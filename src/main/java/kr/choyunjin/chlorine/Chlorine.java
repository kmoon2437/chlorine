package kr.choyunjin.chlorine;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import kr.choyunjin.chlorine.listeners.*;
import kr.choyunjin.chlorine.commands.*;
import kr.choyunjin.commands.CommandManager;

public class Chlorine extends JavaPlugin {
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        // 설정파일 생성
        this.saveDefaultConfig();

        // event listener 추가
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ChatListener(this), this);
        pluginManager.registerEvents(new PlayerJoinQuitListener(), this);

        // 명령어 세팅
        this.commandManager = new CommandManager(this);
        this.commandManager.registerCommands(
            new TellCommand()
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command ctx, String label, String[] args) {
        return this.commandManager.onCommand(sender, ctx, label, args);
    }
}