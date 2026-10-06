package me.artic.reassembly.listeners;

import lombok.RequiredArgsConstructor;
import me.artic.reassembly.Reassembly;
import me.artic.reassembly.utils.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class BlockGraftListener implements Listener {
    private final Reassembly plugin;

    private Map<Player, Material> blockGraft = new HashMap<>();
    private Map<Player, EntityType> mobGraft = new HashMap<>();

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;

        Optional<String> itemId = CustomItem.getItemId(item);
        if(itemId.isEmpty()) return;
        // Da qua in poi sappiamo che l'oggetto è custom
        if(!itemId.get().equalsIgnoreCase(CustomItem.BLOCK_GRAFT.id())) return;

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            this.blockGraft.put(player, block.getType());

            player.sendRichMessage("<green>Hai scelto il blocco");
            event.setCancelled(true);
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!this.blockGraft.containsKey(player)) return;

            Block block = event.getClickedBlock();
            Location loc = block.getLocation();
            BlockData data = Bukkit.createBlockData(this.blockGraft.get(player));
            loc.getWorld().setBlockData(loc, data);

            player.sendRichMessage("<green>Blocco cambiato");
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("all")
    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;

        Optional<String> itemId = CustomItem.getItemId(item);
        if(itemId.isEmpty()) return;
        // Da qua in poi sappiamo che l'oggetto è custom
        if(!itemId.get().equalsIgnoreCase(CustomItem.BLOCK_GRAFT.id())) return;

        event.setCancelled(true);

        Entity entity = event.getRightClicked();
        if(entity == null) return;
        if(!this.mobGraft.containsKey(player)) return;

        Location loc = entity.getLocation();
        entity.remove();

        loc.getWorld().spawn(loc, this.mobGraft.get(player).getEntityClass());
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if(!(entity instanceof Player player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;

        Optional<String> itemId = CustomItem.getItemId(item);
        if(itemId.isEmpty()) return;
        // Da qua in poi sappiamo che l'oggetto è custom
        if(!itemId.get().equalsIgnoreCase(CustomItem.BLOCK_GRAFT.id())) return;

        event.setCancelled(true);

        Entity victim = event.getEntity();
        this.mobGraft.put(player, victim.getType());
        player.sendRichMessage("<green>Mob salvato");
    }

}
