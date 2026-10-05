package me.artic.reassembly.object;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public record ItemWrapper(ItemStack item, String id) {
    public void give(Player player) {
        player.give(this.item.clone());
    }
}
