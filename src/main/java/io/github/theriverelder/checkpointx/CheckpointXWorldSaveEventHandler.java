package io.github.theriverelder.checkpointx;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class CheckpointXWorldSaveEventHandler implements Listener {

    protected Plugin plugin;
    protected RaceManager raceManager;

    public CheckpointXWorldSaveEventHandler(Plugin plugin, RaceManager raceManager) {
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
