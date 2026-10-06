package me.artic.reassembly;

import lombok.Getter;
import me.artic.reassembly.listeners.BlockGraftListener;
import me.artic.reassembly.listeners.DropListener;
import me.artic.reassembly.listeners.PortalClickListener;
import me.artic.reassembly.commands.MainCommand;
import me.artic.reassembly.commands.PortalCommand;
import me.artic.reassembly.object.PortalManager;
import me.artic.reassembly.utils.Config;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public final class Reassembly extends JavaPlugin {

    private Config messages;
    private Config portals;
    @Getter
    private PortalManager portalManager;

    private static int id = 0;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.messages = new Config(this, "messages.yml");
        this.portals = new Config(this, "portals.yml");

        this.portalManager = new PortalManager(this);
        this.getServer().getPluginManager().registerEvents(new PortalClickListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockGraftListener(this), this);
        this.getServer().getPluginManager().registerEvents(new DropListener(), this);

        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this).build();
        lamp.register(new PortalCommand(this));
        lamp.register(new MainCommand(this));

        this.getLogger().info("Reassembly: Enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Reassembly: Disabled!");
    }

    public void reload() {
        this.reloadConfig();
        this.messages.reload();
        this.portals.reload();

        this.getLogger().info("Reassembly: Reloaded!");
    }

    public YamlConfiguration getMessages() {
        return this.messages.getConfig();
    }

    public YamlConfiguration getPortals() {
        return this.portals.getConfig();
    }

    public static Reassembly getInstance() {
        return getPlugin(Reassembly.class);
    }

    public static int getNextId() {
        return id++;
    }
}
