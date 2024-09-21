package kr.choyunjin.chlorine.i18n

import org.json.JSONObject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

private fun getValueRecursively(jsonObj: JSONObject, key: List<String>): String? {
    val obj = jsonObj.get(key.get(0))
    if (key.size > 1) {
        if (obj is JSONObject) {
            return getValueRecursively(obj as JSONObject, key.subList(1, key.size))
        } else return null
    } else if (obj is String) {
        return obj as String
    } else return null
}

class I18n(val translations: JSONObject) {
    private fun getTranslatedString(key: String): String {
        val tlValue = getValueRecursively(this.translations, key.split("."));
        return tlValue ?: key;
    }

    fun tl(key: String): Component {
        return MiniMessage.miniMessage().deserialize(this.getTranslatedString(key));
    }

    fun tl(key: String, initContext: TranslationContext.() -> Unit): Component {
        val ctx = TranslationContext()
        ctx.initContext()
        return MiniMessage.miniMessage().deserialize(this.getTranslatedString(key), *ctx.params.toTypedArray());
    }

    // minimessage 문법이 없는 경우 사용
    fun tlraw(key: String,): String {
        return MiniMessage.miniMessage().serialize(this.tl(key));
    }

    fun tlraw(key: String, initContext: TranslationContext.() -> Unit): String {
        return MiniMessage.miniMessage().serialize(this.tl(key, initContext));
    }
}