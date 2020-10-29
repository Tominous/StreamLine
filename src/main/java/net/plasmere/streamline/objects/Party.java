package net.plasmere.streamline.objects;

import me.lucko.luckperms.common.node.types.Permission;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.plasmere.streamline.utils.MessagingUtils;

import java.util.*;

public class Party {
    public int maxSize;
    public ProxiedPlayer leader;
    public List<ProxiedPlayer> totalMembers = new ArrayList<>();
    public List<ProxiedPlayer> members = new ArrayList<>();
    public List<ProxiedPlayer> moderators = new ArrayList<>();
    public String name;
    public boolean isPublic = false;
    // to , from
    public List<ProxiedPlayer> invites = new ArrayList<>();

    private final LuckPerms api = LuckPermsProvider.get();
    private final StreamLine plugin;
    public enum Level {
        MEMBER,
        MODERATOR,
        LEADER
    }

    public Party(StreamLine streamLine, ProxiedPlayer leader){
        this.plugin = streamLine;
        this.leader = leader;
        this.totalMembers.add(leader);
        this.maxSize = getMaxSize(leader);
        this.isPublic = false;
    }

    public Party(StreamLine streamLine, ProxiedPlayer leader, int size){
        this.plugin = streamLine;
        this.leader = leader;
        this.totalMembers.add(leader);
        this.maxSize = Math.min(size, getMaxSize(leader));
        this.isPublic = true;
    }

    public void dispose() throws Throwable {
        this.leader = null;
        this.finalize();
    }

    public Level getLevel(ProxiedPlayer member){
        if (this.members.contains(member))
            return Level.MEMBER;
        else if (this.moderators.contains(member))
            return Level.MODERATOR;
        else if (this.leader.equals(member))
            return Level.LEADER;
        else
            return Level.MEMBER;
    }

    public void addInvite(ProxiedPlayer invite){
        this.invites.add(invite);
    }

    public void removeInvite(ProxiedPlayer invite){
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

    public void replaceLeader(ProxiedPlayer newLeader){
        setModerator(leader);
        this.leader = newLeader;
    }

    public void setModerator(ProxiedPlayer mod){
        this.moderators.add(mod);
        this.members.remove(mod);
    }

    public void setMember(ProxiedPlayer member){
        this.members.add(member);
        this.moderators.remove(member);
    }

    public void addMember(ProxiedPlayer member){
        this.totalMembers.add(member);
    }

    public void removeMember(ProxiedPlayer member){
        this.totalMembers.remove(member);
    }

    public boolean hasMember(ProxiedPlayer member){
        return this.totalMembers.contains(member);
    }

    public boolean isModerator(ProxiedPlayer member) {
        return this.moderators.contains(member);
    }

    public boolean isLeader(ProxiedPlayer member) {
        return this.leader.equals(member);
    }

    public boolean hasModPerms(ProxiedPlayer member){
        return isModerator(member) || isLeader(member);
    }

    private int getMaxSize(ProxiedPlayer leader){
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
