package kr.choyunjin.chlorine;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import kr.choyunjin.chlorine.listeners.*;
import kr.choyunjin.chlorine.commands.*;

public class Chlorine extends JavaPlugin {
    @Override
    public void onEnable() {
        // 설정파일 생성
        this.saveDefaultConfig();

        // event listener 추가
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ChatListener(this), this);
        pluginManager.registerEvents(new PlayerJoinQuitListener(), this);

        // 명령어 세팅
        // ...
    }
}