package com.kiocg.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class KiocgComponents {
    private KiocgComponents() {
        throw new RuntimeException("KiocgComponents is not to be instantiated!");
    }

    public static TextComponent normalPrefix() {
        return Component.empty()
                        .append(Component.text("[", NamedTextColor.GREEN))
                        .append(Component.text("豆渣子", NamedTextColor.AQUA))
                        .append(Component.text("]", NamedTextColor.GREEN))
                        .append(Component.space());
    }
}
