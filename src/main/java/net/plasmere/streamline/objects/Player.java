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
    public ProxiedPlayer player;
    public UUID uuid;
    public int xp;
    public int lvl;
    public String ips;
    public String names;
    public String latestIP;
    public String latestName;
    public List<String> ipList;
    public List<String> nameList;
    public boolean online;
    public String displayName;

    public Player(ProxiedPlayer player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.latestIP = player.getSocketAddress().toString();
        this.latestName = player.getName();
        this.ips = player.getSocketAddress().toString();
        this.names = player.getName();
        this.online = true;
        this.displayName = player.getDisplayName();
        construct(player.getUniqueId(), true);
    }

    public Player(ProxiedPlayer player, boolean create){
        this.player = player;
        this.uuid = player.getUniqueId();
        this.latestIP = player.getSocketAddress().toString();
        this.latestName = player.getName();
        this.ips = player.getSocketAddress().toString();
        this.names = player.getName();
        this.online = true;
        this.displayName = player.getDisplayName();
        construct(player.getUniqueId(), create);
    }

    public Player(String username){
        this.player = null;
        construct(Objects.requireNonNull(UUIDFetcher.fetch(username)), false);
    }

    private void construct(UUID uuid, boolean createNew){
        this.file = new File(filePrePath + uuid.toString() + ".properties");

        if (createNew || file.exists()) {

            StreamLine.getInstance().getLogger().info("Player file: " + file.getName() + " (In the \"players\" folder.)");

            try {
                getFromConfigFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void getFromConfigFile() throws IOException {
        if (file.exists()){
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

            if (needUpdate()) {
                updateWithNewDefaults();
            }

            loadVars();
        } else {
            updateWithNewDefaults();
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
        defaults.add("uuid=" + this.uuid);
        defaults.add("ips=" + this.ips);
        defaults.add("names=" + this.names);
        defaults.add("latestip=" + this.ips.split(",")[0]);
        defaults.add("latestname=" + this.names.split(",")[0]);
        defaults.add("xp=0");
        defaults.add("lvl=1");
        defaults.add("displayname=" + this.displayName);
        //defaults.add("");
        return defaults;
    }

    public void loadVars(){
        this.uuid = UUID.fromString(getFromKey("uuid"));
        this.player = UUIDFetcher.getPlayer(this.uuid);
        this.ips = getFromKey("ips");
        this.names = getFromKey("names");
        this.latestIP = getFromKey("latestip");
        this.latestName = getFromKey("latestname");
        this.ipList = loadIPs();
        this.nameList = loadNames();
        this.xp = Integer.parseInt(getFromKey("xp"));
        this.lvl = Integer.parseInt(getFromKey("lvl"));
        this.displayName = getFromKey("displayname");
        this.online = getIsOnline();
    }

    public boolean getIsOnline(){
        try {
            return StreamLine.getInstance().getProxy().getPlayers().contains(this.player);
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> loadIPs(){
        List<String> thing = new ArrayList<>();

        String search = "ips";

        try {
            if (getFromKey(search).equals("") || getFromKey(search) == null) return thing;
            if (! getFromKey(search).contains(".")) {
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
            if (! getFromKey(search).contains(".")) {
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
            writer.write(s + "\n");
        }
        writer.close();

        //StreamLine.getInstance().getLogger().info("Just saved Player info for player: " + PlayerUtils.getOffOnReg(Objects.requireNonNull(PlayerUtils.getStat(player))));
    }

    /*
   Experience required =
   2 × current_level + 7 (for levels 0–15)
   5 × current_level – 38 (for levels 16–30)
   9 × current_level – 158 (for levels 31+)
    */
    public int getNeededXp(){
        int needed = 0;

        needed = 50000 + (5000 * lvl);

        return needed;
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
        this.uuid = null;
        this.finalize();
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
            player.sendMessage(position, message);
        }
    }

    @Override
    public void sendMessage(ChatMessageType position, BaseComponent message) {
        if (online) {
            player.sendMessage(position, message);
        }
    }

    @Override
    public void sendMessage(UUID sender, BaseComponent... message) {
        if (online) {
            player.sendMessage(sender, message);
        }
    }

    @Override
    public void sendMessage(UUID sender, BaseComponent message) {
        if (online) {
            player.sendMessage(sender, message);
        }
    }

    @Override
    public void connect(ServerInfo target) {
        if (online) {
            player.connect(target);
        }
    }

    @Override
    public void connect(ServerInfo target, ServerConnectEvent.Reason reason) {
        if (online) {
            player.connect(target, reason);
        }
    }

    @Override
    public void connect(ServerInfo target, Callback<Boolean> callback) {
        if (online) {
            player.connect(target, callback);
        }
    }

    @Override
    public void connect(ServerInfo target, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {
        if (online) {
            player.connect(target, callback, reason);
        }
    }

    @Override
    public void connect(ServerConnectRequest request) {
        if (online) {
            player.connect(request);
        }
    }

    @Override
    public Server getServer() {
        if (online) {
            return player.getServer();
        }
        return null;
    }

    @Override
    public int getPing() {
        if (online) {
            return player.getPing();
        }
        return -1;
    }

    @Override
    public void sendData(String channel, byte[] data) {
        if (online) {
            player.sendData(channel, data);
        }
    }

    @Override
    public PendingConnection getPendingConnection() {
        if (online) {
            return player.getPendingConnection();
        }
        return null;
    }

    @Override
    public void chat(String message) {
        if (online) {
            player.chat(message);
        }
    }

    @Override
    public ServerInfo getReconnectServer() {
        if (online) {
            return player.getReconnectServer();
        }
        return null;
    }

    @Override
    public void setReconnectServer(ServerInfo server) {
        if (online) {
            player.setReconnectServer(server);
        }
    }

    @Override
    public String getUUID() {
        return uuid.toString();
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public Locale getLocale() {
        if (online) {
            return player.getLocale();
        }
        return null;
    }

    @Override
    public byte getViewDistance() {
        if (online) {
            return player.getViewDistance();
        }
        return -1;
    }

    @Override
    public ChatMode getChatMode() {
        if (online) {
            return player.getChatMode();
        }
        return null;
    }

    @Override
    public boolean hasChatColors() {
        if (online) {
            return player.hasChatColors();
        }
        return false;
    }

    @Override
    public SkinConfiguration getSkinParts() {
        if (online) {
            return player.getSkinParts();
        }
        return null;
    }

    @Override
    public MainHand getMainHand() {
        if (online) {
            return player.getMainHand();
        }
        return null;
    }

    @Override
    public void setTabHeader(BaseComponent header, BaseComponent footer) {
        if (online) {
            player.setTabHeader(header, footer);
        }
    }

    @Override
    public void setTabHeader(BaseComponent[] header, BaseComponent[] footer) {
        if (online) {
            player.setTabHeader(header, footer);
        }
    }

    @Override
    public void resetTabHeader() {
        if (online) {
            player.resetTabHeader();
        }
    }

    @Override
    public void sendTitle(Title title) {
        if (online) {
            player.sendTitle(title);
        }
    }

    @Override
    public boolean isForgeUser() {
        if (online) {
            return player.isForgeUser();
        }
        return false;
    }

    @Override
    public Map<String, String> getModList() {
        if (online) {
            return player.getModList();
        }
        return null;
    }

    @Override
    public Scoreboard getScoreboard() {
        if (online) {
            return player.getScoreboard();
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
            player.sendMessage(message);
        }
    }

    @Deprecated
    @Override
    public void sendMessages(String... messages) {
        if (online) {
            player.sendMessages(messages);
        }
    }

    @Override
    public void sendMessage(BaseComponent... message) {
        if (online) {
            player.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(BaseComponent message) {
        if (online) {
            player.sendMessage(message);
        }
    }

    @Override
    public Collection<String> getGroups() {
        if (online) {
            return player.getGroups();
        }
        return null;
    }

    @Override
    public void addGroups(String... groups) {
        if (online) {
            player.addGroups(groups);
        }
    }

    @Override
    public void removeGroups(String... groups) {
        if (online) {
            player.removeGroups(groups);
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        if (online) {
            return player.hasPermission(permission);
        }
        return false;
    }

    @Override
    public void setPermission(String permission, boolean value) {
        if (online) {
            player.setPermission(permission, value);
        }
    }

    @Override
    public Collection<String> getPermissions() {
        if (online) {
            return player.getPermissions();
        }
        return null;
    }

    @Deprecated
    @Override
    public InetSocketAddress getAddress() {
        if (online) {
            return player.getAddress();
        }
        return InetSocketAddress.createUnresolved(latestIP, new Random().nextInt(26666));
    }

    @Override
    public SocketAddress getSocketAddress() {
        if (online) {
            player.getSocketAddress();
        }
        return InetSocketAddress.createUnresolved(latestIP, new Random().nextInt(26666));
    }

    @Deprecated
    @Override
    public void disconnect(String reason) {
        if (online) {
            player.disconnect(reason);
        }
    }

    @Override
    public void disconnect(BaseComponent... reason) {
        if (online) {
            player.disconnect(reason);
        }
    }

    @Override
    public void disconnect(BaseComponent reason) {
        if (online) {
            player.disconnect(reason);
        }
    }

    @Override
    public boolean isConnected() {
        return online;
    }

    @Override
    public Unsafe unsafe() {
        if (online) {
            return player.unsafe();
        }
        return null;
    }
}