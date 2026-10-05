package me.artic.reassembly.commands;

import lombok.RequiredArgsConstructor;
import me.artic.reassembly.Reassembly;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("reassembly")
@CommandPermission("reassembly.commands.admin.use")
@RequiredArgsConstructor
public class MainCommand {
    private final Reassembly plugin;

    @Subcommand("reload")
    public void reload(BukkitCommandActor actor){
        plugin.reload();
        String msg = plugin.getMessages().getString("admin.reload");
        actor.sender().sendRichMessage(msg);
    }
}
