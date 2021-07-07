package net.plasmere.streamline.objects;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.utils.PartyUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.*;

public class Party {
    public int maxSize;
    public Player leader;
    public String leaderUUID;
    public List<Player> totalMembers = new ArrayList<>();
    public List<String> totalMembersByUUID = new ArrayList<>();
    public List<Player> members = new ArrayList<>();
    public List<String> membersByUUID = new ArrayList<>();
    public List<Player> moderators = new ArrayList<>();
    public List<String> modsByUUID = new ArrayList<>();
    public String name;
    public boolean isPublic = false;
    public boolean isMuted = false;
    // to , from
    public List<Player> invites = new ArrayList<>();
    public List<String> invitesByUUID = new ArrayList<>();

    public enum Level {
        MEMBER,
        MODERATOR,
        LEADER
    }

    public Party(Player leader){
        this.leader = leader;
        this.leaderUUID = leader.uuid;
        this.totalMembers.add(leader);
        this.totalMembersByUUID.add(leaderUUID);
        this.maxSize = getMaxSize(leader);
        this.isPublic = false;
    }

    public Party(Player leader, int size){
        this.leader = leader;
        this.leaderUUID = leader.uuid;
        this.totalMembers.add(leader);
        this.totalMembersByUUID.add(leaderUUID);
        this.maxSize = Math.min(size, getMaxSize(leader));
        this.isPublic = true;
    }

    public void toggleMute() {
        isMuted = ! isMuted;
    }

    public void dispose() {
        this.leader = null;
        try {
            this.finalize();
        } catch (Throwable e){
            e.printStackTrace();
        }
    }

    public Level getLevel(Player member){
        if (this.members.contains(member))
            return Level.MEMBER;
        else if (this.moderators.contains(member))
            return Level.MODERATOR;
        else if (this.leader.equals(member))
            return Level.LEADER;
        else
            return Level.MEMBER;
    }

    public void addInvite(Player invite){
        this.invites.add(invite);
        this.invitesByUUID.add(invite.uuid);
    }

    public void removeInvite(Player invite){
        this.invites.remove(invite);
        this.invitesByUUID.remove(invite.uuid);
    }

    public void setPublic(boolean bool){
        this.isPublic = bool;
    }

    public void setMaxSize(int size){
        if (size < getMaxSize(this.leader))
            this.maxSize = size;
    }

    public int getSize(){
        return totalMembers.size();
    }

    public int getMaxSize() { return maxSize; }

    public void replaceLeader(Player newLeader){
        setModerator(leader);

        removeMember(newLeader);
        removeMod(newLeader);

        this.leader = newLeader;
        this.leaderUUID = newLeader.uuid;
    }

    public void removeMod(Player mod){
        removeFromModerators(mod);
    }

    public void removeMember(Player member){
        remFromMembers(member);
    }

    public void setModerator(Player mod){
        removeFromModerators(mod);
        this.moderators.add(mod);
        this.modsByUUID.add(mod.uuid);
        this.members.remove(mod);
        this.membersByUUID.remove(mod.uuid);
    }

    public void setMember(Player member){
        remFromMembers(member);
        this.members.add(member);
        this.membersByUUID.add(member.uuid);
        this.moderators.remove(member);
        this.modsByUUID.remove(member.uuid);
    }

    public void addMember(Player member){
        removeMemberFromParty(member);
        this.members.add(member);
        this.membersByUUID.add(member.uuid);
        this.totalMembers.add(member);
        this.totalMembersByUUID.add(member.uuid);
    }

    public void removeMemberFromParty(Player member){
        remFromMembers(member);
        removeFromModerators(member);
        remFromTMembers(member);
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

        PartyUtils.removeInvite(PartyUtils.getParty(from), player);

        return builder.toString();
    }

    public boolean hasMember(Player member){
        if (this.totalMembers.contains(member)) return true;

        loadLists();

        return this.totalMembers.contains(member) || this.totalMembersByUUID.contains(member.uuid);
    }

    public void loadLists(){
        totalMembers.clear();
        for (String u : totalMembersByUUID) {
            Player p = PlayerUtils.getOrCreateByUUID(u, true);
            if (p == null) continue;

            totalMembers.add(p);
        }

        members.clear();
        for (String u : membersByUUID) {
            Player p = PlayerUtils.getOrCreateByUUID(u, true);
            if (p == null) continue;

            members.add(p);
        }

        moderators.clear();
        for (String u : modsByUUID) {
            Player p = PlayerUtils.getOrCreateByUUID(u, true);
            if (p == null) continue;

            moderators.add(p);
        }

        invites.clear();
        for (String u : invitesByUUID) {
            Player p = PlayerUtils.getOrCreateByUUID(u, true);
            if (p == null) continue;

            invites.add(p);
        }

        Player pl = PlayerUtils.getOrCreateByUUID(leaderUUID, true);
        if (pl == null) return;

        this.leader = pl;
    }

    public boolean isModerator(Player member) {
        return this.moderators.contains(member) || this.modsByUUID.contains(member.uuid);
    }

    public boolean isLeader(Player member) {
        return this.leader.equals(member) || this.leaderUUID.equals(member.uuid);
    }

    public boolean hasModPerms(Player member){
        return isModerator(member) || isLeader(member);
    }

    public boolean hasModPerms(String uuid) {
        try {
            return modsByUUID.contains(uuid) || leaderUUID.equals(uuid);
        } catch (Exception e) {
            return false;
        }
    }

    public int getMaxSize(Player leader){
        if (! StreamLine.lpHolder.enabled) return ConfigUtils.partyMax;

        try {
            Collection<PermissionNode> perms =
                    Objects.requireNonNull(StreamLine.lpHolder.api.getGroupManager().getGroup(
                            Objects.requireNonNull(StreamLine.lpHolder.api.getUserManager().getUser(leader.getName())).getPrimaryGroup()
                    )).getNodes(NodeType.PERMISSION);

            for (Group group : Objects.requireNonNull(StreamLine.lpHolder.api.getUserManager().getUser(leader.getName())).getInheritedGroups(QueryOptions.defaultContextualOptions())){
                perms.addAll(group.getNodes(NodeType.PERMISSION));
            }

            boolean isGood = false;

            int highestSize = 1;
            for (PermissionNode perm : perms) {
                try {
                    String p = perm.getPermission();
                    for (int i = 1; i <= ConfigUtils.partyMax; i++) {
                        String pTry = ConfigUtils.partyMaxPerm + i;
                        if (p.equals(pTry)) {
                            isGood = true;

                            if (highestSize < i)
                                highestSize = i;
                        }
                    }
                } catch (Exception e) {
                    // Do nothing.
                }
            }

            if (highestSize == 1)
                return ConfigUtils.partyMax;
            else if (isGood)
                return highestSize;
            else
                return ConfigUtils.partyMax;
        } catch (Exception e) {
            e.printStackTrace();
            return ConfigUtils.partyMax;
        }
    }
}
