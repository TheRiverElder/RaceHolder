package io.github.theriverelder.raceholder.model;

import java.util.List;

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
    public static final String STATE_UNUSED = "U";
    public static final String STATE_WAITING = "W";

    public static final String RACETYPE_WALK = "walk";
    public static final String RACETYPE_BOAT = "boat";
    public static final String RACETYPE_HORSE = "horse";
    public static final String RACETYPE_PIG = "pig";

    protected String author;
    protected String state;
    protected String description;
    protected boolean available;
    protected String racetype;
    protected List<RaceAward> raceaward;
    protected List<CheckPointData> checkpoints;
    protected List<Record> record;

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

    public List<RaceAward> getRaceaward() {
        return raceaward;
    }

    public void setRaceaward(List<RaceAward> raceaward) {
        this.raceaward = raceaward;
    }

    public List<CheckPointData> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<CheckPointData> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public List<Record> getRecord() {
        return record;
    }

    public void setRecord(List<Record> record) {
        this.record = record;
    }
}
