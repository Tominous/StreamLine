package net.plasmere.streamline.objects;

import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.query.QueryOptions;

import java.util.*;

public class Party {
    public int maxSize;
    public Player leader;
    public List<Player> totalMembers = new ArrayList<>();
    public List<Player> members = new ArrayList<>();
    public List<Player> moderators = new ArrayList<>();
    public String name;
    public boolean isPublic = false;
    public boolean isMuted = false;
    // to , from
    public List<Player> invites = new ArrayList<>();

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
        this.totalMembers.add(leader);
        this.maxSize = getMaxSize(leader);
        this.isPublic = false;
    }

    public Party(StreamLine streamLine, Player leader, int size){
        this.plugin = streamLine;
        this.leader = leader;
        this.totalMembers.add(leader);
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
    }

    public void removeInvite(Player invite){
        this.invites.remove(invite);
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
        this.members.remove(mod);
    }

    public void setMember(Player member){
        forMemberRemove(member);
        this.members.add(member);
        this.moderators.remove(member);
    }

    public void addMember(Player member){
        removeMemberFromParty(member);
        this.members.add(member);
        this.totalMembers.add(member);
    }

    public void removeMemberFromParty(Player member){
        forMemberRemove(member);
        forModeratorRemove(member);
        forTotalMembers(member);
    }

    public void forMemberRemove(Player member){
        this.members.removeIf(m -> m.equals(member));
    }

    public void forModeratorRemove(Player mod){
        this.moderators.removeIf(m -> m.equals(mod));
    }

    public void forTotalMembers(Player member){
        this.totalMembers.removeIf(m -> m.equals(member));
    }

    public boolean hasMember(Player member){
        return this.totalMembers.contains(member);
    }

    public boolean isModerator(Player member) {
        return this.moderators.contains(member);
    }

    public boolean isLeader(Player member) {
        return this.leader.equals(member);
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
