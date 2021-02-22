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
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.objects.messaging.DiscordMessage;

import java.util.*;

public class PartyUtils {
    private static final List<Party> parties = new ArrayList<>();
    private static final Configuration message = Config.getMess();

    private static final LuckPerms api = LuckPermsProvider.get();

    public static List<Party> getParties() {
        return parties;
    }
    // Party , Invites
    public static Map<Party, List<Player>> invites = new HashMap<>();

    public static Party getParty(Player player) {
        try {
            for (Party party : parties) {
                if (party.hasMember(player))
                    return party;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Party getParty(UUID uuid) {
        try {
            for (Party party : parties) {
                if (party.hasMember(UUIDFetcher.getPlayer(uuid)))
                    return party;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasParty(Player player) {
        for (Party party : parties) {
            if (party.hasMember(player)) return true;
        }
        return false;
    }

    public static boolean isParty(Party party){
        return parties.contains(party);
    }

    public static boolean checkPlayer(Party party, Player player, Player sender){
        if (! isParty(party) || party == null) {
            MessagingUtils.sendBUserMessage(sender, noPartyFound);
            return false;
        }

        if (! party.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(sender, notInParty);
            return false;
        }

        if (hasParty(player)) {
            MessagingUtils.sendBUserMessage(sender, alreadyHasOne);
            return false;
        }

        return true;
    }

    public static void createParty(StreamLine streamLine, Player player) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(player.uuid);

        if (p == null) return;

        if (getParty(player) != null) {
            MessagingUtils.sendBUserMessage(p, already);
            return;
        }

        try {
            Party party = new Party(streamLine, player);

            addParty(party);

            MessagingUtils.sendBPUserMessage(party, p, p, create);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createPartySized(StreamLine streamLine, Player player, int size) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(player.uuid);

        if (p == null) return;

        if (getParty(player) != null) {
            MessagingUtils.sendBUserMessage(p, already);
            return;
        }

        try {
            int maxSize = getMaxSize(player);

            if (size > maxSize) {
                MessagingUtils.sendBUserMessage(p, tooBig);
                return;
            }

            Party party = new Party(streamLine, player, size);

            parties.add(party);

            MessagingUtils.sendBPUserMessage(party, p, p, create);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addParty(Party party){
        Party p = getParty(party.leader);

        if (p != null) return;

        parties.add(party);
    }

    public static void removeParty(Party party){ parties.remove(party); }

    public static void sendInvite(Player to, Player from) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(from.uuid);

        if (p == null) return;

        try {
            Party party = getParty(PlayerUtils.getStat(from));

            if (!checkPlayer(party, to, from)) return;

            if (to.equals(from)) {
                MessagingUtils.sendBUserMessage(p, inviteNonSelf);
                return;
            }

            if (! party.hasModPerms(from)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            if (party.invites.contains(to)) {
                MessagingUtils.sendBUserMessage(p, inviteFailure);
                return;
            }

            if (to.online) {
                MessagingUtils.sendBPUserMessage(party, p, to, inviteUser
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                        .replace("%leaderdefault%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                );
            }

            for (Player pl : party.totalMembers) {
                if (! pl.online) continue;

                ProxiedPlayer member = UUIDFetcher.getPPlayer(pl.uuid);

                if (member == null) continue;

                if (pl.equals(from)) {
                    MessagingUtils.sendBPUserMessage(party, p, member, inviteLeader
                            .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                            .replace("%leaderdefault%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, p, member, inviteMembers
                            .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                            .replace("%leaderdefault%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                    );
                }
            }

            party.addInvite(to);
            invites.remove(party);
            invites.put(party, party.invites);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void acceptInvite(Player accepter, Player from) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(accepter.uuid);

        if (p == null) return;

        try {
            Party party = getParty(PlayerUtils.getStat(from));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, acceptFailure);
                return;
            }

            if (! party.hasMember(from)) {
                MessagingUtils.sendBUserMessage(p, otherNotInParty);
                return;
            }

            if (! party.invites.contains(accepter)) {
                MessagingUtils.sendBUserMessage(p, denyFailure);
                return;
            }

            if (party.invites.contains(accepter)) {
                if (party.getSize() >= party.maxSize) {
                    MessagingUtils.sendBPUserMessage(party, p, p, notEnoughSpace);
                    return;
                }

                MessagingUtils.sendBPUserMessage(party, p, p, acceptUser
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(accepter))
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                );

                for (Player pl : party.totalMembers){
                    if (! pl.online) continue;

                    ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                    if (m == null) continue;

                    if (m.equals(party.leader)){
                        MessagingUtils.sendBPUserMessage(party, p, m, acceptLeader
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(accepter))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, m, acceptMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(accepter))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                        );
                    }
                }

                party.addMember(accepter);
                party.removeInvite(accepter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void denyInvite(Player denier, Player from) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(denier.uuid);

        if (p == null) return;

        try {
            Party party = getParty(PlayerUtils.getStat(from));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, denyFailure);
                return;
            }

            if (! party.hasMember(from)) {
                MessagingUtils.sendBUserMessage(p, otherNotInParty);
                return;
            }

            if (! party.invites.contains(denier)) {
                MessagingUtils.sendBUserMessage(p, denyFailure);
                return;
            }

            party.removeInvite(denier);

            MessagingUtils.sendBPUserMessage(party, p, p, denyUser
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(denier))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
            );

            for (Player pl : party.totalMembers){
                if (! pl.online) continue;

                ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                if (m == null) continue;

                if (m.equals(party.leader)){
                    MessagingUtils.sendBPUserMessage(party, p, m, denyLeader
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(denier))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, p, m, denyMembers
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(denier))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void warpParty(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        Party party = getParty(PlayerUtils.getStat(sender));

        if (!isParty(party) || party == null) {
            MessagingUtils.sendBUserMessage(p, noPartyFound);
            return;
        }

        if (! party.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(p, notInParty);
            return;
        }

        if (! party.hasModPerms(sender)) {
            MessagingUtils.sendBPUserMessage(party, p, p, noPermission);
            return;
        }

        for (Player player : party.totalMembers){
            if (! player.online) continue;

            ProxiedPlayer m = UUIDFetcher.getPPlayer(player.uuid);

            if (m == null) continue;

            if (player.equals(sender)) {
                MessagingUtils.sendBPUserMessage(party, p, m, warpSender);
            } else {
                MessagingUtils.sendBPUserMessage(party, p, m, warpMembers);
            }

            m.connect(sender.getServer().getInfo());
        }
    }

    public static void muteParty(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        Party party = getParty(PlayerUtils.getStat(sender));

        if (!isParty(party) || party == null) {
            MessagingUtils.sendBUserMessage(p, noPartyFound);
            return;
        }

        if (! party.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(p, notInParty);
            return;
        }

        if (! party.hasModPerms(sender)) {
            MessagingUtils.sendBPUserMessage(party, p, p, noPermission);
            return;
        }

        if (party.isMuted) {
            for (Player player : party.totalMembers) {
                if (! player.online) continue;

                ProxiedPlayer m = UUIDFetcher.getPPlayer(player.uuid);

                if (m == null) continue;

                if (player.equals(sender)){
                    MessagingUtils.sendBPUserMessage(party, p, m, unmuteUser);
                } else {
                    MessagingUtils.sendBPUserMessage(party, p, m, unmuteMembers);
                }
            }

        } else {
            for (Player player : party.totalMembers) {
                if (! player.online) continue;

                ProxiedPlayer m = UUIDFetcher.getPPlayer(player.uuid);

                if (m == null) continue;

                if (player.equals(sender)){
                    MessagingUtils.sendBPUserMessage(party, p, m, muteUser);
                } else {
                    MessagingUtils.sendBPUserMessage(party, p, m, muteMembers);
                }
            }

        }
        party.toggleMute();
    }

    public static void kickMember(Player sender, Player player) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        Party party = getParty(PlayerUtils.getStat(sender));

        if (!isParty(party) || party == null) {
            MessagingUtils.sendBUserMessage(p, kickFailure);
            return;
        }

        if (!party.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(p, notInParty);
            return;
        }

        if (!party.hasMember(player)) {
            MessagingUtils.sendBUserMessage(p, otherNotInParty);
            return;
        }

        if (!party.hasModPerms(sender)) {
            MessagingUtils.sendBPUserMessage(party, p, p, noPermission);
            return;
        }

        if (party.hasModPerms(player)) {
            MessagingUtils.sendBPUserMessage(party, p, p, kickMod);
            return;
        }

        if (sender.equals(player)) {
            MessagingUtils.sendBPUserMessage(party, p, p, kickSelf);
        } else if (! party.hasModPerms(sender)) {
            MessagingUtils.sendBPUserMessage(party, p, p, noPermission);
        } else if (party.hasModPerms(player)) {
            MessagingUtils.sendBPUserMessage(party, p, p, kickMod);
        } else {
            for (Player pl : party.totalMembers) {
                if (!pl.online) continue;

                ProxiedPlayer m = UUIDFetcher.getPPlayer(player.uuid);

                if (m == null) continue;

                if (pl.equals(sender)) {
                    MessagingUtils.sendBPUserMessage(party, p, m, kickSender
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(player))
                    );
                } else if (pl.equals(player)) {
                    MessagingUtils.sendBPUserMessage(party, p, m, kickUser
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(player))
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, p, m, kickMembers
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(player))
                    );
                }
            }

            party.removeMemberFromParty(player);
        }
    }

    public static void disband(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        try {
            Party party = getParty(PlayerUtils.getStat(sender));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInParty);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            for (Player pl : party.totalMembers) {
                if (! pl.online) continue;

                ProxiedPlayer member = UUIDFetcher.getPPlayer(pl.uuid);

                if (member == null) continue;

                if (!member.equals(party.leader)) {
                    MessagingUtils.sendBPUserMessage(party, p, member, disbandMembers
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, p, member, disbandLeader
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                    );
                }

            }

            removeParty(party);

            party.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openParty(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        try {
            Party party = getParty(PlayerUtils.getStat(sender));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInParty);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            if (party.isPublic) {
                MessagingUtils.sendBPUserMessage(party, p, p, openFailure
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                        .replace("%size%", Integer.toString(party.getSize()))
                );
            } else {
                party.setPublic(true);

                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer member = UUIDFetcher.getPPlayer(pl.uuid);

                    if (member == null) continue;

                    if (member.equals(party.leader)) {
                        MessagingUtils.sendBPUserMessage(party, p, member, openLeader
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, member, openMembers
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openPartySized(Player sender, int size) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        try {
            Party party = getParty(PlayerUtils.getStat(sender));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInParty);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            if (party.isPublic) {
                MessagingUtils.sendBPUserMessage(party, p, p, openFailure
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                        .replace("%max%", Integer.toString(party.getMaxSize()))
                );
            } else {
                party.setPublic(true);
                party.setMaxSize(size);

                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer member = UUIDFetcher.getPPlayer(pl.uuid);

                    if (member == null) continue;

                    if (member.equals(party.leader)) {
                        MessagingUtils.sendBPUserMessage(party, p, member, openLeader
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                .replace("%max%", Integer.toString(party.getMaxSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, member, openMembers
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                .replace("%max%", Integer.toString(party.getMaxSize()))
                        );
                    }
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeParty(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(PlayerUtils.getStat(sender));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInParty);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            if (!party.isPublic) {
                MessagingUtils.sendBPUserMessage(party, p, p, closeFailure
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                        .replace("%size%", Integer.toString(party.getSize()))
                );
            } else {
                party.setPublic(false);

                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer member = UUIDFetcher.getPPlayer(pl.uuid);

                    if (member == null) continue;

                    if (member.equals(pl)) {
                        MessagingUtils.sendBPUserMessage(party, p, member, closeSender
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, member, closeMembers
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listParty(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(PlayerUtils.getStat(sender));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInParty);
                return;
            }

            String leaderBulk = listLeaderBulk
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%size%", Integer.toString(party.getSize()));
            String moderatorBulk = listModBulkMain
                    .replace("%moderators%", moderators(party))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                    .replace("%size%", Integer.toString(party.getSize()));
            String memberBulk = listMemberBulkMain
                    .replace("%members%", members(party))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                    .replace("%size%", Integer.toString(party.getSize()));

            MessagingUtils.sendBPUserMessage(party, p, p, listMain
                    .replace("%leaderbulk%", leaderBulk)
                    .replace("%moderatorbulk%", moderatorBulk)
                    .replace("%memberbulk%", memberBulk)
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                    .replace("%size%", Integer.toString(party.getSize()))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String moderators(Party party) {
        try {
            if (! (party.moderators.size() > 0)) {
                return listModBulkNone;
            }

            StringBuilder mods = new StringBuilder();

            int i = 1;

            for (Player m : party.moderators) {
                if (i <= party.moderators.size()) {
                    mods.append(listModBulkNotLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                            .replace("%size%", Integer.toString(party.getSize()))
                    );
                } else {
                    mods.append(listModBulkLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                            .replace("%size%", Integer.toString(party.getSize()))
                    );
                }
                i++;
            }

            return mods.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String members(Party party) {
        try {
            if (! (party.members.size() > 0)) {
                return listMemberBulkNone;
            }

            StringBuilder mems = new StringBuilder();

            int i = 1;

            for (Player m : party.members) {
                if (i <= party.moderators.size()) {
                    mems.append(listMemberBulkNotLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                            .replace("%size%", Integer.toString(party.getSize()))
                    );
                } else {
                    mems.append(listMemberBulkLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                            .replace("%size%", Integer.toString(party.getSize()))
                    );
                }
                i++;
            }

            return mems.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void promotePlayer(Player sender, Player member) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(PlayerUtils.getStat(sender));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInParty);
                return;
            }

            if (! party.hasMember(member)) {
                MessagingUtils.sendBUserMessage(p, otherNotInParty);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            switch (party.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBPUserMessage(party, p, p, promoteFailure
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                            .replace("%level%", textLeader
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                    .replace("%size%", Integer.toString(party.getSize()))
                            )
                    );
                    return;
                case MODERATOR:
                    party.replaceLeader(member);

                    for (Player pl : party.totalMembers) {
                        if (! pl.online) continue;

                        ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                        if (m == null) continue;

                        if (m.equals(party.leader)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    party.setModerator(member);

                    for (Player pl : party.totalMembers) {
                        if (! pl.online) continue;

                        ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                        if (m == null) continue;

                        if (m.equals(party.leader)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void demotePlayer(Player sender, Player member) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(PlayerUtils.getStat(sender));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInParty);
                return;
            }

            if (! party.hasMember(member)) {
                MessagingUtils.sendBUserMessage(p, otherNotInParty);
                return;
            }

            if (!party.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            switch (party.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBPUserMessage(party, p, p, demoteIsLeader
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                            .replace("%level%", textLeader)
                    );
                    return;
                case MODERATOR:
                    party.setMember(member);

                    for (Player pl : party.totalMembers) {
                        if (! pl.online) continue;

                        ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                        if (m == null) continue;

                        if (m.equals(party.leader)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, demoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                    .replace("%level%", textMember)
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, demoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                    .replace("%level%", textMember)
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, p, m, demoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                                    .replace("%level%", textMember)
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    MessagingUtils.sendBPUserMessage(party, p, p, demoteFailure
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                            .replace("%level%", textMember)
                    );
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void joinParty(Player sender, Player from) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(PlayerUtils.getStat(from));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(from)) {
                MessagingUtils.sendBUserMessage(p, otherNotInParty);
                return;
            }

            if (party.getSize() >= party.maxSize) {
                MessagingUtils.sendBPUserMessage(party, p, p, notEnoughSpace);
                return;
            }

            if (party.isPublic) {
                party.addMember(sender);

                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBPUserMessage(party, p, m, joinUser
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, m, joinMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                        );
                    }
                }
            } else {
                MessagingUtils.sendBPUserMessage(party, p, p, joinFailure);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getMaxSize(Player leader){
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

    public static void leaveParty(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(PlayerUtils.getStat(sender));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInParty);
                return;
            }

            if (party.leader.equals(sender)) {
                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBPUserMessage(party, p, m, leaveUser);
                        MessagingUtils.sendBPUserMessage(party, p, m, disbandLeader);
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, m, leaveMembers);
                        MessagingUtils.sendBPUserMessage(party, p, m, disbandMembers);
                    }
                }

                parties.remove(party);
                party.dispose();
                return;
            }

            if (party.hasMember(sender)) {
                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBPUserMessage(party, p, m, leaveUser
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, m, leaveMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(party.leader))))
                        );
                    }
                }

                party.removeMemberFromParty(sender);
            } else {
                MessagingUtils.sendBPUserMessage(party, p, p, leaveFailure);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void sendChat(Player sender, String msg) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(PlayerUtils.getStat(sender));

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInParty);
                return;
            }

            if (party.isMuted && ! party.hasModPerms(sender)) {
                MessagingUtils.sendBPUserMessage(party, p, p, chatMuted
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(sender))
                        .replace("%message%", msg)
                );
                return;
            }

            if (ConfigUtils.partyConsole) {
                MessagingUtils.sendBPUserMessage(party, p, StreamLine.getInstance().getProxy().getConsole(), chatConsole
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(sender))
                        .replace("%message%", msg)
                );
            }

            for (Player pl : party.totalMembers) {
                if (! pl.online) continue;

                ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                if (m == null) continue;

                MessagingUtils.sendBPUserMessage(party, p, m, chat
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(sender))
                        .replace("%message%", msg)
                );
            }

            if (ConfigUtils.partyToDiscord) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, discordTitle, msg, ConfigUtils.textChannelParties));
            }

            for (ProxiedPlayer pp : StreamLine.getInstance().getProxy().getPlayers()){
                if (! pp.hasPermission(ConfigUtils.partyView)) continue;

                Player them = PlayerUtils.getStat(pp);

                if (them == null) continue;

                if (! them.pspy) continue;

                MessagingUtils.sendBPUserMessage(party, p, pp, spy.replace("%message%", msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MESSAGES...
    // Text.
    public static final String textLeader = message.getString("party.text.leader");
    public static final String textModerator = message.getString("party.text.moderator");
    public static final String textMember = message.getString("party.text.member");
    // Discord.
    public static final String discordTitle = message.getString("party.discord.title");
    // Spy.
    public static final String spy = message.getString("party.spy");
    // No party.
    public static final String noPartyFound = message.getString("party.no-party");
    // Already made.
    public static final String already = message.getString("party.already-made");
    // Already in one.
    public static final String alreadyHasOne = message.getString("party.already-has");
    // Too big.
    public static final String tooBig = message.getString("party.too-big");
    // Not high enough permissions.
    public static final String noPermission = message.getString("party.no-permission");
    // Not in a party.
    public static final String notInParty = message.getString("party.not-in-a-party");
    public static final String otherNotInParty = message.getString("party.other-not-in-party");
    // Not enough space in party.
    public static final String notEnoughSpace = message.getString("party.not-enough-space");
    // Chat.
    public static final String chat = message.getString("party.chat.message");
    public static final String chatMuted = message.getString("party.chat.muted");
    public static final String chatConsole = message.getString("party.chat.console");
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
    public static final String closeSender = message.getString("party.close.sender");
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
    public static final String inviteNonSelf = message.getString("party.invite.non-self");
    // Kick.
    public static final String kickUser = message.getString("party.kick.user");
    public static final String kickSender = message.getString("party.kick.sender");
    public static final String kickMembers = message.getString("party.kick.members");
    public static final String kickFailure = message.getString("party.kick.failure");
    public static final String kickMod = message.getString("party.kick.mod");
    public static final String kickSelf = message.getString("party.kick.self");
    // Mute.
    public static final String muteUser = message.getString("party.mute.mute.user");
    public static final String muteMembers = message.getString("party.mute.mute.members");
    public static final String unmuteUser = message.getString("party.mute.unmute.user");
    public static final String unmuteMembers = message.getString("party.mute.unmute.members");
    // Warp.
    public static final String warpSender = message.getString("party.warp.sender");
    public static final String warpMembers = message.getString("party.warp.members");
}
