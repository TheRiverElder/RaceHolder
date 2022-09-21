package io.github.theriverelder.checkpointx.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaceData {

    public static class RaceAward {
        int order;
        String[] command;

        public RaceAward(int order, String... command) {
            this.order = order;
            this.command = command;
        }
    }

    public static class Record {
        int id;
        String user;
        float duration;

        public Record(int id, String user, float duration) {
            this.id = id;
            this.user = user;
            this.duration = duration;
        }
    }

    public static final String STATE_PASSED = "P";
    public static final String STATE_EDITING = "E";
    public static final String STATE_UNPASSED = "U";
    public static final String STATE_WAITING = "W";

    public static final String RACETYPE_WALK = "walk";
    public static final String RACETYPE_BOAT = "boat";
    public static final String RACETYPE_HORSE = "horse";
    public static final String RACETYPE_PIG = "pig";

    public RaceData(String author, String state, String description, String reason, boolean available, String racetype, Map<Integer, RaceAward> raceaward, List<CheckpointData> checkpoints, Map<Integer, Record> record) {
        this.author = author;
        this.state = state;
        this.description = description;
        this.reason = reason;
        this.available = available;
        this.racetype = racetype;
        this.raceaward = raceaward;
        this.checkpoints = checkpoints;
        this.record = record;
    }

    public RaceData() {
        this.author = "";
        this.state = STATE_EDITING;
        this.description = "无描述";
        this.reason = "";
        this.available = false;
        this.racetype = RACETYPE_BOAT;
        this.raceaward = new HashMap<>();
        this.checkpoints = new ArrayList<>();
        this.record = new HashMap<>();
    }

    protected String author;
    protected String state;
    protected String description;
    protected String reason;
    protected boolean available;
    protected String racetype;
    protected Map<Integer, RaceAward> raceaward;
    protected List<CheckpointData> checkpoints;
    protected Map<Integer, Record> record;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getRacetype() {
        return racetype;
    }

    public void setRacetype(String racetype) {
        this.racetype = racetype;
    }

    public Map<Integer, RaceAward> getRaceaward() {
        return raceaward;
    }

    public void setRaceaward(Map<Integer, RaceAward> raceaward) {
        this.raceaward = raceaward;
    }

    public List<CheckpointData> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<CheckpointData> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public Map<Integer, Record> getRecord() {
        return record;
    }

    public void setRecord(Map<Integer, Record> record) {
        this.record = record;
    }
}
