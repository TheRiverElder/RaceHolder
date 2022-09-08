package io.github.theriverelder.raceholder;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;

public final class RaceHolder extends JavaPlugin {

    public static RaceManager RACE_MANAGER = new RaceManager();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginCommand("raceholder").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("raceholder")) return false;
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        try {

            String reign = args[0];
            int nextArgIndex = 1;
            switch (reign) {
                case "manager": {
                    String action = args[nextArgIndex++];
                    switch (action) {
                        case "list": {
                            StringBuilder builder = new StringBuilder("可用的赛事：");
                            List<Integer> indexes = RACE_MANAGER.getValidIndexes();
                            for (int i = 0; i < indexes.size(); i++) {
                                if (i > 0) {
                                    builder.append(", ");
                                }
                                builder.append(indexes.get(i));
                            }
                            player.sendMessage(builder.toString());
                            break;
                        }
                        case "new": {
                            int index = RACE_MANAGER.addRace(new Race());
                            player.sendMessage("新建赛事：" + index);
                            break;
                        }
                        case "remove": {
                            int raceIndex = getIntOf(args, nextArgIndex);
                            RACE_MANAGER.removeRace(raceIndex);
                            player.sendMessage("已经删除赛事：" + raceIndex);
                            break;
                        }
                    }
                    break;
                }
                case "plan": {
                    int raceIndex = getIntOf(args, nextArgIndex++);
                    Optional<Race> raceSlot = RACE_MANAGER.getRace(raceIndex);
                    if (!raceSlot.isPresent()) {
                        player.sendMessage("不存在赛事：" + raceIndex);
                        return false;
                    }
                    Race race = raceSlot.get();
                    String action = args[nextArgIndex++];
                    switch (action) {
                        case "list": {
                            StringBuilder builder = new StringBuilder("赛程路径点：");
                            List<Vector> waypoints = race.getWaypoints();
                            for (int i = 0; i < waypoints.size(); i++) {
                                if (i > 0) {
                                    builder.append(", ");
                                }
                                builder.append(i).append("(").append(waypoints.get(i).toString()).append(")");
                            }
                            player.sendMessage(builder.toString());
                            break;
                        }
                        case "add": {
                            Vector waypoint = player.getLocation().toVector();
                            race.getWaypoints().add(waypoint);
                            sender.sendMessage("已添加路径点：" + (race.getWaypoints().size() - 1) + "(" + waypoint + ")");
                            break;
                        }
                        case "insert": {
                            int waypointIndex = getIntOf(args, nextArgIndex);
                            checkValueIsInRangeOrThrowException(waypointIndex, 0, race.getWaypoints().size() + 1);
                            Vector waypoint = player.getLocation().toVector();
                            race.getWaypoints().add(waypointIndex, waypoint);
                            sender.sendMessage("已添加路径点：" + waypointIndex + "(" + waypoint + ")");
                            break;
                        }
                        case "remove": {
                            int waypointIndex = getIntOf(args, nextArgIndex);
                            checkValueIsInRangeOrThrowException(waypointIndex, 0, race.getWaypoints().size());
                            Vector waypoint = race.getWaypoints().remove(waypointIndex);
                            sender.sendMessage("已删除路径点：" + waypointIndex + "(" + waypoint + ")");
                            break;
                        }
                    }
                    break;
                }
                case "race": {
                    int raceIndex = getIntOf(args, nextArgIndex++);
                    Optional<Race> raceSlot = RACE_MANAGER.getRace(raceIndex);
                    if (!raceSlot.isPresent()) {
                        player.sendMessage("不存在赛事：" + raceIndex);
                        return false;
                    }
                    Race race = raceSlot.get();
                    String action = args[nextArgIndex++];
                    switch (action) {
                        case "join": {
                            race.addParticipant(player);
                            player.sendMessage("已加入赛事：" + raceIndex);
                            break;
                        }
                        case "quit": {
                            race.removeParticipant(player.getUniqueId());
                            player.sendMessage("已退出赛事：" + raceIndex);
                            break;
                        }
                        case "ready": {
                            Optional<Race.Participant> participant = race.getParticipants().stream().filter(it -> it.uuid == player.getUniqueId()).findFirst();
                            if (!participant.isPresent()) {
                                player.sendMessage("未参加赛事：" + raceIndex);
                                return false;
                            }
                            if (race.status == Race.Status.NONE) {
                                race.status = Race.Status.WAITING;
                            }
                            participant.get().status = Race.Participant.Status.READY;
                            player.sendMessage("准备好！");
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            sender.sendMessage("错误：" + e.getMessage());
            return false;
        }

        return true;
    }

    static boolean checkValueIsInRangeOrThrowException(int value, int includeMin, int excludeMax) throws Exception {
        if (value >= includeMin && value < excludeMax) return true;
        throw new Exception(value + "必须在区间中：[" + includeMin + ", " + excludeMax + ")");
    }

    static int getIntOf(String[] args, int index) throws Exception {
        if (index < 0 || index >= args.length) throw new Exception("不存在第" + index + "个参数");
        try {
            return Integer.parseInt(args[index]);
        } catch (Exception e) {
            throw new Exception("第" + index + "个参数必须是整数：" + args[index]);
        }
    }
}
