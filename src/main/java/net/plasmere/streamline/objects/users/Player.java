package net.plasmere.streamline.objects.users;

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
    public boolean online;
    public boolean sspy;
    public boolean gspy;
    public boolean pspy;
    public boolean sc;
    public String latestVersion;
    public boolean muted;
    public Date mutedTill;
    public boolean viewsc;
    public ProxiedPlayer player;

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

    public Player(UUID uuid) {
        super(PlayerUtils.createCheck(uuid.toString()), false);
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
            return;
        }

        String ipSt = player.getSocketAddress().toString().replace("/", "");
        String[] ipSplit = ipSt.split(":");
        ipSt = ipSplit[0];

        this.uuid = player.getUniqueId().toString();
        this.latestIP = ipSt;
        this.latestName = player.getName();
        this.latestServer = player.getServer().getInfo().getName();

        this.ips = ipSt;
        this.names = player.getName();
        this.online = onlineCheck();
        this.displayName = player.getDisplayName();
        this.tagList = ConfigUtils.tagsDefaults;

        if (StreamLine.viaHolder.enabled) {
            if (StreamLine.geyserHolder.enabled && StreamLine.geyserHolder.file.hasProperty(this.uuid)) {
                this.latestVersion = "GEYSER";
            } else {
                this.latestVersion = StreamLine.viaHolder.getProtocal(UUID.fromString(this.uuid)).getName();
            }
        } else {
            this.latestVersion = "Not Enabled";
        }
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
        defaults.add("lvl=1");
        defaults.add("total-xp=0");
        defaults.add("currentXP=0");
        defaults.add("playtime=0");
        defaults.add("sspy=true");
        defaults.add("gspy=true");
        defaults.add("pspy=true");
        defaults.add("sc=false");
        defaults.add("muted=false");
        defaults.add("muted-till=");
        defaults.add("view-sc=true");
        //defaults.add("");
        return defaults;
    }

    @Override
    public List<String> getTagsFromConfig(){
        return ConfigUtils.tagsDefaults;
    }

    @Override
    public void loadMoreVars() {
        this.ips = getFromKey("ips");
        this.names = getFromKey("names");
        this.latestIP = getFromKey("latest-ip");
        this.ipList = loadIPs();
        this.nameList = loadNames();
        this.lvl = Integer.parseInt(getFromKey("lvl"));
        this.totalXP = Integer.parseInt(getFromKey("total-xp"));
        this.currentXP = Integer.parseInt(getFromKey("current-xp"));
        this.playSeconds = Integer.parseInt(getFromKey("playtime"));
        this.online = onlineCheck();
        this.sspy = Boolean.parseBoolean(getFromKey("sspy"));
        this.gspy = Boolean.parseBoolean(getFromKey("gspy"));
        this.pspy = Boolean.parseBoolean(getFromKey("pspy"));
        this.sc = Boolean.parseBoolean(getFromKey("sc"));
        this.muted = Boolean.parseBoolean(getFromKey("muted"));
        try {
            this.mutedTill = new Date(Long.parseLong(getFromKey("muted-till")));
        } catch (Exception e) {
            this.mutedTill = null;
        }
        this.viewsc = Boolean.parseBoolean(getFromKey("view-sc"));
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
            if (getFromKey(search).equals("") || getFromKey(search) == null) return thing;
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
            if (getFromKey(search).equals("") || getFromKey(search) == null) return thing;
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

        needed = 2500 + (2500 * fromLevel);

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

    public void setSSPY(boolean value) {
        sspy = value;
        updateKey("sspy", value);
    }

    public void toggleSSPY() { setSSPY(! sspy); }

    public void setGSPY(boolean value) {
        gspy = value;
        updateKey("gspy", value);
    }

    public void toggleGSPY() { setGSPY(! gspy); }

    public void setPSPY(boolean value) {
        pspy = value;
        updateKey("pspy", value);
    }

    public void togglePSPY() { setPSPY(! pspy); }

    public void setSC(boolean value) {
        sc = value;
        updateKey("sc", value);
    }

    public void toggleSC() { setSC(! sc); }

    public void setSCView(boolean value) {
        viewsc = value;
        updateKey("view-sc", value);
    }

    public void toggleSCView() { setSCView(! viewsc); }

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
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(position, message);
        }
    }

    
    public void sendMessage(ChatMessageType position, BaseComponent message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(position, message);
        }
    }

    
    public void sendMessage(UUID sender, BaseComponent... message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(sender, message);
        }
    }

    
    public void sendMessage(UUID sender, BaseComponent message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(sender, message);
        }
    }

    
    public void connect(ServerInfo target) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).connect(target);
        }
    }

    
    public void connect(ServerInfo target, ServerConnectEvent.Reason reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).connect(target, reason);
        }
    }

    
    public void connect(ServerInfo target, Callback<Boolean> callback) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).connect(target, callback);
        }
    }

    
    public void connect(ServerInfo target, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).connect(target, callback, reason);
        }
    }

    
    public void connect(ServerConnectRequest request) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).connect(request);
        }
    }

    
    public Server getServer() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getServer();
        }
        return null;
    }

    
    public int getPing() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getPing();
        }
        return -1;
    }

    
    public void sendData(String channel, byte[] data) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendData(channel, data);
        }
    }

    
    public PendingConnection getPendingConnection() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getPendingConnection();
        }
        return null;
    }

    
    public void chat(String message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).chat(message);
        }
    }

    
    public ServerInfo getReconnectServer() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getReconnectServer();
        }
        return null;
    }

    
    public void setReconnectServer(ServerInfo server) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).setReconnectServer(server);
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
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getLocale();
        }
        return null;
    }

    
    public byte getViewDistance() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getViewDistance();
        }
        return -1;
    }

    
    public ProxiedPlayer.ChatMode getChatMode() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getChatMode();
        }
        return null;
    }

    
    public boolean hasChatColors() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).hasChatColors();
        }
        return false;
    }

    
    public SkinConfiguration getSkinParts() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getSkinParts();
        }
        return null;
    }

    
    public ProxiedPlayer.MainHand getMainHand() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getMainHand();
        }
        return null;
    }

    
    public void setTabHeader(BaseComponent header, BaseComponent footer) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).setTabHeader(header, footer);
        }
    }

    
    public void setTabHeader(BaseComponent[] header, BaseComponent[] footer) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).setTabHeader(header, footer);
        }
    }

    
    public void resetTabHeader() {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).resetTabHeader();
        }
    }

    
    public void sendTitle(Title title) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendTitle(title);
        }
    }

    
    public boolean isForgeUser() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).isForgeUser();
        }
        return false;
    }

    
    public Map<String, String> getModList() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getModList();
        }
        return null;
    }

    
    public Scoreboard getScoreboard() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getScoreboard();
        }
        return null;
    }

    
    public String getName() {
        return latestName;
    }

    @Deprecated
    
    public void sendMessage(String message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(message);
        }
    }

    @Deprecated
    
    public void sendMessages(String... messages) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessages(messages);
        }
    }

    
    public void sendMessage(BaseComponent... message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(message);
        }
    }

    
    public void sendMessage(BaseComponent message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(message);
        }
    }

    
    public Collection<String> getGroups() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getGroups();
        }
        return null;
    }

    
    public void addGroups(String... groups) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).addGroups(groups);
        }
    }

    
    public void removeGroups(String... groups) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).removeGroups(groups);
        }
    }

    
    public boolean hasPermission(String permission) {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).hasPermission(permission);
        }
        return false;
    }

    
    public void setPermission(String permission, boolean value) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).setPermission(permission, value);
        }
    }

    
    public Collection<String> getPermissions() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getPermissions();
        }
        return null;
    }

    @Deprecated
    public InetSocketAddress getAddress() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getAddress();
        }
        return InetSocketAddress.createUnresolved(latestIP, new Random().nextInt(26666));
    }

    
    public SocketAddress getSocketAddress() {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getSocketAddress();
        }
        return InetSocketAddress.createUnresolved(latestIP, new Random().nextInt(26666));
    }

    @Deprecated
    public void disconnect(String reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).disconnect(reason);
        }
    }

    
    public void disconnect(BaseComponent... reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).disconnect(reason);
        }
    }

    
    public void disconnect(BaseComponent reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).disconnect(reason);
        }
    }

    
    public boolean isConnected() {
        return online;
    }

    
    public Connection.Unsafe unsafe() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).unsafe();
        }
        return null;
    }
}
