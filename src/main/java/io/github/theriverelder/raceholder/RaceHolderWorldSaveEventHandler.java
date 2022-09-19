package io.github.theriverelder.raceholder;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class RaceHolderWorldSaveEventHandler implements Listener {

    protected Plugin plugin;
    protected RaceManager raceManager;

    public RaceHolderWorldSaveEventHandler(Plugin plugin, RaceManager raceManager) {
        this.plugin = plugin;
        this.raceManager = raceManager;
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        World world = event.getWorld();
        world.getPlayers().get(0).getPersistentDataContainer();
        File races = new File(plugin.getDataFolder(), "races.yml");

    }
}
