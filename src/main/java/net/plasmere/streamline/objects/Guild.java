package net.plasmere.streamline.objects;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Guild {
    private HashMap<String, String> info = new HashMap<>();
    private final String filePrePath = StreamLine.getInstance().getDataFolder() + File.separator + "guilds" + File.separator;

    public File file;
    public String name;
    public UUID leaderUUID;
    public List<ProxiedPlayer> moderators;
    public List<UUID> modsByUUID;
    public List<ProxiedPlayer> members;
    public List<UUID> membersByUUID;
    public List<ProxiedPlayer> totalMembers;
    public List<UUID> totalMembersByUUID;
    public List<ProxiedPlayer> invites;
    public List<UUID> invitesByUUID;
    public boolean isMuted;
    public boolean isPublic;
    public int xp;
    public int lvl;
    public int maxSize = ConfigUtils.guildMax;

    public enum Level {
        MEMBER,
        MODERATOR,
        LEADER
    }

    public Guild(UUID creatorUUID, String name) {
        this.leaderUUID = creatorUUID;
        this.name = name;
        construct(leaderUUID, true);
    }

    public Guild(UUID uuid, boolean create){
        construct(uuid, create);
    }

    private void construct(UUID uuid, boolean createNew){
        this.file = new File(filePrePath + uuid.toString() + ".properties");

        if (createNew || file.exists()) {

            System.out.println("Guild file: " + file.getAbsolutePath());

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

    public ProxiedPlayer getMember(UUID uuid) throws Exception {
        for (UUID u : totalMembersByUUID){
            if (uuid.equals(u)) {
                return Objects.requireNonNull(UUIDFetcher.getProxiedPlayer(u));
            }
        }
        throw new Exception("Player not found!");
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

    public void loadVars(){
        this.name = getFromKey("name");
        this.leaderUUID = UUID.fromString(getFromKey("leader"));
        this.modsByUUID = loadMods();
        this.membersByUUID = loadMembers();
        this.totalMembersByUUID = loadTotalMembers();
        this.invitesByUUID = loadInvites();
        this.isMuted = Boolean.parseBoolean(getFromKey("muted"));
        this.isPublic = Boolean.parseBoolean(getFromKey("public"));
        this.xp = Integer.parseInt(getFromKey("xp"));
        this.lvl = Integer.parseInt(getFromKey("lvl"));

        try {
            if (moderators != null) {
                moderators.clear();
                for (UUID uuid : modsByUUID) {
                    moderators.add(getMember(uuid));
                }
            } else {
                moderators = new ArrayList<>();
            }

            if (members != null) {
                members.clear();
                for (UUID uuid : membersByUUID) {
                    members.add(getMember(uuid));
                }
            } else {
                members = new ArrayList<>();
            }

            if (totalMembers != null) {
                totalMembers.clear();
                for (UUID uuid : totalMembersByUUID) {
                    totalMembers.add(getMember(uuid));
                }
            } else {
                totalMembers = new ArrayList<>();
            }

            if (invites != null) {
                invites.clear();
                for (UUID uuid : invitesByUUID) {
                    invites.add(getMember(uuid));
                }
            } else {
                invites = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<UUID> loadMods(){
        List<UUID> uuids = new ArrayList<>();

        try {
            if (getFromKey("mods").equals("") || getFromKey("mods") == null) return uuids;
            if (! getFromKey("mods").contains(".")) {
                uuids.add(UUID.fromString(getFromKey("mods")));
                return uuids;
            }

            for (String uuid : getFromKey("mods").split("\\.")) {
                try {
                    uuids.add(UUID.fromString(uuid));
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return uuids;
    }

    private List<UUID> loadMembers() {
        List<UUID> uuids = new ArrayList<>();

        try {
            if (getFromKey("members").equals("") || getFromKey("members") == null) return uuids;
            if (! getFromKey("members").contains(".")) {
                uuids.add(UUID.fromString(getFromKey("members")));
                return uuids;
            }

            for (String uuid : getFromKey("members").split("\\.")) {
                try {
                    uuids.add(UUID.fromString(uuid));
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuids;
    }

    private List<UUID> loadTotalMembers() {
        List<UUID> uuids = new ArrayList<>();

        try {
            if (getFromKey("totalmembers").equals("") || getFromKey("totalmembers") == null) return uuids;
            if (! getFromKey("totalmembers").contains(".")) {
                uuids.add(UUID.fromString(getFromKey("totalmembers")));
                return uuids;
            }

            for (String uuid : getFromKey("totalmembers").split("\\.")) {
                try {
                    uuids.add(UUID.fromString(uuid));
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuids;
    }

    private List<UUID> loadInvites() {
        List<UUID> uuids = new ArrayList<>();

        try {
            if (getFromKey("invites").equals("") || getFromKey("invites") == null) return uuids;
            if (! getFromKey("invites").contains(".")) {
                uuids.add(UUID.fromString(getFromKey("invites")));
                return uuids;
            }

            for (String uuid : getFromKey("invites").split("\\.")) {
                try {
                    uuids.add(UUID.fromString(uuid));
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuids;
    }

    public List<String> propertiesDefaults() {
        List<String> defaults = new ArrayList<>();
        defaults.add("name=" + name);
        defaults.add("leader=" + leaderUUID);
        defaults.add("mods=" + "");
        defaults.add("members=" + "");
        defaults.add("totalmembers=" + leaderUUID);
        defaults.add("invites=" + "");
        defaults.add("muted=false");
        defaults.add("public=false");
        defaults.add("xp=0");
        defaults.add("lvl=1");
        //defaults.add("");
        return defaults;
    }

    public void saveInfo() throws IOException {
        file.delete();

        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        for (String s : getInfoAsPropertyList()){
            writer.write(s + "\n");
        }
        writer.close();
    }

    /*
   Experience required =
   2 × current_level + 7 (for levels 0–15)
   5 × current_level – 38 (for levels 16–30)
   9 × current_level – 158 (for levels 31+)
    */
    public int getNeededXp(){
        int needed = 0;
        if (this.lvl <= 15){
            needed = 2 * this.lvl + 7;
        } else if (this.lvl >= 16 && this.lvl <= 30){
            needed = 5 * this.lvl - 38;
        } else if (this.lvl > 30) {
            needed = 9 * this.lvl - 158;
        } else {
            needed = 100;
        }

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

    private String getInvitesAsStringed(){
        StringBuilder builder = new StringBuilder();

        int i = 1;
        for (UUID uuid : invitesByUUID){
            if (i != invitesByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
            i++;
        }

        return builder.toString();
    }

    private String getTotalMembersAsStringed(){
        StringBuilder builder = new StringBuilder();

        int i = 1;
        for (UUID uuid : totalMembersByUUID){
            if (i != totalMembersByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
            i++;
        }

        return builder.toString();
    }

    private String getMembersAsStringed(){
        StringBuilder builder = new StringBuilder();

        int i = 1;
        for (UUID uuid : membersByUUID){
            if (i != membersByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
            i++;
        }

        return builder.toString();
    }

    private String getModeratorsAsStringed(){
        StringBuilder builder = new StringBuilder();

        int i = 1;
        for (UUID uuid : modsByUUID){
            if (i != modsByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
            i++;
        }

        return builder.toString();
    }

    public boolean hasMember(UUID uuid){
        return totalMembersByUUID.contains(uuid);
    }

    public boolean hasMember(ProxiedPlayer player){
        try {
            return members.contains(player);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasModPerms(UUID uuid) {
        try {
            return modsByUUID.contains(uuid) || leaderUUID.equals(uuid);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasModPerms(ProxiedPlayer player) {
        try {
            return moderators.contains(player) || leaderUUID.equals(player.getUniqueId());
        } catch (Exception e) {
            return false;
        }
    }

    public int getSize(){
        return totalMembersByUUID.size();
    }

    public void removeInvite(UUID uuid){
        invitesByUUID.remove(uuid);
        updateKey("invites", getInvitesAsStringed());
    }

    public void removeInvite(ProxiedPlayer player){
        invitesByUUID.remove(player.getUniqueId());
        updateKey("invites", getInvitesAsStringed());
    }

    public void addMember(ProxiedPlayer player){
        totalMembersByUUID.add(player.getUniqueId());
        totalMembers.add(player);
        membersByUUID.add(player.getUniqueId());
        members.add(player);
        updateKey("totalmembers", getTotalMembersAsStringed());
        updateKey("members", getMembersAsStringed());
    }

    public void removeMemberFromGuild(ProxiedPlayer player){
        Random RNG = new Random();

        if (leaderUUID.equals(player.getUniqueId())){
            if (totalMembers.size() <= 1) {
                try {
                    totalMembersByUUID.remove(leaderUUID);
                    membersByUUID.remove(player.getUniqueId());
                    modsByUUID.remove(player.getUniqueId());
                    updateKey("totalmembers", getTotalMembersAsStringed());
                    updateKey("members", getMembersAsStringed());
                    updateKey("mods", getModeratorsAsStringed());
                    dispose();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                if (moderators.size() > 0) {
                    int r = RNG.nextInt(moderators.size());
                    ProxiedPlayer newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.getUniqueId();
                    modsByUUID.remove(newLeader.getUniqueId());
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    ProxiedPlayer newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.getUniqueId();
                    membersByUUID.remove(newLeader.getUniqueId());
                }
            }
        }

        totalMembersByUUID.remove(player.getUniqueId());
        membersByUUID.remove(player.getUniqueId());
        modsByUUID.remove(player.getUniqueId());
        updateKey("totalmembers", getTotalMembersAsStringed());
        updateKey("leader", leaderUUID);
        updateKey("members", getMembersAsStringed());
        updateKey("mods", getModeratorsAsStringed());
    }

    public void addInvite(ProxiedPlayer to) {
        invitesByUUID.add(to.getUniqueId());
        updateKey("invites", getInvitesAsStringed());
        loadVars();
    }

    public void toggleMute(){
        updateKey("muted", ! isMuted);
    }

    public void setPublic(boolean bool){
        updateKey("public", bool);
    }

    public Level getLevel(ProxiedPlayer member){
        if (this.members.contains(member))
            return Level.MEMBER;
        else if (this.moderators.contains(member))
            return Level.MODERATOR;
        else if (this.leaderUUID.equals(member.getUniqueId()))
            return Level.LEADER;
        else
            return Level.MEMBER;
    }

    public void setModerator(ProxiedPlayer player){
        Random RNG = new Random();

        forModeratorRemove(player);

        this.modsByUUID.add(player.getUniqueId());
        this.membersByUUID.remove(player.getUniqueId());

        if (leaderUUID.equals(player.getUniqueId())){
            if (totalMembers.size() <= 1) {
                try {
                    totalMembersByUUID.remove(leaderUUID);
                    membersByUUID.remove(player.getUniqueId());
                    modsByUUID.remove(player.getUniqueId());
                    updateKey("totalmembers", getTotalMembersAsStringed());
                    updateKey("members", getMembersAsStringed());
                    updateKey("mods", getModeratorsAsStringed());
                    dispose();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                if (moderators.size() > 0) {
                    int r = RNG.nextInt(moderators.size());
                    ProxiedPlayer newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.getUniqueId();
                    modsByUUID.remove(newLeader.getUniqueId());
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    ProxiedPlayer newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.getUniqueId();
                    membersByUUID.remove(newLeader.getUniqueId());
                }
            }
        }

        updateKey("leader", leaderUUID);
        updateKey("members", getMembersAsStringed());
        updateKey("mods", getModeratorsAsStringed());
    }

    public void setMember(ProxiedPlayer player){
        Random RNG = new Random();

        forMemberRemove(player);

        this.modsByUUID.remove(player.getUniqueId());
        this.membersByUUID.add(player.getUniqueId());

        if (leaderUUID.equals(player.getUniqueId())){
            if (totalMembers.size() <= 1) {
                try {
                    totalMembersByUUID.remove(leaderUUID);
                    membersByUUID.remove(player.getUniqueId());
                    modsByUUID.remove(player.getUniqueId());
                    updateKey("totalmembers", getTotalMembersAsStringed());
                    updateKey("members", getMembersAsStringed());
                    updateKey("mods", getModeratorsAsStringed());
                    dispose();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                if (moderators.size() > 0) {
                    int r = RNG.nextInt(moderators.size());
                    ProxiedPlayer newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.getUniqueId();
                    modsByUUID.remove(newLeader.getUniqueId());
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    ProxiedPlayer newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.getUniqueId();
                    membersByUUID.remove(newLeader.getUniqueId());
                }
            }
        }

        updateKey("leader", leaderUUID);
        updateKey("members", getMembersAsStringed());
        updateKey("mods", getModeratorsAsStringed());
    }

    public void forModeratorRemove(ProxiedPlayer player){
        this.modsByUUID.removeIf(m -> m.equals(player.getUniqueId()));
        updateKey("mods", getModeratorsAsStringed());
    }

    public void forMemberRemove(ProxiedPlayer player){
        this.membersByUUID.removeIf(m -> m.equals(player.getUniqueId()));
        updateKey("members", getMembersAsStringed());
    }

    public void forTotalMembersRemove(ProxiedPlayer player){
        this.totalMembersByUUID.removeIf(m -> m.equals(player.getUniqueId()));
        updateKey("totalmembers", getTotalMembersAsStringed());
    }

    public void replaceLeader(ProxiedPlayer player){
        setModerator(UUIDFetcher.getProxiedPlayer(leaderUUID));
        file.delete();
        updateKey("leader", player.getUniqueId());
        try {
            saveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dispose() throws Throwable {
        this.leaderUUID = null;
        this.finalize();
    }
}
