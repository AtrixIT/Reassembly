package me.artic.reassembly.guis;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.artic.reassembly.Reassembly;
import me.artic.reassembly.enums.GraftType;
import me.artic.reassembly.utils.MsgUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class GraftGui {
    private final Reassembly plugin = Reassembly.getInstance();
    private Player player;
    private Window window;

    private List<Item> buttons = new ArrayList<>();

    public GraftGui(Player player) {
        this.player = player;

        String title = plugin.getMessages().getString("gui.graft.title");

        Item mobToMob = this.buildItem(Material.ZOMBIE_SPAWN_EGG, GraftType.MOB_TO_MOB);
        Item blockToBlock = this.buildItem(Material.GRASS_BLOCK, GraftType.BLOCK_TO_BLOCK);
        Item mobToBlock = this.buildItem(Material.MAGMA_CUBE_SPAWN_EGG, GraftType.MOB_TO_BLOCK);
        Item blockToMob = this.buildItem(Material.SLIME_BLOCK, GraftType.BLOCK_TO_MOB);

        this.buttons.add(mobToMob);
        this.buttons.add(blockToBlock);
        this.buttons.add(mobToBlock);
        this.buttons.add(blockToMob);

        Gui gui = Gui.builder()
                .setStructure(
                        "# # # # # # # # #",
                        "# # # # # # # # #",
                        "# 1 # 2 # 3 # 4 #",
                        "# # # # # # # # #",
                        "# # # # # # # # #"
                )
                .addIngredient('#', Item.simple(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)))
                .addIngredient('1', mobToMob)
                .addIngredient('2', blockToBlock)
                .addIngredient('3', mobToBlock)
                .addIngredient('4', blockToMob)
                .build();

        this.window = Window.builder()
                .setTitle(MsgUtils.component(title))
                .setUpperGui(gui)
                .setViewer(player)
                .build();

    }

    public void open() {
        if (this.window != null) window.open();
    }

    private Item buildItem(Material material, GraftType type) {
        return Item.builder()
                .setItemProvider(p -> {
                    GraftType currentGraft = plugin.getGraftManager().getGraftType(player);
                    boolean same = currentGraft == type;

                    String path = "gui.graft.item-name";
                    if(same) {
                        path = path + "-selected";
                    }

                    String name = plugin.getMessages().getString(path)
                            .replace("%graft%", MsgUtils.formatGraft(type));
                    Component cName = MsgUtils.component(name);

                    ItemStack item = ItemStack.of(material);
                    item.setData(DataComponentTypes.CUSTOM_NAME, cName);
                    item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, same);

                    return new ItemBuilder(item);
                })
                .addClickHandler((_, _) -> {
                    this.plugin.getGraftManager().setGraftType(player, type);
                    this.buttons.forEach(Item::notifyWindows);
                })
                .build();
    }
}
