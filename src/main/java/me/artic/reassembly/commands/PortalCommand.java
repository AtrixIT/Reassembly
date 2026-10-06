package me.artic.reassembly.commands;

import lombok.RequiredArgsConstructor;
import me.artic.reassembly.Reassembly;
import me.artic.reassembly.object.Portal;
import me.artic.reassembly.utils.CustomItem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;

@Command("portal")
@CommandPermission("reassembly.commands.portal.use")
@RequiredArgsConstructor
public class PortalCommand {
    private final Reassembly plugin;

    @Subcommand("list")
    public void onList(Player player) {
        List<Portal> portals = this.plugin.getPortalManager().getPlayerPortals(player);
        if (portals.isEmpty()) {
            String error = plugin.getMessages().getString("error.no-portals");
            player.sendRichMessage(error);
            return;
        }

        String header = plugin.getMessages().getString("player.portal-list-header")
                .replace("%count%", Integer.toString(portals.size()));
        player.sendRichMessage(header);

        for (Portal p : portals) {
            String format = plugin.getMessages().getString("player.portal-list-format")
                    .replace("%id%", Integer.toString(p.getId()))
                    .replace("%loc1%", loc(p.getLoc1()))
                    .replace("%loc2%", loc(p.getLoc2()));

            player.sendRichMessage(format);
        }
    }

    @Subcommand("delete")
    public void onDelete(Player player, @Range(min = 0) int id) {
        plugin.getPortalManager().removePortalFromPlayer(player, id);

        String del = plugin.getMessages().getString("player.portal-deleted")
                .replace("%id%", Integer.toString(id));

        player.sendRichMessage(del);
    }

    private String loc(Location location) {
        return location.getWorld().getName() + " "
                + location.getX() + " "
                + location.getY() + " "
                + location.getZ();
    }

    @Subcommand("getwand")
    public void getWand(Player player) {
        CustomItem.PORTAL_WAND.give(player);
        CustomItem.BLOCK_GRAFT.give(player); // DA RIMUOVERE

        String msg = plugin.getMessages().getString("player.get-wand");
        player.sendRichMessage(msg);
    }
}
