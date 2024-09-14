package kr.choyunjin.chlorine.i18n

import org.tomlj.TomlParseResult
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

class I18n(val translations: TomlParseResult) {
    fun tl(key: String, vararg params: TagResolver): Component {
        return MiniMessage.miniMessage().deserialize(this.translations.getString(key) ?: key, *params);
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