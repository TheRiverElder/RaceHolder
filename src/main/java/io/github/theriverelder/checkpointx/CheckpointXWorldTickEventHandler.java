package io.github.theriverelder.checkpointx;

import org.bukkit.scheduler.BukkitRunnable;

public class CheckpointXWorldTickEventHandler extends BukkitRunnable {

    protected RaceManager raceManager;

    public CheckpointXWorldTickEventHandler(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    public void tick() {
        for (RaceRuntime raceRuntime : raceManager.getRaces()) {
            raceRuntime.tick();
        }
    }

    @Override
    public void run() {
        tick();
    }
}
