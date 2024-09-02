package kr.choyunjin.chlorine.textrenderers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * 추후 번역 쪽에서 형식을 바꿀 수 있도록 할 예정
 */
public class WhisperRenderer {
    public Component render(String from, Component to, String message) {
        return this.render(Component.text(from), to, message);
    }

    public Component render(Component from, String to, String message) {
        return this.render(from, Component.text(to), message);
    }

    public Component render(Component from, Component to, String message) {
        return Component.text("[").color(NamedTextColor.AQUA)
            .append(Component.text("").color(NamedTextColor.WHITE).append(from))
            .append(Component.text(" -> ").color(NamedTextColor.YELLOW))
            .append(Component.text("").color(NamedTextColor.WHITE).append(to))
            .append(Component.text("]").color(NamedTextColor.AQUA))
            .append(Component.text(" > ").color(NamedTextColor.GRAY))
            .append(Component.text(message).color(NamedTextColor.WHITE));
    }
}