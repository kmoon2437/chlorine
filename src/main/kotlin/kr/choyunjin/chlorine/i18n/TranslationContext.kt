package kr.choyunjin.chlorine.i18n

import java.util.ArrayList
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

class TranslationContext {
    val params: ArrayList<TagResolver> = ArrayList()

    fun param(name: String, componentValue: Component) {
        this.params.add(Placeholder.component(name, componentValue))
    }

    fun param(name: String, stringValue: String) {
        this.params.add(Placeholder.unparsed(name, stringValue))
    }
}