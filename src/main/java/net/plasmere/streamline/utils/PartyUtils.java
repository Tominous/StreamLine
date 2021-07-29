package net.plasmere.streamline.utils;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Party;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.objects.messaging.DiscordMessage;

import java.util.*;

public class PartyUtils {
    private static final List<Party> parties = new ArrayList<>();

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

    public static Party getParty(String uuid) {
        try {
            for (Party party : parties) {
                if (party.hasMember(PlayerUtils.getOrCreatePlayerStatByUUID(uuid)))
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

    public static void removeInvite(Party party, Player player) {
        invites.get(party).remove(player);
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

    public static void createParty(Player player) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(player.uuid);

        if (p == null) return;

        if (getParty(player) != null) {
            MessagingUtils.sendBUserMessage(p, already);
            return;
        }

        try {
            Party party = new Party(player);

            addParty(party);

            MessagingUtils.sendBPUserMessage(party, p, p, create);

            // if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("CREATE : totalMembers --> "  + party.totalMembers.size());

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleCreates) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, createTitle,
                        createConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(player))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(player))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createPartySized(Player player, int size) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(player.uuid);

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

            Party party = new Party(player, size);

            parties.add(party);

            MessagingUtils.sendBPUserMessage(party, p, p, create);

            // if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("OPEN : totalMembers --> "  + party.totalMembers.size());

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleCreates) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, createTitle,
                        createConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(player))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(player))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleOpens) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, openTitle,
                        openConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(player))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(player))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }
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
        ProxiedPlayer player = PlayerUtils.getPPlayerByUUID(from.uuid);

        if (player == null) return;

        try {
            Party party = getParty(from);

            if (party != null) {
                if (party.totalMembers.size() <= 0) {
                    if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("#1 NO PARTY MEMBERS!");
                }
            }

            if (! checkPlayer(party, to, from)) return;

            if (party != null) {
                if (party.totalMembers.size() <= 0) {
                    if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("#2 NO PARTY MEMBERS!");
                }
            }

            if (to.equals(from)) {
                MessagingUtils.sendBUserMessage(player, inviteNonSelf);
                return;
            }

            if (! party.hasModPerms(from.uuid)) {
                MessagingUtils.sendBUserMessage(player, noPermission);
                return;
            }

            if (party.invites.contains(to)) {
                MessagingUtils.sendBUserMessage(player, inviteFailure);
                return;
            }

            if (party.totalMembers.size() <= 0) {
                if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("#3 NO PARTY MEMBERS!");
            }

            if (to.online) {
                MessagingUtils.sendBPUserMessage(party, player, to.player, inviteUser
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                        .replace("%leader_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                );
            }

            if (party.totalMembers.size() <= 0) {
                if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("#4 NO PARTY MEMBERS!");
            }

            for (Player pl : party.totalMembers) {
                if (! pl.online) continue;

                ProxiedPlayer member = PlayerUtils.getPPlayerByUUID(pl.uuid);

                if (member == null) {
                    if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("member == null");
                    continue;
                }

                if (pl.equals(from)) {
                    MessagingUtils.sendBPUserMessage(party, player, member, inviteLeader
                            .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                            .replace("%leader_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, player, member, inviteMembers
                            .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                            .replace("%leader_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                    );
                }
            }

            party.addInvite(to);
            invites.remove(party);
            invites.put(party, party.invites);

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleInvites) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(player, inviteTitle,
                        inviteConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(from))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(from))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%user%", PlayerUtils.getOffOnDisplayDiscord(to))
                                .replace("%user_normal%", PlayerUtils.getOffOnRegDiscord(to))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void acceptInvite(Player accepter, Player from) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(accepter.uuid);

        if (p == null) return;

        try {
            Party party = getParty(from);

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

                    ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                    if (m == null) continue;

                    if (m.equals(party.leader.player)){
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

                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleJoins) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, joinsTitle,
                            joinsConsole
                                    .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(accepter))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                    .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(accepter))
                                    .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                    .replace("%size%", String.valueOf(party.maxSize))
                            , ConfigUtils.textChannelParties));
                }

                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleAccepts) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, acceptTitle,
                            acceptConsole
                                    .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(accepter))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                    .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(accepter))
                                    .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                    .replace("%size%", String.valueOf(party.maxSize))
                            , ConfigUtils.textChannelParties));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void denyInvite(Player denier, Player from) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(denier.uuid);

        if (p == null) return;

        try {
            Party party = getParty(from);

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

                ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                if (m == null) continue;

                if (m.equals(party.leader.player)){
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

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleDenies) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, denyTitle,
                        denyConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(denier))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(denier))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void warpParty(Player sender) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;

        Party party = getParty(sender);

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

            ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(player.uuid);

            if (m == null) continue;

            if (player.equals(sender)) {
                MessagingUtils.sendBPUserMessage(party, p, m, warpSender);
            } else {
                MessagingUtils.sendBPUserMessage(party, p, m, warpMembers);
            }

            m.connect(sender.getServer().getInfo());
        }

        if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleWarps) {
            MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, warpTitle,
                    warpConsole
                            .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                            .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                            .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                            .replace("%size%", String.valueOf(party.maxSize))
                    , ConfigUtils.textChannelParties));
        }
    }

    public static void muteParty(Player sender) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;

        Party party = getParty(sender);

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

                ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(player.uuid);

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

                ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(player.uuid);

                if (m == null) continue;

                if (player.equals(sender)){
                    MessagingUtils.sendBPUserMessage(party, p, m, muteUser);
                } else {
                    MessagingUtils.sendBPUserMessage(party, p, m, muteMembers);
                }
            }

        }
        party.toggleMute();

        if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleMutes) {
            MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, muteTitle,
                    muteConsole
                            .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                            .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                            .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                            .replace("%size%", String.valueOf(party.maxSize))
                            .replace("%toggle%", party.isMuted ? muteToggleMuted : muteToggleUnMuted)
                    , ConfigUtils.textChannelParties));
        }
    }

    public static void kickMember(Player sender, Player player) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;

        Party party = getParty(sender);

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
                if (! pl.online) continue;

                ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

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


        if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleKicks) {
            MessagingUtils.sendDiscordEBMessage(new DiscordMessage(player.player, kickTitle,
                    kickConsole
                            .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                            .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                            .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                            .replace("%user%", PlayerUtils.getOffOnDisplayDiscord(player))
                            .replace("%user_normal%", PlayerUtils.getOffOnRegDiscord(player))
                            .replace("%size%", String.valueOf(party.maxSize))
                    , ConfigUtils.textChannelParties));
        }
    }

    public static void disband(Player sender) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;

        try {
            Party party = getParty(sender);

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

                ProxiedPlayer member = PlayerUtils.getPPlayerByUUID(pl.uuid);

                if (member == null) continue;

                if (!member.equals(party.leader.player)) {
                    MessagingUtils.sendBPUserMessage(party, p, member, disbandMembers
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, p, member, disbandLeader
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                    );
                }

            }

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleDisbands) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, disbandTitle,
                        disbandConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }

            removeParty(party);

            party.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openParty(Player sender) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;

        try {
            Party party = getParty(sender);

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
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                        .replace("%size%", Integer.toString(party.getSize()))
                );
            } else {
                party.setPublic(true);

                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer member = PlayerUtils.getPPlayerByUUID(pl.uuid);

                    if (member == null) continue;

                    if (member.equals(party.leader.player)) {
                        MessagingUtils.sendBPUserMessage(party, p, member, openLeader
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, member, openMembers
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    }
                }
            }

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleOpens) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, openTitle,
                        openConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openPartySized(Player sender, int size) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;

        try {
            Party party = getParty(sender);

            if (! isParty(party) || party == null) {
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
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                        .replace("%max%", Integer.toString(party.getMaxSize()))
                );
            } else {
                party.setPublic(true);
                party.setMaxSize(size);

                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer member = PlayerUtils.getPPlayerByUUID(pl.uuid);

                    if (member == null) continue;

                    if (member.equals(party.leader.player)) {
                        MessagingUtils.sendBPUserMessage(party, p, member, openLeader
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                .replace("%max%", Integer.toString(party.getMaxSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, member, openMembers
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                .replace("%max%", Integer.toString(party.getMaxSize()))
                        );
                    }
                }
            }

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleOpens) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, openTitle,
                        openConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeParty(Player sender) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(sender);

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
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                        .replace("%size%", Integer.toString(party.getSize()))
                );
            } else {
                party.setPublic(false);

                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer member = PlayerUtils.getPPlayerByUUID(pl.uuid);

                    if (member == null) continue;

                    if (member.equals(pl)) {
                        MessagingUtils.sendBPUserMessage(party, p, member, closeSender
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, member, closeMembers
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                .replace("%size%", Integer.toString(party.getSize()))
                        );
                    }
                }
            }

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleCloses) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, closeTitle,
                        closeConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listParty(Player sender) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInParty);
                return;
            }

            String leaderBulk = listLeaderBulk
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%size%", Integer.toString(party.getSize()));
            String moderatorBulk = listModBulkMain
                    .replace("%moderators%", moderators(party))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                    .replace("%size%", Integer.toString(party.getSize()));
            String memberBulk = listMemberBulkMain
                    .replace("%members%", members(party))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                    .replace("%size%", Integer.toString(party.getSize()));

            MessagingUtils.sendBPUserMessage(party, p, p, listMain
                    .replace("%leaderbulk%", leaderBulk)
                    .replace("%moderatorbulk%", moderatorBulk)
                    .replace("%memberbulk%", memberBulk)
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                    .replace("%size%", Integer.toString(party.getSize()))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String moderators(Party party) {
        try {
            if (party.moderators.size() <= 0) {
                return listModBulkNone;
            }

            StringBuilder mods = new StringBuilder();

            int i = 1;

            for (Player m : party.moderators) {
                if (i < party.moderators.size()) {
                    mods.append(listModBulkNotLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                            .replace("%size%", Integer.toString(party.getSize()))
                    );
                } else {
                    mods.append(listModBulkLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
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
            if (party.members.size() <= 0) {
                return listMemberBulkNone;
            }

            StringBuilder mems = new StringBuilder();

            int i = 1;

            for (Player m : party.members) {
                if (i < party.moderators.size()) {
                    mems.append(listMemberBulkNotLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                            .replace("%size%", Integer.toString(party.getSize()))
                    );
                } else {
                    mems.append(listMemberBulkLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
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
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(sender);

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

            if (! party.isLeader(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            switch (party.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBPUserMessage(party, p, p, promoteFailure
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                            .replace("%level%", textLeader
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                    .replace("%size%", Integer.toString(party.getSize()))
                            )
                    );
                    return;
                case MODERATOR:
                    party.replaceLeader(member);

                    for (Player pl : party.totalMembers) {
                        if (! pl.online) continue;

                        ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                        if (m == null) continue;

                        if (m.equals(party.leader.player)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
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

                        ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                        if (m == null) continue;

                        if (m.equals(party.leader.player)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                            .replace("%size%", Integer.toString(party.getSize()))
                                    )
                            );
                        }
                    }
                    break;
            }

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsolePromotes) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, promoteTitle,
                        promoteConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%user%", PlayerUtils.getOffOnDisplayDiscord(member))
                                .replace("%user_normal%", PlayerUtils.getOffOnRegDiscord(member))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void demotePlayer(Player sender, Player member) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(sender);

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

            if (! party.isLeader(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            switch (party.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBPUserMessage(party, p, p, demoteIsLeader
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                            .replace("%level%", textLeader)
                    );
                    return;
                case MODERATOR:
                    party.setMember(member);

                    for (Player pl : party.totalMembers) {
                        if (! pl.online) continue;

                        ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                        if (m == null) continue;

                        if (m.equals(party.leader.player)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, demoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                    .replace("%level%", textMember)
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, demoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                    .replace("%level%", textMember)
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, p, m, demoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                                    .replace("%level%", textMember)
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    MessagingUtils.sendBPUserMessage(party, p, p, demoteFailure
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                            .replace("%level%", textMember)
                    );
                    break;
            }

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleDemotes) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, demoteTitle,
                        demoteConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%user%", PlayerUtils.getOffOnDisplayDiscord(member))
                                .replace("%user_normal%", PlayerUtils.getOffOnRegDiscord(member))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void joinParty(Player sender, Player from) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(from);

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

                    ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBPUserMessage(party, p, m, joinUser
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, m, joinMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                        );
                    }
                }

                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleJoins) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, joinsTitle,
                            joinsConsole
                                    .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                    .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                    .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                    .replace("%size%", String.valueOf(party.maxSize))
                            , ConfigUtils.textChannelParties));
                }
            } else {
                MessagingUtils.sendBPUserMessage(party, p, p, joinFailure);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getMaxSize(Player leader){
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

    public static void leaveParty(Player sender) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(sender);

            if (!isParty(party) || party == null) {
                MessagingUtils.sendBUserMessage(p, noPartyFound);
                return;
            }

            if (! party.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInParty);
                return;
            }

            if (party.leader.player.equals(sender)) {
                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

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

                    ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBPUserMessage(party, p, m, leaveUser
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, m, leaveMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getPlayerStat(party.leader.player))))
                        );
                    }
                }

                party.removeMemberFromParty(sender);

                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleLeaves) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, leaveTitle,
                            leaveConsole
                                    .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                    .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                    .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                    .replace("%size%", String.valueOf(party.maxSize))
                            , ConfigUtils.textChannelParties));
                }
            } else {
                MessagingUtils.sendBPUserMessage(party, p, p, leaveFailure);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void sendChat(Player sender, String msg) {
        ProxiedPlayer p = PlayerUtils.getPPlayerByUUID(sender.uuid);

        if (p == null) return;
        try {
            Party party = getParty(sender);

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

//            if (ConfigUtils.partyConsoleChats) {
//                MessagingUtils.sendBPUserMessage(party, p, StreamLine.getInstance().getProxy().getConsole(), chatConsole
//                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(sender))
//                        .replace("%message%", msg)
//                );
//            }

            for (Player pl : party.totalMembers) {
                if (! pl.online) continue;

                ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                if (m == null) continue;

                MessagingUtils.sendBPUserMessage(party, p, m, chat
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(sender))
                        .replace("%message%", msg)
                );
            }

            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleChats) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, chatTitle,
                        chatConsole
                                .replace("%message%", msg)
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreatePlayerStatByUUID(party.leaderUUID)))
                                .replace("%size%", String.valueOf(party.maxSize))
                        , ConfigUtils.textChannelParties));
            }

            for (ProxiedPlayer pp : StreamLine.getInstance().getProxy().getPlayers()){
                if (! pp.hasPermission(ConfigUtils.partyView)) continue;

                Player them = PlayerUtils.getPlayerStat(pp);

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
    public static final String textLeader = StreamLine.getConfig().getMessString("party.text.leader");
    public static final String textModerator = StreamLine.getConfig().getMessString("party.text.moderator");
    public static final String textMember = StreamLine.getConfig().getMessString("party.text.member");
    // Spy.
    public static final String spy = StreamLine.getConfig().getMessString("party.spy");
    // No party.
    public static final String noPartyFound = StreamLine.getConfig().getMessString("party.no-party");
    // Already made.
    public static final String already = StreamLine.getConfig().getMessString("party.alreadyMade-made");
    // Already in one.
    public static final String alreadyHasOne = StreamLine.getConfig().getMessString("party.alreadyMade-has");
    // Too big.
    public static final String tooBig = StreamLine.getConfig().getMessString("party.too-big");
    // Not high enough permissions.
    public static final String noPermission = StreamLine.getConfig().getMessString("party.no-permission");
    // Not in a party.
    public static final String notInParty = StreamLine.getConfig().getMessString("party.not-in-a-party");
    public static final String otherNotInParty = StreamLine.getConfig().getMessString("party.other-not-in-party");
    // Not enough space in party.
    public static final String notEnoughSpace = StreamLine.getConfig().getMessString("party.not-enough-space");
    // Chat.
    public static final String chat = StreamLine.getConfig().getMessString("party.chat.message");
    public static final String chatMuted = StreamLine.getConfig().getMessString("party.chat.muted");
    public static final String chatConsole = StreamLine.getConfig().getMessString("party.chat.console");;
    public static final String chatTitle = StreamLine.getConfig().getMessString("party.chat.title");
    // Create.
    public static final String create = StreamLine.getConfig().getMessString("party.create.sender");
    public static final String createConsole = StreamLine.getConfig().getMessString("party.create.console");
    public static final String createTitle = StreamLine.getConfig().getMessString("party.create.title");
    // Join.
    public static final String joinMembers = StreamLine.getConfig().getMessString("party.join.members");
    public static final String joinUser = StreamLine.getConfig().getMessString("party.join.user");
    public static final String joinFailure = StreamLine.getConfig().getMessString("party.join.failure");
    public static final String joinsConsole = StreamLine.getConfig().getMessString("party.join.console");
    public static final String joinsTitle = StreamLine.getConfig().getMessString("party.join.title");
    // Leave.
    public static final String leaveMembers = StreamLine.getConfig().getMessString("party.leave.members");
    public static final String leaveUser = StreamLine.getConfig().getMessString("party.leave.user");
    public static final String leaveFailure = StreamLine.getConfig().getMessString("party.leave.failure");
    public static final String leaveConsole = StreamLine.getConfig().getMessString("party.leave.console");
    public static final String leaveTitle = StreamLine.getConfig().getMessString("party.leave.title");
    // Promote.
    public static final String promoteMembers = StreamLine.getConfig().getMessString("party.promote.members");
    public static final String promoteUser = StreamLine.getConfig().getMessString("party.promote.user");
    public static final String promoteLeader = StreamLine.getConfig().getMessString("party.promote.leader");
    public static final String promoteFailure = StreamLine.getConfig().getMessString("party.promote.failure");
    public static final String promoteConsole = StreamLine.getConfig().getMessString("party.promote.console");
    public static final String promoteTitle = StreamLine.getConfig().getMessString("party.promote.title");
    // Demote.
    public static final String demoteMembers = StreamLine.getConfig().getMessString("party.demote.members");
    public static final String demoteUser = StreamLine.getConfig().getMessString("party.demote.user");
    public static final String demoteLeader = StreamLine.getConfig().getMessString("party.demote.leader");
    public static final String demoteFailure = StreamLine.getConfig().getMessString("party.demote.failure");
    public static final String demoteIsLeader = StreamLine.getConfig().getMessString("party.demote.is-leader");
    public static final String demoteConsole = StreamLine.getConfig().getMessString("party.demote.console");
    public static final String demoteTitle = StreamLine.getConfig().getMessString("party.demote.title");
    // List.
    public static final String listMain = StreamLine.getConfig().getMessString("party.list.main");
    public static final String listLeaderBulk = StreamLine.getConfig().getMessString("party.list.leaderbulk");
    public static final String listModBulkMain = StreamLine.getConfig().getMessString("party.list.moderatorbulk.main");
    public static final String listModBulkNotLast = StreamLine.getConfig().getMessString("party.list.moderatorbulk.moderators.not-last");
    public static final String listModBulkLast = StreamLine.getConfig().getMessString("party.list.moderatorbulk.moderators.last");
    public static final String listModBulkNone = StreamLine.getConfig().getMessString("party.list.moderatorbulk.moderators.if-none");
    public static final String listMemberBulkMain = StreamLine.getConfig().getMessString("party.list.memberbulk.main");
    public static final String listMemberBulkNotLast = StreamLine.getConfig().getMessString("party.list.memberbulk.members.not-last");
    public static final String listMemberBulkLast = StreamLine.getConfig().getMessString("party.list.memberbulk.members.last");
    public static final String listMemberBulkNone = StreamLine.getConfig().getMessString("party.list.memberbulk.members.if-none");
    // Open.
    public static final String openMembers = StreamLine.getConfig().getMessString("party.open.members");
    public static final String openLeader = StreamLine.getConfig().getMessString("party.open.leader");
    public static final String openFailure = StreamLine.getConfig().getMessString("party.open.failure");
    public static final String openConsole = StreamLine.getConfig().getMessString("party.open.console");
    public static final String openTitle = StreamLine.getConfig().getMessString("party.open.title");
    // Close.
    public static final String closeMembers = StreamLine.getConfig().getMessString("party.close.members");
    public static final String closeSender = StreamLine.getConfig().getMessString("party.close.sender");
    public static final String closeFailure = StreamLine.getConfig().getMessString("party.close.failure");
    public static final String closeConsole = StreamLine.getConfig().getMessString("party.close.console");
    public static final String closeTitle = StreamLine.getConfig().getMessString("party.close.title");
    // Disband.
    public static final String disbandMembers = StreamLine.getConfig().getMessString("party.disband.members");
    public static final String disbandLeader = StreamLine.getConfig().getMessString("party.disband.leader");
    public static final String disbandConsole = StreamLine.getConfig().getMessString("party.disband.console");
    public static final String disbandTitle = StreamLine.getConfig().getMessString("party.disband.title");
    // Accept.
    public static final String acceptUser = StreamLine.getConfig().getMessString("party.accept.user");
    public static final String acceptLeader = StreamLine.getConfig().getMessString("party.accept.leader");
    public static final String acceptMembers = StreamLine.getConfig().getMessString("party.accept.members");
    public static final String acceptFailure = StreamLine.getConfig().getMessString("party.accept.failure");
    public static final String acceptConsole = StreamLine.getConfig().getMessString("party.accept.console");
    public static final String acceptTitle = StreamLine.getConfig().getMessString("party.accept.title");
    // Deny.
    public static final String denyUser = StreamLine.getConfig().getMessString("party.deny.user");
    public static final String denyLeader = StreamLine.getConfig().getMessString("party.deny.leader");
    public static final String denyMembers = StreamLine.getConfig().getMessString("party.deny.members");
    public static final String denyFailure = StreamLine.getConfig().getMessString("party.deny.failure");
    public static final String denyConsole = StreamLine.getConfig().getMessString("party.deny.console");
    public static final String denyTitle = StreamLine.getConfig().getMessString("party.deny.title");
    // Invite.
    public static final String inviteUser = StreamLine.getConfig().getMessString("party.invite.user");
    public static final String inviteLeader = StreamLine.getConfig().getMessString("party.invite.leader");
    public static final String inviteMembers = StreamLine.getConfig().getMessString("party.invite.members");
    public static final String inviteFailure = StreamLine.getConfig().getMessString("party.invite.failure");
    public static final String inviteNonSelf = StreamLine.getConfig().getMessString("party.invite.non-self");
    public static final String inviteConsole = StreamLine.getConfig().getMessString("party.invite.console");
    public static final String inviteTitle = StreamLine.getConfig().getMessString("party.invite.title");
    // Kick.
    public static final String kickUser = StreamLine.getConfig().getMessString("party.kick.user");
    public static final String kickSender = StreamLine.getConfig().getMessString("party.kick.sender");
    public static final String kickMembers = StreamLine.getConfig().getMessString("party.kick.members");
    public static final String kickFailure = StreamLine.getConfig().getMessString("party.kick.failure");
    public static final String kickMod = StreamLine.getConfig().getMessString("party.kick.mod");
    public static final String kickSelf = StreamLine.getConfig().getMessString("party.kick.self");
    public static final String kickConsole = StreamLine.getConfig().getMessString("party.kick.console");
    public static final String kickTitle = StreamLine.getConfig().getMessString("party.kick.title");
    // Mute.
    public static final String muteUser = StreamLine.getConfig().getMessString("party.mute.mute.user");
    public static final String muteMembers = StreamLine.getConfig().getMessString("party.mute.mute.members");
    public static final String unmuteUser = StreamLine.getConfig().getMessString("party.mute.unmute.user");
    public static final String unmuteMembers = StreamLine.getConfig().getMessString("party.mute.unmute.members");
    public static final String muteConsole = StreamLine.getConfig().getMessString("party.mute.console");
    public static final String muteTitle = StreamLine.getConfig().getMessString("party.mute.title");
    public static final String muteToggleMuted = StreamLine.getConfig().getMessString("party.mute.toggle.muted");
    public static final String muteToggleUnMuted = StreamLine.getConfig().getMessString("party.mute.toggle.unmuted");
    // Warp.
    public static final String warpSender = StreamLine.getConfig().getMessString("party.warp.sender");
    public static final String warpMembers = StreamLine.getConfig().getMessString("party.warp.members");
    public static final String warpConsole = StreamLine.getConfig().getMessString("party.warp.console");
    public static final String warpTitle = StreamLine.getConfig().getMessString("party.warp.title");
}
