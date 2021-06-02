package net.plasmere.streamline.objects;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;
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
    public String leaderUUID;
    public List<Player> moderators = new ArrayList<>();
    public List<String> modsByUUID= new ArrayList<>();
    public List<Player> members = new ArrayList<>();
    public List<String> membersByUUID = new ArrayList<>();
    public List<Player> totalMembers = new ArrayList<>();
    public List<String> totalMembersByUUID = new ArrayList<>();
    public List<Player> invites = new ArrayList<>();
    public List<String> invitesByUUID = new ArrayList<>();
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

    public Guild(String creatorUUID, String name) {
        this.leaderUUID = creatorUUID;
        this.name = name;
        this.totalMembersByUUID.add(creatorUUID);
        this.totalMembers.add(PlayerUtils.getStatByUUID(creatorUUID));
        try {
            Objects.requireNonNull(UUIDFetcher.getPlayerByUUID(creatorUUID, true)).updateKey("guild", creatorUUID.toString());
        } catch (Exception e){
            // do nothing
        }
        construct(leaderUUID, true);
    }

    public Guild(String uuid, boolean create){
        construct(uuid, create);
    }

    private void construct(String uuid, boolean createNew) {
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
        if (info.containsKey(key)) return;

        info.put(key, value);
    }

    public Player getMember(String uuid) {
        Player player = UUIDFetcher.getPlayerByUUID(uuid, true);

        if (player == null) {
            removeUUID(uuid);
            return null;
        }

        return player;
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
        if (getFromKey("leader") == null) return;
        if (getFromKey("leader").equals("")) return;

        this.name = getFromKey("name");
        try {
            this.leaderUUID = getFromKey("leader");
        } catch (Exception e) {
            return;
        }

        if (this.leaderUUID == null) {
            try {
                throw new Exception("Improper use of the Guild's class! Report this to the owner of the StreamLine plugin...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (this.leaderUUID.equals("null") || this.leaderUUID.equals("")) {
            try {
                throw new Exception("Improper use of the Guild's class! Report this to the owner of the StreamLine plugin...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

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

    public void removeUUID(String uuid) {
        updateKey("totalmembers", TextUtils.removeExtraDot(getFromKey("totalmembers").replace(uuid, "")));
        updateKey("members", TextUtils.removeExtraDot(getFromKey("members").replace(uuid, "")));
        updateKey("mods", TextUtils.removeExtraDot(getFromKey("mods").replace(uuid, "")));
        updateKey("uuid", TextUtils.removeExtraDot(getFromKey("uuid").replace(uuid, "")));
    }

    public void loadMods(){
        if (modsByUUID != null && moderators != null) {
            moderators.clear();
            for (String uuid : modsByUUID) {
                try {
                    Player player = getMember(uuid);

                    if (player == null) continue;

                    moderators.add(player);
                } catch (Exception e) {
                    // do nothing
                }
            }
        } else {
            moderators = new ArrayList<>();
        }
    }

    public void loadMems(){
        if (membersByUUID != null && members != null) {
            members.clear();
            for (String uuid : membersByUUID) {
                try {
                    Player player = getMember(uuid);

                    if (player == null) continue;

                    members.add(player);
                } catch (Exception e) {
                    // do nothing
                }
            }
        } else {
            members = new ArrayList<>();
        }
    }

    public void loadTMems(){
        if (totalMembersByUUID != null && totalMembers != null) {
            totalMembers.clear();
            for (String uuid : totalMembersByUUID) {
                try {
                    //StreamLine.getInstance().getLogger().info("UUID : " + uuid.toString());
                    Player player = getMember(uuid);

                    if (player == null) continue;

                    totalMembers.add(player);
                } catch (Exception e) {
                    // do nothing
                }
            }
        } else {
            totalMembers = new ArrayList<>();
        }
    }

    public void loadInvs(){
        if (invitesByUUID != null && invites != null) {
            invites.clear();
            for (String uuid : invitesByUUID) {
                try {
                    Player player = getMember(uuid);

                    if (player == null) continue;

                    invites.add(player);
                } catch (Exception e) {
                    // do nothing
                }
            }
        } else {
            invites = new ArrayList<>();
        }
    }

    private List<String> loadModerators(){
        List<String> uuids = new ArrayList<>();

        try {
            if (getFromKey("mods").equals("") || getFromKey("mods") == null) return uuids;
            if (! getFromKey("mods").contains(".")) {
                uuids.add(getFromKey("mods"));
                return uuids;
            }

            for (String uuid : getFromKey("mods").split("\\.")) {
                try {
                    uuids.add(uuid);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return uuids;
    }

    private List<String> loadMembers() {
        List<String> uuids = new ArrayList<>();

        try {
            if (getFromKey("members").equals("") || getFromKey("members") == null) return uuids;
            if (! getFromKey("members").contains(".")) {
                uuids.add(getFromKey("members"));
                return uuids;
            }

            for (String uuid : getFromKey("members").split("\\.")) {
                try {
                    uuids.add(uuid);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuids;
    }

    private List<String> loadTotalMembers() {
        List<String> uuids = new ArrayList<>();

        try {
            if (getFromKey("totalmembers").equals("") || getFromKey("totalmembers") == null) return uuids;
            if (! getFromKey("totalmembers").contains(".")) {
                uuids.add(getFromKey("totalmembers"));
                return uuids;
            }

            for (String uuid : getFromKey("totalmembers").split("\\.")) {
                try {
                    uuids.add(uuid);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuids;
    }

    private List<String> loadInvites() {
        List<String> uuids = new ArrayList<>();

        try {
            if (getFromKey("invites").equals("") || getFromKey("invites") == null) return uuids;
            if (! getFromKey("invites").contains(".")) {
                uuids.add(getFromKey("invites"));
                return uuids;
            }

            for (String uuid : getFromKey("invites").split("\\.")) {
                try {
                    uuids.add(uuid);
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

        needed = 5000 + (5000 * lvl);

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

    private String getInvitesAsStringed(){
        StringBuilder builder = new StringBuilder();

        int i = 1;
        for (String uuid : invitesByUUID){
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
        for (String uuid : totalMembersByUUID){
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
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : membersByUUID){
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
        for (String uuid : modsByUUID){
            i++;
            if (i != modsByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public boolean hasMember(String uuid){
        return totalMembersByUUID.contains(uuid);
    }

    public boolean hasMember(Player player){
        loadMods();
        loadMems();
        loadTMems();
        loadInvs();

        return hasPMember(player) || hasUUIDMember(player);
    }

    public boolean hasPMember(Player player){
        try {
            return totalMembers.contains(player);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasUUIDMember(Player player){
        try {
          return hasMember(player.uuid);
        } catch (Exception e){
            return false;
        }
    }

    public boolean hasModPerms(String uuid) {
        try {
            return modsByUUID.contains(uuid) || leaderUUID.equals(uuid);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasModPerms(Player player) {
        try {
            return moderators.contains(player) || leaderUUID.equals(player.uuid);
        } catch (Exception e) {
            return false;
        }
    }

    public int getSize(){
        return totalMembersByUUID.size();
    }

    public String removeFromModerators(Player player){
        modsByUUID.remove(player.uuid);
        moderators.remove(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : modsByUUID) {
            i++;
            if (i != modsByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String remFromMembers(Player player){
        membersByUUID.remove(player.uuid);
        members.remove(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : membersByUUID) {
            i++;
            if (i != membersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String remFromTMembers(Player player){
        totalMembersByUUID.remove(player.uuid);
        totalMembers.remove(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : totalMembersByUUID) {
            i++;
            if (i != totalMembersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String remFromInvites(Player from, Player player){
        invitesByUUID.remove(player.uuid);
        invites.remove(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : invitesByUUID) {
            i++;
            if (i != invitesByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        GuildUtils.removeInvite(GuildUtils.getGuild(from), player);

        updateKey("invites", builder.toString());

        return builder.toString();
    }

    public String addToModerators(Player player){
        modsByUUID.add(player.uuid);
        moderators.add(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : modsByUUID) {
            i++;
            if (i != modsByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String addToMembers(Player player){
        membersByUUID.add(player.uuid);
        members.add(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : membersByUUID) {
            i++;
            if (i != membersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String addToTMembers(Player player){
        totalMembersByUUID.add(player.uuid);
        totalMembers.add(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : totalMembersByUUID) {
            i++;
            if (i != totalMembersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String addToInvites(Player player){
        invitesByUUID.add(player.uuid);
        invites.add(player);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : invitesByUUID) {
            i++;
            if (i != invitesByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public void addMember(Player player){
        updateKey("totalmembers", addToTMembers(player));
        updateKey("members", addToMembers(player));

        player.updateKey("guild", leaderUUID.toString());

        try {
            saveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeMemberFromGuild(Player player){
        Random RNG = new Random();

        if (leaderUUID.equals(player.uuid)){
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
                    Player newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    modsByUUID.remove(newLeader.uuid);
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    Player newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    membersByUUID.remove(newLeader.uuid);
                }
            }
        }

        updateKey("totalmembers", remFromTMembers(player));
        updateKey("leader", leaderUUID);
        updateKey("members", remFromMembers(player));
        updateKey("mods", removeFromModerators(player));
    }

    public void addInvite(Player to) {
        updateKey("invites", addToInvites(to));
        loadVars();
    }

    public void toggleMute(){
        updateKey("muted", ! isMuted);
    }

    public void setPublic(boolean bool){
        updateKey("public", bool);
    }

    public Level getLevel(Player member){
        if (this.membersByUUID.contains(member.uuid))
            return Level.MEMBER;
        else if (this.modsByUUID.contains(member.uuid))
            return Level.MODERATOR;
        else if (this.leaderUUID.equals(member.uuid))
            return Level.LEADER;
        else
            return Level.MEMBER;
    }

    public void setModerator(Player player){
        Random RNG = new Random();

        forModeratorRemove(player);

        if (leaderUUID.equals(player.uuid)){
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
                    Player newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    modsByUUID.remove(newLeader.uuid);
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    Player newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    membersByUUID.remove(newLeader.uuid);
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

    public void setMember(Player player){
        Random RNG = new Random();

        forMemberRemove(player);

        if (leaderUUID.equals(player.uuid)){
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
                    Player newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    modsByUUID.remove(newLeader.uuid);
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    Player newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    membersByUUID.remove(newLeader.uuid);
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

    public void forModeratorRemove(Player player){
        this.modsByUUID.removeIf(m -> m.equals(player.uuid));
        updateKey("mods", getModeratorsAsStringed());
    }

    public void forMemberRemove(Player player){
        this.membersByUUID.removeIf(m -> m.equals(player.uuid));
        updateKey("members", getMembersAsStringed());
    }

    public void forTotalMembersRemove(Player player){
        this.totalMembersByUUID.removeIf(m -> m.equals(player.uuid));
        updateKey("totalmembers", getTotalMembersAsStringed());
    }

    public void replaceLeader(Player player){
        updateKey("mods", getModeratorsAsStringed() + "." + leaderUUID.toString());
        modsByUUID = loadModerators();
        updateKey("leader", player.uuid);
        updateKey("mods", getModeratorsAsStringed()
                .replace("." + leaderUUID.toString(), "")
                .replace(leaderUUID.toString() + ".", "")
                .replace(leaderUUID.toString(), "")
        );

        leaderUUID = getFromKey("leader");

        loadMods();

        GuildUtils.removeGuild(Objects.requireNonNull(GuildUtils.getGuild(player)));

        file.delete();

//        if (! file.renameTo(new File(filePrePath + leaderUUID.toString() + ".properties"))){
//            StreamLine.getInstance().getLogger().info("Could not rename a guild file for " + leaderUUID + "...");
//        }

        file = null;
        file = new File(filePrePath + leaderUUID.toString() + ".properties");

        try {
            for (Player p : totalMembers) {
                p.updateKey("guild", leaderUUID.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            saveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GuildUtils.addGuild(this);
    }

    public void dispose() throws Throwable {
        this.leaderUUID = null;
        this.finalize();
    }

    public void disband(){
        for (String uuid : totalMembersByUUID){
            Player player = UUIDFetcher.getPlayerByUUID(uuid, true);

            if (player == null) continue;

            player.updateKey("guild", "");
        }

        GuildUtils.removeGuild(this);

        file.delete();

        try {
            dispose();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public String toString(){
        return PlayerUtils.forStats(totalMembers);
    }
}
