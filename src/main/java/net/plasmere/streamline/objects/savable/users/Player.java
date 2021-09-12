package net.plasmere.streamline.objects.savable.users;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.score.Scoreboard;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

public class Player extends SavableUser {
    public int totalXP;
    public int currentXP;
    public int lvl;
    public int playSeconds;
    public String ips;
    public String names;
    public String latestIP;
    public List<String> ipList;
    public List<String> nameList;
    public boolean muted;
    public Date mutedTill;
    public ProxiedPlayer player;
    public ChatLevel chatLevel;

    public int defaultLevel = 1;

    public Player(ProxiedPlayer player) {
        super(player.getUniqueId().toString());
        this.player = player;
    }

    public Player(ProxiedPlayer player, boolean create){
        super(player.getUniqueId().toString(), create);
        this.player = player;
    }

    public Player(String thing){
        super(PlayerUtils.createCheck(thing), false);
    }

    public Player(String thing, boolean createNew){
        super(PlayerUtils.createCheck(thing), createNew);
    }

    public Player(UUID uuid) {
        super(uuid.toString(), false);
    }

    public boolean onlineCheck(){
        for (ProxiedPlayer p : StreamLine.getInstance().getProxy().getPlayers()){
            if (p.getName().equals(this.latestName)) return true;
        }

        return false;
    }

    @Override
    public void preConstruct(String string) {
        this.player = PlayerUtils.getPPlayerByUUID(string);

        if (this.player == null) {
            this.uuid = string;
            this.online = false;
            return;
        }

        String ipSt = player.getSocketAddress().toString().replace("/", "");
        String[] ipSplit = ipSt.split(":");
        ipSt = ipSplit[0];

        this.uuid = player.getUniqueId().toString();
        this.latestIP = ipSt;
        this.latestName = player.getName();

        this.ips = ipSt;
        this.names = player.getName();
        this.online = onlineCheck();

        String toLatestVersion = "";

        if (StreamLine.viaHolder.enabled) {
            if (StreamLine.geyserHolder.enabled && StreamLine.geyserHolder.file.hasProperty(this.uuid)) {
                toLatestVersion = "GEYSER";
            } else {
                toLatestVersion = StreamLine.viaHolder.getProtocal(UUID.fromString(this.uuid)).getName();
            }
        } else {
            toLatestVersion = "Not Enabled";
        }


        this.latestVersion = toLatestVersion;
        updateKeyNoLoad("latest-version", toLatestVersion);
    }

    @Override
    public int getPointsFromConfig(){
        return ConfigUtils.pointsDefault;
    }

    @Override
    public TreeSet<String> addedProperties() {
        TreeSet<String> defaults = new TreeSet<>();
        defaults.add("ips=" + ips);
        defaults.add("names=" + names);
        defaults.add("latest-ip=" + latestIP);
        defaults.add("lvl=" + defaultLevel);
        defaults.add("total-xp=0");
        defaults.add("currentXP=0");
        defaults.add("playtime=0");
        defaults.add("muted=false");
        defaults.add("muted-till=");
        defaults.add("chat-level=LOCAL");
        //defaults.add("");
        return defaults;
    }

    @Override
    public List<String> getTagsFromConfig(){
        return ConfigUtils.tagsDefaults;
    }

    @Override
    public void loadMoreVars() {
        this.online = onlineCheck();
        if (! this.online) this.latestVersion = getFromKey("latest-version");

        this.ips = getFromKey("ips");
        this.names = getFromKey("names");
        this.latestIP = getFromKey("latest-ip");
        this.ipList = loadIPs();
        this.nameList = loadNames();
        this.playSeconds = Integer.parseInt(getFromKey("playtime"));
        this.muted = Boolean.parseBoolean(getFromKey("muted"));
        try {
            this.mutedTill = new Date(Long.parseLong(getFromKey("muted-till")));
        } catch (Exception e) {
            this.mutedTill = null;
        }

        this.lvl = Integer.parseInt(getFromKey("lvl"));
        this.totalXP = Integer.parseInt(getFromKey("total-xp"));
        this.currentXP = Integer.parseInt(getFromKey("current-xp"));

        this.chatLevel = parseChatLevel(getFromKey("chat-level"));
    }

    public static ChatLevel parseChatLevel(String string) {
        switch (string) {
            case "GLOBAL":
                return ChatLevel.GLOBAL;
            case "GUILD":
                return ChatLevel.GUILD;
            case "PARTY":
                return ChatLevel.PARTY;
            case "GOFFICER":
                return ChatLevel.GOFFICER;
            case "POFFICER":
                return ChatLevel.POFFICER;
            case "LOCAL":
            default:
                return ChatLevel.LOCAL;
        }
    }

    @Override
    TreeMap<String, String> addedUpdatableKeys() {
        TreeMap<String, String> thing = new TreeMap<>();

        thing.put("latestip", "latest-ip");
        thing.put("latestname", "latest-name");
        thing.put("displayname", "display-name");
        thing.put("latestversion", "latest-version");
        thing.put("xp", "total-xp");
        thing.put("totalXP", "total-xp");
        thing.put("currentXP", "current-xp");

        return thing;
    }

    public static void sendMessageFormatted(CommandSender sender, String formatFrom, ChatLevel newLevel, ChatLevel oldLevel) {
        MessagingUtils.sendBUserMessage(sender, formatFrom
                .replace("%new_channel%", newLevel.toString())
                .replace("%old_channel%", oldLevel.toString())
        );
    }

    public ChatLevel setChatLevel(String string) {
        ChatLevel newLevel = parseChatLevel(string);

        switch (newLevel) {
            case LOCAL:
                sendMessageFormatted(player, MessageConfUtils.chatChannelsLocalSwitch(), newLevel, chatLevel);
                break;
            case GLOBAL:
                sendMessageFormatted(player, MessageConfUtils.chatChannelsGlobalSwitch(), newLevel, chatLevel);
                break;
            case GUILD:
                sendMessageFormatted(player, MessageConfUtils.chatChannelsGuildSwitch(), newLevel, chatLevel);
                break;
            case PARTY:
                sendMessageFormatted(player, MessageConfUtils.chatChannelsPartySwitch(), newLevel, chatLevel);
                break;
            case GOFFICER:
                sendMessageFormatted(player, MessageConfUtils.chatChannelsGOfficerSwitch(), newLevel, chatLevel);
                break;
            case POFFICER:
                sendMessageFormatted(player, MessageConfUtils.chatChannelsPOfficerSwitch(), newLevel, chatLevel);
                break;
        }

        this.chatLevel = newLevel;
        updateKey("chat-level", newLevel.toString());

        return newLevel;
    }

    public ChatLevel setChatLevel(ChatLevel chatLevel) {
        this.chatLevel = chatLevel;
        updateKey("chat-level", chatLevel.toString());

        return chatLevel;
    }

    public void tryAddNewName(String name){
        if (nameList == null) this.nameList = new ArrayList<>();

        if (nameList.contains(name)) return;

        this.nameList.add(name);

        this.names = stringifyList(nameList, ",");

        updateKey("names", this.names);
    }

    public void tryRemName(String name){
        if (nameList == null) this.nameList = new ArrayList<>();

        if (! nameList.contains(name)) return;

        this.nameList.remove(name);

        this.names = stringifyList(nameList, ",");

        updateKey("names", this.names);
    }

    public void tryAddNewIP(String ip){
        if (ipList == null) this.ipList = new ArrayList<>();

        if (ipList.contains(ip)) return;

        this.ipList.add(ip);

        this.ips = stringifyList(ipList, ",");

        updateKey("ips", this.ips);
    }

    public void tryAddNewIP(ProxiedPlayer player){
        String ipSt = player.getSocketAddress().toString().replace("/", "");
        String[] ipSplit = ipSt.split(":");
        ipSt = ipSplit[0];

        tryAddNewIP(ipSt);
    }

    public void tryRemIP(String ip){
        if (ipList == null) this.ipList = new ArrayList<>();

        if (! ipList.contains(ip)) return;

        this.ipList.remove(ip);

        this.ips = stringifyList(ipList, ",");

        updateKey("ips", this.ips);
    }

    public void tryRemIP(ProxiedPlayer player){
        String ipSt = player.getSocketAddress().toString().replace("/", "");
        String[] ipSplit = ipSt.split(":");
        ipSt = ipSplit[0];

        tryRemIP(ipSt);
    }

    public void addPlaySecond(int amount){
        setPlaySeconds(playSeconds + amount);
    }

    public void setPlaySeconds(int amount){
        updateKey("playtime", amount);
    }

    public double getPlayDays(){
        return playSeconds / (60.0d * 60.0d * 24.0d);
    }

    public double getPlayHours(){
        return playSeconds / (60.0d * 60.0d);
    }

    public double getPlayMinutes(){
        return playSeconds / (60.0d);
    }

    public List<String> loadIPs(){
        List<String> thing = new ArrayList<>();

        String search = "ips";

        try {
            if (getFromKey(search) == null) return thing;
            if (getFromKey(search).equals("")) return thing;

            if (! getFromKey(search).contains(",")) {
                thing.add(getFromKey(search));
                return thing;
            }

            for (String t : getFromKey(search).split(",")) {
                if (t == null) continue;
                if (t.equals("")) continue;

                try {
                    thing.add(t);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return thing;
    }

    public List<String> loadNames(){
        List<String> thing = new ArrayList<>();

        String search = "names";

        try {
            if (getFromKey(search) == null) return thing;
            if (getFromKey(search).equals("")) return thing;

            if (! getFromKey(search).contains(",")) {
                thing.add(getFromKey(search));
                return thing;
            }

            for (String t : getFromKey(search).split(",")) {
                if (t == null) continue;
                if (t.equals("")) continue;

                try {
                    thing.add(t);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return thing;
    }

    /*
   Experience required =
   2 × current_level + 7 (for levels 0–15)
   5 × current_level – 38 (for levels 16–30)
   9 × current_level – 158 (for levels 31+)
    */

    public int getNeededXp(int fromLevel){
        int needed = 0;

        needed = 2500 + (2500 * (fromLevel - defaultLevel));

        return needed;
    }

    public int xpUntilNextLevel(){
        return getNeededXp(this.lvl + 1) - this.totalXP;
    }

    public void addTotalXP(int amount){
        setTotalXP(amount + this.totalXP);
    }

    public void setTotalXP(int amount){
        int setAmount = amount;
        int required = getNeededXp(this.lvl + 1);

        while (setAmount >= required) {
            setAmount -= required;
            int setLevel = this.lvl + 1;
            updateKey("lvl", setLevel);
        }

        updateKey("total-xp", setAmount);
        updateKey("current-xp", getCurrentXP());
    }

    public int getCurrentLevelXP(){
        int xpTill = 0;
        for (int i = 0; i <= this.lvl; i++) {
            xpTill += getNeededXp(i);
        }

        return xpTill;
    }

    public int getCurrentXP(){
        return this.totalXP - getCurrentLevelXP();
    }

    public void setMuted(boolean value) {
        muted = value;
        updateKey("muted", value);
    }

    public void setMutedTill(long value) {
        mutedTill = new Date(value);
        updateKey("muted-till", value);
    }

    public void removeMutedTill(){
        mutedTill = null;
        updateKey("muted-till", "");
    }

    public void updateMute(boolean set, Date newMutedUntil){
        setMuted(set);
        setMutedTill(newMutedUntil.getTime());
    }

    public void toggleMuted() { setMuted(! muted); }

    
    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String name) {
        updateKey("display-name", name);
    }

    public void sendMessage(ChatMessageType position, BaseComponent... message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).sendMessage(position, message);
        }
    }

    
    public void sendMessage(ChatMessageType position, BaseComponent message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).sendMessage(position, message);
        }
    }

    
    public void sendMessage(UUID sender, BaseComponent... message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).sendMessage(sender, message);
        }
    }

    
    public void sendMessage(UUID sender, BaseComponent message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).sendMessage(sender, message);
        }
    }

    
    public void connect(ServerInfo target) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).connect(target);
        }
    }

    
    public void connect(ServerInfo target, ServerConnectEvent.Reason reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).connect(target, reason);
        }
    }

    
    public void connect(ServerInfo target, Callback<Boolean> callback) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).connect(target, callback);
        }
    }

    
    public void connect(ServerInfo target, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).connect(target, callback, reason);
        }
    }

    
    public void connect(ServerConnectRequest request) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).connect(request);
        }
    }

    
    public Server getServer() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getServer();
        }
        return null;
    }

    
    public int getPing() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getPing();
        }
        return -1;
    }

    
    public void sendData(String channel, byte[] data) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).sendData(channel, data);
        }
    }

    
    public PendingConnection getPendingConnection() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getPendingConnection();
        }
        return null;
    }

    
    public void chat(String message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).chat(message);
        }
    }

    
    public ServerInfo getReconnectServer() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getReconnectServer();
        }
        return null;
    }

    
    public void setReconnectServer(ServerInfo server) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).setReconnectServer(server);
        }
    }

    
    public String getUUID() {
        return uuid;
    }

    
    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    
    public Locale getLocale() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getLocale();
        }
        return null;
    }

    
    public byte getViewDistance() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getViewDistance();
        }
        return -1;
    }

    
    public ProxiedPlayer.ChatMode getChatMode() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getChatMode();
        }
        return null;
    }

    
    public boolean hasChatColors() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).hasChatColors();
        }
        return false;
    }

    
    public SkinConfiguration getSkinParts() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getSkinParts();
        }
        return null;
    }

    
    public ProxiedPlayer.MainHand getMainHand() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getMainHand();
        }
        return null;
    }

    
    public void setTabHeader(BaseComponent header, BaseComponent footer) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).setTabHeader(header, footer);
        }
    }

    
    public void setTabHeader(BaseComponent[] header, BaseComponent[] footer) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).setTabHeader(header, footer);
        }
    }

    
    public void resetTabHeader() {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).resetTabHeader();
        }
    }

    
    public void sendTitle(Title title) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).sendTitle(title);
        }
    }

    
    public boolean isForgeUser() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).isForgeUser();
        }
        return false;
    }

    
    public Map<String, String> getModList() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getModList();
        }
        return null;
    }

    
    public Scoreboard getScoreboard() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getScoreboard();
        }
        return null;
    }

    
    public String getName() {
        return latestName;
    }

    @Deprecated
    
    public void sendMessage(String message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).sendMessage(message);
        }
    }

    @Deprecated
    
    public void sendMessages(String... messages) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).sendMessages(messages);
        }
    }

    
    public void sendMessage(BaseComponent... message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).sendMessage(message);
        }
    }

    
    public void sendMessage(BaseComponent message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).sendMessage(message);
        }
    }

    
    public Collection<String> getGroups() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getGroups();
        }
        return null;
    }

    
    public void addGroups(String... groups) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).addGroups(groups);
        }
    }

    
    public void removeGroups(String... groups) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).removeGroups(groups);
        }
    }

    
    public boolean hasPermission(String permission) {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).hasPermission(permission);
        }
        return false;
    }

    
    public void setPermission(String permission, boolean value) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).setPermission(permission, value);
        }
    }

    
    public Collection<String> getPermissions() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getPermissions();
        }
        return null;
    }

    @Deprecated
    public InetSocketAddress getAddress() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getAddress();
        }
        return InetSocketAddress.createUnresolved(latestIP, new Random().nextInt(26666));
    }

    
    public SocketAddress getSocketAddress() {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).getSocketAddress();
        }
        return InetSocketAddress.createUnresolved(latestIP, new Random().nextInt(26666));
    }

    @Deprecated
    public void disconnect(String reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).disconnect(reason);
        }
    }

    
    public void disconnect(BaseComponent... reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).disconnect(reason);
        }
    }

    
    public void disconnect(BaseComponent reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).disconnect(reason);
        }
    }

    
    public boolean isConnected() {
        return online;
    }

    
    public Connection.Unsafe unsafe() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getPPlayer(latestName)).unsafe();
        }
        return null;
    }
}
