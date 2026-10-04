package me.artic.reassembly.Listeners;

import lombok.RequiredArgsConstructor;
import me.artic.reassembly.Reassembly;
import me.artic.reassembly.object.Portal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ClickListener implements Listener {
    private final Reassembly plugin;
    private Map<Player, Location> loc1 = new HashMap<>();

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;

        if (item.getType() != Material.STICK) return;

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            Location loc = block.getLocation().clone().add(0.5, 1.5, 0.5);

            this.loc1.put(player, loc);
            player.sendRichMessage("<green>Hai impostato la prima pos");
            event.setCancelled(true);
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!this.loc1.containsKey(player)) return;

            Block block = event.getClickedBlock();
            Location loc = block.getLocation().clone().add(0.5, 1.5, 0.5);

            Location from = this.loc1.get(player);

            this.loc1.remove(player);

            Portal portal = new Portal(player.getUniqueId().toString(), from, loc);
            this.plugin.getPortalManager().addPortal(portal);

            player.sendRichMessage("<green>Portale creato!");
            event.setCancelled(true);
        }
    }
}


