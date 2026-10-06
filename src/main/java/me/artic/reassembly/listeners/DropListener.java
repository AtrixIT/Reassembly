package me.artic.reassembly.listeners;

import me.artic.reassembly.utils.CustomItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class DropListener implements Listener {
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();

        if(CustomItem.getItemId(item).isEmpty()) return;

        // event.setCancelled(true);

        event.getItemDrop().remove();
    }
}
