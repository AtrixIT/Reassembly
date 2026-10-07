package me.artic.reassembly;

import lombok.Getter;
import me.artic.reassembly.commands.GraftCommand;
import me.artic.reassembly.listeners.GraftListener;
import me.artic.reassembly.listeners.DropListener;
import me.artic.reassembly.listeners.PortalClickListener;
import me.artic.reassembly.commands.MainCommand;
import me.artic.reassembly.commands.PortalCommand;
import me.artic.reassembly.managers.GraftManager;
import me.artic.reassembly.managers.PortalManager;
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
    @Getter
    private GraftManager graftManager;

    private static int id = 0;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.messages = new Config(this, "messages.yml");
        this.portals = new Config(this, "portals.yml");

        this.portalManager = new PortalManager(this);
        this.graftManager = new GraftManager(this);

        this.getServer().getPluginManager().registerEvents(new PortalClickListener(this), this);
        this.getServer().getPluginManager().registerEvents(new GraftListener(this), this);
        this.getServer().getPluginManager().registerEvents(new DropListener(), this);

        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this).build();
        lamp.register(new GraftCommand(this));
        lamp.register(new PortalCommand(this));
        lamp.register(new MainCommand(this));

        this.graftManager.start();

        this.getLogger().info("Reassembly: Enabled!");
    }

    @Override
    public void onDisable() {
        this.graftManager.stop();

        this.getLogger().info("Reassembly: Disabled!");
    }

    public void reload() {
        this.reloadConfig();
        this.messages.reload();
        this.portals.reload();

        this.graftManager.reload();

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
