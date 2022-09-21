package io.github.theriverelder.checkpointx;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class RaceRuntime {

    static class Participant {

        enum Status {
            OFF, // 不在线或者未参加
            WAITING, // 参加，正在等待
            READY, // 就绪
            RUNNING, // 正在进行比赛
            ;
        }

        public static int PROGRESS_NONE = 0;
        public static int RANK_NONE = -1;

        public UUID uuid;
        public int progress;
        public int rank; // 名次，只是一个缓存，不会用于持久化
        public Status status;
        public Player player; // 暂存玩家实例，不会用于持久化

        public Participant(UUID uuid, int progress, Status status, Player player) {
            this.uuid = uuid;
            this.progress = progress;
            this.status = status;
            this.player = player;
            rank = RANK_NONE;
        }

        public Participant(UUID uuid, int progress) {
            this(uuid, progress, Status.WAITING, null);
        }

        public Participant(UUID uuid) {
            this(uuid, PROGRESS_NONE, Status.WAITING, null);
        }

        public Participant(Player player) {
            this(player.getUniqueId(), PROGRESS_NONE, Status.WAITING, player);
        }
    }

    enum Status {
        NONE, // 没有比赛
        WAITING, // 有比赛，但是不是所有人都就绪
        READY, // 所有人就绪
        RUNNING, // 所有人都在比赛
    }

    protected Status status = Status.NONE;
    protected int startCountDown = 0; // 比赛开始倒计时，当status == READY时候要设置此值
    protected int rankCounter = 0; // 计数已经跑完全程的人数
    protected List<Vector> waypoints = new ArrayList<>();
    protected List<Participant> participants = new ArrayList<>();

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Vector> getWaypoints() {
        return waypoints;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void addWaypoint(Vector waypoint) {
        waypoints.add(waypoint);
    }

    public void removeWaypoint(Vector waypoint) {
        waypoints.removeIf(it -> it.equals(waypoint));
    }

    public boolean addParticipant(Player player) {
        if (!new HashSet<>(participants.stream().map(it -> it.uuid).collect(Collectors.toList())).contains(player.getUniqueId())) {
            participants.add(new Participant(player));
            return true;
        } else return false;
    }

    public void addParticipant(UUID uuid) {
        participants.add(new Participant(uuid));
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public void removeParticipant(UUID participant) {
        participants.removeIf(it -> it.uuid.equals(participant));
    }

    public void tick() {
        switch (status) {
            case NONE: break;
            case WAITING:
                if (checkAllReady()) {
                    startCountDown = 10 * 20;
                    status = Status.READY;
                }
                break;
            case READY:
                if (!checkAllReady()) {
                    status = Status.WAITING;
                } else if (startCountDown > 0) {
                    if (startCountDown % 20 == 0) {
                        broadcastTitleToParticipants("倒计时" + (startCountDown / 20) + "秒");
                    }
                    startCountDown--;
                } else {
                    broadcastTitleToParticipants("开始！");
                    rankCounter = 0;
                    participants.forEach(it -> {
                        it.status = Participant.Status.RUNNING;
                        it.progress = Participant.PROGRESS_NONE;
                        it.rank = Participant.RANK_NONE;
                    });
                    status = Status.RUNNING;
                }
                break;
            case RUNNING:
                participants.forEach(this::checkRunningProgress);
                if (checkFinished()) {
                    status = Status.NONE;
                    Optional<Participant> winner = participants.stream().filter(it -> it.rank == 0).findFirst();
                    broadcastMessageToParticipants("比赛结束，获胜者是" + (winner.map(participant -> (participant.player != null) ? participant.player.getName() : null).orElse("???")) + "");

                    participants.forEach(it -> {
                        it.status = Participant.Status.WAITING;
                        it.progress = Participant.PROGRESS_NONE;
                        it.rank = Participant.RANK_NONE;
                    });
                }
                break;
        }
    }

    public void checkRunningProgress(Participant participant) {
        Player player = participant.player;
        if (player == null) {
            participant.status = Participant.Status.OFF;
            return;
        }
        if (participant.progress >= 0 && participant.progress < waypoints.size()) {
            Vector nextWaypoint = waypoints.get(participant.progress);
            double distanceSquared = player.getLocation().toVector().distanceSquared(nextWaypoint);
            if (distanceSquared < 8 * 8) {
                if (participant.progress > 0) {
                    sendMessageToParticipant(participant, "里程点：" + (participant.progress) + "/" + (waypoints.size() - 1));
                }
                participant.progress++;
            }
        }
        if (participant.progress >= waypoints.size()) {
            participant.rank = rankCounter++;
            String message = "完成赛程，你是第" + (participant.rank + 1) + "名";
            sendTitleToParticipant(participant, message);
            sendMessageToParticipant(participant, message);
        }
    }

    public boolean checkFinished() {
        return participants.stream().filter(it -> it.status == Participant.Status.RUNNING).allMatch(it -> it.progress >= waypoints.size());
    }

    public boolean checkAllReady() {
        return participants.stream().allMatch(it -> it.status == Participant.Status.READY);
    }

    public void refillPlayers(World world) {
        HashMap<UUID, Participant> uuidIndexedParticipants = new HashMap<>();
        participants.forEach(it -> uuidIndexedParticipants.put(it.uuid, it));
        int counter = 0;
        for (Player player : world.getPlayers()) {
            UUID uuid = player.getUniqueId();
            Participant participant = uuidIndexedParticipants.get(uuid);
            if (participant == null) continue;
            participant.player = player;
            counter++;
            if (counter >= uuidIndexedParticipants.size()) return;
        }
    }

    protected void broadcastTitleToParticipants(String message) {
        participants.forEach(it -> sendTitleToParticipant(it, message));
    }

    protected void broadcastMessageToParticipants(String message) {
        participants.forEach(it -> sendMessageToParticipant(it, message));
    }

    protected void sendTitleToParticipant(Participant participant, String message) {
        sendTitleToParticipant(participant, message, null);
    }

    protected void sendTitleToParticipant(Participant participant, String message, String secondaryMessage) {
        Player player = participant.player;
        if (player == null) return;
        player.sendTitle(message, secondaryMessage, 5, 20, 5);
    }

    protected void sendMessageToParticipant(Participant participant, String message) {
        Player player = participant.player;
        if (player == null) return;
        player.sendMessage(message);
    }
}
