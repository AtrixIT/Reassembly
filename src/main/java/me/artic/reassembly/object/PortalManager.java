package me.artic.reassembly.object;

import lombok.RequiredArgsConstructor;
import me.artic.reassembly.Reassembly;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

@SuppressWarnings("all")
@RequiredArgsConstructor
public class PortalManager {
    private Set<Portal> portals = new HashSet<>();
    private final Reassembly plugin;
    private BukkitTask task;
    private Map<UUID, Long> cooldowns = new HashMap<>();

    public void addPortal(Portal portal) {
        this.portals.add(portal);
        if (task == null) {
            this.task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::tick, 5L, 5L);
        }
    }

    public List<Portal> getPlayerPortals(Player player) {
        return this.portals.stream()
                .filter(p -> p.getPlayerId().equalsIgnoreCase(player.getUniqueId().toString()))
                .toList();
    }

    public void removePortal(int id) {
        this.portals.removeIf(p -> p.getId() ==  id);
        /*
        Portal p = null;
        for(Portal portal : portals){
            if(portal.getId() == id) {
                p = portal;
                break;
            }
        }

        if(p != null) portals.remove(p);
         */
        if(portals.isEmpty()) {
            if(this.task != null && !this.task.isCancelled()) {
                this.task.cancel();
                this.task = null;
            }
        }
    }

    public void removePortalFromPlayer(Player player, int id) {
        this.portals.removeIf(p -> p.getId() ==  id && p.getPlayerId().equalsIgnoreCase(player.getUniqueId().toString()));
        if(portals.isEmpty()) {
            if(this.task != null && !this.task.isCancelled()) {
                this.task.cancel();
                this.task = null;
            }
        }
    }

    private void tick() {
        long now = System.currentTimeMillis();

        Iterator<Portal> portalIterator = portals.iterator();

        while (portalIterator.hasNext()) {
            Portal portal = portalIterator.next();

            portal.getLoc1().getWorld().spawnParticle(Particle.PORTAL, portal.getLoc1(), 10,0.3,0.5,0.3,0.1);
            portal.getLoc2().getWorld().spawnParticle(Particle.PORTAL, portal.getLoc2(), 10,0.3,0.5,0.3,0.1);
            this.transfer(portal.getLoc1(), portal.getLoc2(), now, portal.getPlayerId());
            this.transfer(portal.getLoc2(), portal.getLoc1(), now, portal.getPlayerId());
        }

    }

    private void transfer(Location from, Location to, long now, String playerId) {
        if (from.getWorld() == null || to.getWorld() == null) return;

        for (Entity e : from.getWorld().getNearbyEntities(from, 0.5, 0.5, 0.5)) {
            if (!(e instanceof Player p)) continue;

            UUID id = p.getUniqueId();
            if (!id.toString().equalsIgnoreCase(playerId)) continue;

            if (this.cooldowns.containsKey(id)) {
                long last = this.cooldowns.get(id);
                if (now - last < 2000) continue;

            }

            Vector velocity = e.getVelocity();
            Location dest = to.clone();

            dest.setYaw(e.getLocation().getYaw());
            dest.setPitch(e.getLocation().getPitch());

            e.teleport(dest);
            e.setVelocity(velocity);

            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.6f);

            cooldowns.put(id, now);
        }
    }


}
