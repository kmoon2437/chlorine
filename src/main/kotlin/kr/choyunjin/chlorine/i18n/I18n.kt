package kr.choyunjin.chlorine.i18n

import org.json.JSONObject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

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

    fun tl(key: String, vararg params: TagResolver): Component {
        return MiniMessage.miniMessage().deserialize(this.getTranslatedString(key), *params);
    }

    // minimessage 문법이 없는 경우 사용
    fun tlraw(key: String, vararg params: TagResolver): String {
        return MiniMessage.miniMessage().serialize(this.tl(key, *params));
    }
    
    fun param(name: String, componentValue: Component): TagResolver {
        return Placeholder.component(name, componentValue);
    }

    fun param(name: String, stringValue: String): TagResolver {
        return Placeholder.unparsed(name, stringValue);
    }
}