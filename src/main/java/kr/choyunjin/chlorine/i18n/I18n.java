package kr.choyunjin.chlorine.i18n;

import java.io.InputStream;
import java.io.IOException;
import org.tomlj.TomlParseResult;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class I18n {
    private MiniMessage mm;
    private TomlParseResult data;
    
    public I18n(TomlParseResult data) {
        this.mm = MiniMessage.miniMessage();
        this.data = data;
    }
    
    public Component tl(String key, TagResolver... params) {
        return this.mm.deserialize(this.data.getString(key), params);
    }

    // minimessage 문법이 없는 경우 사용
    public String tlraw(String key, TagResolver... params) {
        return this.mm.serialize(this.tl(key, params));
    }
    
    public TagResolver param(String name, Component value) {
        return Placeholder.component(name, value);
    }

    public TagResolver param(String name, String value) {
        return Placeholder.unparsed(name, value);
    }
}