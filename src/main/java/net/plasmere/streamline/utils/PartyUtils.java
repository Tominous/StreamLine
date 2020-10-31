package net.plasmere.streamline.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Party;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.*;

public class PartyUtils {
    private static final List<Party> parties = new ArrayList<>();
    private static final Configuration message = Config.getMess();

    private static final LuckPerms api = LuckPermsProvider.get();

    public static List<Party> getParties() {
        return parties;
    }
    // Party , Invites
    public static Map<Party, List<ProxiedPlayer>> invites = new HashMap<>();

    public static Party getParty(ProxiedPlayer player) throws Exception {
        try {
            Party it = null;
            for (Party party : parties) {
                if (party.hasMember(player))
                    it = party;
            }
            return it;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static boolean isParty(Party party){
        return parties.contains(party);
    }

    public static void reloadParty(Party party) throws Exception {
        parties.remove(getParty(party.leader));
        parties.add(party);
    }

    public static void createParty(StreamLine streamLine, ProxiedPlayer player) throws Exception {
        try {
            Party party = new Party(streamLine, player);

            addParty(party);

            MessagingUtils.sendBPUserMessage(party, player, player, create);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void createPartySized(StreamLine streamLine, ProxiedPlayer player, int size) throws Exception {
        try {
            int maxSize = getMaxSize(player);

            if (size > maxSize) {
                MessagingUtils.sendBUserMessage(player, tooBig);
                return;
            }

            Party party = new Party(streamLine, player, size);

            parties.add(party);

            MessagingUtils.sendBPUserMessage(party, player, player, create);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void addParty(Party party){ parties.add(party); }

    public static void removeParty(Party party){ parties.remove(party); }

    public static void sendInvite(ProxiedPlayer to, ProxiedPlayer from) throws Exception {
        try {
            Party party = getParty(from);

            if (! isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(to, noPartyFound);
                return;
            }

            if (! party.hasModPerms(from)) {
                MessagingUtils.sendBUserMessage(from, noPermission);
                return;
            }

            if (party.invites.contains(to)){
                MessagingUtils.sendBUserMessage(from, inviteFailure);
                return;
            }

            MessagingUtils.sendBPUserMessage(party, from, to, inviteUser
                    .replace("%sender%", from.getDisplayName())
                    .replace("%user%", to.getDisplayName())
                    .replace("%leader%", getParty(from).leader.getDisplayName())
                    .replace("%leaderdefault%", getParty(from).leader.getName())
            );

            for (ProxiedPlayer member : party.totalMembers) {
                if (member.equals(from)) {
                    MessagingUtils.sendBPUserMessage(party, from, member, inviteLeader
                            .replace("%sender%", from.getDisplayName())
                            .replace("%user%", to.getDisplayName())
                            .replace("%leader%", getParty(from).leader.getDisplayName())
                            .replace("%leaderdefault%", getParty(from).leader.getName())
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, from, member, inviteMembers
                            .replace("%sender%", from.getDisplayName())
                            .replace("%user%", to.getDisplayName())
                            .replace("%leader%", getParty(from).leader.getDisplayName())
                            .replace("%leaderdefault%", getParty(from).leader.getName())
                    );
                }
            }

            reloadParty(party);

            party.addInvite(to);
            invites.remove(party);
            invites.put(party, party.invites);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void acceptInvite(ProxiedPlayer accepter, ProxiedPlayer from) throws Exception {
        try {
            Party party = getParty(from);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(accepter, acceptFailure);
                return;
            }

            if (party.invites.contains(accepter)) {
                if (party.getSize() >= party.maxSize) {
                    MessagingUtils.sendBPUserMessage(party, accepter, accepter, notEnoughSpace);
                    return;
                }

                MessagingUtils.sendBPUserMessage(party, accepter, accepter, acceptUser
                        .replace("%user%", accepter.getDisplayName())
                        .replace("%leader%", from.getDisplayName())
                );

                for (ProxiedPlayer m : party.totalMembers){
                    if (m.equals(party.leader)){
                        MessagingUtils.sendBPUserMessage(party, accepter, m, acceptLeader
                                .replace("%user%", accepter.getDisplayName())
                                .replace("%leader%", from.getDisplayName())
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, accepter, m, acceptMembers
                                .replace("%user%", accepter.getDisplayName())
                                .replace("%leader%", from.getDisplayName())
                        );
                    }
                }

                party.addMember(accepter);
                party.removeInvite(accepter);
            }

            reloadParty(party);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void denyInvite(ProxiedPlayer denier, ProxiedPlayer from) throws Exception {
        try {
            Party party = getParty(from);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(denier, denyFailure);
                return;
            }

            party.removeInvite(denier);

            MessagingUtils.sendBPUserMessage(party, denier, denier, denyUser
                    .replace("%user%", denier.getDisplayName())
                    .replace("%leader%", from.getDisplayName())
            );

            for (ProxiedPlayer m : party.totalMembers){
                if (m.equals(party.leader)){
                    MessagingUtils.sendBPUserMessage(party, denier, m, denyLeader
                            .replace("%user%", denier.getDisplayName())
                            .replace("%leader%", from.getDisplayName())
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, denier, m, denyMembers
                            .replace("%user%", denier.getDisplayName())
                            .replace("%leader%", from.getDisplayName())
                    );
                }
            }

            reloadParty(party);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void warpParty(ProxiedPlayer sender) throws Exception{
        Party party = getParty(sender);

        if (!isParty(party) || party == null) {
            MessagingUtils.sendBUserMessage(sender, noPartyFound);
            return;
        }

        if (! party.leader.equals(sender)) {
            MessagingUtils.sendBPUserMessage(party, sender, sender, noPermission);
            return;
        }

        for (ProxiedPlayer m : party.totalMembers){
            if (m.equals(sender)) {
                MessagingUtils.sendBPUserMessage(party, sender, m, warpLeader);
            } else {
                MessagingUtils.sendBPUserMessage(party, sender, m, warpMembers);
            }

            m.connect(sender.getServer().getInfo());
        }

        reloadParty(party);
    }

    public static void muteParty(ProxiedPlayer sender) throws Exception{
        Party party = getParty(sender);

        if (!isParty(party) || party == null) {
            MessagingUtils.sendBUserMessage(sender, noPartyFound);
            return;
        }

        if (! party.hasModPerms(sender)) {
            MessagingUtils.sendBPUserMessage(party, sender, sender, noPermission);
            return;
        }

        if (party.isMuted) {
            for (ProxiedPlayer m : party.totalMembers) {
                if (m.equals(sender)){
                    MessagingUtils.sendBPUserMessage(party, sender, m, muteUser);
                } else {
                    MessagingUtils.sendBPUserMessage(party, sender, m, muteMembers);
                }
            }

            party.toggleMute();
        } else {
            for (ProxiedPlayer m : party.totalMembers) {
                if (m.equals(sender)){
                    MessagingUtils.sendBPUserMessage(party, sender, m, unmuteUser);
                } else {
                    MessagingUtils.sendBPUserMessage(party, sender, m, unmuteMembers);
                }
            }

            party.toggleMute();
        }

        reloadParty(party);
    }

    public static void kickMember(ProxiedPlayer sender, ProxiedPlayer player) throws Exception{
        Party party = getParty(sender);

        if (!isParty(party) || party == null) {
            MessagingUtils.sendBUserMessage(sender, noPartyFound);
            return;
        }

        if (! party.hasMember(player)) {
            MessagingUtils.sendBPUserMessage(party, sender, sender, kickFailure
                    .replace("%user%", player.getDisplayName())
            );
        }

        if (! party.hasModPerms(sender)) {
            MessagingUtils.sendBPUserMessage(party, sender, sender, noPermission);
        } else {
            if (sender.equals(player)) {
                MessagingUtils.sendBPUserMessage(party, sender, sender, kickSelf);
            } else if (player.equals(party.leader)) {
                MessagingUtils.sendBPUserMessage(party, sender, sender, noPermission);
            } else {
                for (ProxiedPlayer m : party.totalMembers){
                    if (m.equals(sender)) {
                        MessagingUtils.sendBPUserMessage(party, sender, m, kickSender
                                .replace("%user%", player.getDisplayName())
                        );
                    } else if (m.equals(player)) {
                        MessagingUtils.sendBPUserMessage(party, sender, m, kickUser
                                .replace("%user%", player.getDisplayName())
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, sender, m, kickMembers
                                .replace("%user%", player.getDisplayName())
                        );
                    }
                }

                party.removeMemberFromParty(player);
            }
        }

        reloadParty(party);
    }

    public static void disband(ProxiedPlayer sender) throws Throwable {
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(sender, noPartyFound);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender, noPermission);
                return;
            }

            for (ProxiedPlayer member : party.totalMembers) {
                if (!member.equals(party.leader)) {
                    MessagingUtils.sendBPUserMessage(party, sender, member, disbandMembers
                            .replace("%leader%", party.leader.getDisplayName())
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, sender, member, disbandLeader
                            .replace("%leader%", party.leader.getDisplayName())
                    );
                }

            }

            removeParty(party);

            party.dispose();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void openParty(ProxiedPlayer sender) throws Exception {
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(sender, noPartyFound);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender, noPermission);
                return;
            }

            if (party.isPublic) {
                MessagingUtils.sendBPUserMessage(party, sender, party.leader, openFailure
                        .replace("%leader%", party.leader.getDisplayName())
                        .replace("%size%", Integer.toString(party.getSize()))
                );
            } else {
                party.setPublic(true);

                for (ProxiedPlayer member : party.totalMembers) {
                    if (member.equals(party.leader)) {
                        MessagingUtils.sendBPUserMessage(party, sender, party.leader, openLeader
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, sender, party.leader, openMembers
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    }
                }
            }

            reloadParty(party);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void openPartySized(ProxiedPlayer sender, int size) throws Exception {
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(sender, noPartyFound);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender, noPermission);
                return;
            }

            if (party.isPublic) {
                MessagingUtils.sendBPUserMessage(party, sender, party.leader, openFailure
                        .replace("%leader%", party.leader.getDisplayName())
                        .replace("%max%", Integer.toString(party.getMaxSize()))
                );
            } else {
                party.setPublic(true);
                party.setMaxSize(size);

                for (ProxiedPlayer member : party.totalMembers) {
                    if (member.equals(party.leader)) {
                        MessagingUtils.sendBPUserMessage(party, sender, party.leader, openLeader
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%max%", Integer.toString(party.getMaxSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, sender, party.leader, openMembers
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%max%", Integer.toString(party.getMaxSize()))
                        );
                    }
                }
            }

            reloadParty(party);
        }  catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void closeParty(ProxiedPlayer sender) throws Exception {
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(sender, noPartyFound);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender, noPermission);
                return;
            }

            if (!party.isPublic) {
                MessagingUtils.sendBPUserMessage(party, sender, party.leader, closeFailure
                        .replace("%leader%", party.leader.getDisplayName())
                        .replace("%size%", Integer.toString(party.getSize()))
                );
            } else {
                party.setPublic(false);

                for (ProxiedPlayer member : party.totalMembers) {
                    if (member.equals(party.leader)) {
                        MessagingUtils.sendBPUserMessage(party, sender, party.leader, closeLeader
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, sender, party.leader, closeMembers
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    }
                }
            }

            reloadParty(party);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void listParty(ProxiedPlayer sender) throws Exception {
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(sender, noPartyFound);
                return;
            }

            String leaderBulk = listLeaderBulk
                    .replace("%leader%", party.leader.getDisplayName())
                    .replace("%user%", sender.getDisplayName())
                    .replace("%size%", Integer.toString(party.getSize()));
            String moderatorBulk = listModBulkMain
                    .replace("%moderators%", moderators(party))
                    .replace("%user%", sender.getDisplayName())
                    .replace("%leader%", party.leader.getDisplayName())
                    .replace("%size%", Integer.toString(party.getSize()));
            String memberBulk = listMemberBulkMain
                    .replace("%members%", members(party))
                    .replace("%user%", sender.getDisplayName())
                    .replace("%leader%", party.leader.getDisplayName())
                    .replace("%size%", Integer.toString(party.getSize()));

            MessagingUtils.sendBPUserMessage(party, sender, sender, listMain
                    .replace("%leaderbulk%", leaderBulk)
                    .replace("%moderatorbulk%", moderatorBulk)
                    .replace("%memberbulk%", memberBulk)
                    .replace("%user%", sender.getDisplayName())
                    .replace("%leader%", party.leader.getDisplayName())
                    .replace("%size%", Integer.toString(party.getSize()))
            );

            reloadParty(party);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static String moderators(Party party) throws Exception {
        try {
            if (! (party.moderators.size() > 0)) {
                return listModBulkNone;
            }

            StringBuilder mods = new StringBuilder();

            int i = 1;

            for (ProxiedPlayer m : party.moderators) {
                if (i <= party.moderators.size()) {
                    mods.append(listModBulkNotLast
                            .replace("%user%", m.getDisplayName())
                            .replace("%leader%", party.leader.getDisplayName())
                            .replace("%size%", Integer.toString(party.getSize()))
                    );
                } else {
                    mods.append(listModBulkLast
                            .replace("%user%", m.getDisplayName())
                            .replace("%leader%", party.leader.getDisplayName())
                            .replace("%size%", Integer.toString(party.getSize()))
                    );
                }
                i++;
            }

            return mods.toString();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static String members(Party party) throws Exception {
        try {
            if (! (party.members.size() > 0)) {
                return listMemberBulkNone;
            }

            StringBuilder mems = new StringBuilder();

            int i = 1;

            for (ProxiedPlayer m : party.members) {
                if (i <= party.moderators.size()) {
                    mems.append(listMemberBulkNotLast
                            .replace("%user%", m.getDisplayName())
                            .replace("%leader%", party.leader.getDisplayName())
                            .replace("%size%", Integer.toString(party.getSize()))
                    );
                } else {
                    mems.append(listMemberBulkLast
                            .replace("%user%", m.getDisplayName())
                            .replace("%leader%", party.leader.getDisplayName())
                            .replace("%size%", Integer.toString(party.getSize()))
                    );
                }
                i++;
            }

            return mems.toString();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void promotePlayer(ProxiedPlayer sender, ProxiedPlayer member) throws Exception {
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(sender, noPartyFound);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender, noPermission);
                return;
            }

            switch (party.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBPUserMessage(party, sender, party.leader, promoteFailure
                            .replace("%user%", member.getDisplayName())
                            .replace("%leader%", party.leader.getDisplayName())
                            .replace("%level%", textLeader
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%size%", Integer.toString(party.getSize()))
                            )
                    );
                    return;
                case MODERATOR:
                    party.replaceLeader(member);

                    for (ProxiedPlayer m : party.totalMembers) {
                        if (m.equals(party.leader)) {
                            MessagingUtils.sendBPUserMessage(party, sender, m, promoteLeader
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textLeader
                                            .replace("%leader%", party.leader.getDisplayName())
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, sender, m, promoteUser
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textLeader
                                            .replace("%leader%", party.leader.getDisplayName())
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, sender, m, promoteMembers
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textLeader
                                            .replace("%leader%", party.leader.getDisplayName())
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    party.setModerator(member);

                    for (ProxiedPlayer m : party.totalMembers) {
                        if (m.equals(party.leader)) {
                            MessagingUtils.sendBPUserMessage(party, sender, m, promoteLeader
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textModerator
                                            .replace("%leader%", party.leader.getDisplayName())
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, sender, m, promoteUser
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textModerator
                                            .replace("%leader%", party.leader.getDisplayName())
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, sender, m, promoteMembers
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textModerator
                                            .replace("%leader%", party.leader.getDisplayName())
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        }
                    }
                    break;
            }

            reloadParty(party);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void demotePlayer(ProxiedPlayer sender, ProxiedPlayer member) throws Exception {
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(sender, noPartyFound);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender, noPermission);
                return;
            }

            switch (party.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBPUserMessage(party, sender, party.leader, demoteIsLeader
                            .replace("%user%", member.getDisplayName())
                            .replace("%leader%", party.leader.getDisplayName())
                            .replace("%level%", textLeader)
                    );
                    return;
                case MODERATOR:
                    party.setMember(member);

                    for (ProxiedPlayer m : party.totalMembers) {
                        if (m.equals(party.leader)) {
                            MessagingUtils.sendBPUserMessage(party, sender, m, demoteLeader
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textMember)
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, sender, m, demoteUser
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textMember)
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, sender, m, demoteMembers
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textMember)
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    MessagingUtils.sendBPUserMessage(party, sender, party.leader, demoteFailure
                            .replace("%user%", member.getDisplayName())
                            .replace("%leader%", party.leader.getDisplayName())
                            .replace("%level%", textMember)
                    );
                    break;
            }

            reloadParty(party);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void joinParty(ProxiedPlayer sender, ProxiedPlayer from) throws Exception {
        try {
            Party party = getParty(from);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(sender, noPartyFound);
                return;
            }

            if (party.getSize() >= party.maxSize) {
                MessagingUtils.sendBPUserMessage(party, sender, sender, notEnoughSpace);
                return;
            }

            if (party.isPublic) {
                party.addMember(sender);

                for (ProxiedPlayer m : party.totalMembers) {
                    if (m.equals(sender)) {
                        MessagingUtils.sendBPUserMessage(party, sender, m, joinUser
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", party.leader.getDisplayName())
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, sender, m, joinMembers
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", party.leader.getDisplayName())
                        );
                    }
                }
            } else {
                MessagingUtils.sendBPUserMessage(party, sender, sender, joinFailure);
            }

            reloadParty(party);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static int getMaxSize(ProxiedPlayer leader){
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

    public static void leaveParty(ProxiedPlayer sender) throws Exception {
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(sender, noPartyFound);
                return;
            }

            if (party.leader.equals(sender)) {
                for (ProxiedPlayer m : party.totalMembers) {
                    MessagingUtils.sendBPUserMessage(party, sender, m, disbandLeader);
                }
                for (ProxiedPlayer m : party.totalMembers) {
                    if (m.equals(sender)) {
                        MessagingUtils.sendBPUserMessage(party, sender, m, leaveUser);
                        MessagingUtils.sendBPUserMessage(party, sender, m, disbandLeader);
                    } else {
                        MessagingUtils.sendBPUserMessage(party, sender, m, leaveMembers);
                        MessagingUtils.sendBPUserMessage(party, sender, m, disbandMembers);
                    }
                }

                parties.remove(party);
                party.dispose();
                return;
            }

            if (party.hasMember(sender)) {
                for (ProxiedPlayer m : party.totalMembers) {
                    if (m.equals(sender)) {
                        MessagingUtils.sendBPUserMessage(party, sender, m, leaveUser
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", party.leader.getDisplayName())
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, sender, m, leaveMembers
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", party.leader.getDisplayName())
                        );
                    }
                }

                party.removeMemberFromParty(sender);
            } else {
                MessagingUtils.sendBPUserMessage(party, sender, sender, leaveFailure);
            }

            reloadParty(party);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static void sendChat(ProxiedPlayer sender, String msg) throws Exception {
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(sender, noPartyFound);
                return;
            }

            if (party.isMuted) {
                MessagingUtils.sendBPUserMessage(party, sender, sender, chatMuted
                        .replace("%sender%", sender.getDisplayName())
                        .replace("%message%", msg)
                );
                return;
            }

            for (ProxiedPlayer member : party.totalMembers) {
                MessagingUtils.sendBPUserMessage(party, sender, member, chat
                        .replace("%sender%", sender.getDisplayName())
                        .replace("%message%", msg)
                );
            }

            reloadParty(party);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    // MESSAGES...
    // Text.
    public static final String textLeader = message.getString("party.text.leader");
    public static final String textModerator = message.getString("party.text.moderator");
    public static final String textMember = message.getString("party.text.member");
    // No party.
    public static final String noPartyFound = message.getString("party.no-party");
    // Too big.
    public static final String tooBig = message.getString("party.too-big");
    // Not high enough permissions.
    public static final String noPermission = message.getString("party.no-permission");
    // Not in a party.
    public static final String notInParty = message.getString("party.not-in-a-party");
    // Not enough space in party.
    public static final String notEnoughSpace = message.getString("party.not-enough-space");
    // Chat.
    public static final String chat = message.getString("party.chat.message");
    public static final String chatMuted = message.getString("party.chat.muted");
    // Create.
    public static final String create = message.getString("party.create");
    // Join.
    public static final String joinMembers = message.getString("party.join.members");
    public static final String joinUser = message.getString("party.join.user");
    public static final String joinFailure = message.getString("party.join.failure");
    // Leave.
    public static final String leaveMembers = message.getString("party.leave.members");
    public static final String leaveUser = message.getString("party.leave.user");
    public static final String leaveFailure = message.getString("party.leave.failure");
    // Promote.
    public static final String promoteMembers = message.getString("party.promote.members");
    public static final String promoteUser = message.getString("party.promote.user");
    public static final String promoteLeader = message.getString("party.promote.leader");
    public static final String promoteFailure = message.getString("party.promote.failure");
    // Demote.
    public static final String demoteMembers = message.getString("party.demote.members");
    public static final String demoteUser = message.getString("party.demote.user");
    public static final String demoteLeader = message.getString("party.demote.leader");
    public static final String demoteFailure = message.getString("party.demote.failure");
    public static final String demoteIsLeader = message.getString("party.demote.is-leader");
    // List.
    public static final String listMain = message.getString("party.list.main");
    public static final String listLeaderBulk = message.getString("party.list.leaderbulk");
    public static final String listModBulkMain = message.getString("party.list.moderatorbulk.main");
    public static final String listModBulkNotLast = message.getString("party.list.moderatorbulk.moderators.not-last");
    public static final String listModBulkLast = message.getString("party.list.moderatorbulk.moderators.last");
    public static final String listModBulkNone = message.getString("party.list.moderatorbulk.moderators.if-none");
    public static final String listMemberBulkMain = message.getString("party.list.memberbulk.main");
    public static final String listMemberBulkNotLast = message.getString("party.list.memberbulk.members.not-last");
    public static final String listMemberBulkLast = message.getString("party.list.memberbulk.members.last");
    public static final String listMemberBulkNone = message.getString("party.list.memberbulk.members.if-none");
    // Open.
    public static final String openMembers = message.getString("party.open.members");
    public static final String openLeader = message.getString("party.open.leader");
    public static final String openFailure = message.getString("party.open.failure");
    // Close.
    public static final String closeMembers = message.getString("party.close.members");
    public static final String closeLeader = message.getString("party.close.leader");
    public static final String closeFailure = message.getString("party.close.failure");
    // Disband.
    public static final String disbandMembers = message.getString("party.disband.members");
    public static final String disbandLeader = message.getString("party.disband.leader");
    // Accept.
    public static final String acceptUser = message.getString("party.accept.user");
    public static final String acceptLeader = message.getString("party.accept.leader");
    public static final String acceptMembers = message.getString("party.accept.members");
    public static final String acceptFailure = message.getString("party.accept.failure");
    // Deny.
    public static final String denyUser = message.getString("party.deny.user");
    public static final String denyLeader = message.getString("party.deny.leader");
    public static final String denyMembers = message.getString("party.deny.members");
    public static final String denyFailure = message.getString("party.deny.failure");
    // Invite.
    public static final String inviteUser = message.getString("party.invite.user");
    public static final String inviteLeader = message.getString("party.invite.leader");
    public static final String inviteMembers = message.getString("party.invite.members");
    public static final String inviteFailure = message.getString("party.invite.failure");
    // Kick.
    public static final String kickUser = message.getString("party.kick.user");
    public static final String kickSender = message.getString("party.kick.sender");
    public static final String kickMembers = message.getString("party.kick.members");
    public static final String kickFailure = message.getString("party.kick.failure");
    public static final String kickSelf = message.getString("party.kick.self");
    // Mute.
    public static final String muteUser = message.getString("party.mute.mute.user");
    public static final String muteMembers = message.getString("party.mute.mute.members");
    public static final String unmuteUser = message.getString("party.mute.unmute.user");
    public static final String unmuteMembers = message.getString("party.mute.unmute.members");
    // Warp.
    public static final String warpLeader = message.getString("party.warp.leader");
    public static final String warpMembers = message.getString("party.warp.members");
}
