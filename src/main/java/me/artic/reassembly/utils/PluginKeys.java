package me.artic.reassembly.utils;

import lombok.experimental.UtilityClass;
import me.artic.reassembly.Reassembly;
import org.bukkit.NamespacedKey;

@UtilityClass
public class PluginKeys {
    private static final Reassembly plugin = Reassembly.getInstance();

    public static final NamespacedKey CUSTOM_ITEM_ID = new NamespacedKey(plugin, "CUSTOM_ITEM_ID");
    public static final NamespacedKey CURRENT_GRAFT = new NamespacedKey(plugin, "CURRENT_GRAFT");
}
