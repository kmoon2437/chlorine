package kr.choyunjin.chlorine;

import java.util.List;
import java.io.InputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.tomlj.Toml;
import kr.choyunjin.commands.CommandManager;
import kr.choyunjin.commands.exceptions.NoPermissionException;
import kr.choyunjin.commands.exceptions.NotEnoughArgumentsException;
import kr.choyunjin.chlorine.i18n.I18n;
import kr.choyunjin.chlorine.listeners.*;
import kr.choyunjin.chlorine.models.PlayerMap;
import kr.choyunjin.chlorine.commands.*;
import kr.choyunjin.chlorine.exceptions.AdventureComponentException;

public class Chlorine extends JavaPlugin {
    private CommandManager commandManager;
    private I18n i18n;
    private PlayerMap playerMap;

    @Override
    public void onEnable() {
        // 로그 남기는 용도
        Logger logger = this.getSLF4JLogger();

        // 설정파일 생성
        this.saveDefaultConfig();
        
        String langName = this.getConfig().getString("lang");
        StringBuilder langFileName = new StringBuilder("translation_");
        langFileName.append(langName);
        langFileName.append(".toml");

        // 번역 파일 세팅
        try {
            InputStream langFileData = this.getResource(langFileName.toString());
            if (langFileData == null) {
                logger.warn("Language \"{}\" not found, falling back to \"en\" (English)", langName);
                langFileData = this.getResource("translation_en.toml");
            }
            this.i18n = new I18n(Toml.parse(langFileData));
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
            new TellCommand(this.i18n),
            new TPACommand(this, this.i18n)
        );
    }

    @SuppressWarnings("finally")
    @Override
    public boolean onCommand(CommandSender sender, Command ctx, String label, String[] args) {
        boolean result = true;
        try {
            result = this.commandManager.onCommand(sender, ctx, label, args);
        } catch (NoPermissionException e) {
            sender.sendMessage(i18n.tl("command.noPermission"));
        } catch (NotEnoughArgumentsException e) {
            sender.sendMessage(i18n.tl("command.notEnoughArguments"));
            e.getCommand().showHelp(sender);
        } catch (AdventureComponentException e) {
            sender.sendMessage(e.getMessageComponent());
        } catch (Exception e) {
            sender.sendMessage(i18n.tl("command.error"));
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command ctx, String label, String[] args) {
        return this.commandManager.onTabComplete(sender, ctx, label, args);
    }

    public PlayerMap players() {
        return this.playerMap;
    }
}