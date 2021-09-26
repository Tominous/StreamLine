package net.plasmere.streamline.objects.savable.users;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.PluginUtils;
import net.plasmere.streamline.utils.UUIDUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public abstract class SavableUser {
    private TreeMap<String, String> info = new TreeMap<>();
    private final String filePrePath = StreamLine.getInstance().getDataFolder() + File.separator + "players" + File.separator;
    private SavableUser savableUser;

    public File file;
    public String uuid;
    public String latestName;
    public String displayName;
    public String guild;
    public String tags;
    public List<String> tagList;
    public int points;
    public String lastFromUUID;
    public String lastToUUID;
    public String replyToUUID;
    public String lastMessage;
    public String lastToMessage;
    public String lastFromMessage;
    public String ignoreds;
    public List<String> ignoredList;
    public String friends;
    public List<String> friendList;
    public String pendingToFriends;
    public List<String> pendingToFriendList;
    public String pendingFromFriends;
    public List<String> pendingFromFriendList;
    public String latestVersion;
//    public String latestServer;
    public boolean online;
    public boolean sspy;
    public boolean gspy;
    public boolean pspy;
    public boolean viewsc;
    public boolean sc;
    public boolean sspyvs;
    public boolean pspyvs;
    public boolean gspyvs;
    public boolean scvs;

    public List<String> savedKeys = new ArrayList<>();

    public SavableUser(String fileName) {
        this(fileName, true);
    }

    public SavableUser(String fileName, boolean createNew){
        if (PluginUtils.isLocked()) return;

        this.uuid = fileName;
        this.savableUser = this;
        this.latestVersion = "UNKNOWN";

        preConstruct(fileName);

        this.file = UUIDUtils.getCachedFile(StreamLine.getInstance().getPlDir(), fileName);

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

    public SavableUser getSavableUser() {
        return savableUser;
    }

//    public void updateSender() {
//        this.sender = findSender();
//    }

//    public void updateServer() {
//        this.latestServer = findServer();
//    }

    public CommandSender findSender() {
        if (this.uuid.equals("%")) {
            return StreamLine.getInstance().getProxy().getConsole();
        } else {
            return PlayerUtils.getPPlayerByUUID(this.uuid);
        }
    }

    public String findServer() {
        if (this.uuid.equals("%")) {
            return ConfigUtils.consoleServer;
        } else {
            ProxiedPlayer player = PlayerUtils.getPPlayerByUUID(this.uuid);

            if (player == null) return MessageConfUtils.nullB();

            if (player.getServer() == null) return ConfigUtils.consoleServer;

            return player.getServer().getInfo().getName();
        }
    }

    abstract public void preConstruct(String string);

    public TreeMap<String, String> getInfo() {
        return info;
    }
    public void remKey(String key){
        info.remove(key);
    }
    public File getFile() { return file; }

    public String getFromKey(String key){
        return info.get(key);
    }

//    public int getInfoIntFor(String key) {
//        for (Integer i : info.keySet()) {
//            if (info.get(i).key.equals(key)) return i;
//        }
//
//        return 0;
//    }

    public void updateKey(String key, Object value) {
        info.remove(key);
        addKeyValuePair(key, String.valueOf(value));
        loadVars();
    }

    public void updateKeyNoLoad(String key, Object value) {
        info.remove(key);
        addKeyValuePair(key, String.valueOf(value));
    }

    public boolean hasProperty(String property) {
        for (String info : getInfoAsPropertyList()) {
            if (info.startsWith(property)) return true;
        }

        return false;
    }

    public TreeSet<String> getInfoAsPropertyList() {
        TreeSet<String> infoList = new TreeSet<>();
        List<String> keys = new ArrayList<>();
        for (String key : info.keySet()){
            if (keys.contains(key)) continue;

            infoList.add(key + "=" + getFromKey(key));
            keys.add(key);
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
        this.info = new TreeMap<>();
    }

    public void addKeyValuePair(String key, String value){
        if (info.containsKey(key)) return;

        info.put(key, value);
    }

//    public boolean infoContainsKey(String key){
//        for (Integer i : info.keySet()) {
//            if (info.get(i).key.equals(key)) return true;
//        }
//
//        return false;
//    }

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

            List<String> keys = new ArrayList<>();
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                while (data.startsWith("#")) {
                    data = reader.nextLine();
                }
                String[] dataSplit = data.split("=", 2);
                if (keys.contains(dataSplit[0])) continue;
                keys.add(dataSplit[0]);
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
            if (! startsWithForKeys(p)) return true;
            i++;
        }

        return false;
    }

    public boolean startsWithForKeys(String string){
        for (String p : propertiesDefaults()) {
            if (tryUpdateFormat(string.split("=", 2)[0]).equals(p.split("=", 2)[0])) return true;
        }

        return false;
    }

    public void updateWithNewDefaults() throws IOException {
        file.delete();

        file.createNewFile();

        FileWriter writer = new FileWriter(file);

        savedKeys = new ArrayList<>();

        for (String p : propertiesDefaults()) {
            String key = p.split("=", 2)[0];
            if (savedKeys.contains(key)) continue;
            savedKeys.add(key);

            String[] propSplit = p.split("=", 2);

            String property = propSplit[0];

            String write = "";
            try {
                write = getFullProperty(property);
            } catch (Exception e) {
                write = p;
            }

            writer.write(tryUpdateFormatRaw(write) + "\n");
        }

        writer.close();

        flushInfo();

        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            while (data.startsWith("#")) {
                data = reader.nextLine();
            }

            if (! data.contains("=")) if (ConfigUtils.debug) {
                MessagingUtils.logInfo("PLAYER DATA (" + this.latestName + ") ERROR : data has no split for --> " + data);
                continue;
            }

            String[] dataSplit = data.split("=", 2);
            addKeyValuePair(tryUpdateFormat(dataSplit[0]), dataSplit[1]);
        }

        reader.close();

        loadVars();
    }

    public TreeSet<String> propertiesDefaults(){
        TreeSet<String> defaults = new TreeSet<>();
        defaults.add("uuid=" + uuid);
        defaults.add("latest-name=" + latestName);
        defaults.add("display-name=" + ((displayName == null) ? latestName : displayName));
        defaults.add("latest-version=" + latestVersion);
        defaults.add("guild=");
        defaults.add("tags=" + defaultTags());
        defaults.add("points=" + (this.uuid.equals("%") ? ConfigUtils.consoleDefaultPoints : ConfigUtils.pointsDefault));
        defaults.add("last-from=");
        defaults.add("last-to=");
        defaults.add("last-message=");
        defaults.add("last-to-message=");
        defaults.add("last-from-message=");
        defaults.add("reply-to=");
        defaults.add("ignored=");
        defaults.add("friends=");
        defaults.add("pending-to-friends=");
        defaults.add("pending-from-friends=");
        defaults.add("latest-server=" + findServer());
        defaults.add("sspy=true");
        defaults.add("gspy=true");
        defaults.add("pspy=true");
        defaults.add("sc=false");
        defaults.add("view-sc=true");
        defaults.add("sspy-vs=false");
        defaults.add("pspy-vs=false");
        defaults.add("gspy-vs=false");
        defaults.add("sc-vs=true");
        //defaults.add("");
        defaults.addAll(addedProperties());
        return defaults;
    }

    abstract public int getPointsFromConfig();

    abstract public TreeSet<String> addedProperties();

    public String defaultTags(){
        StringBuilder stringBuilder = new StringBuilder();

        int i = 1;
        for (String tag : getTagsFromConfig()) {
            if (tag == null) continue;
            if (tag.equals("")) continue;
            if (i < tag.length()) {
                stringBuilder.append(tag).append(",");
            } else {
                stringBuilder.append(tag);
            }
            i++;
        }

        return stringBuilder.toString();
    }

    abstract public List<String> getTagsFromConfig();

    public void loadVars() {
        this.uuid = getFromKey("uuid");
        this.latestName = getFromKey("latest-name");
        this.displayName = getFromKey("display-name");
        this.guild = getFromKey("guild");
        this.tagList = loadTags();
        this.points = Integer.parseInt(getFromKey("points") == null ? "0" : getFromKey("points"));
        this.lastFromUUID = getFromKey("last-from");
        this.lastToUUID = getFromKey("last-to");
        this.lastMessage = getFromKey("last-message");
        this.lastToMessage = getFromKey("last-to-message");
        this.lastFromMessage = getFromKey("last-from-message");
        this.replyToUUID = getFromKey("reply-to");
        this.ignoredList = loadIgnored();
        this.friendList = loadFriends();
        this.pendingToFriendList = loadPendingToFriends();
        this.pendingFromFriendList = loadPendingFromFriends();
        this.sspy = Boolean.parseBoolean(getFromKey("sspy"));
        this.gspy = Boolean.parseBoolean(getFromKey("gspy"));
        this.pspy = Boolean.parseBoolean(getFromKey("pspy"));
        this.sc = Boolean.parseBoolean(getFromKey("sc"));
        this.sspyvs = Boolean.parseBoolean(getFromKey("sspy-vs"));
        this.pspyvs = Boolean.parseBoolean(getFromKey("pspy-vs"));
        this.gspyvs = Boolean.parseBoolean(getFromKey("gspy-vs"));
        this.scvs = Boolean.parseBoolean(getFromKey("sc-vs"));
        this.viewsc = Boolean.parseBoolean(getFromKey("view-sc"));
//        this.latestServer = getFromKey("latest-server");

        loadMoreVars();

        if (this.latestName == null) {
            try {
                if (ConfigUtils.deleteBadStats) {
                    this.dispose();
                }
                throw new Exception("Bad User Data for user: " + this.uuid + "!");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }

        if (this.latestName.equals("null")) {
            try {
                if (ConfigUtils.deleteBadStats) {
                    this.dispose();
                }
                throw new Exception("Bad User Data for user: " + this.uuid + "!");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }
    }

    abstract public void loadMoreVars();

    public TreeMap<String, String> updatableKeys() {
        TreeMap<String, String> keys = new TreeMap<>();

        keys.putAll(addedUpdatableKeys());

        keys.put("last-messenger", "last-from");

        return keys;
    }

    abstract TreeMap<String, String> addedUpdatableKeys();

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

    public void tryAddNewTag(String tag){
        if (tagList == null) this.tagList = new ArrayList<>();

        if (tagList.contains(tag)) return;

        this.tagList.add(tag);

        this.tags = stringifyList(tagList, ",");

        updateKey("tags", this.tags);
    }

    public void tryRemTag(String tag){
        if (tagList == null) this.tagList = new ArrayList<>();

        if (! tagList.contains(tag)) return;

        this.tagList.remove(tag);

        this.tags = stringifyList(tagList, ",");

        updateKey("tags", this.tags);
    }

    public void tryAddNewIgnored(String uuid){
        if (ignoredList == null) this.ignoredList = new ArrayList<>();

        if (ignoredList.contains(uuid)) return;

        this.ignoredList.add(uuid);

        this.ignoreds = stringifyList(ignoredList, ",");

        updateKey("ignored", this.ignoreds);
    }

    public void tryRemIgnored(String uuid){
        if (ignoredList == null) this.ignoredList = new ArrayList<>();

        if (! ignoredList.contains(uuid)) return;

        this.ignoredList.remove(uuid);

        this.ignoreds = stringifyList(ignoredList, ",");

        updateKey("ignored", this.ignoreds);
    }

    public void tryAddNewFriend(String uuid){
        if (friendList == null) this.friendList = new ArrayList<>();

        tryRemPendingToFriend(uuid);
        tryRemPendingFromFriend(uuid);

        if (friendList.contains(uuid)) return;

        this.friendList.add(uuid);

        this.friends = stringifyList(friendList, ",");

        updateKey("friends", this.friends);
    }

    public void tryRemFriend(String uuid){
        if (friendList == null) this.friendList = new ArrayList<>();

        if (! friendList.contains(uuid)) return;

        this.friendList.remove(uuid);

        this.friends = stringifyList(friendList, ",");

        updateKey("friends", this.friends);
    }

    public void tryAddNewPendingToFriend(String uuid){
        if (pendingToFriendList == null) this.pendingToFriendList = new ArrayList<>();

        if (pendingToFriendList.contains(uuid)) return;

        this.pendingToFriendList.add(uuid);

        this.pendingToFriends = stringifyList(pendingToFriendList, ",");

        updateKey("pending-to-friends", this.pendingToFriends);
    }

    public void tryRemPendingToFriend(String uuid){
        if (pendingToFriendList == null) this.pendingToFriendList = new ArrayList<>();

        if (! pendingToFriendList.contains(uuid)) return;

        this.pendingToFriendList.remove(uuid);

        this.pendingToFriends = stringifyList(pendingToFriendList, ",");

        updateKey("pending-to-friends", this.pendingToFriends);
    }

    public void tryAddNewPendingFromFriend(String uuid){
        if (pendingFromFriendList == null) this.pendingFromFriendList = new ArrayList<>();

        if (pendingFromFriendList.contains(uuid)) return;

        this.pendingFromFriendList.add(uuid);

        this.pendingFromFriends = stringifyList(pendingFromFriendList, ",");

        updateKey("pending-from-friends", this.pendingFromFriends);
    }

    public void tryRemPendingFromFriend(String uuid){
        if (pendingFromFriendList == null) this.pendingFromFriendList = new ArrayList<>();

        if (! pendingFromFriendList.contains(uuid)) return;

        this.pendingFromFriendList.remove(uuid);

        this.pendingFromFriends = stringifyList(pendingFromFriendList, ",");

        updateKey("pending-from-friends", this.pendingFromFriends);
    }

    public List<String> loadTags(){
        List<String> thing = new ArrayList<>();

        String search = "tags";

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

    public List<String> loadIgnored(){
        List<String> thing = new ArrayList<>();

        String search = "ignored";

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

    public List<String> loadFriends(){
        List<String> thing = new ArrayList<>();

        String search = "friends";

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

    public List<String> loadPendingToFriends(){
        List<String> thing = new ArrayList<>();

        String search = "pending-to-friends";

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

    public List<String> loadPendingFromFriends(){
        List<String> thing = new ArrayList<>();

        String search = "pending-from-friends";

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

    public void dispose() throws Throwable {
        try {
            PlayerUtils.removeStat(this);
            this.uuid = null;
            this.file.delete();
        } finally {
            super.finalize();
        }
    }

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

    public void setLatestServer(String server) {
        //this.latestServer = server;
        updateKey("latest-server", server);
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

    public void setSSPYVS(boolean value) {
        sspyvs = value;
        updateKey("sspy-vs", value);
    }

    public void toggleSSPYVS() { setSSPYVS(! sspyvs); }

    public void setPSPYVS(boolean value) {
        pspyvs = value;
        updateKey("pspy-vs", value);
    }

    public void togglePSPYVS() { setPSPYVS(! pspyvs); }

    public void setGSPYVS(boolean value) {
        gspyvs = value;
        updateKey("gspy-vs", value);
    }

    public void toggleGSPYVS() { setGSPYVS(! gspyvs); }

    public void setSCVS(boolean value) {
        scvs = value;
        updateKey("sc-vs", value);
    }

    public void toggleSCVS() { setSCVS(! scvs); }

    public String toString(){
        return latestName;
    }

    public void updateLastMessage(String message){
        updateKey("last-message", message);
    }

    public void updateLastToMessage(String message){
        updateKey("last-to-message", message);
    }

    public void updateLastFromMessage(String message){
        updateKey("last-from-message", message);
    }

    public void updateLastFrom(SavableUser messenger){
        updateKey("last-from", messenger.uuid);
    }

    public void updateLastTo(SavableUser to){
        updateKey("last-to", to.uuid);
    }

    public void updateReplyTo(SavableUser to){
        updateKey("reply-to", to.uuid);
    }
    
    public String getName() {
        return latestName;
    }

    @Deprecated
    public void sendMessage(String message) {
        findSender().sendMessage(message);
    }

    @Deprecated
    public void sendMessages(String... messages) {
        findSender().sendMessages(messages);
    }

    public void sendMessage(BaseComponent... message) {
        findSender().sendMessage(message);
    }

    public void sendMessage(BaseComponent message) {
        findSender().sendMessage(message);
    }

    public Collection<String> getGroups() {
        return findSender().getGroups();
    }

    public void addGroups(String... groups) {
        findSender().addGroups(groups);
    }

    public void removeGroups(String... groups) {
        findSender().removeGroups(groups);
    }

    public boolean hasPermission(String permission) {
        return findSender().hasPermission(permission);
    }

    public void setPermission(String permission, boolean value) {
        findSender().setPermission(permission, value);
    }

    public Collection<String> getPermissions() {
        return findSender().getPermissions();
    }

    public void saveInfo() throws IOException {
        file.delete();

        file.createNewFile();

        savedKeys = new ArrayList<>();
        FileWriter writer = new FileWriter(file);
        for (String s : getInfoAsPropertyList()){
            String key = s.split("=")[0];
            if (savedKeys.contains(key)) continue;
            savedKeys.add(key);

            writer.write(tryUpdateFormatRaw(s) + "\n");
        }
        writer.close();

        if (ConfigUtils.debug) MessagingUtils.logInfo("Just saved Player info for player: " + this.uuid + " (Player: " + this.latestName + ")");
    }
}
