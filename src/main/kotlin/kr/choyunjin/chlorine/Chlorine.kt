package kr.choyunjin.chlorine

import java.io.IOException
import org.bukkit.plugin.java.JavaPlugin
import org.tomlj.Toml
import kr.choyunjin.chlorine.i18n.I18n
import kr.choyunjin.chlorine.listeners.*

class Chlorine : JavaPlugin() {
    private lateinit var i18n: I18n

    override fun onEnable() {
        // 로그 남기는 용도
        val logger = this.getSLF4JLogger()

        // 설정파일 생성
        this.saveDefaultConfig()
        
        // 번역 파일 세팅
        val langName = this.config.getString("lang")
        val langFileName = StringBuilder("translation_")
        langFileName.append(langName)
        langFileName.append(".toml")

        try {
            var langFileData = this.getResource(langFileName.toString())
            if (langFileData == null) {
                logger.warn("Language \"{}\" not found, falling back to \"en\" (English)", langName)
                langFileData = this.getResource("translation_en.toml")!!
            }
            this.i18n = I18n(Toml.parse(langFileData))
            langFileData.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        
        // event listener 추가
        this.server.pluginManager.registerEvents(ChatListener(this), this)
        this.server.pluginManager.registerEvents(PlayerJoinQuitListener(this), this)
    }
}