package io.github.theriverelder.checkpointx;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CheckpointX extends JavaPlugin {

    public RaceManager raceManager = new RaceManager();
    public CheckpointXCommandExecutor checkpointXCommandExecutor;
    public CheckpointXWorldTickEventHandler checkpointXWorldTickEventHandler = new CheckpointXWorldTickEventHandler(raceManager);
    public CheckpointXWorldSaveEventHandler checkpointXWorldSaveEventHandler;
    public PlaceholderAPIPlugin placeholderAPIPlugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        checkpointXCommandExecutor = new CheckpointXCommandExecutor(this);
        Bukkit.getPluginCommand("raceholder").setExecutor(checkpointXCommandExecutor);
        checkpointXWorldTickEventHandler.runTaskTimer(this, 0, 1);
        checkpointXWorldSaveEventHandler = new CheckpointXWorldSaveEventHandler(this, raceManager);
//        Bukkit.getPluginManager().registerEvents(RACE_HOLDER_WORLD_SAVE_EVENT_HANDLER, this);

        placeholderAPIPlugin = (PlaceholderAPIPlugin) Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderAPIPlugin != null) {
//            PLACEHOLDER_API_PLUGIN.
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        checkpointXWorldTickEventHandler.cancel();
    }

    protected void loadRaces() {
        File racesFile = new File(getDataFolder(), "races.yml");
        if (!racesFile.exists() || !racesFile.isFile()) return;
        YamlConfiguration racesData = YamlConfiguration.loadConfiguration(racesFile);
//        racesData.
    }

    protected void saveRaces() {
        File racesFile = new File(getDataFolder(), "races.yml");
        YamlConfiguration racesData = new YamlConfiguration();
//        new Constructor()
//        Yaml yaml = new Yaml();
    }
}
