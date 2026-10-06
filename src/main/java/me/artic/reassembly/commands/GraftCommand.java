package me.artic.reassembly.commands;


import lombok.RequiredArgsConstructor;
import me.artic.reassembly.Reassembly;
import me.artic.reassembly.utils.CustomItem;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("graft")
@CommandPermission("reassembly.command.graft.use")
@RequiredArgsConstructor
public class GraftCommand {
    private final Reassembly plugin;

    @Subcommand("getGraft")
    public void getGraft(Player player) {
        CustomItem.GRAFT.give(player);
        String msg = plugin.getMessages().getString("player.get-graft");
        player.sendRichMessage(msg);
    }
}
