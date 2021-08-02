package net.plasmere.streamline.objects;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.users.SavableUser;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.plasmere.streamline.utils.UUIDUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Guild {
    private TreeMap<String, String> info = new TreeMap<>();
    private final String filePrePath = StreamLine.getInstance().getDataFolder() + File.separator + "guilds" + File.separator;

    public File file;
    public String name;
    public String leaderUUID;
    public List<SavableUser> moderators = new ArrayList<>();
    public List<String> modsByUUID= new ArrayList<>();
    public List<SavableUser> members = new ArrayList<>();
    public List<String> membersByUUID = new ArrayList<>();
    public List<SavableUser> totalMembers = new ArrayList<>();
    public List<String> totalMembersByUUID = new ArrayList<>();
    public List<SavableUser> invites = new ArrayList<>();
    public List<String> invitesByUUID = new ArrayList<>();
    public boolean isMuted;
    public boolean isPublic;
    public int totalXP;
    public int currentXP;
    public int lvl;
    public int maxSize = ConfigUtils.guildMax;

    public List<String> savedKeys;

    public enum Level {
        MEMBER,
        MODERATOR,
        LEADER
    }

    public Guild(String creatorUUID, String name) {
        this.leaderUUID = creatorUUID;
        this.name = name;
        this.totalMembersByUUID.add(creatorUUID);
        this.totalMembers.add(PlayerUtils.getOrCreateSUByUUID(creatorUUID));
        try {
            PlayerUtils.getOrCreateSUByUUID(creatorUUID).updateKey("guild", creatorUUID);
        } catch (Exception e){
            // do nothing
        }
        construct(leaderUUID, true);
    }

    public Guild(String uuid, boolean create){
        construct(uuid, create);
    }

    public void createCheck(String thing){
        if (thing.contains("-")){
            construct(thing, false);
        } else {
            construct(Objects.requireNonNull(UUIDUtils.getCachedUUID(thing)), false);
        }
    }

    private void construct(String uuid, boolean createNew){
        if (uuid == null) return;

        this.file = UUIDUtils.getCachedFile(StreamLine.getInstance().getGDir(), uuid + ".properties");

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
            String[] dataSplit = data.split("=", 2);
            addKeyValuePair(tryUpdateFormat(dataSplit[0]), dataSplit[1]);
        }

        reader.close();

        loadVars();
    }

    public TreeSet<String> propertiesDefaults() {
        TreeSet<String> defaults = new TreeSet<>();
        defaults.add("name=" + name);
        defaults.add("leader=" + leaderUUID);
        defaults.add("mods=" + "");
        defaults.add("members=" + "");
        defaults.add("total-members=" + leaderUUID);
        defaults.add("invites=" + "");
        defaults.add("muted=false");
        defaults.add("public=false");
        defaults.add("total-xp=0");
        defaults.add("current-xp=0");
        defaults.add("lvl=1");
        //defaults.add("");
        return defaults;
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
        this.totalXP = Integer.parseInt(getFromKey("total-xp"));
        this.currentXP = getCurrentXP();
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

    public TreeMap<String, String> updatableKeys() {
        TreeMap<String, String> thing = new TreeMap<>();

        thing.put("xp", "total-xp");
        thing.put("totalmembers", "total-members");

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

    public SavableUser getMember(String uuid) {
        SavableUser stat = PlayerUtils.getOrCreateSUByUUID(uuid);

        if (stat == null) {
            removeUUID(uuid);
            return null;
        }

        return stat;
    }

    public void removeUUID(String uuid) {
        updateKey("total-members", TextUtils.removeExtraDot(getFromKey("total-members").replace(uuid, "")));
        updateKey("members", TextUtils.removeExtraDot(getFromKey("members").replace(uuid, "")));
        updateKey("mods", TextUtils.removeExtraDot(getFromKey("mods").replace(uuid, "")));
        updateKey("uuid", TextUtils.removeExtraDot(getFromKey("uuid").replace(uuid, "")));
    }

    public void loadMods(){
        if (modsByUUID != null && moderators != null) {
            moderators.clear();
            for (String uuid : modsByUUID) {
                try {
                    SavableUser stat = getMember(uuid);

                    if (stat == null) continue;

                    moderators.add(stat);
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
                    SavableUser stat = getMember(uuid);

                    if (stat == null) continue;

                    members.add(stat);
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
                    //MessagingUtils.logInfo("UUID : " + uuid.toString());
                    SavableUser stat = getMember(uuid);

                    if (stat == null) continue;

                    totalMembers.add(stat);
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
                    SavableUser stat = getMember(uuid);

                    if (stat == null) continue;

                    invites.add(stat);
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
            if (getFromKey("total-members").equals("") || getFromKey("total-members") == null) return uuids;
            if (! getFromKey("total-members").contains(".")) {
                uuids.add(getFromKey("total-members"));
                return uuids;
            }

            for (String uuid : getFromKey("total-members").split("\\.")) {
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

    public boolean hasMember(SavableUser stat){
        loadMods();
        loadMems();
        loadTMems();
        loadInvs();

        return hasPMember(stat) || hasUUIDMember(stat);
    }

    public boolean hasPMember(SavableUser stat){
        try {
            return totalMembers.contains(stat);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasUUIDMember(SavableUser stat){
        try {
          return hasMember(stat.uuid);
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

    public boolean hasModPerms(SavableUser stat) {
        try {
            return moderators.contains(stat) || leaderUUID.equals(stat.uuid);
        } catch (Exception e) {
            return false;
        }
    }

    public int getSize(){
        return totalMembersByUUID.size();
    }

    public String removeFromModerators(SavableUser stat){
        modsByUUID.remove(stat.uuid);
        moderators.remove(stat);

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

    public String remFromMembers(SavableUser stat){
        membersByUUID.remove(stat.uuid);
        members.remove(stat);

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

    public String remFromTMembers(SavableUser stat){
        totalMembersByUUID.remove(stat.uuid);
        totalMembers.remove(stat);

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

    public String remFromInvites(SavableUser from, SavableUser stat){
        invitesByUUID.remove(stat.uuid);
        invites.remove(stat);

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

        GuildUtils.removeInvite(GuildUtils.getGuild(from), stat);

        updateKey("invites", builder.toString());

        return builder.toString();
    }

    public String addToModerators(SavableUser stat){
        modsByUUID.add(stat.uuid);
        moderators.add(stat);

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

    public String addToMembers(SavableUser stat){
        membersByUUID.add(stat.uuid);
        members.add(stat);

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

    public String addToTMembers(SavableUser stat){
        totalMembersByUUID.add(stat.uuid);
        totalMembers.add(stat);

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

    public String addToInvites(SavableUser stat){
        invitesByUUID.add(stat.uuid);
        invites.add(stat);

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

    public void addMember(SavableUser stat){
        updateKey("total-members", addToTMembers(stat));
        updateKey("members", addToMembers(stat));

        stat.updateKey("guild", leaderUUID.toString());

        try {
            saveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeMemberFromGuild(SavableUser stat){
        Random RNG = new Random();

        if (leaderUUID.equals(stat.uuid)){
            if (totalMembers.size() <= 1) {
                try {
                    updateKey("total-members", remFromTMembers(stat));
                    updateKey("members", remFromMembers(stat));
                    updateKey("mods", removeFromModerators(stat));
                    disband();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                if (moderators.size() > 0) {
                    int r = RNG.nextInt(moderators.size());
                    SavableUser newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    modsByUUID.remove(newLeader.uuid);
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    SavableUser newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    membersByUUID.remove(newLeader.uuid);
                }
            }
        }

        updateKey("total-members", remFromTMembers(stat));
        updateKey("leader", leaderUUID);
        updateKey("members", remFromMembers(stat));
        updateKey("mods", removeFromModerators(stat));
    }

    public void addInvite(SavableUser to) {
        updateKey("invites", addToInvites(to));
        loadVars();
    }

    public void toggleMute(){
        updateKey("muted", ! isMuted);
    }

    public void setPublic(boolean bool){
        updateKey("public", bool);
    }

    public Level getLevel(SavableUser member){
        if (this.membersByUUID.contains(member.uuid))
            return Level.MEMBER;
        else if (this.modsByUUID.contains(member.uuid))
            return Level.MODERATOR;
        else if (this.leaderUUID.equals(member.uuid))
            return Level.LEADER;
        else
            return Level.MEMBER;
    }

    public void setModerator(SavableUser stat){
        Random RNG = new Random();

        forModeratorRemove(stat);

        if (leaderUUID.equals(stat.uuid)){
            if (totalMembers.size() <= 1) {
                try {
                    updateKey("total-members", remFromTMembers(stat));
                    updateKey("members", remFromMembers(stat));
                    updateKey("mods", removeFromModerators(stat));
                    disband();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                if (moderators.size() > 0) {
                    int r = RNG.nextInt(moderators.size());
                    SavableUser newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    modsByUUID.remove(newLeader.uuid);
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    SavableUser newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    membersByUUID.remove(newLeader.uuid);
                }
            }
        }

        loadMods();
        loadMems();

        updateKey("leader", leaderUUID);
        updateKey("members", remFromMembers(stat));
        updateKey("mods", addToModerators(stat));

        try {
            saveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMember(SavableUser stat){
        Random RNG = new Random();

        forMemberRemove(stat);

        if (leaderUUID.equals(stat.uuid)){
            if (totalMembers.size() <= 1) {
                try {
                    updateKey("total-members", remFromTMembers(stat));
                    updateKey("members", remFromMembers(stat));
                    updateKey("mods", removeFromModerators(stat));
                    disband();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                if (moderators.size() > 0) {
                    int r = RNG.nextInt(moderators.size());
                    SavableUser newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    modsByUUID.remove(newLeader.uuid);
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    SavableUser newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    membersByUUID.remove(newLeader.uuid);
                }
            }
        }

        loadMods();
        loadMems();

        updateKey("leader", leaderUUID);
        updateKey("members", addToMembers(stat));
        updateKey("mods", removeFromModerators(stat));

        try {
            saveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forModeratorRemove(SavableUser stat){
        this.modsByUUID.removeIf(m -> m.equals(stat.uuid));
        updateKey("mods", getModeratorsAsStringed());
    }

    public void forMemberRemove(SavableUser stat){
        this.membersByUUID.removeIf(m -> m.equals(stat.uuid));
        updateKey("members", getMembersAsStringed());
    }

    public void forTotalMembersRemove(SavableUser stat){
        this.totalMembersByUUID.removeIf(m -> m.equals(stat.uuid));
        updateKey("total-members", getTotalMembersAsStringed());
    }

    public void replaceLeader(SavableUser stat){
        updateKey("mods", getModeratorsAsStringed() + "." + leaderUUID.toString());
        modsByUUID = loadModerators();
        updateKey("leader", stat.uuid);
        updateKey("mods", getModeratorsAsStringed()
                .replace("." + leaderUUID.toString(), "")
                .replace(leaderUUID.toString() + ".", "")
                .replace(leaderUUID.toString(), "")
        );

        leaderUUID = getFromKey("leader");

        loadMods();

        GuildUtils.removeGuild(Objects.requireNonNull(GuildUtils.getGuild(stat)));

        file.delete();

//        if (! file.renameTo(new File(filePrePath + leaderUUID.toString() + ".properties"))){
//            MessagingUtils.logInfo("Could not rename a guild file for " + leaderUUID + "...");
//        }

        file = null;
        file = new File(filePrePath + leaderUUID.toString() + ".properties");

        try {
            for (SavableUser p : totalMembers) {
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
            SavableUser stat = PlayerUtils.getOrCreateSUByUUID(uuid);

            stat.updateKey("guild", "");
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

        //MessagingUtils.logInfo("Just saved SavableUser info for stat: " + PlayerUtils.getOffOnReg(stat));
    }
}
