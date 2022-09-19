package io.github.theriverelder.raceholder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RaceManager {

    static class RaceSlot {
        Race race;

        public RaceSlot(Race race) {
            this.race = race;
        }

        public RaceSlot() {
            this(null);
        }

        boolean isEmpty() {
            return race == null;
        }
    }

    protected static RaceSlot EMPTY_RACE_SLOT = new RaceSlot();

    protected List<RaceSlot> racePool = new ArrayList<>();

    public List<RaceSlot> getRacePool() {
        return racePool;
    }

    public List<Race> getRaces() {
        List<Race> res = new ArrayList<>(racePool.size());
        for (RaceSlot slot : racePool) {
            if (!slot.isEmpty()) {
                res.add(slot.race);
            }
        }
        return res;
    }

    protected int getOrAddEmptyRaceSlotIndex() {
        for (int i = 0; i < racePool.size(); i++) {
            if (racePool.get(i).isEmpty()) return i;
        }
        int index = racePool.size();
        racePool.add(new RaceSlot());
        return index;
    }

    public int addRace(Race race) {
        int index = getOrAddEmptyRaceSlotIndex();
        RaceSlot slot = racePool.get(index);
        slot.race = race;
        return index;
    }

    public Optional<Race> getRace(int index) {
        return (index >= 0 && index < racePool.size()) ? Optional.ofNullable(racePool.get(index).race) : Optional.empty();
    }

    public Optional<Race> removeRace(int index) {
        if (index >= 0 && index < racePool.size()) {
            RaceSlot slot = racePool.get(index);
            Race race = slot.race;
            slot.race = null;
            return Optional.ofNullable(race);
        }
        return Optional.empty();
    }

    public List<Integer> getValidIndexes() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < racePool.size(); i++) {
            RaceSlot slot = racePool.get(i);
            if (!slot.isEmpty()) result.add(i);
        }
        return result;
    }
}
