package net.plasmere.streamline.objects.users;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

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
    public String lastMessengerUUID;
    public String lastToUUID;
    public String lastMessage;
    public String lastToMessage;
    public String ignoreds;
    public List<String> ignoredList;
    public String friends;
    public List<String> friendList;
    public String pendingToFriends;
    public List<String> pendingToFriendList;
    public String pendingFromFriends;
    public List<String> pendingFromFriendList;
    public CommandSender sender;

    public List<String> savedKeys = new ArrayList<>();

    public SavableUser(String fileName) {
        this(fileName, true);
    }

    public SavableUser(String fileName, boolean createNew){
        this.savableUser = this;
        this.sender = findSender(fileName);
        this.uuid = fileName;

        preConstruct(fileName);

        this.file = new File(StreamLine.getInstance().getPlDir(), fileName + ".properties");

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

    public CommandSender findSender(String fileName) {
        if (fileName.equals("console") || fileName.equals("%")) {
            return StreamLine.getInstance().getProxy().getConsole();
        } else {
            return PlayerUtils.getPPlayerByUUID(fileName);
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
                StreamLine.getInstance().getLogger().info("PLAYER DATA (" + this.latestName + ") ERROR : data has no split for --> " + data);
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
        defaults.add("display-name=" + latestName);
        defaults.add("guild=");
        defaults.add("tags=" + defaultTags());
        defaults.add("points=" + ConfigUtils.pointsDefault);
        defaults.add("last-messenger=");
        defaults.add("last-to=");
        defaults.add("last-message=");
        defaults.add("last-to-message=");
        defaults.add("ignored=");
        defaults.add("friends=");
        defaults.add("pending-to-friends=");
        defaults.add("pending-from-friends=");
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
        this.points = Integer.parseInt(getFromKey("points"));
        this.lastMessengerUUID = getFromKey("last-messenger");
        this.lastToUUID = getFromKey("last-to");
        this.lastMessage = getFromKey("last-message");
        this.lastToMessage = getFromKey("last-to-message");
        this.ignoredList = loadIgnored();
        this.friendList = loadFriends();
        this.pendingToFriendList = loadPendingToFriends();
        this.pendingFromFriendList = loadPendingFromFriends();

        loadMoreVars();
    }

    abstract public void loadMoreVars();

    abstract public TreeMap<String, String> updatableKeys();

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

    public List<String> loadIgnored(){
        List<String> thing = new ArrayList<>();

        String search = "ignored";

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

    public List<String> loadFriends(){
        List<String> thing = new ArrayList<>();

        String search = "friends";

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

    public List<String> loadPendingToFriends(){
        List<String> thing = new ArrayList<>();

        String search = "pending-to-friends";

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

    public List<String> loadPendingFromFriends(){
        List<String> thing = new ArrayList<>();

        String search = "pending-from-friends";

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

    public void dispose() throws Throwable {
        try {
            this.uuid = null;
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

        //StreamLine.getInstance().getLogger().info("Just saved Player info for player: " + PlayerUtils.getOffOnReg(player));
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

    public void updateLastMessenger(SavableUser messenger){
        updateKey("last-messenger", messenger.uuid);
    }

    public void updateLastTo(SavableUser to){
        updateKey("last-to", to.uuid);
    }
    
    public String getName() {
        return latestName;
    }

    @Deprecated
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    @Deprecated
    public void sendMessages(String... messages) {
        sender.sendMessages(messages);
    }

    public void sendMessage(BaseComponent... message) {
        sender.sendMessage(message);
    }

    public void sendMessage(BaseComponent message) {
        sender.sendMessage(message);
    }

    public Collection<String> getGroups() {
        return sender.getGroups();
    }

    public void addGroups(String... groups) {
        sender.addGroups(groups);
    }

    public void removeGroups(String... groups) {
        sender.removeGroups(groups);
    }

    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    public void setPermission(String permission, boolean value) {
        sender.setPermission(permission, value);
    }

    public Collection<String> getPermissions() {
        return sender.getPermissions();
    }
}
