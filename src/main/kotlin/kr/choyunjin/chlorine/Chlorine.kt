package kr.choyunjin.chlorine

import org.bukkit.plugin.java.JavaPlugin
import org.json.JSONObject
import org.json.JSONTokener
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
        val langFileName = "translation_${langName}.json"
        var langFileData = this.getResource(langFileName)

        if (langFileData == null) {
            logger.warn("Language \"{}\" not found, falling back to \"en\" (English)", langName)
            logger.info("Loading language \"en\"")
            langFileData = this.getResource("translation_en.json")!!
        } else {
            logger.info("Loading language \"{}\"", langName)
        }

        langFileData.use {
            this.i18n = I18n(JSONObject(JSONTokener(it)))
        }

        // event listener 추가
        this.server.pluginManager.also {
            it.registerEvents(ChatListener(this), this)
            it.registerEvents(PlayerJoinQuitListener(this), this)
        }
    }
}