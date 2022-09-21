package io.github.theriverelder.checkpointx;

import io.github.theriverelder.checkpointx.model.RaceData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static io.github.theriverelder.checkpointx.CheckpointX.RACE_MANAGER;
import static io.github.theriverelder.checkpointx.Utils.toFixedString;

public class CheckpointXCommandExecutor implements CommandExecutor {

    public static final String COMMAND_HEAD = "checkpointx";
    protected CheckpointX plugin;

    public CheckpointXCommandExecutor(CheckpointX plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!COMMAND_HEAD.equals(command.getName())) return false;
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        try {

            String reign = args[0];
            int nextArgIndex = 1;
            switch (reign) {
                case "plan": {
                    int raceIndex = getIntOf(args, nextArgIndex++);
                    Optional<RaceRuntime> raceSlot = RACE_MANAGER.getRace(raceIndex);
                    if (!raceSlot.isPresent()) {
                        player.sendMessage("不存在赛事：" + raceIndex);
                        return false;
                    }
                    RaceRuntime raceRuntime = raceSlot.get();
                    String action = args[nextArgIndex++];
                    switch (action) {
                        case "list": {
                            StringBuilder builder = new StringBuilder("赛程路径点：");
                            List<Vector> waypoints = raceRuntime.getWaypoints();
                            for (int i = 0; i < waypoints.size(); i++) {
                                if (i > 0) {
                                    builder.append(", ");
                                }
                                builder.append(i).append(toFixedString(waypoints.get(i)));
                            }
                            player.sendMessage(builder.toString());
                            break;
                        }
                        case "add": {
                            Vector waypoint = player.getLocation().toVector();
                            raceRuntime.getWaypoints().add(waypoint);
                            sender.sendMessage("已添加路径点：" + (raceRuntime.getWaypoints().size() - 1) + toFixedString(waypoint));
                            break;
                        }
                        case "insert": {
                            int waypointIndex = getIntOf(args, nextArgIndex);
                            checkValueIsInRangeOrThrowException(waypointIndex, 0, raceRuntime.getWaypoints().size() + 1);
                            Vector waypoint = player.getLocation().toVector();
                            raceRuntime.getWaypoints().add(waypointIndex, waypoint);
                            sender.sendMessage("已添加路径点：" + waypointIndex + toFixedString(waypoint));
                            break;
                        }
                        case "remove": {
                            int waypointIndex = getIntOf(args, nextArgIndex);
                            checkValueIsInRangeOrThrowException(waypointIndex, 0, raceRuntime.getWaypoints().size());
                            Vector waypoint = raceRuntime.getWaypoints().remove(waypointIndex);
                            sender.sendMessage("已删除路径点：" + waypointIndex + toFixedString(waypoint));
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

    //#region checkpointx.list

    public String executeList(Player player, String userName) {
        String author = (userName == null || "".equals(userName)) ? player.getName() : userName;
        return "由玩家【" + author + "】创建的差事有：" + getListString(race -> author.equals(race.getAuthor()));
    }

    public String executeListAll() {
        return "全服已过审差事有：" + getListString(race -> RaceData.STATE_PASSED.equals(race.getState()));
    }

    public String executeListReview() {
        return "全服待审核差事有：" + getListString(race -> RaceData.STATE_WAITING.equals(race.getState()));
    }

    public String executeListRefuse() {
        return "全服已拒绝差事有：" + getListString(race -> RaceData.STATE_UNPASSED.equals(race.getState()));
    }

    String getListString(Predicate<RaceData> predicate) {
        List<String> res = new ArrayList<>();
        for (String raceName : plugin.raceManager.getRaceNames()) {
            Optional<RaceData> raceOpt = plugin.raceManager.getRace(raceName);
            if (!raceOpt.isPresent()) continue;
            RaceData race = raceOpt.get();
            if (predicate.test(race)) {
                res.add(raceName);
            }
        }
        StringBuilder builder = new StringBuilder();
        Iterator<String> itr = res.iterator();
        while (itr.hasNext()) {
            builder.append(itr.next());
            if (itr.hasNext()) {
                builder.append("、");
            }
        }
        return builder.toString();
    }

    //#endregion checkpointx.list

    //#region checkpointx.enter

    public String executeEnter(Player player, String raceName, String type) throws Exception {
        raceName = getDesignatedOrDefaultRaceName(raceName, player);
        RaceData race = getRaceByName(raceName);
        // TODO
        return "";
    }

    public String executeSetOut(Player player) {
        // TODO
        return "";
    }

    public String executeLeave(Player player) {
        // TODO
        return "";
    }

    public String executeReady(Player player) {
        // TODO
        return "";
    }

    public String executeStart(Player player) {
        // TODO
        return "";
    }

    public String executeRespawn(Player player) {
        // TODO
        return "";
    }

    //#endregion checkpointx.enter

    //#region checkpointx.create

    public String executeCreate(Player player, String raceName) throws Exception {
        if (plugin.raceManager.getRace(raceName).isPresent()) throw makeException("已存在差事【" + raceName + "】，请考虑另起新名。");
        RaceData race = new RaceData();
        race.setAuthor(player.getName());
        plugin.raceManager.addRace(raceName, race);
        String oldRaceName = plugin.raceManager.freeEditing(player.getName()).orElse(null);
        plugin.raceManager.setEditing(player.getName(), raceName);
        return (
                oldRaceName == null
                        ? "已创建并开始编辑差事【" + raceName + "】。"
                        : "已创建并开始编辑差事【" + raceName + "】，并同时退出对差事【" + oldRaceName + "】的编辑状态。"
        );
    }

    public String executeTypeSet(Player player, String raceNameOrType, String type) throws Exception {
        String raceName;
        if (type == null) {
            type = raceNameOrType;
            raceName = null;
        } else {
            raceName = raceNameOrType;
        }
        raceName = getDesignatedOrDefaultRaceName(raceName, player);
        RaceData race = getRaceByName(raceName);
        race.setRacetype(type);
        return "成功设置差事【" + raceName + "】的类型为：" + getRacetypeString(race.getRacetype());
    }

    public String executeDescription(Player player, String raceNameOrDescription, String description) throws Exception {
        String raceName;
        if (description == null) {
            description = raceNameOrDescription;
            raceName = null;
        } else {
            raceName = raceNameOrDescription;
        }
        raceName = getDesignatedOrDefaultRaceName(raceName, player);
        RaceData race = getRaceByName(raceName);
        race.setDescription(description);
        return "成功设置差事【" + raceName + "】的描述为：" + race.getDescription();
    }

    public String executeEdit(Player player, String raceName) {
        String oldRaceName = plugin.raceManager.freeEditing(player.getName()).orElse(null);
        plugin.raceManager.setEditing(player.getName(), raceName);
        return (
                oldRaceName == null
                ? "开始编辑差事【" + raceName + "】。"
                : "开始编辑差事【" + raceName + "】，并同时退出对差事【" + oldRaceName + "】的编辑状态。"
                );
    }

    public String executeEnd(Player player) throws Exception {
        String raceName = plugin.raceManager.freeEditing(player.getName()).orElseThrow(() -> makeException("当前没有正在编辑的差事。"));
        return "成功退出对差事【" + raceName + "】的编辑状态。";
    }

    public String executePull(Player player, String raceName) throws Exception {
        raceName = getDesignatedOrDefaultRaceName(raceName, player);
        RaceData race = getRaceByName(raceName);
        race.setState(RaceData.STATE_WAITING);
        return "成功提交差事【" + raceName + "】。";
    }

    public String executeReview(Player player, String raceName) throws Exception {
        raceName = getDesignatedOrDefaultRaceName(raceName, player);
        RaceData race = getRaceByName(raceName);
        // TODO
        return "成功启动差事【" + raceName + "】的战局。";
    }

    public String executeReason(Player player, String raceName) throws Exception {
        raceName = getDesignatedOrDefaultRaceName(raceName, player);
        RaceData race = getRaceByName(raceName);
        return "差事【" + raceName + "】的状态为：" + getRaceStateString(race.getState()) + "，原因为：" + race.getReason();
    }

    //#endregion checkpointx.create

    //#region checkpointx.admin

    public String executePass(Player player, String raceName, String reason) throws Exception {
        raceName = getDesignatedOrDefaultRaceName(raceName, player);
        RaceData race = getRaceByName(raceName);
        race.setState(RaceData.STATE_PASSED);
        race.setReason(reason);
        return "成功允许差事【" + raceName + "】，原因为：" + race.getReason();
    }

    public String executeRefuse(Player player, String raceName, String reason) throws Exception {
        raceName = getDesignatedOrDefaultRaceName(raceName, player);
        RaceData race = getRaceByName(raceName);
        race.setState(RaceData.STATE_UNPASSED);
        race.setReason(reason);
        return "成功拒绝差事【" + raceName + "】，原因为：" + race.getReason();
    }

    public String executeSetDes(Player player, String raceName, String description) throws Exception {
        raceName = getDesignatedOrDefaultRaceName(raceName, player);
        RaceData race = getRaceByName(raceName);
        race.setDescription(description);
        return "成功设置差事【" + raceName + "】的描述为：" + race.getDescription();
    }

    public String executeSetRea(Player player, String raceName, String reason) throws Exception {
        raceName = getDesignatedOrDefaultRaceName(raceName, player);
        RaceData race = getRaceByName(raceName);
        race.setReason(reason);
        return "成功设置差事【" + raceName + "】的原因为：" + race.getReason();
    }

    public String executeSetAva(Player player, String raceName, String boolStr) throws Exception {
        raceName = getDesignatedOrDefaultRaceName(raceName, player);
        RaceData race = getRaceByName(raceName);
        boolean option;
        if (boolStr == null || "".equals(boolStr)) {
            option = true;
        } else {
            switch (boolStr) {
                case "t":
                case "true":
                    option = true;
                    break;
                case "f":
                case "false":
                    option = false;
                    break;
                default:
                    throw makeException("该参数应该为t/f：" + boolStr);
            }
        }
        race.setAvailable(option);
        return "成功设置差事【" + raceName + "】为：" + (race.isAvailable() ? "可用" : "不可用");
    }

    //#endregion checkpointx.admin

    static String getRacetypeString(String racetype) {
        switch (racetype) {
            case RaceData.RACETYPE_WALK: return "足力";
            case RaceData.RACETYPE_BOAT: return "赛船";
            case RaceData.RACETYPE_HORSE: return "赛马";
            case RaceData.RACETYPE_PIG: return "猪骑士";
            default: return "未知";
        }
    }

    static String getRaceStateString(String state) {
        switch (state) {
            case RaceData.STATE_EDITING: return "编辑中";
            case RaceData.STATE_PASSED: return "已通过";
            case RaceData.STATE_UNPASSED: return "已拒绝";
            case RaceData.STATE_WAITING: return "待审核";
            default: return "未知";
        }
    }

    String getDesignatedOrDefaultRaceName(String raceName, Player player) throws Exception {
        if (raceName != null && !"".equals(raceName)) return raceName;
        else return plugin.raceManager.getEditing(player.getName()).orElseThrow(() -> makeException("玩家【" + player.getName() + "】没有正在编辑的差事。"));
     }

    RaceData getRaceByName(String raceName) throws Exception {
        Optional<RaceData> res = this.plugin.raceManager.getRace(raceName);
        return res.orElseThrow(() -> makeException("找不到名为【" + raceName + "】的差事。"));
    }

    public Exception makeException(String message) {
        return new Exception(message);
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
