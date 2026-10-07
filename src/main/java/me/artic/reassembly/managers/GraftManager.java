package me.artic.reassembly.managers;

import lombok.RequiredArgsConstructor;
import me.artic.reassembly.Reassembly;
import me.artic.reassembly.enums.GraftType;
import me.artic.reassembly.utils.CustomItem;
import me.artic.reassembly.utils.MsgUtils;
import me.artic.reassembly.utils.PluginKeys;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public class GraftManager {
    private final Reassembly plugin;
    private BukkitTask task;

    public GraftType getGraftType(Player player) {
        String str = player.getPersistentDataContainer().getOrDefault(PluginKeys.CURRENT_GRAFT, PersistentDataType.STRING,"none");

        try {
            return GraftType.valueOf(str);
        } catch (Exception ignored) {
            return GraftType.MOB_TO_MOB;
        }
    }

    public void setGraftType(Player player, GraftType type) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(PluginKeys.CURRENT_GRAFT, PersistentDataType.STRING, type.toString());
    }

    public GraftType setNextGraft(Player player) {
        GraftType type = this.getGraftType(player);
        GraftType next = type.next();
        this.setGraftType(player, next);
        return next;
    }

    public void start() {
        task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if(item == null) continue;
                if(!CustomItem.getItemId(item).orElse("none").equalsIgnoreCase(CustomItem.GRAFT.id())) continue;

                String bar = plugin.getMessages().getString("action-bar.current-graft")
                        .replace("%graft%", MsgUtils.formatGraft(plugin.getGraftManager().getGraftType(player)));
                Component component = MsgUtils.component(bar);
                player.sendActionBar(component);
            }
        }, 0L, 5L);
    }



    public void stop() {
        if(this.task != null) {
            if(!this.task.isCancelled()) {
                this.task.cancel();
            }
        }
    }

    public void reload() {
        this.stop();
        this.start();
    }
}
