package net.plasmere.streamline.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;
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
    /* ----------------------------

    PlayerUtils <-- Setup.

    ---------------------------- */

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

    /* ----------------------------

    PlayerUtils <-- Utils.

    ---------------------------- */

    public static boolean isNameEqual(SavableUser user, String name){
        if (user.latestName == null) return false;

        return user.latestName.equals(name);
    }

    public static boolean hasStat(String latestName){
        return getSavableUser(latestName) != null;
    }

    public static void removePlayerIf(Predicate<SavableUser> predicate){
        stats.removeIf(predicate);
    }

    public static String createCheck(String thing){
        if (thing.contains("-")){
            return thing;
        } else {
            return Objects.requireNonNull(UUIDFetcher.getCachedUUID(thing));
        }
    }

    public static boolean exists(String username){
        if (username.equals("%")) return existsByUUID(username);

        return existsByUUID(UUIDFetcher.getCachedUUID(username));
    }

    public static boolean existsByUUID(String uuid){
        if (uuid.equals("%")) return getConsoleStat().file.exists();

        return new File(StreamLine.getInstance().getPlDir(), uuid + ".properties").exists();
    }

    public static boolean isStats(Player stat){
        return stats.contains(stat);
    }

    public static void reloadStats(Player stat) {
        stats.remove(getSavableUser(stat.latestName));
        stats.add(stat);
    }

    public static void addStat(String uuid){
        if (isInStatsListByUUID(uuid)) return;

        if (uuid.equals("%")) {
            applyConsole();
        } else {
            stats.add(new Player(uuid, true));
        }
    }

    public static SavableUser addStat(SavableUser stat){
        if (isInStatsList(stat)) return getSavableUser(stat.latestName);

        stats.add(stat);

        return stat;
    }

    public static Player addStat(Player stat){
        if (isInStatsList(stat)) return getPlayerStat(stat.latestName);

        stats.add(stat);

        return stat;
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

    public static boolean isInStatsList(SavableUser stat) {
        return isInStatsList(stat.latestName);
    }

    public static boolean isInStatsListByIP(String ip) {
        for (Player player : getJustPlayers()) {
            for (String IP : player.ipList) {
                if (IP.equals(ip)) return true;
            }
        }

        return false;
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

    public static String getNameFromString(String thing) {
        if (thing.equals("%")) return getConsoleStat().latestName;

        if (thing.contains("-")) return UUIDFetcher.getCachedName(thing);
        else return thing;
    }

    public static String getUUIDFromString(String thing) {
        if (thing.equals("%")) return thing;

        return UUIDFetcher.getCachedUUID(thing);
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

    public static String checkIfIPBanned(String ip) {
        Configuration bans = StreamLine.bans.getBans();

        String bannedIP = ip.replace(".", "_");

        if (bans.contains(bannedIP)) {
            if (! bans.getBoolean(bannedIP + ".banned")) return null;

            String reason = bans.getString(bannedIP + ".reason");
            String bannedMillis = bans.getString(bannedIP + ".till");
            if (bannedMillis == null) bannedMillis = "";
            Date date = new Date();

            if (! bannedMillis.equals("")) {
                date = new Date(Long.parseLong(bannedMillis));

                if (date.before(new Date())) {
                    bans.set(bannedIP + ".banned", false);
                    StreamLine.bans.saveConfig();
                    return null;
                }
            }


            if (bannedMillis.equals("")) {
                return TextUtils.codedString(MessageConfUtils.punIPBannedPerm
                        .replace("%reason%", reason)
                );
            } else {
                return TextUtils.codedString(MessageConfUtils.punIPBannedTemp
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

    public static boolean hasOfflinePermission(String permission, String uuid){
        if (! StreamLine.lpHolder.enabled) {
            StreamLine.getInstance().getLogger().info("Tried to do an offline permissions check, but failed due to not having LuckPerms installed!");
            return false;
        }

        LuckPerms api = StreamLine.lpHolder.api;

        User user = api.getUserManager().getUser(UUID.fromString(uuid));

        if (user == null) return false;

        for (PermissionNode node : user.resolveInheritedNodes(NodeType.PERMISSION, QueryOptions.builder(QueryMode.NON_CONTEXTUAL).build())) {
            if (node.getPermission().equals(permission)) return true;
        }

        for (PermissionNode node : user.getNodes(NodeType.PERMISSION)) {
            if (node.getPermission().equals(permission)) return true;
        }

        Group group = api.getGroupManager().getGroup(user.getPrimaryGroup());

        if (group == null) return false;

        for (PermissionNode node : group.getNodes(NodeType.PERMISSION)) {
            if (node.getPermission().equals(permission)) return true;
        }

        return false;
    }

    /* ----------------------------

    PlayerUtils <-- Lists.

    ---------------------------- */

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

    public static List<Player> getPlayerStatsByIP(String ip) {
        List<Player> players = new ArrayList<>();

        for (Player player : getJustPlayers()) {
            for (String IP : player.ipList) {
                if (IP.equals(ip)) players.add(player);
            }
        }

        return players;
    }

    /* ----------------------------

    PlayerUtils <-- Creates.

    ---------------------------- */

    // SavableUsers.

    public static SavableUser createSavableUser(String thing){
        if (thing.equals("%")) return getConsoleStat();

        if (thing.contains("-")) {
            return createPlayerStatByUUID(thing);
        } else {
            return createPlayerStat(thing);
        }
    }

    // Player Stats.

    public static Player createPlayerStat(ProxiedPlayer player) {
        Player stat = addStat(new Player(player, true));

        if (ConfigUtils.statsTell) {
            MessagingUtils.sendStatUserMessage(stat, player, create);
        }

        return stat;
    }

    public static Player createPlayerStat(CommandSender sender) {
        return createPlayerStat(sender.getName());
    }

    public static Player createPlayerStat(String name) {
        Player stat = addStat(new Player(UUIDFetcher.getCachedUUID(name), true));

        if (ConfigUtils.statsTell && stat.online) {
            MessagingUtils.sendStatUserMessage(stat, stat.player, create);
        }

        return stat;
    }

    public static Player createPlayerStatByUUID(String uuid) {
        Player stat = addStat(new Player(uuid, true));

        if (ConfigUtils.statsTell && stat.online) {
            MessagingUtils.sendStatUserMessage(stat, stat.player, create);
        }

        return stat;
    }

    /* ----------------------------

    PlayerUtils <-- Single Gets.

    ---------------------------- */

    // SavableUsers.

    public static SavableUser getSavableUser(String name) {
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

    public static SavableUser getSavableUserByUUID(String uuid) {
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

    public static SavableUser getSUFromNameOrUUID(String string) {
        if (string.equals("%")) return getConsoleStat(StreamLine.getInstance().getProxy().getConsole());

        if (string.contains("-")) return getSavableUserByUUID(string);
        else return getSavableUser(string);
    }

    // ConsolePlayers.

    public static ConsolePlayer getConsoleStat() {
        return PlayerUtils.getConsoleStat(StreamLine.getInstance().getProxy().getConsole());
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

    // Player Stats.

    public static Player getPlayerStat(CommandSender sender) {
        try {
            for (Player stat : getJustPlayers()) {
                if (sender.equals(stat.sender)) {
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

    public static Player getPlayerStatByUUID(String uuid) {
        try {
            for (Player stat : getJustPlayers()) {
                if (stat.uuid.equals(uuid)) {
                    return stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ----------------------
    // Get or create!
    // ----------------------

    // SavableUsers.

    public static SavableUser getOrCreateSavableUser(String name){
        if (name.equals("%")) {
            return getOrCreateSUByUUID(name);
        }

        SavableUser user = getOrGetSavableUser(name);

        if (user == null) {
            if (isInOnlineList(name)) {
                user = createPlayerStat(getPPlayer(name));
            } else {
                user = createPlayerStat(name);
            }
        }

        return user;
    }

    public static SavableUser getOrCreateSavableUser(CommandSender sender){
        return getOrCreateSavableUser(sender.getName());
    }

    public static SavableUser getOrCreateSavableUser(ProxiedPlayer sender){
        return getOrCreateSavableUser(sender.getName());
    }

    public static SavableUser getOrCreateSUByUUID(String uuid){
        SavableUser player = getSavableUserByUUID(uuid);

        if (player == null) {
            player = createSavableUser(uuid);
        }

        return player;
    }

    public static SavableUser getOrCreateSUFromNameOrUUID(String string) {
        if (string.equals("%")) return getConsoleStat(StreamLine.getInstance().getProxy().getConsole());

        if (string.contains("-")) return getOrCreateSUByUUID(string);
        else return getOrCreateSavableUser(string);
    }

    // Players.

    public static Player getOrCreatePlayerStat(ProxiedPlayer pp){
        Player player = getOrGetPlayerStat(pp.getName());

        if (player == null) {
            player = createPlayerStat(pp);
        }

        return player;
    }

    public static Player getOrCreatePlayerStat(CommandSender sender){
        Player player = getOrGetPlayerStat(sender.getName());

        if (player == null) {
            player = createPlayerStat(sender);
        }

        return player;
    }

    public static Player getOrCreatePlayerStat(String name){
        Player player = getOrGetPlayerStat(name);

        if (player == null) {
            player = createPlayerStat(name);
        }

        return  player;
    }

    public static Player getOrCreatePlayerStatByUUID(String uuid) {
        Player player = getOrGetPlayerStatByUUID(uuid);

        if (player == null) {
            player = createPlayerStatByUUID(uuid);
        }

        return player;
    }

    // ----------------------
    // Get or get!
    // ----------------------

    public static SavableUser getOrGetSavableUser(String thing) {
        try {
            if (thing.equals("%")) return getConsoleStat().getSavableUser();

            if (thing.contains("-")) {
                if (existsByUUID(thing)){
                    if (isInStatsListByUUID(thing)) {
                        return getPlayerStatByUUID(thing);
                    } else {
                        return new Player(thing, false);
                    }
                } else return null;
            } else {
                if (exists(thing)){
                    if (isInStatsList(thing)) {
                        return getPlayerStat(thing);
                    } else {
                        return new Player(thing, false);
                    }
                } else return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Player getOrGetPlayerStat(String name) {
        try {
            if (exists(name)){
                if (isInStatsList(name)) {
                    return getPlayerStat(name);
                } else {
                    return new Player(UUIDFetcher.getCachedUUID(name), false);
                }
            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Player getOrGetPlayerStatByUUID(String uuid) {
        try {
            if (existsByUUID(uuid)){
                if (isInStatsListByUUID(uuid)) {
                    return getPlayerStatByUUID(uuid);
                } else {
                    return new Player(uuid, false);
                }
            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /* ----------------------------

    PlayerUtils <-- S Functions.

    ---------------------------- */

    public static void info(CommandSender sender, SavableUser of){
        if (! sender.hasPermission(ConfigUtils.comBStatsPerm)) {
            MessagingUtils.sendBUserMessage(sender, noPermission);
        }

        if (of instanceof ConsolePlayer) {
            MessagingUtils.sendStatUserMessage(of, sender, consolePlayerInfo);
        } else if (of instanceof Player) {
            MessagingUtils.sendStatUserMessage(of, sender, info);
        }
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
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(uuid)))
                );
            } else {
                ignored.append(MessageConfUtils.ignoreListLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(uuid)))
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
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(uuid)))
                );
            } else {
                thing.append(MessageConfUtils.friendListFLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(uuid)))
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
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(uuid)))
                );
            } else {
                thing.append(MessageConfUtils.friendListPTLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(uuid)))
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
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(uuid)))
                );
            } else {
                thing.append(MessageConfUtils.friendListPFLast
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(uuid)))
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
                Player p = PlayerUtils.getOrCreatePlayerStat(player);

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;

                MessagingUtils.sendBMessagenging(player, from, to, message, MessageConfUtils.replySSPY);
            }
        } else {
            MessagingUtils.sendBMessagenging(from.sender, from, to, message, MessageConfUtils.messageSender);

            MessagingUtils.sendBMessagenging(to.sender, from, to, message, MessageConfUtils.messageTo);

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getOrCreatePlayerStat(player);

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
                Player p = PlayerUtils.getOrCreatePlayerStat(player);

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;

                MessagingUtils.sendBMessagenging(player, from, to, message, MessageConfUtils.replySSPY);
            }
        } else {
            MessagingUtils.sendBMessagenging(from.sender, from, to, message, MessageConfUtils.messageSender);

            MessagingUtils.sendBMessagenging(to.sender, from, to, message, MessageConfUtils.messageTo);

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getOrCreatePlayerStat(player);

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;

                MessagingUtils.sendBMessagenging(player, from, to, message, MessageConfUtils.messageSSPY);
            }
        }
    }

    /* ----------------------------

    PlayerUtils <-- Connections.

    ---------------------------- */

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

    /* ----------------------------

    PlayerUtils <-- DisplayNames.

    ---------------------------- */

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

        prefix = preWeight.get(PluginUtils.getCeilingInt(preWeight.keySet()));
        suffix = sufWeight.get(PluginUtils.getCeilingInt(sufWeight.keySet()));

        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        return TextUtils.codedString(prefix + username + suffix);
    }

    /* ----------------------------

    PlayerUtils <-- Get Off / On.

    ---------------------------- */

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

    /* ----------------------------

    PlayerUtils <-- ProxiedPlayer.

    ---------------------------- */

    public static Collection<ProxiedPlayer> getOnlinePPlayers(){
        return StreamLine.getInstance().getProxy().getPlayers();
    }

    public static ProxiedPlayer getPPlayer(UUID uuid) {
        return getPPlayer(uuid.toString());
    }

    public static ProxiedPlayer getPPlayer(String string){
        if (string.contains("-")) {
            return StreamLine.getInstance().getProxy().getPlayer(UUID.fromString(string));
        }

        return StreamLine.getInstance().getProxy().getPlayer(string);
    }

    public static ProxiedPlayer getPPlayerByUUID(String uuid){
        try {
            if (StreamLine.geyserHolder.enabled) {
                if (StreamLine.geyserHolder.file.hasProperty(uuid)) {
                    return StreamLine.geyserHolder.getPPlayerByUUID(uuid);
                }
            }

            return getPPlayer(UUID.fromString(uuid));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /* ----------------------------

    PlayerUtils <-- ???.

    ---------------------------- */

    public static boolean isInOnlineList(String name){
        for (ProxiedPlayer player : getOnlinePPlayers()) {
            if (player.getName().equals(name)) return true;
        }

        return false;
    }

    public static List<Player> transposeList(List<ProxiedPlayer> players){
        List<Player> ps = new ArrayList<>();
        for (ProxiedPlayer player : players){
            ps.add(PlayerUtils.getOrCreatePlayerStatByUUID(player.getUniqueId().toString()));
        }

        return ps;
    }

    /* ----------------------------

    PlayerUtils <-- Functionals.

    ---------------------------- */

    public static void kickAll(boolean withMessage) {
        if (withMessage) {
            for (ProxiedPlayer player : getOnlinePPlayers()) {
                kick(player, MessageConfUtils.kicksStopping);
            }
        } else {
            for (ProxiedPlayer player : getOnlinePPlayers()) {
                kick(player);
            }
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
    public static final String info = StreamLine.getConfig().getMessString("stats.player");
    public static final String consolePlayerInfo = StreamLine.getConfig().getMessString("stats.console-player");
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
}
