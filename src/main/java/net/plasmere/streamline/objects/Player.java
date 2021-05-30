package net.plasmere.streamline.objects;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.score.Scoreboard;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

public class Player implements ProxiedPlayer {
    private HashMap<String, String> info = new HashMap<>();
    private final String filePrePath = StreamLine.getInstance().getDataFolder() + File.separator + "players" + File.separator;

    public File file;
    public String uuid;
    public int xp;
    public int lvl;
    public int playSeconds;
    public String ips;
    public String names;
    public String latestIP;
    public String latestName;
    public List<String> ipList;
    public List<String> nameList;
    public boolean online;
    public String displayName;
    public String guild;
    public boolean sspy;
    public boolean gspy;
    public boolean pspy;
    public boolean sc;
    public String latestVersion;
    public List<String> tags;
    public int points;
    public String lastMessengerUUID;
    public String lastMessage;
    public String lastToMessage;

    public Player(ProxiedPlayer player) {
        String ipSt = player.getSocketAddress().toString().replace("/", "");
        String[] ipSplit = ipSt.split(":");
        ipSt = ipSplit[0];

        this.uuid = player.getUniqueId().toString();
        this.latestIP = ipSt;
        this.latestName = player.getName();

        this.ips = ipSt;
        this.names = player.getName();
        this.online = true;
        this.displayName = player.getDisplayName();
        this.tags = ConfigUtils.tagsDefaults;

        if (StreamLine.viaHolder.enabled) {
            if (StreamLine.geyserHolder.enabled && StreamLine.geyserHolder.file.hasProperty(this.uuid)) {
                this.latestVersion = "GEYSER";
            } else {
                this.latestVersion = StreamLine.viaHolder.getProtocal(UUID.fromString(this.uuid)).getName();
            }
        } else {
            this.latestVersion = "Not Enabled";
        }
        construct(player.getUniqueId().toString(), true);
    }

    public Player(ProxiedPlayer player, boolean create){
        String ipSt = player.getSocketAddress().toString().replace("/", "");
        String[] ipSplit = ipSt.split(":");
        ipSt = ipSplit[0];

        this.uuid = player.getUniqueId().toString();
        this.latestIP = ipSt;
        this.latestName = player.getName();

        this.ips = ipSt;
        this.names = player.getName();
        this.online = true;
        this.displayName = player.getDisplayName();
        this.tags = ConfigUtils.tagsDefaults;

        if (StreamLine.viaHolder.enabled) {
            if (StreamLine.geyserHolder.enabled && StreamLine.geyserHolder.file.hasProperty(this.uuid)) {
                this.latestVersion = "GEYSER";
            } else {
                this.latestVersion = StreamLine.viaHolder.getProtocal(UUID.fromString(this.uuid)).getName();
            }
        } else {
            this.latestVersion = "Not Enabled";
        }
        construct(player.getUniqueId().toString(), create);
    }

    public Player(String thing){
        createCheck(thing);
    }

    public Player(UUID uuid) {
        createCheck(uuid.toString());
    }

    public void createCheck(String thing){
        if (thing.contains("-")){
            construct(thing, false);
            this.online = onlineCheck();
        } else {
            construct(Objects.requireNonNull(UUIDFetcher.getCachedUUID(thing)), false);
            this.online = onlineCheck();
        }
    }

    public boolean onlineCheck(){
        for (ProxiedPlayer p : StreamLine.getInstance().getProxy().getPlayers()){
            if (p.getName().equals(this.latestName)) return true;
        }

        return false;
    }

    private void construct(String uuid, boolean createNew){
        if (uuid == null) return;

        this.uuid = uuid;

        this.file = new File(filePrePath + uuid + ".properties");

        if (createNew) {
            try {
                this.updateWithNewDefaults();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            getFromConfigFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getInfo() {
        return info;
    }
    public void remKey(String key){
        info.remove(key);
    }
    public String getFromKey(String key){
        return info.get(key);
    }
    public void updateKey(String key, Object value) {
        info.put(key, String.valueOf(value));
        loadVars();
        try {
            saveInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public File getFile() { return file; }

    public boolean hasProperty(String property) {
        for (String info : getInfoAsPropertyList()) {
            if (info.startsWith(property)) return true;
        }

        return false;
    }

    public List<String> getInfoAsPropertyList() {
        List<String> infoList = new ArrayList<>();
        for (String key : info.keySet()){
            infoList.add(key + "=" + getFromKey(key));
        }

        return infoList;
    }

    public String getFullProperty(String key) throws Exception {
        if (hasProperty(key)) {
            return key + "=" + getFromKey(key);
        } else {
            throw new Exception("No property saved!");
        }
    }

    public void flushInfo(){
        this.info = new HashMap<>();
    }

    public void addKeyValuePair(String key, String value){
        info.put(key, value);
    }

    public String stringifyList(List<String> list, String splitter){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i <= list.size(); i++) {
            if (i < list.size()) {
                stringBuilder.append(list.get(i - 1)).append(splitter);
            } else {
                stringBuilder.append(list.get(i - 1));
            }
        }

        return stringBuilder.toString();
    }

    public void getFromConfigFile() throws IOException {
        if (file.exists()){
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                while (data.startsWith("#")) {
                    data = reader.nextLine();
                }
                String[] dataSplit = data.split("=", 2);
                addKeyValuePair(tryUpdateFormat(dataSplit[0]), dataSplit[1]);
            }

            reader.close();

            if (needUpdate()) {
                updateWithNewDefaults();
            }

            loadVars();
        }
    }

    public boolean needUpdate() {
        if (info.size() != propertiesDefaults().size()) return true;

        int i = 0;
        for (String p : getInfoAsPropertyList()) {
            if (! p.startsWith(propertiesDefaults().get(i).split("=", 2)[0])) return true;
            i++;
        }

        return false;
    }

    public void updateWithNewDefaults() throws IOException {
        file.delete();
        FileWriter writer = new FileWriter(file);

        for (String p : propertiesDefaults()) {
            String[] propSplit = p.split("=", 2);

            String property = propSplit[0];

            String write = "";
            try {
                write = getFullProperty(property);
            } catch (Exception e) {
                write = p;
            }

            writer.write(write + "\n");
        }

        writer.close();

        flushInfo();

        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            while (data.startsWith("#")) {
                data = reader.nextLine();
            }
            String[] dataSplit = data.split("=", 2);
            addKeyValuePair(dataSplit[0], dataSplit[1]);
        }

        reader.close();

        loadVars();
    }

    public List<String> propertiesDefaults() {
        List<String> defaults = new ArrayList<>();
        defaults.add("uuid=" + uuid);
        defaults.add("ips=" + ips);
        defaults.add("names=" + names);
        defaults.add("latest-ip=" + latestIP);
        defaults.add("latest-name=" + latestName);
        defaults.add("xp=0");
        defaults.add("lvl=1");
        defaults.add("playtime=0");
        defaults.add("display-name=" + displayName);
        defaults.add("guild=");
        defaults.add("sspy=true");
        defaults.add("gspy=true");
        defaults.add("pspy=true");
        defaults.add("sc=true");
        defaults.add("latest-version=" + latestVersion);
        defaults.add("tags=" + defaultTags());
        defaults.add("points=" + ConfigUtils.pointsDefault);
        defaults.add("last-messenger=");
        defaults.add("last-message=");
        defaults.add("last-to-message=");
        //defaults.add("");
        return defaults;
    }

    public String defaultTags(){
        StringBuilder stringBuilder = new StringBuilder();

        int i = 1;
        for (String tag : ConfigUtils.tagsDefaults) {
            if (tag == null) continue;
            if (tag.equals("")) continue;
            if (i != tag.length()) {
                stringBuilder.append(tag).append(",");
            } else {
                stringBuilder.append(tag);
            }
            i++;
        }

        return stringBuilder.toString();
    }

    public void loadVars(){
        // StreamLine.getInstance().getLogger().info("UUID : " + getFromKey("uuid"));

        this.uuid = getFromKey("uuid");
        this.ips = getFromKey("ips");
        this.names = getFromKey("names");
        this.latestIP = getFromKey("latest-ip");
        this.latestName = getFromKey("latest-name");
        this.ipList = loadIPs();
        this.nameList = loadNames();
        this.xp = Integer.parseInt(getFromKey("xp"));
        this.lvl = Integer.parseInt(getFromKey("lvl"));
        this.playSeconds = Integer.parseInt(getFromKey("playtime"));
        this.displayName = getFromKey("display-name");
        this.guild = getFromKey("guild");
        this.online = onlineCheck();
        this.sspy = Boolean.parseBoolean(getFromKey("sspy"));
        this.gspy = Boolean.parseBoolean(getFromKey("gspy"));
        this.pspy = Boolean.parseBoolean(getFromKey("pspy"));
        this.sc = Boolean.parseBoolean(getFromKey("sc"));
        this.latestVersion = getFromKey("latest-version");
        this.tags = loadTags();
        this.points = Integer.parseInt(getFromKey("points"));
        this.lastMessengerUUID = getFromKey("last-messenger");
        this.lastMessage = getFromKey("last-message");
        this.lastToMessage = getFromKey("last-to-message");
    }

    public TreeMap<String, String> updatableKeys() {
        TreeMap<String, String> thing = new TreeMap<>();

        thing.put("latestip", "latest-ip");
        thing.put("latestname", "latest-name");
        thing.put("displayname", "display-name");
        thing.put("latestversion", "latest-version");

        return thing;
    }

    public String tryUpdateFormat(String from){
        for (String key : updatableKeys().keySet()) {
            if (! from.equals(key)) continue;

            return updatableKeys().get(key);
        }

        return from;
    }

    public String tryUpdateFormatRaw(String from){
        String[] fromSplit = from.split("=", 2);

        return tryUpdateFormat(fromSplit[0]) + "=" + fromSplit[1];
    }

    public void tryAddNewName(String name){
        if (nameList.contains(name)) return;

        this.nameList.add(name);

        this.names = stringifyList(nameList, ",");
    }

    public void tryAddNewIP(String ip){
        if (ipList.contains(ip)) return;

        this.ipList.add(ip);

        this.ips = stringifyList(ipList, ",");
    }

    public void tryAddNewIP(ProxiedPlayer player){
        String ipSt = player.getSocketAddress().toString().replace("/", "");
        String[] ipSplit = ipSt.split(":");
        ipSt = ipSplit[0];

        tryAddNewIP(ipSt);
    }

    public void addPlaySecond(int amount){
        updateKey("playtime", playSeconds + amount);
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

    public List<String> loadTags(){
        List<String> thing = new ArrayList<>();

        String search = "tags";

        try {
            if (getFromKey(search).equals("") || getFromKey(search) == null) return thing;
            if (! getFromKey(search).contains(",")) {
                thing.add(getFromKey(search));
                return thing;
            }

            for (String t : getFromKey(search).split(",")) {
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

    public void saveInfo() throws IOException {
        file.delete();

        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        for (String s : getInfoAsPropertyList()){
            writer.write(tryUpdateFormatRaw(s) + "\n");
        }
        writer.close();

        //StreamLine.getInstance().getLogger().info("Just saved Player info for player: " + PlayerUtils.getOffOnReg(player));
    }

    /*
   Experience required =
   2 × current_level + 7 (for levels 0–15)
   5 × current_level – 38 (for levels 16–30)
   9 × current_level – 158 (for levels 31+)
    */

    public void remTag(String tag){
        tags.remove(tag);
        updateKey("tags", stringifyList(tags, ","));
    }

    public void addTag(String tag){
        tags.add(tag);
        updateKey("tags", stringifyList(tags, ","));
    }

    public int getNeededXp(){
        int needed = 0;

        needed = 2500 + (2500 * lvl);

        return needed;
    }

    public int xpUntilNextLevel(){
        return getNeededXp() - this.xp;
    }

    public void addXp(int amount){
        int setAmount = this.xp + amount;

        while (setAmount >= getNeededXp()) {
            setAmount -= getNeededXp();
            int setLevel = this.lvl + 1;
            updateKey("lvl", setLevel);
        }

        updateKey("xp", setAmount);
    }

    public void setXp(int amount){
        int setAmount = amount;

        while (setAmount >= getNeededXp()) {
            setAmount -= getNeededXp();
            int setLevel = this.lvl + 1;
            updateKey("lvl", setLevel);
        }

        updateKey("xp", setAmount);
    }

    public void dispose() throws Throwable {
        try {
            this.uuid = null;
        } finally {
            super.finalize();
        }
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

    public void setPoints(int amount) {
        points = amount;
        updateKey("points", amount);
    }

    public void addPoints(int amount) {
        setPoints(points + amount);
    }

    public void remPoints(int amount) {
        setPoints(points - amount);
    }

    public String toString(){
        return latestName;
    }

    public void updateLastMessage(String message){
        updateKey("last-message", message);
    }

    public void updateLastToMessage(String message){
        updateKey("last-to-message", message);
    }

    public void updateLastMessenger(Player messenger){
        updateKey("last-messenger", messenger.uuid);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(String name) {
        updateKey("displayname", name);
    }

    @Override
    public void sendMessage(ChatMessageType position, BaseComponent... message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(position, message);
        }
    }

    @Override
    public void sendMessage(ChatMessageType position, BaseComponent message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(position, message);
        }
    }

    @Override
    public void sendMessage(UUID sender, BaseComponent... message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(sender, message);
        }
    }

    @Override
    public void sendMessage(UUID sender, BaseComponent message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(sender, message);
        }
    }

    @Override
    public void connect(ServerInfo target) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).connect(target);
        }
    }

    @Override
    public void connect(ServerInfo target, ServerConnectEvent.Reason reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).connect(target, reason);
        }
    }

    @Override
    public void connect(ServerInfo target, Callback<Boolean> callback) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).connect(target, callback);
        }
    }

    @Override
    public void connect(ServerInfo target, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).connect(target, callback, reason);
        }
    }

    @Override
    public void connect(ServerConnectRequest request) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).connect(request);
        }
    }

    @Override
    public Server getServer() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getServer();
        }
        return null;
    }

    @Override
    public int getPing() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getPing();
        }
        return -1;
    }

    @Override
    public void sendData(String channel, byte[] data) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendData(channel, data);
        }
    }

    @Override
    public PendingConnection getPendingConnection() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getPendingConnection();
        }
        return null;
    }

    @Override
    public void chat(String message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).chat(message);
        }
    }

    @Override
    public ServerInfo getReconnectServer() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getReconnectServer();
        }
        return null;
    }

    @Override
    public void setReconnectServer(ServerInfo server) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).setReconnectServer(server);
        }
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    @Override
    public Locale getLocale() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getLocale();
        }
        return null;
    }

    @Override
    public byte getViewDistance() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getViewDistance();
        }
        return -1;
    }

    @Override
    public ChatMode getChatMode() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getChatMode();
        }
        return null;
    }

    @Override
    public boolean hasChatColors() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).hasChatColors();
        }
        return false;
    }

    @Override
    public SkinConfiguration getSkinParts() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getSkinParts();
        }
        return null;
    }

    @Override
    public MainHand getMainHand() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getMainHand();
        }
        return null;
    }

    @Override
    public void setTabHeader(BaseComponent header, BaseComponent footer) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).setTabHeader(header, footer);
        }
    }

    @Override
    public void setTabHeader(BaseComponent[] header, BaseComponent[] footer) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).setTabHeader(header, footer);
        }
    }

    @Override
    public void resetTabHeader() {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).resetTabHeader();
        }
    }

    @Override
    public void sendTitle(Title title) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendTitle(title);
        }
    }

    @Override
    public boolean isForgeUser() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).isForgeUser();
        }
        return false;
    }

    @Override
    public Map<String, String> getModList() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getModList();
        }
        return null;
    }

    @Override
    public Scoreboard getScoreboard() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getScoreboard();
        }
        return null;
    }

    @Override
    public String getName() {
        return latestName;
    }

    @Deprecated
    @Override
    public void sendMessage(String message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(message);
        }
    }

    @Deprecated
    @Override
    public void sendMessages(String... messages) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessages(messages);
        }
    }

    @Override
    public void sendMessage(BaseComponent... message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(message);
        }
    }

    @Override
    public void sendMessage(BaseComponent message) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).sendMessage(message);
        }
    }

    @Override
    public Collection<String> getGroups() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getGroups();
        }
        return null;
    }

    @Override
    public void addGroups(String... groups) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).addGroups(groups);
        }
    }

    @Override
    public void removeGroups(String... groups) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).removeGroups(groups);
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).hasPermission(permission);
        }
        return false;
    }

    @Override
    public void setPermission(String permission, boolean value) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).setPermission(permission, value);
        }
    }

    @Override
    public Collection<String> getPermissions() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getPermissions();
        }
        return null;
    }

    @Deprecated
    @Override
    public InetSocketAddress getAddress() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getAddress();
        }
        return InetSocketAddress.createUnresolved(latestIP, new Random().nextInt(26666));
    }

    @Override
    public SocketAddress getSocketAddress() {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).getSocketAddress();
        }
        return InetSocketAddress.createUnresolved(latestIP, new Random().nextInt(26666));
    }

    @Deprecated
    @Override
    public void disconnect(String reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).disconnect(reason);
        }
    }

    @Override
    public void disconnect(BaseComponent... reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).disconnect(reason);
        }
    }

    @Override
    public void disconnect(BaseComponent reason) {
        if (online) {
            Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).disconnect(reason);
        }
    }

    @Override
    public boolean isConnected() {
        return online;
    }

    @Override
    public Unsafe unsafe() {
        if (online) {
            return Objects.requireNonNull(PlayerUtils.getProxiedPlayer(latestName)).unsafe();
        }
        return null;
    }
}
