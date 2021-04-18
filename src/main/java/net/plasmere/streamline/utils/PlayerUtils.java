package net.plasmere.streamline.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.objects.lists.SingleSet;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerUtils {
    private static final List<Player> stats = new ArrayList<>();
    private static final LuckPerms api = LuckPermsProvider.get();
    private static final Configuration message = Config.getMess();

    private static HashMap<Player, SingleSet<Integer, Integer>> connections = new HashMap<>();

    public static List<Player> getStats() {
        return stats;
    }

    public static ProxiedPlayer getProxiedPlayer(String latestName){
        for (ProxiedPlayer p : StreamLine.getInstance().getProxy().getPlayers()){
            if (p.getName().equals(latestName)) return p;
        }

        return null;
    }

    public static boolean hasStat(String latestName){
        return getStat(latestName) != null;
    }

    public static Player getOffOnStat(String name){
        Player p = getStat(name);

        if (p == null) {
            if (exists(name)) {
                p = new Player(name);
                addStat(p);
                return p;
            } else {
                return null;
            }
        }

        return p;
    }

    public static HashMap<Player, SingleSet<Integer, Integer>> getConnections(){
        return connections;
    }

    public static void addOneToConn(Player player) {
        SingleSet<Integer, Integer> conn = connections.get(player);

        int timer = conn.key;
        int num = conn.value;

        num++;

        connections.remove(player);
        connections.put(player, new SingleSet<>(timer, num));
    }

    public static void removeSecondFromConn(Player player){
        SingleSet<Integer, Integer> conn = connections.get(player);

        int timer = conn.key;
        int num = conn.value;

        timer--;

        connections.remove(player);
        connections.put(player, new SingleSet<>(timer, num));
    }

    public static SingleSet<Integer, Integer> getConnection(Player player){
        try {
            return connections.get(player);
        } catch (Exception e){
            return null;
        }
    }

    public static void removeConn(Player player){
        connections.remove(player);
    }

    public static void addConn(Player player){
        connections.put(player, new SingleSet<>(ConfigUtils.lobbyTimeOut, 0));
    }

    public static Player getOrCreate(UUID uuid){
        Player player = getStat(uuid);

        if (player == null) {
            player = new Player(uuid);
            addStat(player);
        }

        return player;
    }

    public static boolean isNameEqual(Player player, String name){
        if (player.latestName == null) return false;

        return player.latestName.equals(name);
    }

    public static Player getStat(ProxiedPlayer player) {
        if (player instanceof Player) {
            return getStat(player.getName());
        }

        try {
            for (Player stat : stats) {
                if (isNameEqual(stat, player.getName())) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String forStats(List<Player> players){
        StringBuilder builder = new StringBuilder("[");

        int i = 1;
        for (Player p : players){
            if (i != players.size()) {
                builder.append(p.toString()).append(", ");
            } else {
                builder.append(p.toString()).append("]");
            }

            i++;
        }

        return builder.toString();
    }

    public static Player getStat(CommandSender player) {
        if (player instanceof Player) {
            return getStat(player.getName());
        }

        try {
            for (Player stat : stats) {
                if (isNameEqual(stat, player.getName())) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Player getStat(String name) {
        try {
            for (Player stat : stats) {
                if (isNameEqual(stat, name)) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Player getStat(UUID uuid) {
        try {
            for (Player stat : stats) {
                if (stat.uuid.equals(uuid)) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Player> transposeList(List<ProxiedPlayer> players){
        List<Player> ps = new ArrayList<>();
        for (ProxiedPlayer player : players){
            ps.add(UUIDFetcher.getPlayer(player));
        }

        return ps;
    }

    public static String getOffOnDisplayBungee(Player player){
        if (player == null) {
            return "&c&lNULL";
        }

        if (player.online) {
            return MessageConfUtils.onlineB.replace("%player%", player.displayName);
        } else {
            return MessageConfUtils.offlineB.replace("%player%", player.displayName);
        }
    }

    public static String getOffOnRegBungee(Player player){
        if (player == null) {
            return "&c&lNULL";
        }

        if (player.online) {
            return MessageConfUtils.onlineB.replace("%player%", player.latestName);
        } else {
            return MessageConfUtils.offlineB.replace("%player%", player.latestName);
        }
    }

    public static String getOffOnDisplayDiscord(Player player){
        if (player.online) {
            return MessageConfUtils.onlineD.replace("%player%", player.displayName);
        } else {
            return MessageConfUtils.offlineD.replace("%player%", player.displayName);
        }
    }

    public static String getOffOnRegDiscord(Player player){
        if (player.online) {
            return MessageConfUtils.onlineD.replace("%player%", player.latestName);
        } else {
            return MessageConfUtils.offlineD.replace("%player%", player.latestName);
        }
    }

    public static boolean exists(String username){
        File file = new File(StreamLine.getInstance().getPlDir(), UUIDFetcher.getCachedUUID(username) + ".properties");

        return file.exists();
    }

    public static boolean isStats(Player stat){
        return stats.contains(stat);
    }

    public static void reloadStats(Player stat) {
        stats.remove(getStat(stat.latestName));
        stats.add(stat);
    }

    public static void createStat(ProxiedPlayer player) {
        try {
            Player stat = new Player(player);

            addStat(stat);

            if (ConfigUtils.statsTell) {
                MessagingUtils.sendStatUserMessage(stat, player, create);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addStat(Player stat){
        stats.add(stat);
    }

    public static void removeStat(Player stat){
        try {
            stat.saveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stats.remove(stat);
    }

    public static void info(CommandSender sender, Player of){
        if (! sender.hasPermission(ConfigUtils.comBStatsPerm)) {
            MessagingUtils.sendBUserMessage(sender, noPermission);
        }

        MessagingUtils.sendStatUserMessage(of, sender, info);
    }

    public static void remTag(CommandSender sender, Player of, String tag){
        if (! sender.hasPermission(ConfigUtils.comBBTagPerm)) {
            MessagingUtils.sendBUserMessage(sender, noPermission);
            return;
        }

        of.remTag(tag);

        MessagingUtils.sendBUserMessage(sender, tagRem
                .replace("%player%", getOffOnDisplayBungee(of))
                .replace("%tag%", tag)
        );
    }

    public static void addTag(CommandSender sender, Player of, String tag){
        if (! sender.hasPermission(ConfigUtils.comBBTagPerm)) {
            MessagingUtils.sendBUserMessage(sender, noPermission);
            return;
        }

        of.addTag(tag);

        MessagingUtils.sendBUserMessage(sender, tagAdd
                .replace("%player%", getOffOnDisplayBungee(of))
                .replace("%tag%", tag)
        );
    }

    public static void listTags(CommandSender sender, Player of){
        if (! sender.hasPermission(ConfigUtils.comBBTagPerm)) {
            MessagingUtils.sendBUserMessage(sender, noPermission);
            return;
        }

        MessagingUtils.sendBUserMessage(sender, tagListMain
                .replace("%player%", getOffOnDisplayBungee(of))
                .replace("%tags%", compileTagList(of))
        );
    }

    public static String compileTagList(Player of) {
        StringBuilder stringBuilder = new StringBuilder();

        int i = 1;
        for (String tag : of.tags){
            if (i < of.tags.size()) {
                stringBuilder.append(tagListNotLast
                        .replace("%player%", getOffOnDisplayBungee(of))
                        .replace("%tag%", tag)
                );
            } else {
                stringBuilder.append(tagListLast
                        .replace("%player%", getOffOnDisplayBungee(of))
                        .replace("%tag%", tag)
                );
            }
            i++;
        }

        return stringBuilder.toString();
    }

    // No stats.
    public static final String noStatsFound = message.getString("stats.no-stats");
    // Not high enough permissions.
    public static final String noPermission = message.getString("stats.no-permission");
    // Create.
    public static final String create = message.getString("stats.create");
    // Info.
    public static final String info = message.getString("stats.info");
    // Tags.
    public static final String tagRem = message.getString("btag.remove");
    public static final String tagAdd = message.getString("btag.add");
    public static final String tagListMain = message.getString("btag.list.main");
    public static final String tagListLast = message.getString("btag.list.tags.last");
    public static final String tagListNotLast = message.getString("btag.list.tags.not-last");
}
