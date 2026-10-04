package me.artic.reassembly.commands;

import lombok.RequiredArgsConstructor;
import me.artic.reassembly.Reassembly;
import me.artic.reassembly.object.Portal;
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
        if(portals.isEmpty()){
            player.sendRichMessage("<red>Nessun portale");
            return;
        }
        player.sendRichMessage("<green>Hai " + portals.size() + " portali attivi");
        for (Portal p : portals) {
            StringBuilder builder = new StringBuilder("<green>");
            builder.append(p.getId());
            builder.append(". ");
            builder.append(loc(p.getLoc1()));
            builder.append(" -> ");
            builder.append(loc(p.getLoc2()));


            player.sendRichMessage(builder.toString());
        }
    }

    @Subcommand("delete")
    public void onDelete(Player player, @Range(min = 0) int id) {
        plugin.getPortalManager().removePortalFromPlayer(player, id);

        player.sendRichMessage("<green>Hai eliminato il portale con id " + id + " (se esisteva)");
    }

    private String loc(Location location) {
        return location.getWorld().getName() + " "
                + location.getX() + " "
                + location.getY() + " "
                + location.getZ();
    }
}
