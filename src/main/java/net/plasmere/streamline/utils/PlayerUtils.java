package net.plasmere.streamline.utils;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
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
import java.util.function.Predicate;

public class PlayerUtils {
    private static final List<Player> stats = new ArrayList<>();
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

    public static void removePlayerIf(Predicate<Player> predicate){
        stats.removeIf(predicate);
    }

    public static HashMap<Player, SingleSet<Integer, Integer>> getConnections(){
        return connections;
    }

    public static void addOneToConn(Player player) {
        SingleSet<Integer, Integer> conn = connections.get(player);

        if (conn == null) {
            connections.remove(player);
            return;
        }

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

    public static Player getOrCreate(String uuid){
        Player player = getStatByUUID(uuid);

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

    public static Player getStatByUUID(String uuid) {
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

    public static void updateDisplayName(Player player){
        if (! ConfigUtils.updateDisplayNames) return;
        if (! StreamLine.lpHolder.enabled) return;

        player.setDisplayName(getDisplayName(player));
    }

    public static String getDisplayName(Player player) {
        User user = StreamLine.lpHolder.api.getUserManager().getUser(player.latestName);
        if (user == null) return player.displayName;

        Group group = StreamLine.lpHolder.api.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) return player.displayName;

        String prefix = "";
        String suffix = "";

        TreeMap<Integer, String> preWeight = new TreeMap<>();
        TreeMap<Integer, String> sufWeight = new TreeMap<>();

        for (PrefixNode node : group.getNodes(NodeType.PREFIX)) {
            preWeight.put(node.getPriority(), node.getMetaValue());
        }

        for (PrefixNode node : user.getNodes(NodeType.PREFIX)) {
            preWeight.put(node.getPriority(), node.getMetaValue());
        }

        for (SuffixNode node : group.getNodes(NodeType.SUFFIX)) {
            sufWeight.put(node.getPriority(), node.getMetaValue());
        }

        for (SuffixNode node : user.getNodes(NodeType.SUFFIX)) {
            sufWeight.put(node.getPriority(), node.getMetaValue());
        }

        prefix = preWeight.get(getCeilingInt(preWeight.keySet()));
        suffix = sufWeight.get(getCeilingInt(sufWeight.keySet()));

        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        return TextUtils.codedString(prefix + player.latestName + suffix);
    }

    public static String getDisplayName(String username) {
        User user = StreamLine.lpHolder.api.getUserManager().getUser(username);
        if (user == null) return username;

        Group group = StreamLine.lpHolder.api.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) return username;

        String prefix = "";
        String suffix = "";

        TreeMap<Integer, String> preWeight = new TreeMap<>();
        TreeMap<Integer, String> sufWeight = new TreeMap<>();

        for (PrefixNode node : group.getNodes(NodeType.PREFIX)) {
            preWeight.put(node.getPriority(), node.getMetaValue());
        }

        for (PrefixNode node : user.getNodes(NodeType.PREFIX)) {
            preWeight.put(node.getPriority(), node.getMetaValue());
        }

        for (SuffixNode node : group.getNodes(NodeType.SUFFIX)) {
            sufWeight.put(node.getPriority(), node.getMetaValue());
        }

        for (SuffixNode node : user.getNodes(NodeType.SUFFIX)) {
            sufWeight.put(node.getPriority(), node.getMetaValue());
        }

        prefix = preWeight.get(getCeilingInt(preWeight.keySet()));
        suffix = sufWeight.get(getCeilingInt(sufWeight.keySet()));

        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        return TextUtils.codedString(prefix + username + suffix);
    }

    public static int getCeilingInt(Set<Integer> ints){
        int value = 0;

        for (Integer i : ints) {
            if (i >= value) value = i;
        }

        return value;
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
        if (isInStatsList(stat)) return;

        stats.add(stat);
    }

    public static boolean isInStatsList(Player stat) {
            return isInStatsList(stat.latestName);
    }

    public static boolean isInStatsList(String username) {
        for (Player player : getStats()) {
            if (player.latestName.equals(username)) return true;
        }

        return false;
    }

    public static void removeStat(Player stat){
        List<Player> toRemove = new ArrayList<>();

        for (Player player : getStats()) {
            if (player.latestName.equals(stat.latestName)) {
                toRemove.add(player);
            }
        }

        for (Player player : toRemove) {
            stats.remove(player);
            try {
                stat.saveInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveAll(){
        for (Player player : getStats()) {
            try {
                player.saveInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

        of.tryRemTag(tag);

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

        of.tryAddNewTag(tag);

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
        for (String tag : of.tagList){
            if (i <= of.tagList.size()) {
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

    public static String getIgnored(Player stat){
        StringBuilder ignored = new StringBuilder();

        int i = 1;

        for (String uuid : stat.ignoredList) {
            if (i <= stat.ignoredList.size()) {
                ignored.append(MessageConfUtils.ignoreListLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(uuid)))
                );
            } else {
                ignored.append(MessageConfUtils.ignoreListNLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(uuid)))
                );
            }

            i ++;
        }

        return ignored.toString();
    }

    public static String getFriended(Player stat){
        StringBuilder thing = new StringBuilder();

        int i = 1;

        for (String uuid : stat.friendList) {
            if (i <= stat.friendList.size()) {
                thing.append(MessageConfUtils.friendListFLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(uuid)))
                );
            } else {
                thing.append(MessageConfUtils.friendListFNLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(uuid)))
                );
            }

            i ++;
        }

        return thing.toString();
    }

    public static String getPTFriended(Player stat){
        StringBuilder thing = new StringBuilder();

        int i = 1;

        for (String uuid : stat.pendingToFriendList) {
            if (i <= stat.pendingToFriendList.size()) {
                thing.append(MessageConfUtils.friendListPTLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(uuid)))
                );
            } else {
                thing.append(MessageConfUtils.friendListPTNLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(uuid)))
                );
            }

            i ++;
        }

        return thing.toString();
    }

    public static String getPFFriended(Player stat){
        StringBuilder thing = new StringBuilder();

        int i = 1;

        for (String uuid : stat.pendingFromFriendList) {
            if (i <= stat.pendingFromFriendList.size()) {
                thing.append(MessageConfUtils.friendListPFLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(uuid)))
                );
            } else {
                thing.append(MessageConfUtils.friendListPFNLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(uuid)))
                );
            }

            i ++;
        }

        return thing.toString();
    }

    public static void doMessage(Player from, Player to, String message, boolean reply){
        if (! to.online) {
            MessagingUtils.sendBUserMessage(from, MessageConfUtils.noPlayer);
            return;
        }

        switch (ConfigUtils.messReplyTo) {
            case "sent-to":
                from.updateLastTo(to);
                break;
            case "sent-from":
                to.updateLastMessenger(from);
                break;
            default:
                from.updateLastTo(to);
                to.updateLastMessenger(from);
                break;
        }

        from.updateLastToMessage(message);

        if (reply) {
            MessagingUtils.sendBUserMessage(from, MessageConfUtils.replySender
                    .replace("%from%", getOffOnDisplayBungee(from))
                    .replace("%to%", getOffOnDisplayBungee(to))
                    .replace("%message%", message)
            );

            MessagingUtils.sendBUserMessage(to, MessageConfUtils.replyTo
                    .replace("%from%", getOffOnDisplayBungee(from))
                    .replace("%to%", getOffOnDisplayBungee(to))
                    .replace("%message%", message)
            );

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                if (! player.hasPermission(ConfigUtils.messViewPerm)) continue;

                MessagingUtils.sendBUserMessage(player, MessageConfUtils.replySSPY
                        .replace("%from%", getOffOnDisplayBungee(from))
                        .replace("%to%", getOffOnDisplayBungee(to))
                        .replace("%message%", message)
                );
            }
        } else {
            MessagingUtils.sendBUserMessage(from, MessageConfUtils.messageSender
                    .replace("%from%", getOffOnDisplayBungee(from))
                    .replace("%to%", getOffOnDisplayBungee(to))
                    .replace("%message%", message)
            );

            MessagingUtils.sendBUserMessage(to, MessageConfUtils.messageTo
                    .replace("%from%", getOffOnDisplayBungee(from))
                    .replace("%to%", getOffOnDisplayBungee(to))
                    .replace("%message%", message)
            );

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                if (! player.hasPermission(ConfigUtils.messViewPerm)) continue;

                MessagingUtils.sendBUserMessage(player, MessageConfUtils.messageSSPY
                        .replace("%from%", getOffOnDisplayBungee(from))
                        .replace("%to%", getOffOnDisplayBungee(to))
                        .replace("%message%", message)
                );
            }
        }
    }

    public static void doMessageWithIgnoreCheck(Player from, Player to, String message, boolean reply){
        if (! to.online) {
            MessagingUtils.sendBUserMessage(from, MessageConfUtils.noPlayer);
            return;
        }

        if (to.ignoredList.contains(from.uuid)) {
            MessagingUtils.sendBUserMessage(from, MessageConfUtils.messageIgnored);
            return;
        }

        switch (ConfigUtils.messReplyTo) {
            case "sent-to":
                from.updateLastTo(to);
                break;
            case "sent-from":
                to.updateLastMessenger(from);
                break;
            default:
                from.updateLastTo(to);
                to.updateLastMessenger(from);
                break;
        }

        from.updateLastToMessage(message);

        if (reply) {
            MessagingUtils.sendBUserMessage(from, MessageConfUtils.replySender
                    .replace("%from%", getOffOnDisplayBungee(from))
                    .replace("%from_server%", from.getServer().getInfo().getName())
                    .replace("%to%", getOffOnDisplayBungee(to))
                    .replace("%to_server%", to.getServer().getInfo().getName())
                    .replace("%message%", message)
            );

            MessagingUtils.sendBUserMessage(to, MessageConfUtils.replyTo
                    .replace("%from%", getOffOnDisplayBungee(from))
                    .replace("%from_server%", from.getServer().getInfo().getName())
                    .replace("%to%", getOffOnDisplayBungee(to))
                    .replace("%to_server%", to.getServer().getInfo().getName())
                    .replace("%message%", message)
            );

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getStat(player);

                if (p == null) {
                    if (PlayerUtils.exists(player.getName())) {
                        PlayerUtils.addStat(new Player(player, false));
                    } else {
                        PlayerUtils.createStat(player);
                    }
                    p = PlayerUtils.getStat(player);
                    if (p == null) {
                        StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + player.getName());
                        continue;
                    }
                }

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;

                MessagingUtils.sendBUserMessage(player, MessageConfUtils.replySSPY
                        .replace("%from%", getOffOnDisplayBungee(from))
                        .replace("%from_server%", from.getServer().getInfo().getName())
                        .replace("%to%", getOffOnDisplayBungee(to))
                        .replace("%to_server%", to.getServer().getInfo().getName())
                        .replace("%message%", message)
                );
            }
        } else {
            MessagingUtils.sendBUserMessage(from, MessageConfUtils.messageSender
                    .replace("%from%", getOffOnDisplayBungee(from))
                    .replace("%from_server%", from.getServer().getInfo().getName())
                    .replace("%to%", getOffOnDisplayBungee(to))
                    .replace("%to_server%", to.getServer().getInfo().getName())
                    .replace("%message%", message)
            );

            MessagingUtils.sendBUserMessage(to, MessageConfUtils.messageTo
                    .replace("%from%", getOffOnDisplayBungee(from))
                    .replace("%from_server%", from.getServer().getInfo().getName())
                    .replace("%to%", getOffOnDisplayBungee(to))
                    .replace("%to_server%", to.getServer().getInfo().getName())
                    .replace("%message%", message)
            );

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getStat(player);

                if (p == null) {
                    if (PlayerUtils.exists(player.getName())) {
                        PlayerUtils.addStat(new Player(player, false));
                    } else {
                        PlayerUtils.createStat(player);
                    }
                    p = PlayerUtils.getStat(player);
                    if (p == null) {
                        StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + player.getName());
                        continue;
                    }
                }

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;

                MessagingUtils.sendBUserMessage(player, MessageConfUtils.messageSSPY
                        .replace("%from%", getOffOnDisplayBungee(from))
                        .replace("%from_server%", from.getServer().getInfo().getName())
                        .replace("%to%", getOffOnDisplayBungee(to))
                        .replace("%to_server%", to.getServer().getInfo().getName())
                        .replace("%message%", message)
                );
            }
        }
    }

    public static String checkIfBanned(String uuid) {
        Configuration bans = StreamLine.bans.getBans();

        if (bans.contains(uuid)) {
            if (! bans.getBoolean(uuid + ".banned")) return null;

            String reason = bans.getString(uuid + ".reason");
            String bannedMillis = bans.getString(uuid + ".till");
            if (bannedMillis == null) bannedMillis = "";
            Date date = new Date();

            if (! bannedMillis.equals("")) {
                date = new Date(Long.parseLong(bannedMillis));

                if (date.before(new Date())) {
                    bans.set(uuid + ".banned", false);
                    StreamLine.bans.saveConfig();
                    return null;
                }
            }


            if (bannedMillis.equals("")) {
                return TextUtils.codedString(MessageConfUtils.punBannedPerm
                        .replace("%reason%", reason)
                );
            } else {
                return TextUtils.codedString(MessageConfUtils.punBannedTemp
                        .replace("%reason%", reason)
                        .replace("%date%", date.toString())
                );
            }
        }

        return null;
    }

    public static boolean checkIfMuted(ProxiedPlayer sender, Player stat){
        if (stat.mutedTill != null) {
            if (stat.mutedTill.before(new Date())) {
                stat.setMuted(false);
                stat.removeMutedTill();
                return false;
            }
        }

        if (stat.mutedTill != null) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.punMutedTemp.replace("%date%", stat.mutedTill.toString()));
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.punMutedPerm);
        }
        return true;
    }

    // No stats.
    public static final String noStatsFound = message.getString("stats.no-stats");
    // Not high enough permissions.
    public static final String noPermission = message.getString("stats.no-permission");
    // Create.
    public static final String create = message.getString("stats.create");
    // Info.
    public static final String info = message.getString("stats.info");
    public static final String tagsLast = message.getString("stats.tags.last");
    public static final String tagsNLast = message.getString("stats.tags.not-last");
    public static final String ipsLast = message.getString("stats.ips.last");
    public static final String ipsNLast = message.getString("stats.ips.not-last");
    public static final String namesLast = message.getString("stats.names.last");
    public static final String namesNLast = message.getString("stats.names.not-last");
    public static final String sspyT = message.getString("stats.sspy.true");
    public static final String sspyF = message.getString("stats.sspy.false");
    public static final String gspyT = message.getString("stats.gspy.true");
    public static final String gspyF = message.getString("stats.gspy.false");
    public static final String pspyT = message.getString("stats.pspy.true");
    public static final String pspyF = message.getString("stats.pspy.false");
    public static final String onlineT = message.getString("stats.online.true");
    public static final String onlineF = message.getString("stats.online.false");
    public static final String notSet = message.getString("stats.not-set");
    // Tags.
    public static final String tagRem = message.getString("btag.remove");
    public static final String tagAdd = message.getString("btag.add");
    public static final String tagListMain = message.getString("btag.list.main");
    public static final String tagListLast = message.getString("btag.list.tags.last");
    public static final String tagListNotLast = message.getString("btag.list.tags.not-last");
    // Points.
    public static final String pointsName = message.getString("stats.points-name");
}
