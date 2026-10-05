package me.artic.reassembly.utils;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.persistence.PersistentDataContainerView;
import lombok.experimental.UtilityClass;
import me.artic.reassembly.object.ItemWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class CustomItem {
    private static final ItemStack PORTAL_WAND_ITEM;
    public static final ItemWrapper PORTAL_WAND;

    private static final ItemStack BLOCK_GRAFT_ITEM;
    public static final ItemWrapper BLOCK_GRAFT;

    static {
        PORTAL_WAND_ITEM = ItemStack.of(Material.STICK);
        PORTAL_WAND_ITEM.setData(DataComponentTypes.CUSTOM_NAME, component("<red>Portal Maker"));
        PORTAL_WAND_ITEM.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        PORTAL_WAND_ITEM.setData(DataComponentTypes.LORE, lore("<gray>Click to", "<gray>make portals"));

        PORTAL_WAND = new ItemWrapper(PORTAL_WAND_ITEM,  "portal_wand");
        assignId(PORTAL_WAND);


        BLOCK_GRAFT_ITEM = ItemStack.of(Material.HEART_OF_THE_SEA);
        BLOCK_GRAFT_ITEM.setData(DataComponentTypes.CUSTOM_NAME, component("<red>Block Graft"));
        BLOCK_GRAFT_ITEM.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        BLOCK_GRAFT_ITEM.setData(DataComponentTypes.LORE, lore("<gray>Click to", "<gray>graft a block"));

        BLOCK_GRAFT = new ItemWrapper(BLOCK_GRAFT_ITEM, "block_graft");
        assignId(BLOCK_GRAFT);
    }

    private static Component component(String text) {
        return MiniMessage.miniMessage().deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    private static ItemLore lore(String... lore) {
        List<Component> components = Arrays.stream(lore)
                .map(CustomItem::component)
                .toList();

        return ItemLore.lore().addLines(components).build();
    }

    private static void assignId(ItemStack item, String id) {
        item.editPersistentDataContainer(pdc -> {
            pdc.set(PluginKeys.CUSTOM_ITEM_ID, PersistentDataType.STRING, id);
        });
    }

    private static void assignId(ItemWrapper itemWrapper) {
        assignId(itemWrapper.item(), itemWrapper.id());
    }

    @SuppressWarnings("all")
    public static Optional<String> getItemId(ItemStack item) {
        PersistentDataContainerView pdc = item.getPersistentDataContainer();
        if(pdc == null) return Optional.empty();  // Se non c'è il PDC ritorno il vuoto

        if(pdc.has(PluginKeys.CUSTOM_ITEM_ID, PersistentDataType.STRING)) { // Se nel PDC c'è la voce assegnata
            return Optional.ofNullable(pdc.get(PluginKeys.CUSTOM_ITEM_ID, PersistentDataType.STRING)); // Ritorno il contenuto della voce
        }

        return Optional.empty(); // Altrimenti ritorno il vuoto
    }
}