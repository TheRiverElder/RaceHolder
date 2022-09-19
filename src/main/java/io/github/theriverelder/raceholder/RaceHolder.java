package io.github.theriverelder.raceholder;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;

public final class RaceHolder extends JavaPlugin {

    public static RaceManager RACE_MANAGER = new RaceManager();
    public static RaceHolderCommandExecutor RACE_HOLDER_COMMAND_EXECUTOR = new RaceHolderCommandExecutor();
    public static RaceHolderWorldTickEventHandler RACE_HOLDER_WORLD_TICK_EVENT_HANDLER = new RaceHolderWorldTickEventHandler(RACE_MANAGER);
    public static RaceHolderWorldSaveEventHandler RACE_HOLDER_WORLD_SAVE_EVENT_HANDLER;
    public static PlaceholderAPIPlugin PLACEHOLDER_API_PLUGIN;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginCommand("raceholder").setExecutor(RACE_HOLDER_COMMAND_EXECUTOR);
        RACE_HOLDER_WORLD_TICK_EVENT_HANDLER.runTaskTimer(this, 0, 1);
        RACE_HOLDER_WORLD_SAVE_EVENT_HANDLER = new RaceHolderWorldSaveEventHandler(this, RACE_MANAGER);
//        Bukkit.getPluginManager().registerEvents(RACE_HOLDER_WORLD_SAVE_EVENT_HANDLER, this);

        PLACEHOLDER_API_PLUGIN = (PlaceholderAPIPlugin) Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (PLACEHOLDER_API_PLUGIN != null) {
            PLACEHOLDER_API_PLUGIN.
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        RACE_HOLDER_WORLD_TICK_EVENT_HANDLER.cancel();
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
