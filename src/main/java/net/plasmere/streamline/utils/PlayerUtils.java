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
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.config.Configuration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Guild;
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

    private static final String pathToPlayers = StreamLine.getInstance().getDataFolder() + File.separator + "players" + File.separator;

    private static final List<SavableUser> stats = new ArrayList<>();

    public static HashMap<ProxiedPlayer, SingleSet<Integer, ProxiedPlayer>> teleports = new HashMap<>();

    private static HashMap<Player, SingleSet<Integer, Integer>> connections = new HashMap<>();
    private static TreeMap<String, TreeMap<Integer, SavableUser>> toSave = new TreeMap<>();

    public static ConsolePlayer applyConsole(){
        if (exists("%")) {
            return applyConsole(new ConsolePlayer(false));
        } else {
            return applyConsole(new ConsolePlayer(true));
        }
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
            return Objects.requireNonNull(UUIDUtils.getCachedUUID(thing));
        }
    }

    public static boolean exists(String username){
        if (username.equals("%")) return existsByUUID(username);

        return existsByUUID(UUIDUtils.getCachedUUID(username));
    }

    public static boolean existsByUUID(String uuid){
        if (uuid.equals("%")) return new File(pathToPlayers, "console" + ".properties").exists();

        return new File(StreamLine.getInstance().getPlDir(), uuid + ".properties").exists();
    }

    public static boolean isStats(Player stat){
        return stats.contains(stat);
    }

    public static void reloadStats(Player stat) {
        stats.remove(getSavableUser(stat.latestName));
        stats.add(stat);
    }

    public static SavableUser addStatByUUID(String uuid){
        if (isInStatsListByUUID(uuid)) return getPlayerStatByUUID(uuid);

        if (uuid.equals("%")) {
            return getConsoleStat();
        } else {
            if (existsByUUID(uuid)) {
                Player player = getOrGetPlayerStatByUUID(uuid);
                return addPlayerStat(player);
            } else {
                if (! isInOnlineList(UUIDUtils.getCachedName(uuid))) return null;
                else return addPlayerStat(new Player(uuid, true));
            }
        }
    }

    public static SavableUser addStat(SavableUser stat){
        if (isInStatsList(stat)) return getSavableUser(stat.latestName);

        stats.add(stat);

        return stat;
    }

    public static Player addPlayerStatByUUID(String uuid){
        Player player = getOrGetPlayerStatByUUID(uuid);

        if (player == null) {
            if (isInStatsListByUUID(uuid)) {
                player = getPlayerStatByUUID(uuid);
            } else {
                if (existsByUUID(uuid)) {
                    player = new Player(uuid, false);
                } else {
                    player = new Player(uuid, true);
                }
            }
        }

        addPlayerStat(player);

        return player;
    }

    public static Player addPlayerStat(Player stat){
        addStat(stat);

        return stat;
    }

    public static Player addPlayerStat(ProxiedPlayer pp){
        if (isInStatsListByUUID(pp.getUniqueId().toString())) {
            Player player = getPlayerStat(pp);
            if (player != null) return player;
        }

        Player player = getOrGetPlayerStatByUUID(pp.getUniqueId().toString());

        if (player == null) {
            if (existsByUUID(pp.getUniqueId().toString())) {
                player = new Player(pp, false);
            } else {
                player = new Player(pp, true);
            }
        }

        addStat(player);

        return player;
    }

    public static void addStat(ConsolePlayer stat){
        if (isInStatsList(stat)) return;

        stats.add(stat);
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
            if (player.latestName == null) {
                stats.remove(player);
                return false;
            }
            if (player.latestName.equals(stat.latestName)) return true;
        }

        return false;
    }

    public static boolean isInStatsList(String username) {
        List<SavableUser> toRemove = new ArrayList<>();

        for (SavableUser user : getStats()) {
            if (user.uuid == null) {
                toRemove.add(user);
                continue;
            }
            if (user.latestName == null) {
                toRemove.add(user);
                continue;
            }

            if (user.latestName.equals(username)) return true;
        }

        for (SavableUser user : toRemove) {
            removeStat(user);
        }

        return false;
    }

    public static boolean isInStatsListByUUID(String uuid) {
        List<SavableUser> toRemove = new ArrayList<>();

        for (SavableUser user : getStats()) {
            if (user.uuid == null) {
                toRemove.add(user);
                continue;
            }

            if (user.uuid.equals(uuid)) return true;
        }

        for (SavableUser user : toRemove) {
            removeStat(user);
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
        }
    }

    public static void saveAll(){
        List<SavableUser> users = new ArrayList<>(getStats());

        for (SavableUser user : users) {
            try {
                addCascadingSave(user);
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
            if (player.uuid == null) toRemove.add(player);

            if (! player.online) toRemove.add(player);
        }

        for (Player player : toRemove) {
            try {
                addCascadingSave(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
            PlayerUtils.removeStat(player);

            count ++;
        }

        GuildUtils.loadAllMembersInAllGuilds();

        return count;
    }

    public static String getNameFromString(String thing) {
        if (thing.equals("%")) return getConsoleStat().latestName;

        if (thing.contains("-")) return UUIDUtils.getCachedName(thing);
        else return thing;
    }

    public static String getUUIDFromString(String thing) {
        if (thing.equals("%")) return thing;

        return UUIDUtils.getCachedUUID(thing);
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
            MessagingUtils.logInfo("Tried to do an offline permissions check, but failed due to not having LuckPerms installed!");
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

    public static List<String> getNamesJustPlayers(){
        return getUserNamesFrom(getJustPlayers());
    }

    public static List<String> getNamesJustStaffOnline(){
        return getUserNamesFrom(getJustStaffOnline());
    }

    public static List<String> getNamesJustProxy(){
        return getUserNamesFrom(getJustProxies());
    }

    public static List<String> getNamesFromAllUsers(){
        return getUserNamesFrom(stats);
    }


    public static List<String> getUserNamesFrom(Iterable<? extends SavableUser> users) {
        List<String> names = new ArrayList<>();

        for (SavableUser user : users) {
            names.add(user.getName());
        }

        return names;
    }

    public static List<SavableUser> getJustStaffOnline(){
        List<SavableUser> users = new ArrayList<>();

        for (SavableUser user : getJustPlayersOnline()) {
            if (! user.online) continue;
            if (user.hasPermission(ConfigUtils.staffPerm)) {
                users.add(user);
            }
        }

        users.add(getConsoleStat());

        return users;
    }

    public static List<Player> getJustPlayersOnline(){
        List<Player> players = new ArrayList<>(getJustPlayers());
        List<Player> online = new ArrayList<>();

        for (Player player : players) {
            if (player.online) online.add(player);
        }

        return online;
    }

    public static List<SavableUser> getStatsOnline(){
        List<Player> players = new ArrayList<>(getJustPlayers());
        List<SavableUser> online = new ArrayList<>();

        for (Player player : players) {
            if (player.online) online.add(player);
        }

        online.add(getConsoleStat());

        return online;
    }

    public static boolean isStatOnline(SavableUser user) {
        for (SavableUser online : PlayerUtils.getStatsOnline()) {
            if (online.equals(user)) return true;
        }

        return false;
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
        Player stat = addPlayerStat(new Player(player, true));

        if (ConfigUtils.statsTell) {
            MessagingUtils.sendStatUserMessage(stat, player, create);
        }

        return stat;
    }

    public static Player createPlayerStat(CommandSender sender) {
        return createPlayerStat(sender.getName());
    }

    public static Player createPlayerStat(String name) {
        Player stat = addPlayerStat(new Player(UUIDUtils.getCachedUUID(name), true));

        if (ConfigUtils.statsTell && stat.online) {
            MessagingUtils.sendStatUserMessage(stat, stat.player, create);
        }

        return stat;
    }

    public static Player createPlayerStatByUUID(String uuid) {
        Player stat = addPlayerStat(new Player(uuid, true));

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
        return getConsoleStat(StreamLine.getInstance().getProxy().getConsole());
    }

    public static ConsolePlayer getConsoleStat(CommandSender sender) {
        try {
            for (ConsolePlayer stat : getJustProxies()) {
                if (stat.uuid.equals("%")) return stat;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return applyConsole();
    }

    // Player Stats.

    public static Player getPlayerStat(CommandSender sender) {
        try {
            for (Player stat : getJustPlayers()) {
                if (sender.equals(stat.findSender())) {
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
        } else {
            addStat(user);
        }

        return user;
    }

    public static SavableUser getOrCreateSavableUserByUUID(String uuid){
        if (uuid.equals("%")) {
            return getOrCreateSUByUUID(uuid);
        }

        SavableUser user = getOrGetSavableUser(uuid);

        if (user == null) {
            if (isInOnlineList(uuid)) {
                user = createPlayerStat(getPPlayerByUUID(uuid));
            } else {
                user = createPlayerStatByUUID(uuid);
            }
        } else {
            addStat(user);
        }

        return user;
    }

    public static SavableUser getOrCreateSavableUser(CommandSender sender){
        if (sender.equals(StreamLine.getInstance().getProxy().getConsole())) {
            return getConsoleStat();
        }

        return getOrCreateSavableUser(sender.getName());
    }

    public static SavableUser getOrCreateSavableUser(ProxiedPlayer sender){
        return getOrCreateSavableUser(sender.getName());
    }

    public static SavableUser getOrCreateSUByUUID(String uuid){
        SavableUser user = getSavableUserByUUID(uuid);

        if (user == null) {
            user = createSavableUser(uuid);
        } else {
            addStat(user);
        }

        return user;
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
        } else {
            addPlayerStat(player);
        }

        return player;
    }

    public static Player getOrCreatePlayerStat(CommandSender sender){
        Player player = getOrGetPlayerStat(sender.getName());

        if (player == null) {
            player = createPlayerStat(sender);
        } else {
            addPlayerStat(player);
        }

        return player;
    }

    public static Player getOrCreatePlayerStat(String name){
        Player player = getOrGetPlayerStat(name);

        if (player == null) {
            player = createPlayerStat(name);
        } else {
            addPlayerStat(player);
        }

        return  player;
    }

    public static Player getOrCreatePlayerStatByUUID(String uuid) {
        Player player = getOrGetPlayerStatByUUID(uuid);

        if (player == null) {
            player = createPlayerStatByUUID(uuid);
        } else {
            addPlayerStat(player);
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

    public static SavableUser getOrGetSavableUser(CommandSender sender) {
        if (sender.getName().equals(StreamLine.getInstance().getProxy().getConsole().getName())) return getConsoleStat();

        try {
            if (exists(sender.getName())){
                if (isInStatsList(sender.getName())) {
                    return getPlayerStat(sender.getName());
                } else {
                    return new Player(UUIDUtils.getCachedUUID(sender.getName()), false);
                }
            } else return null;
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
                    return new Player(UUIDUtils.getCachedUUID(name), false);
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
                MessagingUtils.sendBUserMessage(from.findSender(), MessageConfUtils.noPlayer);
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
            MessagingUtils.sendBMessagenging(from.findSender(), from, to, message, MessageConfUtils.replySender);

            MessagingUtils.sendBMessagenging(to.findSender(), from, to, message, MessageConfUtils.replyTo);

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getOrCreatePlayerStat(player);

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;
                if (! p.sspyvs) if (from.uuid.equals(p.uuid) || to.uuid.equals(p.uuid)) continue;

                MessagingUtils.sendBMessagenging(player, from, to, message, MessageConfUtils.replySSPY);
            }
        } else {
            MessagingUtils.sendBMessagenging(from.findSender(), from, to, message, MessageConfUtils.messageSender);

            MessagingUtils.sendBMessagenging(to.findSender(), from, to, message, MessageConfUtils.messageTo);

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getOrCreatePlayerStat(player);

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;
                if (! p.sspyvs) if (from.uuid.equals(p.uuid) || to.uuid.equals(p.uuid)) continue;

                MessagingUtils.sendBMessagenging(player, from, to, message, MessageConfUtils.messageSSPY);
            }
        }
    }

    public static void doMessageWithIgnoreCheck(SavableUser from, SavableUser to, String message, boolean reply){
        if (to instanceof Player) {
            if (! ((Player) to).online) {
                MessagingUtils.sendBUserMessage(from.findSender(), MessageConfUtils.noPlayer);
                return;
            }
        }

        if (to.ignoredList.contains(from.uuid)) {
            MessagingUtils.sendBUserMessage(from.findSender(), MessageConfUtils.messageIgnored);
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
            MessagingUtils.sendBMessagenging(from.findSender(), from, to, message, MessageConfUtils.replySender);

            MessagingUtils.sendBMessagenging(to.findSender(), from, to, message, MessageConfUtils.replyTo);

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getOrCreatePlayerStat(player);

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;
                if (! p.sspyvs) if (from.uuid.equals(p.uuid) || to.uuid.equals(p.uuid)) continue;

                MessagingUtils.sendBMessagenging(player, from, to, message, MessageConfUtils.replySSPY);
            }
        } else {
            MessagingUtils.sendBMessagenging(from.findSender(), from, to, message, MessageConfUtils.messageSender);

            MessagingUtils.sendBMessagenging(to.findSender(), from, to, message, MessageConfUtils.messageTo);

            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                Player p = PlayerUtils.getOrCreatePlayerStat(player);

                if (! player.hasPermission(ConfigUtils.messViewPerm) || ! p.sspy) continue;
                if (! p.sspyvs) if (from.uuid.equals(p.uuid) || to.uuid.equals(p.uuid)) continue;

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

    public static void tickConn(){
        if (connections == null) return;

        if (connections.size() <= 0) connections = new HashMap<>();

        List<Player> conns = new ArrayList<>(connections.keySet());
        List<Player> toRemove = new ArrayList<>();

        for (Player player : conns) {
            SingleSet<Integer, Integer> conn = PlayerUtils.getConnection(player);

            if (conn == null) continue;

            PlayerUtils.removeSecondFromConn(player, conn);

            conn = PlayerUtils.getConnection(player);

            if (conn == null) continue;
            if (conn.key <= 0) toRemove.add(player);
        }

        for (Player remove : toRemove) {
            PlayerUtils.removeConn(remove);
        }
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

    public static String getDisplayBungee(SavableUser stat){
        if (stat == null) {
            return MessageConfUtils.nullB;
        }

        if (stat instanceof ConsolePlayer) {
            return ConfigUtils.consoleDisplayName;
        }

        if (stat instanceof Player) {
            if (((Player) stat).online) {
                return stat.displayName;
            } else {
                return stat.displayName;
            }
        }

        return MessageConfUtils.nullB;
    }

    public static String getAbsoluteBungee(SavableUser stat){
        if (stat == null) {
            return MessageConfUtils.nullB;
        }

        if (stat instanceof ConsolePlayer) {
            return "%";
        }

        if (stat instanceof Player) {
            return stat.latestName;
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

    public static String getDisplayDiscord(SavableUser stat){
        if (stat == null) {
            return MessageConfUtils.nullD;
        }

        if (stat instanceof ConsolePlayer) {
            return ConfigUtils.consoleDisplayName;
        }

        if (stat instanceof Player) {
            if (((Player) stat).online) {
                return stat.displayName;
            } else {
                return stat.displayName;
            }
        }

        return MessageConfUtils.nullD;
    }

    public static String getAbsoluteDiscord(SavableUser stat){
        if (stat == null) {
            return MessageConfUtils.nullD;
        }

        if (stat instanceof ConsolePlayer) {
            return "%";
        }

        if (stat instanceof Player) {
            return stat.latestName;
        }

        return MessageConfUtils.nullD;
    }

    /* ----------------------------

    PlayerUtils <-- ProxiedPlayer.

    ---------------------------- */

    public static Collection<ProxiedPlayer> getOnlinePPlayers(){
        return StreamLine.getInstance().getProxy().getPlayers();
    }



    public static List<ProxiedPlayer> getServeredPPlayers(String serverName) {
        List<ProxiedPlayer> players = new ArrayList<>();

        for (ProxiedPlayer player : getOnlinePPlayers()) {
            if (player.getServer() == null) continue;
            if (player.getServer().getInfo().getName().equals(serverName)) players.add(player);
        }

        return players;
    }

    public static List<String> getPlayerNamesForAllOnline(){
        return getPlayerNamesFrom(getOnlinePPlayers());
    }

    public static List<String> getPlayerNamesByServer(Server server) {
        return getPlayerNamesFrom(getServeredPPlayers(server.getInfo().getName()));
    }

    public static List<String> getPlayerNamesFrom(Iterable<ProxiedPlayer> players) {
        List<String> names = new ArrayList<>();

        for (ProxiedPlayer player : players) {
            names.add(player.getName());
        }

        return names;
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

    public static void tickTeleport() {
        List<ProxiedPlayer> toTp = new ArrayList<>(teleports.keySet());

        for (ProxiedPlayer player : toTp) {
            if (teleports.get(player).key <= 0) {
                MessagingUtils.sendTeleportPluginMessageRequest(player, teleports.get(player).value);
                teleports.remove(player);
                continue;
            }

            teleports.replace(player, new SingleSet<>(teleports.get(player).key - 1, teleports.get(player).value));
        }
    }

    public static void addTeleport(ProxiedPlayer sender, ProxiedPlayer to) {
        teleports.put(sender, new SingleSet<>(ConfigUtils.helperTeleportDelay, to));
    }

    /* ----------------------------

    PlayerUtils <-- Functionals.

    ---------------------------- */

    public static void kickAll(String message) {
        if (message == null) {
            for (ProxiedPlayer player : getOnlinePPlayers()) {
                kick(player);
            }
            return;
        }

        if (message.equals("")) {
            for (ProxiedPlayer player : getOnlinePPlayers()) {
                kick(player);
            }
            return;
        }

        for (ProxiedPlayer player : getOnlinePPlayers()) {
            kick(player, message);
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

    public static TreeMap<String, TreeMap<Integer, SavableUser>> getCascadingSaves(){
        return toSave;
    }

    public static void addCascadingSave(SavableUser user){
        if (toSave == null) {
            TreeMap<Integer, SavableUser> map = new TreeMap<>();
            map.put(1, user);
            toSave.put(user.uuid, map);
            return;
        }

        if (user.uuid == null) {
            removeStat(user);
            return;
        }

        if (toSave.containsKey(user.uuid)) {
            TreeMap<Integer, SavableUser> map = toSave.get(user.uuid);
            if (map.containsValue(user)) {
                map.put(map.lastKey() + 1, user);
            }
            toSave.get(user.uuid).clear();
            toSave.get(user.uuid).putAll(map);
        } else {
            TreeMap<Integer, SavableUser> map = new TreeMap<>();
            map.put(1, user);
            toSave.put(user.uuid, map);
        }
    }

    public static void cascadeSave(String uuid){
        if (! toSave.containsKey(uuid)) return;

        TreeMap<Integer, SavableUser> map = toSave.get(uuid);

        if (map.firstEntry() == null) {
            toSave.remove(uuid);
            return;
        }

        SavableUser user = map.firstEntry().getValue();
        try {
            user.saveInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TreeMap<Integer, SavableUser> users = new TreeMap<>(map);
        TreeMap<Integer, SavableUser> toNew = new TreeMap<>();

        for (int it : users.keySet()) {
            if (it == users.firstKey()) continue;
            toNew.put(it - 1, users.get(it));
        }

        toSave.get(uuid).clear();
        toSave.get(uuid).putAll(toNew);
    }

//    public static void updateServerAll(){
//        for (SavableUser user : PlayerUtils.getStats()) {
//            if (! isInOnlineList(user.latestName)) continue;
//            user.updateServer();
//        }
//    }

    // No stats.
    public static final String noStatsFound = StreamLine.config.getMessString("stats.no-stats");
    // Not high enough permissions.
    public static final String noPermission = StreamLine.config.getMessString("stats.no-permission");
    // Create.
    public static final String create = StreamLine.config.getMessString("stats.create");
    // Info.
    public static final String info = StreamLine.config.getMessString("stats.player");
    public static final String consolePlayerInfo = StreamLine.config.getMessString("stats.console-player");
    public static final String tagsLast = StreamLine.config.getMessString("stats.tags.last");
    public static final String tagsNLast = StreamLine.config.getMessString("stats.tags.not-last");
    public static final String ipsLast = StreamLine.config.getMessString("stats.ips.last");
    public static final String ipsNLast = StreamLine.config.getMessString("stats.ips.not-last");
    public static final String namesLast = StreamLine.config.getMessString("stats.names.last");
    public static final String namesNLast = StreamLine.config.getMessString("stats.names.not-last");
    public static final String sspyT = StreamLine.config.getMessString("stats.sspy.true");
    public static final String sspyF = StreamLine.config.getMessString("stats.sspy.false");
    public static final String gspyT = StreamLine.config.getMessString("stats.gspy.true");
    public static final String gspyF = StreamLine.config.getMessString("stats.gspy.false");
    public static final String pspyT = StreamLine.config.getMessString("stats.pspy.true");
    public static final String pspyF = StreamLine.config.getMessString("stats.pspy.false");
    public static final String onlineT = StreamLine.config.getMessString("stats.online.true");
    public static final String onlineF = StreamLine.config.getMessString("stats.online.false");
    public static final String notSet = StreamLine.config.getMessString("stats.not-set");
    // Tags.
    public static final String tagRem = StreamLine.config.getMessString("btag.remove");
    public static final String tagAdd = StreamLine.config.getMessString("btag.add");
    public static final String tagListMain = StreamLine.config.getMessString("btag.list.main");
    public static final String tagListLast = StreamLine.config.getMessString("btag.list.tags.last");
    public static final String tagListNotLast = StreamLine.config.getMessString("btag.list.tags.not-last");
    // Points.
    public static final String pointsName = StreamLine.config.getMessString("stats.points-name");

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
