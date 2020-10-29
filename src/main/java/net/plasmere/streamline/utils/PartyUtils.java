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

import java.security.PublicKey;
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

    public static void createParty(StreamLine streamLine, ProxiedPlayer player) throws Exception {
        try {
            Party party = new Party(streamLine, player);

            addParty(party);

            MessagingUtils.sendBPUserMessage(party, player, create);
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

            MessagingUtils.sendBPUserMessage(party, player, create);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void addParty(Party party){ parties.add(party); }

    public static void removeParty(Party party){ parties.remove(party); }

    public static void sendInvite(ProxiedPlayer to, ProxiedPlayer from) throws Exception {
        try {
            Party party = getParty(from);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(to, noPartyFound);
                return;
            }

            if (!party.hasModPerms(from)) {
                MessagingUtils.sendBUserMessage(from, noPermission);
                return;
            }

            MessagingUtils.sendBPUserMessage(party, to, inviteUser
                    .replace("%user%", to.getDisplayName())
                    .replace("%leader%", from.getDisplayName())
                    .replace("%leaderdefault%", from.getName())
            );
            MessagingUtils.sendBPUserMessage(party, from, inviteLeader
                    .replace("%user%", to.getDisplayName())
                    .replace("%leader%", from.getDisplayName())
                    .replace("%leaderdefault%", from.getName())
            );

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
                MessagingUtils.sendBUserMessage(accepter, noPartyFound);
                return;
            }

            if (party.invites.contains(accepter)) {
                if (party.getSize() >= party.maxSize) {
                    MessagingUtils.sendBPUserMessage(party, accepter, notEnoughSpace);
                    return;
                }

                party.addMember(accepter);
                party.removeInvite(accepter);

                MessagingUtils.sendBPUserMessage(party, accepter, acceptUser
                        .replace("%user%", accepter.getDisplayName())
                        .replace("%leader%", from.getDisplayName())
                );
                MessagingUtils.sendBPUserMessage(party, from, acceptLeader
                        .replace("%user%", accepter.getDisplayName())
                        .replace("%leader%", from.getDisplayName())
                );
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void denyInvite(ProxiedPlayer denier, ProxiedPlayer from) throws Exception {
        try {
            Party party = getParty(denier);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(from, noPartyFound);
                return;
            }

            party.removeInvite(denier);

            MessagingUtils.sendBPUserMessage(party, denier, denyUser
                    .replace("%user%", denier.getDisplayName())
                    .replace("%leader%", from.getDisplayName())
            );
            MessagingUtils.sendBPUserMessage(party, from, denyLeader
                    .replace("%user%", denier.getDisplayName())
                    .replace("%leader%", from.getDisplayName())
            );
        } catch (Exception e) {
            throw new Exception(e);
        }
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
                    MessagingUtils.sendBPUserMessage(party, member, disbandMembers
                            .replace("%leader%", party.leader.getDisplayName())
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, member, disbandLeader
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
                MessagingUtils.sendBPUserMessage(party, party.leader, openFailure
                        .replace("%leader%", party.leader.getDisplayName())
                        .replace("%size%", Integer.toString(party.getSize()))
                );
            } else {
                party.setPublic(true);

                for (ProxiedPlayer member : party.totalMembers) {
                    if (member.equals(party.leader)) {
                        MessagingUtils.sendBPUserMessage(party, party.leader, openLeader
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, party.leader, openMembers
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    }
                }
            }
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
                MessagingUtils.sendBPUserMessage(party, party.leader, openFailure
                        .replace("%leader%", party.leader.getDisplayName())
                        .replace("%size%", Integer.toString(party.getSize()))
                );
            } else {
                party.setMaxSize(size);

                for (ProxiedPlayer member : party.totalMembers) {
                    if (member.equals(party.leader)) {
                        MessagingUtils.sendBPUserMessage(party, party.leader, openLeader
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, party.leader, openMembers
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    }
                }
            }
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
                MessagingUtils.sendBPUserMessage(party, party.leader, closeFailure
                        .replace("%leader%", party.leader.getDisplayName())
                        .replace("%size%", Integer.toString(party.getSize()))
                );
            } else {
                party.setPublic(false);

                for (ProxiedPlayer member : party.totalMembers) {
                    if (member.equals(party.leader)) {
                        MessagingUtils.sendBPUserMessage(party, party.leader, closeLeader
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, party.leader, closeMembers
                                .replace("%leader%", party.leader.getDisplayName())
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    }
                }
            }
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
                    .replace("%moderatorbulk%", moderators(party))
                    .replace("%user%", sender.getDisplayName())
                    .replace("%leader%", party.leader.getDisplayName())
                    .replace("%size%", Integer.toString(party.getSize()));
            String memberBulk = listMemberBulkMain
                    .replace("%memberbulk%", members(party))
                    .replace("%user%", sender.getDisplayName())
                    .replace("%leader%", party.leader.getDisplayName())
                    .replace("%size%", Integer.toString(party.getSize()));

            MessagingUtils.sendBPUserMessage(party, sender, listMain
                    .replace("%leaderbulk%", leaderBulk)
                    .replace("%moderatorbulk%", moderatorBulk)
                    .replace("%memberbulk%", memberBulk)
                    .replace("%user%", sender.getDisplayName())
                    .replace("%leader%", party.leader.getDisplayName())
                    .replace("%size%", Integer.toString(party.getSize()))
            );
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static String moderators(Party party) throws Exception {
        try {
            StringBuilder mods = new StringBuilder();

            int i = 1;

            for (ProxiedPlayer m : party.moderators) {
                if (i < party.moderators.size()) {
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
            StringBuilder mems = new StringBuilder();

            int i = 1;

            for (ProxiedPlayer m : party.members) {
                if (i < party.moderators.size()) {
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

    public static void promotePlayer(ProxiedPlayer member) throws Exception {
        try {
            Party party = getParty(member);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(member, noPartyFound);
                return;
            }

            if (!party.hasModPerms(member)) {
                MessagingUtils.sendBUserMessage(member, noPermission);
                return;
            }

            switch (party.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBPUserMessage(party, party.leader, promoteFailure
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
                            MessagingUtils.sendBPUserMessage(party, m, promoteLeader
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textLeader
                                            .replace("%leader%", party.leader.getDisplayName())
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, m, promoteUser
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textLeader
                                            .replace("%leader%", party.leader.getDisplayName())
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, m, promoteMembers
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
                            MessagingUtils.sendBPUserMessage(party, m, promoteLeader
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textModerator
                                            .replace("%leader%", party.leader.getDisplayName())
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, m, promoteUser
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textModerator
                                            .replace("%leader%", party.leader.getDisplayName())
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, m, promoteMembers
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
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void demotePlayer(ProxiedPlayer member) throws Exception {
        try {
            Party party = getParty(member);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(member, noPartyFound);
                return;
            }

            if (!party.hasModPerms(member)) {
                MessagingUtils.sendBUserMessage(member, noPermission);
                return;
            }

            switch (party.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBPUserMessage(party, party.leader, demoteIsLeader
                            .replace("%user%", member.getDisplayName())
                            .replace("%leader%", party.leader.getDisplayName())
                            .replace("%level%", textLeader)
                    );
                    return;
                case MODERATOR:
                    party.setMember(member);

                    for (ProxiedPlayer m : party.totalMembers) {
                        if (m.equals(party.leader)) {
                            MessagingUtils.sendBPUserMessage(party, m, demoteLeader
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textMember)
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, m, demoteUser
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textMember)
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, m, demoteMembers
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", party.leader.getDisplayName())
                                    .replace("%level%", textMember)
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    MessagingUtils.sendBPUserMessage(party, party.leader, demoteFailure
                            .replace("%user%", member.getDisplayName())
                            .replace("%leader%", party.leader.getDisplayName())
                            .replace("%level%", textMember)
                    );
                    break;
            }
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
                MessagingUtils.sendBPUserMessage(party, sender, notEnoughSpace);
                return;
            }

            if (party.invites.contains(sender)) {
                party.addMember(sender);
                party.removeInvite(sender);
                invites.remove(party);
                invites.put(party, party.invites);

                for (ProxiedPlayer m : party.totalMembers) {
                    if (m.equals(sender)) {
                        MessagingUtils.sendBPUserMessage(party, m, joinUser
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", party.leader.getDisplayName())
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, m, joinMembers
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", party.leader.getDisplayName())
                        );
                    }
                }
            } else {
                MessagingUtils.sendBPUserMessage(party, sender, joinFailure);
            }
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

            if (party.hasMember(sender)) {
                party.removeMember(sender);

                for (ProxiedPlayer m : party.totalMembers) {
                    if (m.equals(sender)) {
                        MessagingUtils.sendBPUserMessage(party, m, leaveUser
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", party.leader.getDisplayName())
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, m, leaveMembers
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", party.leader.getDisplayName())
                        );
                    }
                }
            } else {
                MessagingUtils.sendBPUserMessage(party, sender, leaveFailure);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void sendChat(ProxiedPlayer sender, String msg) throws Exception {
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(sender, noPartyFound);
                return;
            }

            for (ProxiedPlayer member : party.totalMembers) {
                MessagingUtils.sendBPUserMessage(party, sender, chat
                        .replace("%message%", msg)
                );
            }
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
    public static final String chat = message.getString("party.chat");
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
    public static final String listMemberBulkMain = message.getString("party.list.memberbulk.main");
    public static final String listMemberBulkNotLast = message.getString("party.list.memberbulk.members.not-last");
    public static final String listMemberBulkLast = message.getString("party.list.memberbulk.members.last");
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
    public static final String acceptFailure = message.getString("party.accept.failure");
    // Deny.
    public static final String denyUser = message.getString("party.deny.user");
    public static final String denyLeader = message.getString("party.deny.leader");
    public static final String denyFailure = message.getString("party.deny.failure");
    // Invite.
    public static final String inviteUser = message.getString("party.invite.user");
    public static final String inviteLeader = message.getString("party.invite.leader");
    public static final String inviteFailure = message.getString("party.invite.failure");
}
