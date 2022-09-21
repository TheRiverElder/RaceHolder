package io.github.theriverelder.checkpointx;

import io.github.theriverelder.checkpointx.model.RaceData;

import java.util.*;

public class RaceManager {

    protected Map<String, String> editors = new HashMap<>();

    public void setEditing(String editorName, String raceName) {
        editors.put(editorName, raceName);
    }

    public Optional<String> freeEditing(String editorName) {
        return Optional.ofNullable(editors.remove(editorName));
    }

    public Optional<String> getEditing(String editorName) {
        return Optional.ofNullable(editors.get(editorName));
    }

    protected Map<String, RaceData> races = new HashMap<>();

    public List<RaceData> getRaces() {
        return new ArrayList<>(races.values());
    }

    public List<String> getRaceNames() {
        return new ArrayList<>(races.keySet());
    }

    public void addRace(String name, RaceData raceRuntime) {
        races.put(name, raceRuntime);
    }

    public Optional<RaceData> getRace(String name) {
        return Optional.ofNullable(races.get(name));
    }

    public Optional<RaceData> removeRace(String name) {
        return Optional.ofNullable(races.remove(name));
    }
}
