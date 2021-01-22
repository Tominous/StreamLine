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
            StreamLine.getInstance().getLogger().info("Guild file: " + file.getName() + " (In the \"guilds\" folder.)");

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

    public ProxiedPlayer getMember(UUID uuid) /*throws Exception*/ {
        return Objects.requireNonNull(UUIDFetcher.getPlayer(uuid));

        //throw new Exception("Player not found!");
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
        this.modsByUUID = loadModerators();
        this.membersByUUID = loadMembers();
        this.totalMembersByUUID = loadTotalMembers();
        this.invitesByUUID = loadInvites();
        this.isMuted = Boolean.parseBoolean(getFromKey("muted"));
        this.isPublic = Boolean.parseBoolean(getFromKey("public"));
        this.xp = Integer.parseInt(getFromKey("xp"));
        this.lvl = Integer.parseInt(getFromKey("lvl"));

        try {
            loadMods();

            loadMems();

            loadTMems();

            loadInvs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMods(){
        if (moderators != null) {
            moderators.clear();
            for (UUID uuid : modsByUUID) {
                try {
                    moderators.add(getMember(uuid));
                } catch (Exception e) {
                    // do nothing
                }
            }
        } else {
            moderators = new ArrayList<>();
        }
    }

    public void loadMems(){
        if (members != null) {
            members.clear();
            for (UUID uuid : membersByUUID) {
                try {
                    members.add(getMember(uuid));
                } catch (Exception e) {
                    // do nothing
                }
            }
        } else {
            members = new ArrayList<>();
        }
    }

    public void loadTMems(){
        if (totalMembers != null) {
            totalMembers.clear();
            for (UUID uuid : totalMembersByUUID) {
                try {
                    totalMembers.add(getMember(uuid));
                } catch (Exception e) {
                    // do nothing
                }
            }
        } else {
            totalMembers = new ArrayList<>();
        }
    }

    public void loadInvs(){
        if (invites != null) {
            invites.clear();
            for (UUID uuid : invitesByUUID) {
                try {
                    invites.add(getMember(uuid));
                } catch (Exception e) {
                    // do nothing
                }
            }
        } else {
            invites = new ArrayList<>();
        }
    }

    private List<UUID> loadModerators(){
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

        //StreamLine.getInstance().getLogger().info("Just saved Guild info for leader (UUID): " + leaderUUID);
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
            needed = 10 * this.lvl + 7;
        } else if (this.lvl >= 16 && this.lvl <= 30){
            needed = 25 * this.lvl - 38;
        } else if (this.lvl > 30) {
            needed = 45 * this.lvl - 158;
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

        int i = 0;
        for (UUID uuid : totalMembersByUUID){
            i++;
            if (i != totalMembersByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    private String getMembersAsStringed(){
        System.out.println("GMAS : " + membersByUUID);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (UUID uuid : membersByUUID){
            i++;
            if (i != membersByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    private String getModeratorsAsStringed(){
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (UUID uuid : modsByUUID){
            i++;
            if (i != modsByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public boolean hasMember(UUID uuid){
        return totalMembersByUUID.contains(uuid);
    }

    public boolean hasMember(ProxiedPlayer player){
        try {
            return totalMembers.contains(player);
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

    public String removeFromModerators(ProxiedPlayer player){
        modsByUUID.remove(player.getUniqueId());
        moderators.remove(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (UUID uuid : modsByUUID) {
            i++;
            if (i != modsByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String remFromMembers(ProxiedPlayer player){
        membersByUUID.remove(player.getUniqueId());
        members.remove(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (UUID uuid : membersByUUID) {
            i++;
            if (i != membersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String remFromTMembers(ProxiedPlayer player){
        totalMembersByUUID.remove(player.getUniqueId());
        totalMembers.remove(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (UUID uuid : totalMembersByUUID) {
            i++;
            if (i != totalMembersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String remFromInvites(ProxiedPlayer player){
        invitesByUUID.remove(player.getUniqueId());
        invites.remove(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (UUID uuid : invitesByUUID) {
            i++;
            if (i != invitesByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String addToModerators(ProxiedPlayer player){
        modsByUUID.add(player.getUniqueId());
        moderators.add(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (UUID uuid : modsByUUID) {
            i++;
            if (i != modsByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String addToMembers(ProxiedPlayer player){
        membersByUUID.add(player.getUniqueId());
        members.add(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (UUID uuid : membersByUUID) {
            i++;
            if (i != membersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String addToTMembers(ProxiedPlayer player){
        totalMembersByUUID.add(player.getUniqueId());
        totalMembers.add(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (UUID uuid : totalMembersByUUID) {
            i++;
            if (i != totalMembersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String addToInvites(ProxiedPlayer player){
        invitesByUUID.add(player.getUniqueId());
        invites.add(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (UUID uuid : invitesByUUID) {
            i++;
            if (i != invitesByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public void addMember(ProxiedPlayer player){
        StreamLine.getInstance().getLogger().info("Members as Stringed: " + getMembersAsStringed());
        StreamLine.getInstance().getLogger().info("TMembers as Stringed: " + getTotalMembersAsStringed());
        StreamLine.getInstance().getLogger().info("Members BEFORE: " + getFromKey("members"));
        StreamLine.getInstance().getLogger().info("Members BEFORE raw: " + membersByUUID.toString());
        StreamLine.getInstance().getLogger().info("MembersP BEFORE raw: " + members.toString());
        StreamLine.getInstance().getLogger().info("TMembers BEFORE raw: " + totalMembersByUUID.toString());
        updateKey("totalmembers", addToTMembers(player));
        updateKey("members", addToMembers(player));
        StreamLine.getInstance().getLogger().info("TMembersP BEFORE raw: " + totalMembers.toString());
        StreamLine.getInstance().getLogger().info("Members AFTER: " + getFromKey("members"));
        StreamLine.getInstance().getLogger().info("Members AFTER raw: " + membersByUUID.toString());
        StreamLine.getInstance().getLogger().info("MembersP AFTER raw: " + members.toString());
        StreamLine.getInstance().getLogger().info("TMembers AFTER raw: " + totalMembersByUUID.toString());
        StreamLine.getInstance().getLogger().info("TMembersP AFTER raw: " + totalMembers.toString());
        StreamLine.getInstance().getLogger().info("Members as Stringed: " + getMembersAsStringed());
        StreamLine.getInstance().getLogger().info("TMembers as Stringed: " + getTotalMembersAsStringed());

        try {
            saveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeMemberFromGuild(ProxiedPlayer player){
        Random RNG = new Random();

        if (leaderUUID.equals(player.getUniqueId())){
            if (totalMembers.size() <= 1) {
                try {
                    updateKey("totalmembers", remFromTMembers(player));
                    updateKey("members", remFromMembers(player));
                    updateKey("mods", removeFromModerators(player));
                    disband();
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

        updateKey("totalmembers", remFromTMembers(player));
        updateKey("leader", leaderUUID);
        updateKey("members", remFromMembers(player));
        updateKey("mods", removeFromModerators(player));
    }

    public void addInvite(ProxiedPlayer to) {
        updateKey("invites", addToInvites(to));
        loadVars();
    }

    public void toggleMute(){
        updateKey("muted", ! isMuted);
    }

    public void setPublic(boolean bool){
        updateKey("public", bool);
    }

    public Level getLevel(ProxiedPlayer member){
        if (this.membersByUUID.contains(member.getUniqueId()))
            return Level.MEMBER;
        else if (this.modsByUUID.contains(member.getUniqueId()))
            return Level.MODERATOR;
        else if (this.leaderUUID.equals(member.getUniqueId()))
            return Level.LEADER;
        else
            return Level.MEMBER;
    }

    public void setModerator(ProxiedPlayer player){
        Random RNG = new Random();

        forModeratorRemove(player);

        if (leaderUUID.equals(player.getUniqueId())){
            if (totalMembers.size() <= 1) {
                try {
                    updateKey("totalmembers", remFromTMembers(player));
                    updateKey("members", remFromMembers(player));
                    updateKey("mods", removeFromModerators(player));
                    disband();
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

        loadMods();
        loadMems();

        updateKey("leader", leaderUUID);
        updateKey("members", remFromMembers(player));
        updateKey("mods", addToModerators(player));

        try {
            saveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMember(ProxiedPlayer player){
        Random RNG = new Random();

        forMemberRemove(player);

        if (leaderUUID.equals(player.getUniqueId())){
            if (totalMembers.size() <= 1) {
                try {
                    updateKey("totalmembers", remFromTMembers(player));
                    updateKey("members", remFromMembers(player));
                    updateKey("mods", removeFromModerators(player));
                    disband();
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

        loadMods();
        loadMems();

        updateKey("leader", leaderUUID);
        updateKey("members", addToMembers(player));
        updateKey("mods", removeFromModerators(player));

        try {
            saveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        updateKey("mods", getModeratorsAsStringed() + "." + leaderUUID.toString());
        modsByUUID = loadModerators();
        updateKey("leader", player.getUniqueId());
        updateKey("mods", getModeratorsAsStringed()
                .replace("." + leaderUUID.toString(), "")
                .replace(leaderUUID.toString() + ".", "")
                .replace(leaderUUID.toString(), "")
        );

        leaderUUID = UUID.fromString(getFromKey("leader"));

        loadMods();

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

    public void disband(){
        file.delete();

        try {
            dispose();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
