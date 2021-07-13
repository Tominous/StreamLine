package net.plasmere.streamline.utils;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.users.ConsolePlayer;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.objects.users.SavableUser;
import net.plasmere.streamline.objects.lists.SingleSet;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;

public class PlayerUtils {
    private static final List<SavableUser> stats = new ArrayList<>();

    private static HashMap<Player, SingleSet<Integer, Integer>> connections = new HashMap<>();

    public static ConsolePlayer applyConsole(){
        return applyConsole(new ConsolePlayer(false));
    }

    public static ConsolePlayer applyConsole(ConsolePlayer console){
        addStat(console);

        return console;
    }

    public static List<SavableUser> getStats() {
        return stats;
    }

    public static List<Player> getJustPlayers(){
        List<Player> players = new ArrayList<>();

        for (SavableUser user : stats) {
            if (user instanceof Player) {
                players.add((Player) user);
            }
        }

        return players;
    }

    public static List<ConsolePlayer> getJustProxies(){
        List<ConsolePlayer> proxies = new ArrayList<>();

        for (SavableUser user : stats) {
            if (user instanceof ConsolePlayer) {
                proxies.add((ConsolePlayer) user);
            }
        }

        return proxies;
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

    public static Player getOffOnPlayer(String name){
        Player p = getPlayerStat(name);

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

    public static ConsolePlayer getOffOnConsolePlayer(String name){
        ConsolePlayer p = getConsoleStat(name);

        if (p == null) {
            if (exists(name)) {
                p = new ConsolePlayer();
                addStat(p);
                return p;
            } else {
                return null;
            }
        }

        return p;
    }

    public static void removePlayerIf(Predicate<SavableUser> predicate){
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

    public static void removeSecondFromConn(Player player, SingleSet<Integer, Integer> conn){
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

    public static Player getOrCreateByUUID(String uuid){
//        if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info(forStats(stats));

        Player player = getPlayerByUUID(uuid);

        if (player == null) {
            player = new Player(uuid);
            addStat(player);
        }

        return player;
    }

    public static Player getOrCreate(String name){
        Player player = getPlayerStat(name);

        if (player == null) {
            player = new Player(UUIDFetcher.getCachedUUID(name));
            addStat(player);
        }

        return player;
    }

    public static Player getOrCreate(ProxiedPlayer player){
        return getOrCreateByUUID(player.getUniqueId().toString());
    }

    public static Player getOrCreate(CommandSender sender){
        return getOrCreate(sender.getName());
    }

    public static SavableUser getOrCreateStat(String name){
        if (name.equals("%")) {
            return getOrCreateStatByUUID(name);
        }

        SavableUser player = getStat(name);

        if (player == null) {
            if (isInOnlineList(name)) {
                addStat(new Player(UUIDFetcher.getCachedUUID(name)));
            } else {
                applyConsole();
            }
        }

        return player;
    }

    public static SavableUser getOrCreateStat(CommandSender sender){
        return getOrCreateStat(sender.getName());
    }

    public static SavableUser getOrCreateStat(ProxiedPlayer sender){
        return getOrCreateStat(sender.getName());
    }

    public static SavableUser getOrCreateStatByUUID(String uuid){
        SavableUser player = getStatByUUID(uuid);

        if (player == null) {
            addStat(uuid);
        }

        return player;
    }

    public static boolean isInOnlineList(String name){
        for (ProxiedPlayer player : getPlayers()) {
            if (player.getName().equals(name)) return true;
        }

        return false;
    }

    public static Collection<ProxiedPlayer> getPlayers(){
        return StreamLine.getInstance().getProxy().getPlayers();
    }

    public static ProxiedPlayer getPlayer(UUID uuid) {
        return getPlayer(uuid.toString());
    }

    public static ProxiedPlayer getPlayer(String string){
        if (string.contains("-")) {
            return StreamLine.getInstance().getProxy().getPlayer(UUID.fromString(string));
        }

        return StreamLine.getInstance().getProxy().getPlayer(string);
    }

    public static boolean isNameEqual(SavableUser player, String name){
        if (player.latestName == null) return false;

        return player.latestName.equals(name);
    }

    public static String forStats(List<SavableUser> players){
        StringBuilder builder = new StringBuilder("[");

        int i = 1;
        for (SavableUser p : players){
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
        try {
            for (Player stat : getJustPlayers()) {
                if (isNameEqual(stat, player.getName())) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ConsolePlayer getStat() {
        try {
            for (ConsolePlayer stat : getJustProxies()) {
                if (isNameEqual(stat, ConfigUtils.consoleName)) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SavableUser getStat(CommandSender player) {
        try {
            for (SavableUser stat : stats) {
                if (isNameEqual(stat, player.getName())) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Player getPlayerStat(CommandSender player) {
        try {
            for (Player stat : getJustPlayers()) {
                if (isNameEqual(stat, player.getName())) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SavableUser getStat(String name) {
        try {
            for (SavableUser stat : stats) {
                if (isNameEqual(stat, name)) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Player getPlayerStat(String name) {
        try {
            for (Player stat : getJustPlayers()) {
                if (isNameEqual(stat, name)) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ConsolePlayer getConsoleStat(String name) {
        try {
            for (ConsolePlayer stat : getJustProxies()) {
                if (isNameEqual(stat, name)) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ConsolePlayer getConsoleStat(CommandSender sender) {
        try {
            for (ConsolePlayer stat : getJustProxies()) {
                if (sender.equals(stat.sender)) return stat;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Player getPlayerByUUID(String uuid) {
        try {
            return (Player) getStatByUUID(uuid);
        } catch (Exception e) {
            return null;
        }
    }

    public static SavableUser getStatByUUID(String uuid) {
        try {
            for (SavableUser stat : stats) {
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
            ps.add(PlayerUtils.getOrCreate(player));
        }

        return ps;
    }

    public static void updateDisplayName(Player player){
        if (! ConfigUtils.updateDisplayNames) return;
        if (! StreamLine.lpHolder.enabled) return;

        player.setDisplayName(getDisplayName(player));
    }

    public static String getDisplayName(Player player) {
        return getDisplayName(player.latestName);
    }

    public static String getDisplayName(String username) {
        if (! StreamLine.lpHolder.enabled) return username;

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

    public static String createCheck(String thing){
        if (thing.contains("-")){
            return thing;
        } else {
            return Objects.requireNonNull(UUIDFetcher.getCachedUUID(thing));
        }
    }

    public static int getCeilingInt(Set<Integer> ints){
        int value = 0;

        for (Integer i : ints) {
            if (i >= value) value = i;
        }

        return value;
    }

    public static String getOffOnDisplayBungee(SavableUser stat){
        if (stat == null) {
            return MessageConfUtils.nullB;
        }

        if (stat instanceof ConsolePlayer) {
            return ConfigUtils.consoleDisplayName;
        }

        if (stat instanceof Player) {
            if (((Player) stat).online) {
                return MessageConfUtils.onlineB.replace("%player%", stat.displayName);
            } else {
                return MessageConfUtils.offlineB.replace("%player%", stat.displayName);
            }
        }

        return MessageConfUtils.nullB;
    }

    public static String getOffOnRegBungee(SavableUser stat){
        if (stat == null) {
            return MessageConfUtils.nullB;
        }

        if (stat instanceof ConsolePlayer) {
            return ConfigUtils.consoleName;
        }

        if (stat instanceof Player) {
            if (((Player) stat).online) {
                return MessageConfUtils.onlineB.replace("%player%", stat.latestName);
            } else {
                return MessageConfUtils.offlineB.replace("%player%", stat.latestName);
            }
        }

        return MessageConfUtils.nullB;
    }

    public static String getOffOnDisplayDiscord(SavableUser stat){
        if (stat == null) {
            return MessageConfUtils.nullD;
        }

        if (stat instanceof ConsolePlayer) {
            return ConfigUtils.consoleDisplayName;
        }

        if (stat instanceof Player) {
            if (((Player) stat).online) {
                return MessageConfUtils.onlineD.replace("%player%", stat.displayName);
            } else {
                return MessageConfUtils.offlineD.replace("%player%", stat.displayName);
            }
        }

        return MessageConfUtils.nullD;
    }

    public static String getOffOnRegDiscord(SavableUser stat){
        if (stat == null) {
            return MessageConfUtils.nullD;
        }

        if (stat instanceof ConsolePlayer) {
            return ConfigUtils.consoleName;
        }

        if (stat instanceof Player) {
            if (((Player) stat).online) {
                return MessageConfUtils.onlineD.replace("%player%", stat.latestName);
            } else {
                return MessageConfUtils.offlineD.replace("%player%", stat.latestName);
            }
        }

        return MessageConfUtils.nullD;
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

    public static void addStat(String uuid){
        if (isInStatsListByUUID(uuid)) return;

        if (uuid.equals("%")) {
            applyConsole();
        } else {
            stats.add(new Player(uuid));
        }
    }

    public static void addStat(Player stat){
        if (isInStatsList(stat)) return;

        stats.add(stat);
    }

    public static void addStat(ConsolePlayer stat){
        if (isInStatsList(stat)) return;

        stats.add(stat);
    }

    public static boolean isInStatsListByUUID(String uuid) {
        for (SavableUser user : getStats()) {
            if (user.uuid.equals(uuid)) return true;
        }

        return false;
    }

    public static boolean isInStatsList(Player stat) {
        return isInStatsList(stat.latestName);
    }

    public static boolean isInStatsList(ConsolePlayer stat) {
        for (ConsolePlayer player : getJustProxies()) {
            if (stat.equals(player)) return true;
            if (player.latestName.equals(stat.latestName)) return true;
        }

        return false;
    }

    public static boolean isInStatsList(String username) {
        for (SavableUser player : getStats()) {
            if (player.latestName == null) continue;
            if (player.latestName.equals(username)) return true;
        }

        return false;
    }

    public static boolean isOnline(String username){
        if (isInStatsList(username)) {
            Player player = getPlayerStat(username);
            if (player != null) {
                return player.online;
            }
        }

        for (ProxiedPlayer p : StreamLine.getInstance().getProxy().getPlayers()) {
            if (p.getName().equals(username)) return true;
        }

        return false;
    }

    public static void removeStat(SavableUser stat){
        List<SavableUser> toRemove = new ArrayList<>();

        for (SavableUser player : getStats()) {
            if (player.latestName == null) {
                toRemove.add(player);
                continue;
            }

            if (player.latestName.equals(stat.latestName)) {
                toRemove.add(player);
            }
        }

        for (SavableUser player : toRemove) {
            stats.remove(player);
            try {
                stat.saveInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveAll(){
        for (SavableUser player : getStats()) {
            try {
                player.saveInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int removeOfflineStats(){
        int count = 0;
        List<Player> players = PlayerUtils.getJustPlayers();
        List<Player> toRemove = new ArrayList<>();

        for (Player player : players) {
            if (! player.online) {
                toRemove.add(player);
            }
        }

        for (Player player : toRemove) {
            try {
                player.saveInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PlayerUtils.removeStat(player);

            count ++;
        }

        return count;
    }

    public static void info(CommandSender sender, Player of){
        if (! sender.hasPermission(ConfigUtils.comBStatsPerm)) {
            MessagingUtils.sendBUserMessage(sender, noPermission);
        }

        MessagingUtils.sendStatUserMessage(of, sender, info);
    }

    public static void remTag(CommandSender sender, SavableUser of, String tag){
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

    public static void addTag(CommandSender sender, SavableUser of, String tag){
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

    public static void listTags(CommandSender sender, SavableUser of){
        if (! sender.hasPermission(ConfigUtils.comBBTagPerm)) {
            MessagingUtils.sendBUserMessage(sender, noPermission);
            return;
        }

        MessagingUtils.sendBUserMessage(sender, tagListMain
                .replace("%player%", getOffOnDisplayBungee(of))
                .replace("%tags%", compileTagList(of))
        );
    }

    public static String compileTagList(SavableUser of) {
        StringBuilder stringBuilder = new StringBuilder();

        int i = 1;
        for (String tag : of.tagList){
            if (i < of.tagList.size()) {
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

    public static String getIgnored(SavableUser stat){
        StringBuilder ignored = new StringBuilder();

        int i = 1;

        for (String uuid : stat.ignoredList) {
            if (i < stat.ignoredList.size()) {
                ignored.append(MessageConfUtils.ignoreListNLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateByUUID(uuid)))
                );
            } else {
                ignored.append(MessageConfUtils.ignoreListLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateByUUID(uuid)))
                );
            }

            i ++;
        }

        return ignored.toString();
    }

    public static String getFriended(SavableUser stat){
        StringBuilder thing = new StringBuilder();

        int i = 1;

        for (String uuid : stat.friendList) {
            if (i < stat.friendList.size()) {
                thing.append(MessageConfUtils.friendListFNLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateByUUID(uuid)))
                );
            } else {
                thing.append(MessageConfUtils.friendListFLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateByUUID(uuid)))
                );
            }

            i ++;
        }

        return thing.toString();
    }

    public static String getPTFriended(SavableUser stat){
        StringBuilder thing = new StringBuilder();

        int i = 1;

        for (String uuid : stat.pendingToFriendList) {
            if (i < stat.pendingToFriendList.size()) {
                thing.append(MessageConfUtils.friendListPTNLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateByUUID(uuid)))
                );
            } else {
                thing.append(MessageConfUtils.friendListPTLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateByUUID(uuid)))
                );
            }

            i ++;
        }

        return thing.toString();
    }

    public static String getPFFriended(SavableUser stat){
        StringBuilder thing = new StringBuilder();

        int i = 1;

        for (String uuid : stat.pendingFromFriendList) {
            if (i < stat.pendingFromFriendList.size()) {
                thing.append(MessageConfUtils.friendListPFNLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateByUUID(uuid)))
                );
            } else {
                thing.append(MessageConfUtils.friendListPFLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateByUUID(uuid)))
                );
            }

            i ++;
        }

        return thing.toString();
    }

    public static void doMessage(SavableUser from, SavableUser to, String message, boolean reply){
        if (to instanceof Player) {
            if (! ((Player) to).online) {
                MessagingUtils.sendBUserMessage(from.sender, MessageConfUtils.noPlayer);
                return;
            }
        }

        from.updateLastTo(to);
        to.updateLastFrom(from);

        switch (ConfigUtils.messReplyTo) {
            case "sent-to":
                from.updateReplyTo(to);
                break;
            case "sent-from":
                to.updateReplyTo(from);
                break;
            case "very-last":
            default:
                from.updateReplyTo(to);
                to.updateReplyTo(from);
                break;
        }

        from.updateLastToMessage(message);
        to.updateLastFromMessage(message);

        if (reply) {
            MessagingUtils.sendBMessagenging(from.sender, from, to, message, MessageConfUtils.replySender);

            MessagingUtils.sendBMessagenging(to.sender, from, to, message, MessageConfUtils.replyTo);

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getOrCreate(player);

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;

                MessagingUtils.sendBMessagenging(player, from, to, message, MessageConfUtils.replySSPY);
            }
        } else {
            MessagingUtils.sendBMessagenging(from.sender, from, to, message, MessageConfUtils.messageSender);

            MessagingUtils.sendBMessagenging(to.sender, from, to, message, MessageConfUtils.messageTo);

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getOrCreate(player);

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;

                MessagingUtils.sendBMessagenging(player, from, to, message, MessageConfUtils.messageSSPY);
            }
        }
    }

    public static void doMessageWithIgnoreCheck(SavableUser from, SavableUser to, String message, boolean reply){
        if (to instanceof Player) {
            if (! ((Player) to).online) {
                MessagingUtils.sendBUserMessage(from.sender, MessageConfUtils.noPlayer);
                return;
            }
        }

        if (to.ignoredList.contains(from.uuid)) {
            MessagingUtils.sendBUserMessage(from.sender, MessageConfUtils.messageIgnored);
            return;
        }

        from.updateLastTo(to);
        to.updateLastFrom(from);

        switch (ConfigUtils.messReplyTo) {
            case "sent-to":
                from.updateReplyTo(to);
                break;
            case "sent-from":
                to.updateReplyTo(from);
                break;
            case "very-last":
            default:
                from.updateReplyTo(to);
                to.updateReplyTo(from);
                break;
        }

        from.updateLastToMessage(message);
        to.updateLastFromMessage(message);

        if (reply) {
            MessagingUtils.sendBMessagenging(from.sender, from, to, message, MessageConfUtils.replySender);

            MessagingUtils.sendBMessagenging(to.sender, from, to, message, MessageConfUtils.replyTo);

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getOrCreate(player);

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;

                MessagingUtils.sendBMessagenging(player, from, to, message, MessageConfUtils.replySSPY);
            }
        } else {
            MessagingUtils.sendBMessagenging(from.sender, from, to, message, MessageConfUtils.messageSender);

            MessagingUtils.sendBMessagenging(to.sender, from, to, message, MessageConfUtils.messageTo);

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getOrCreate(player);

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;

                MessagingUtils.sendBMessagenging(player, from, to, message, MessageConfUtils.messageSSPY);
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
        checkAndUpdateIfMuted(stat);

        if (stat.mutedTill != null) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.punMutedTemp.replace("%date%", stat.mutedTill.toString()));
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.punMutedPerm);
        }
        return true;
    }

    public static void checkAndUpdateIfMuted(Player stat){
        if (stat.mutedTill != null) {
            if (stat.mutedTill.before(Date.from(Instant.now()))) {
                stat.setMuted(false);
                stat.removeMutedTill();
            }
        }
    }

    public static void kickAll(boolean withMessage) {
        if (withMessage) {
            for (ProxiedPlayer player : getPlayers()) {
                kick(player, MessageConfUtils.kicksStopping);
            }
        } else {
            for (ProxiedPlayer player : getPlayers()) {
                kick(player);
            }
        }
    }

    public static ProxiedPlayer getPPlayerByUUID(String uuid){
        try {
            if (StreamLine.geyserHolder.enabled) {
                if (StreamLine.geyserHolder.file.hasProperty(uuid)) {
                    return StreamLine.geyserHolder.getPPlayerByUUID(uuid);
                }
            }

            return getPlayer(UUID.fromString(uuid));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void kick(ProxiedPlayer player) {
        if (player != null) player.disconnect();
    }

    public static void kick(ProxiedPlayer player, String message) {
        if (player != null) player.disconnect(TextUtils.codedText(message));
    }

    public static void kick(Player player, String message) {
        if (! player.online) return;
        ProxiedPlayer pp = PlayerUtils.getPPlayerByUUID(player.uuid);
        if (pp != null) pp.disconnect(TextUtils.codedText(message));
    }

    // No stats.
    public static final String noStatsFound = StreamLine.getConfig().getMessString("stats.no-stats");
    // Not high enough permissions.
    public static final String noPermission = StreamLine.getConfig().getMessString("stats.no-permission");
    // Create.
    public static final String create = StreamLine.getConfig().getMessString("stats.create");
    // Info.
    public static final String info = StreamLine.getConfig().getMessString("stats.info");
    public static final String tagsLast = StreamLine.getConfig().getMessString("stats.tags.last");
    public static final String tagsNLast = StreamLine.getConfig().getMessString("stats.tags.not-last");
    public static final String ipsLast = StreamLine.getConfig().getMessString("stats.ips.last");
    public static final String ipsNLast = StreamLine.getConfig().getMessString("stats.ips.not-last");
    public static final String namesLast = StreamLine.getConfig().getMessString("stats.names.last");
    public static final String namesNLast = StreamLine.getConfig().getMessString("stats.names.not-last");
    public static final String sspyT = StreamLine.getConfig().getMessString("stats.sspy.true");
    public static final String sspyF = StreamLine.getConfig().getMessString("stats.sspy.false");
    public static final String gspyT = StreamLine.getConfig().getMessString("stats.gspy.true");
    public static final String gspyF = StreamLine.getConfig().getMessString("stats.gspy.false");
    public static final String pspyT = StreamLine.getConfig().getMessString("stats.pspy.true");
    public static final String pspyF = StreamLine.getConfig().getMessString("stats.pspy.false");
    public static final String onlineT = StreamLine.getConfig().getMessString("stats.online.true");
    public static final String onlineF = StreamLine.getConfig().getMessString("stats.online.false");
    public static final String notSet = StreamLine.getConfig().getMessString("stats.not-set");
    // Tags.
    public static final String tagRem = StreamLine.getConfig().getMessString("btag.remove");
    public static final String tagAdd = StreamLine.getConfig().getMessString("btag.add");
    public static final String tagListMain = StreamLine.getConfig().getMessString("btag.list.main");
    public static final String tagListLast = StreamLine.getConfig().getMessString("btag.list.tags.last");
    public static final String tagListNotLast = StreamLine.getConfig().getMessString("btag.list.tags.not-last");
    // Points.
    public static final String pointsName = StreamLine.getConfig().getMessString("stats.points-name");
}
