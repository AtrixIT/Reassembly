package me.artic.reassembly.listeners;

import lombok.RequiredArgsConstructor;
import me.artic.reassembly.Reassembly;
import me.artic.reassembly.enums.GraftType;
import me.artic.reassembly.utils.CustomItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("all")
@RequiredArgsConstructor
public class GraftListener implements Listener {
    private final Reassembly plugin;

    // We do not have persistence, so we dont care
    private Map<Player, Material> b2b = new HashMap<>();
    private Map<Player, EntityType> m2m = new HashMap<>();
    private Map<Player, EntityType> b2m = new HashMap<>();
    private Map<Player, Material> m2b = new HashMap<>();


    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;

        Optional<String> itemId = CustomItem.getItemId(item);
        if(itemId.isEmpty()) return;
        // Da qua in poi sappiamo che l'oggetto è custom
        if(!itemId.get().equalsIgnoreCase(CustomItem.GRAFT.id())) return;

        GraftType type = plugin.getGraftManager().getGraftType(player);
        Block block = event.getClickedBlock();

        if (event.getAction() == Action.LEFT_CLICK_BLOCK && player.isSneaking()) {

            switch (type) {
                case BLOCK_TO_BLOCK -> b2b.put(player, block.getType());
                case MOB_TO_BLOCK -> m2b.put(player, block.getType());
                default -> {
                    return;
                }
            }

            String blockSelected = plugin.getMessages().getString("player.block-selected")
                    .replace("%id%", block.getType().toString());
            player.sendRichMessage(blockSelected);

            event.setCancelled(true);

        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK && !player.isSneaking()) {
            switch (type) {
                case BLOCK_TO_BLOCK -> {
                    Material material = b2b.get(player);
                    if (material == null) return;

                    Location loc = block.getLocation();
                    loc.getWorld().setBlockData(loc, material.createBlockData());
                }

                case BLOCK_TO_MOB -> {
                    EntityType entityType = b2m.get(player);
                    if (entityType == null) return;

                    Location loc = block.getLocation().clone().add(0.5, 0, 0.5);
                    loc.getWorld().setBlockData(loc, Material.AIR.createBlockData());
                    loc.getWorld().spawn(loc, entityType.getEntityClass());
                }

                default -> {
                    return;
                }
            }

            event.setCancelled(true);

        } else if (event.getAction().isRightClick()) {
            GraftType nextType = plugin.getGraftManager().setNextGraft(player);
        }
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
        if(!itemId.get().equalsIgnoreCase(CustomItem.GRAFT.id())) return;

        Entity victim = event.getEntity();
        if(entity == null) return;

        GraftType type = plugin.getGraftManager().getGraftType(player);

        if(player.isSneaking()) {
            switch (type) {
                case MOB_TO_MOB -> this.m2m.put(player, victim.getType());
                case BLOCK_TO_MOB -> this.b2m.put(player, victim.getType());
                default -> {
                    return;
                }
            }

            String mob = plugin.getMessages().getString("player.mob-selected")
                    .replace("%id%", victim.getType().toString());
            player.sendRichMessage(mob);

            event.setCancelled(true);
            return;
        }

        switch (type) {
            case MOB_TO_MOB -> {
                if(!this.m2m.containsKey(player)) return;

                Location loc = victim.getLocation();
                victim.remove();

                loc.getWorld().spawn(loc, this.m2m.get(player).getEntityClass());
            }
            case MOB_TO_BLOCK -> {
                if(!this.m2b.containsKey(player)) return;

                Location loc = victim.getLocation();
                victim.remove();

                loc.getWorld().setBlockData(loc, this.m2b.get(player).createBlockData());
            }
            default -> {
                return;
            }
        }


        event.setCancelled(true);
    }

}
