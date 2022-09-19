package io.github.theriverelder.raceholder;

import org.bukkit.scheduler.BukkitRunnable;

public class RaceHolderWorldTickEventHandler extends BukkitRunnable {

    protected RaceManager raceManager;

    public RaceHolderWorldTickEventHandler(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    public void tick() {
        for (Race race : raceManager.getRaces()) {
            race.tick();
        }
    }

    @Override
    public void run() {
        tick();
    }
}
