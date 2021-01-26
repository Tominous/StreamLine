package net.plasmere.streamline.objects;

import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.query.QueryOptions;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.*;

public class Party {
    public int maxSize;
    public Player leader;
    public UUID leaderUUID;
    public List<Player> totalMembers = new ArrayList<>();
    public List<UUID> totalUUIDs = new ArrayList<>();
    public List<Player> members = new ArrayList<>();
    public List<UUID> membersUUIDs = new ArrayList<>();
    public List<Player> moderators = new ArrayList<>();
    public List<UUID> modUUIDs = new ArrayList<>();
    public String name;
    public boolean isPublic = false;
    public boolean isMuted = false;
    // to , from
    public List<Player> invites = new ArrayList<>();
    public List<UUID> invitesUUIDs = new ArrayList<>();

    private final LuckPerms api = LuckPermsProvider.get();
    private final StreamLine plugin;
    public enum Level {
        MEMBER,
        MODERATOR,
        LEADER
    }

    public Party(StreamLine streamLine, Player leader){
        this.plugin = streamLine;
        this.leader = leader;
        this.leaderUUID = leader.uuid;
        this.totalMembers.add(leader);
        this.totalUUIDs.add(leaderUUID);
        this.maxSize = getMaxSize(leader);
        this.isPublic = false;
    }

    public Party(StreamLine streamLine, Player leader, int size){
        this.plugin = streamLine;
        this.leader = leader;
        this.leaderUUID = leader.uuid;
        this.totalMembers.add(leader);
        this.totalUUIDs.add(leaderUUID);
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
        this.invitesUUIDs.add(invite.uuid);
    }

    public void removeInvite(Player invite){
        this.invites.remove(invite);
        this.invitesUUIDs.remove(invite.uuid);
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
        forModeratorRemove(mod);
    }

    public void removeMember(Player member){
        forMemberRemove(member);
    }

    public void setModerator(Player mod){
        forModeratorRemove(mod);
        this.moderators.add(mod);
        this.modUUIDs.add(mod.uuid);
        this.members.remove(mod);
        this.membersUUIDs.remove(mod.uuid);
    }

    public void setMember(Player member){
        forMemberRemove(member);
        this.members.add(member);
        this.membersUUIDs.add(member.uuid);
        this.moderators.remove(member);
        this.modUUIDs.remove(member.uuid);
    }

    public void addMember(Player member){
        removeMemberFromParty(member);
        this.members.add(member);
        this.membersUUIDs.add(member.uuid);
        this.totalMembers.add(member);
        this.totalUUIDs.add(member.uuid);
    }

    public void removeMemberFromParty(Player member){
        forMemberRemove(member);
        forModeratorRemove(member);
        forTotalMembers(member);
    }

    public void forMemberRemove(Player member){
        this.members.removeIf(m -> m.equals(member));
        this.membersUUIDs.removeIf(m -> m.equals(member.uuid));
    }

    public void forModeratorRemove(Player mod){
        this.moderators.removeIf(m -> m.equals(mod));
        this.modUUIDs.removeIf(m -> m.equals(mod.uuid));
    }

    public void forTotalMembers(Player member){
        this.totalMembers.removeIf(m -> m.equals(member));
        this.totalUUIDs.removeIf(m -> m.equals(member.uuid));
    }

    public boolean hasMember(Player member){
        if (this.totalMembers.contains(member)) return true;

        loadLists();

        return this.totalMembers.contains(member) || this.totalUUIDs.contains(member.uuid);
    }

    public void loadLists(){
        totalMembers.clear();
        for (UUID u : totalUUIDs) {
            Player p = UUIDFetcher.getPlayer(u);
            if (p == null) continue;

            totalMembers.add(p);
        }

        members.clear();
        for (UUID u : membersUUIDs) {
            Player p = UUIDFetcher.getPlayer(u);
            if (p == null) continue;

            members.add(p);
        }

        moderators.clear();
        for (UUID u : modUUIDs) {
            Player p = UUIDFetcher.getPlayer(u);
            if (p == null) continue;

            moderators.add(p);
        }

        invites.clear();
        for (UUID u : invitesUUIDs) {
            Player p = UUIDFetcher.getPlayer(u);
            if (p == null) continue;

            invites.add(p);
        }

        Player pl = UUIDFetcher.getPlayer(leaderUUID);
        if (pl == null) return;

        this.leader = pl;
    }

    public boolean isModerator(Player member) {
        return this.moderators.contains(member) || this.modUUIDs.contains(member.uuid);
    }

    public boolean isLeader(Player member) {
        return this.leader.equals(member) || this.leaderUUID.equals(member.uuid);
    }

    public boolean hasModPerms(Player member){
        return isModerator(member) || isLeader(member);
    }

    public int getMaxSize(Player leader){
        try {
            Collection<PermissionNode> perms =
                    Objects.requireNonNull(api.getGroupManager().getGroup(
                            Objects.requireNonNull(api.getUserManager().getUser(leader.getName())).getPrimaryGroup()
                    )).getNodes(NodeType.PERMISSION);

            for (Group group : Objects.requireNonNull(api.getUserManager().getUser(leader.getName())).getInheritedGroups(QueryOptions.defaultContextualOptions())){
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
