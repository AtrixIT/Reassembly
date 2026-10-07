package me.artic.reassembly.utils;

import lombok.experimental.UtilityClass;
import me.artic.reassembly.enums.GraftType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

@UtilityClass
public class MsgUtils {
    public static Component component(String text) {
        return MiniMessage.miniMessage().deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    public static CharSequence formatGraft(GraftType graftType) {
        return graftType.toString().replace("_", " ");
    }
}
