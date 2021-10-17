package net.plasmere.streamline.utils;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.DiscordBotConfUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.Party;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.objects.enums.ChatChannel;
import net.plasmere.streamline.objects.savable.users.Player;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.savable.users.SavableUser;

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

            // if (ConfigUtils.debug) MessagingUtils.logInfo("CREATE : totalMembers --> "  + party.totalMembers.size());

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleCreates) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, createTitle,
                            createConsole
                            , DiscordBotConfUtils.textChannelParties));
                }
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

            // if (ConfigUtils.debug) MessagingUtils.logInfo("OPEN : totalMembers --> "  + party.totalMembers.size());

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleCreates) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, createTitle,
                            createConsole
                            , DiscordBotConfUtils.textChannelParties));
                }
            }

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleOpens) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, openTitle,
                            openConsole
                            , DiscordBotConfUtils.textChannelParties));
                }
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
                    if (ConfigUtils.debug) MessagingUtils.logInfo("#1 NO PARTY MEMBERS!");
                }
            }

            if (! checkPlayer(party, to, from)) return;

            if (party != null) {
                if (party.totalMembers.size() <= 0) {
                    if (ConfigUtils.debug) MessagingUtils.logInfo("#2 NO PARTY MEMBERS!");
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
                if (ConfigUtils.debug) MessagingUtils.logInfo("#3 NO PARTY MEMBERS!");
            }

            if (to.online) {
                MessagingUtils.sendBPUserMessage(party, player, to.player, inviteUser
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(to))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(to))
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(to))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(to))
                );
            }

            if (party.totalMembers.size() <= 0) {
                if (ConfigUtils.debug) MessagingUtils.logInfo("#4 NO PARTY MEMBERS!");
            }

            for (Player pl : party.totalMembers) {
                if (! pl.online) continue;

                ProxiedPlayer member = PlayerUtils.getPPlayerByUUID(pl.uuid);

                if (member == null) {
                    if (ConfigUtils.debug) MessagingUtils.logInfo("member == null");
                    continue;
                }

                if (pl.equals(from)) {
                    MessagingUtils.sendBPUserMessage(party, player, member, inviteLeader
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(to))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(to))
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(to))
                            .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(to))
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, player, member, inviteMembers
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(to))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(to))
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(to))
                            .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(to))
                    );
                }
            }

            party.addInvite(to);
            invites.remove(party);
            invites.put(party, party.invites);

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleInvites) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(player, inviteTitle,
                            inviteConsole
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(to))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(to))
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(to))
                                    .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(to))
                            , DiscordBotConfUtils.textChannelParties));
                }
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
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(accepter))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(accepter))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(accepter))
                        .replace("%from_display%", PlayerUtils.getOffOnDisplayBungee(from))
                        .replace("%from_normal%", PlayerUtils.getOffOnRegBungee(from))
                        .replace("%from_absolute%", PlayerUtils.getAbsoluteBungee(from))
                );

                for (Player pl : party.totalMembers){
                    if (! pl.online) continue;

                    ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                    if (m == null) continue;

                    if (m.equals(party.leader.player)){
                        MessagingUtils.sendBPUserMessage(party, p, m, acceptLeader
                                .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(accepter))
                                .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(accepter))
                                .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(accepter))
                                .replace("%from_display%", PlayerUtils.getOffOnDisplayBungee(from))
                                .replace("%from_normal%", PlayerUtils.getOffOnRegBungee(from))
                                .replace("%from_absolute%", PlayerUtils.getAbsoluteBungee(from))
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, m, acceptMembers
                                .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(accepter))
                                .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(accepter))
                                .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(accepter))
                                .replace("%from_display%", PlayerUtils.getOffOnDisplayBungee(from))
                                .replace("%from_normal%", PlayerUtils.getOffOnRegBungee(from))
                                .replace("%from_absolute%", PlayerUtils.getAbsoluteBungee(from))
                        );
                    }
                }

                party.addMember(accepter);
                party.removeInvite(accepter);

                if (ConfigUtils.moduleDEnabled) {
                    if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleJoins) {
                        MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, joinsTitle,
                                joinsConsole
                                        .replace("%player_display%", PlayerUtils.getOffOnDisplayDiscord(accepter))
                                        .replace("%player_normal%", PlayerUtils.getOffOnRegDiscord(accepter))
                                        .replace("%player_absolute%", PlayerUtils.getAbsoluteDiscord(accepter))
                                        .replace("%from_display%", PlayerUtils.getOffOnDisplayDiscord(from))
                                        .replace("%from_normal%", PlayerUtils.getOffOnRegDiscord(from))
                                        .replace("%from_absolute%", PlayerUtils.getAbsoluteDiscord(from))
                                , DiscordBotConfUtils.textChannelParties));
                    }

                    if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleAccepts) {
                        MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, acceptTitle,
                                acceptConsole
                                        .replace("%player_display%", PlayerUtils.getOffOnDisplayDiscord(accepter))
                                        .replace("%player_normal%", PlayerUtils.getOffOnRegDiscord(accepter))
                                        .replace("%player_absolute%", PlayerUtils.getAbsoluteDiscord(accepter))
                                        .replace("%from_display%", PlayerUtils.getOffOnDisplayDiscord(from))
                                        .replace("%from_normal%", PlayerUtils.getOffOnRegDiscord(from))
                                        .replace("%from_absolute%", PlayerUtils.getAbsoluteDiscord(from))
                                , DiscordBotConfUtils.textChannelParties));
                    }
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
                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(denier))
                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(denier))
                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(denier))
                    .replace("%from_display%", PlayerUtils.getOffOnDisplayBungee(from))
                    .replace("%from_normal%", PlayerUtils.getOffOnRegBungee(from))
                    .replace("%from_absolute%", PlayerUtils.getAbsoluteBungee(from))
            );

            for (Player pl : party.totalMembers){
                if (! pl.online) continue;

                ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                if (m == null) continue;

                if (m.equals(party.leader.player)){
                    MessagingUtils.sendBPUserMessage(party, p, m, denyLeader
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(denier))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(denier))
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(denier))
                            .replace("%from_display%", PlayerUtils.getOffOnDisplayBungee(from))
                            .replace("%from_normal%", PlayerUtils.getOffOnRegBungee(from))
                            .replace("%from_absolute%", PlayerUtils.getAbsoluteBungee(from))
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, p, m, denyMembers
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(denier))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(denier))
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(denier))
                            .replace("%from_display%", PlayerUtils.getOffOnDisplayBungee(from))
                            .replace("%from_normal%", PlayerUtils.getOffOnRegBungee(from))
                            .replace("%from_absolute%", PlayerUtils.getAbsoluteBungee(from))
                    );
                }
            }

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleDenies) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, denyTitle,
                            denyConsole
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayDiscord(denier))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegDiscord(denier))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteDiscord(denier))
                                    .replace("%from_display%", PlayerUtils.getOffOnDisplayDiscord(from))
                                    .replace("%from_normal%", PlayerUtils.getOffOnRegDiscord(from))
                                    .replace("%from_absolute%", PlayerUtils.getAbsoluteDiscord(from))
                            , DiscordBotConfUtils.textChannelParties));
                }
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

        if (ConfigUtils.moduleDEnabled) {
            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleWarps) {
                MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, warpTitle,
                        warpConsole
                        , DiscordBotConfUtils.textChannelParties));
            }
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

        if (ConfigUtils.moduleDEnabled) {
            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleMutes) {
                MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, muteTitle,
                        muteConsole
                        , DiscordBotConfUtils.textChannelParties));
            }
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
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                    );
                } else if (pl.equals(player)) {
                    MessagingUtils.sendBPUserMessage(party, p, m, kickUser
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, p, m, kickMembers
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                    );
                }
            }

            party.removeMemberFromParty(player);
        }

        if (ConfigUtils.moduleDEnabled) {
            if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleKicks) {
                MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(player.player, kickTitle,
                        kickConsole
                                .replace("%player_absolute%", PlayerUtils.getAbsoluteDiscord(player))
                                .replace("%player_display%", PlayerUtils.getOffOnDisplayDiscord(player))
                                .replace("%player_normal%", PlayerUtils.getOffOnRegDiscord(player))
                        , DiscordBotConfUtils.textChannelParties));
            }
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
                    );
                } else {
                    MessagingUtils.sendBPUserMessage(party, p, member, disbandLeader
                    );
                }

            }

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleDisbands) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, disbandTitle,
                            disbandConsole
                            , DiscordBotConfUtils.textChannelParties));
                }
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
                );
            } else {
                party.setPublic(true);

                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer member = PlayerUtils.getPPlayerByUUID(pl.uuid);

                    if (member == null) continue;

                    if (member.equals(party.leader.player)) {
                        MessagingUtils.sendBPUserMessage(party, p, member, openLeader
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, member, openMembers
                        );
                    }
                }
            }

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleOpens) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, openTitle,
                            openConsole
                            , DiscordBotConfUtils.textChannelParties));
                }
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
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, member, openMembers
                        );
                    }
                }
            }

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleOpens) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, openTitle,
                            openConsole
                            , DiscordBotConfUtils.textChannelParties));
                }
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
                );
            } else {
                party.setPublic(false);

                for (Player pl : party.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer member = PlayerUtils.getPPlayerByUUID(pl.uuid);

                    if (member == null) continue;

                    if (member.equals(pl)) {
                        MessagingUtils.sendBPUserMessage(party, p, member, closeSender
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, member, closeMembers
                        );
                    }
                }
            }

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleCloses) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, closeTitle,
                            closeConsole
                            , DiscordBotConfUtils.textChannelParties));
                }
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

            String moderatorBulk = listModBulkMain
                    .replace("%moderators%", moderators(party))
                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(sender));
            String memberBulk = listMemberBulkMain
                    .replace("%members%", members(party));

            MessagingUtils.sendBPUserMessage(party, p, p, listMain
                    .replace("%leaderbulk%", listLeaderBulk)
                    .replace("%moderatorbulk%", moderatorBulk)
                    .replace("%memberbulk%", memberBulk)
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
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                            .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
                    );
                } else {
                    mods.append(listModBulkLast
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                            .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
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
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                            .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
                    );
                } else {
                    mems.append(listMemberBulkLast
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                            .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
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

    public static void promotePlayer(Player sender, Player player) {
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

            if (! party.hasMember(player)) {
                MessagingUtils.sendBUserMessage(p, otherNotInParty);
                return;
            }

            if (! party.isLeader(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            switch (party.getLevel(player)) {
                case LEADER:
                    MessagingUtils.sendBPUserMessage(party, p, p, promoteFailure
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                            .replace("%level%", textLeader
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                    );
                    return;
                case MODERATOR:
                    party.replaceLeader(player);

                    for (Player pl : party.totalMembers) {
                        if (! pl.online) continue;

                        ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                        if (m == null) continue;

                        if (m.equals(party.leader.player)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteLeader
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                                    .replace("%level%", textLeader
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                            );
                        } else if (m.equals(player)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteUser
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                                    .replace("%level%", textLeader
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteMembers
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                                    .replace("%level%", textLeader
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    party.setModerator(player);

                    for (Player pl : party.totalMembers) {
                        if (! pl.online) continue;

                        ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                        if (m == null) continue;

                        if (m.equals(party.leader.player)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteLeader
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                                    .replace("%level%", textModerator
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                            );
                        } else if (m.equals(player)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteUser
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                                    .replace("%level%", textModerator
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, p, m, promoteMembers
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                                    .replace("%level%", textModerator
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                            );
                        }
                    }
                    break;
            }

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsolePromotes) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, promoteTitle,
                            promoteConsole
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayDiscord(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegDiscord(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteDiscord(player))
                            , DiscordBotConfUtils.textChannelParties));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void demotePlayer(Player sender, Player player) {
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

            if (! party.hasMember(player)) {
                MessagingUtils.sendBUserMessage(p, otherNotInParty);
                return;
            }

            if (! party.isLeader(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            switch (party.getLevel(player)) {
                case LEADER:
                    MessagingUtils.sendBPUserMessage(party, p, p, demoteIsLeader
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                            .replace("%level%", textLeader
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                    );
                    return;
                case MODERATOR:
                    party.setMember(player);

                    for (Player pl : party.totalMembers) {
                        if (! pl.online) continue;

                        ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                        if (m == null) continue;

                        if (m.equals(party.leader.player)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, demoteLeader
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                                    .replace("%level%", textMember
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                            );
                        } else if (m.equals(player)) {
                            MessagingUtils.sendBPUserMessage(party, p, m, demoteUser
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                                    .replace("%level%", textMember
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                            );
                        } else {
                            MessagingUtils.sendBPUserMessage(party, p, m, demoteMembers
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                                    .replace("%level%", textMember
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    MessagingUtils.sendBPUserMessage(party, p, p, demoteFailure
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                            .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                            .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                            .replace("%level%", textMember
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player)))
                    );
                    break;
            }

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleDemotes) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, demoteTitle,
                            demoteConsole
                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayDiscord(player))
                                    .replace("%player_normal%", PlayerUtils.getOffOnRegDiscord(player))
                                    .replace("%player_absolute%", PlayerUtils.getAbsoluteDiscord(player))
                            , DiscordBotConfUtils.textChannelParties));
                }
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
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, m, joinMembers
                        );
                    }
                }

                if (ConfigUtils.moduleDEnabled) {
                    if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleJoins) {
                        MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, joinsTitle,
                                joinsConsole
                                , DiscordBotConfUtils.textChannelParties));
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
                        );
                    } else {
                        MessagingUtils.sendBPUserMessage(party, p, m, leaveMembers
                        );
                    }
                }

                party.removeMemberFromParty(sender);

                if (ConfigUtils.moduleDEnabled) {
                    if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleLeaves) {
                        MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, leaveTitle,
                                leaveConsole
                                , DiscordBotConfUtils.textChannelParties));
                    }
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
                        .replace("%message%", msg)
                );
                return;
            }

//            if (ConfigUtils.partyConsoleChats) {
//                MessagingUtils.sendBPUserMessage(party, p, StreamLine.getInstance().getProxy().getConsole(), chatConsole
//                        .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(sender))
//                        .replace("%message%", msg)
//                );
//            }

            for (Player pl : party.totalMembers) {
                if (! pl.online) continue;

                ProxiedPlayer m = PlayerUtils.getPPlayerByUUID(pl.uuid);

                if (m == null) continue;

                MessagingUtils.sendBPUserMessage(party, p, m, chat
                        .replace("%message%", msg)
                );
            }

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.partyToDiscord && ConfigUtils.partyConsoleChats) {
                    MessagingUtils.sendDiscordPEBMessage(party, new DiscordMessage(p, chatTitle,
                            chatConsole
                                    .replace("%message%", msg)
                            , DiscordBotConfUtils.textChannelParties));
                }
            }

            if (ConfigUtils.moduleDPC) {
                StreamLine.discordData.sendDiscordChannel(sender.findSender(), ChatChannel.PARTY, party.leaderUUID, msg);
            }

            for (ProxiedPlayer pp : StreamLine.getInstance().getProxy().getPlayers()){
                if (! pp.hasPermission(ConfigUtils.partyView)) continue;

                Player them = PlayerUtils.getPlayerStat(pp);

                if (them == null) continue;

                if (! them.pspy) continue;

                if (! them.pspyvs) if (them.uuid.equals(sender.uuid)) continue;

                MessagingUtils.sendBPUserMessage(party, p, pp, spy.replace("%message%", msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendChatFromDiscord(String nameUsed, Party party, String format, String msg) {
        try {
            for (SavableUser pl : party.totalMembers) {
                MessagingUtils.sendBPUserMessageFromDiscord(party, nameUsed, pl.findSender(), format
                        .replace("%message%", msg)
                );
            }

//            if (ConfigUtils.moduleDEnabled) {
//                if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleChats) {
//                    MessagingUtils.sendDiscordGEBMessage(guild, new DiscordMessage(sender.findSender(), chatTitle,
//                            chatConsole
//                                    .replace("%message%", msg)
//                            , DiscordBotConfUtils.textChannelGuilds));
//                }
//            }

            for (ProxiedPlayer pp : StreamLine.getInstance().getProxy().getPlayers()){
                if (! pp.hasPermission(ConfigUtils.guildView)) continue;

                Player them = PlayerUtils.getOrCreatePlayerStat(pp);

                if (! them.gspy) continue;

                MessagingUtils.sendBPUserMessageFromDiscord(party, nameUsed, them.findSender(), spy.replace("%message%", msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendChatFromDiscord(SavableUser user, Party party, String format, String msg) {
        try {
            for (SavableUser pl : party.totalMembers) {
                MessagingUtils.sendBPUserMessageFromDiscord(party, user, pl.findSender(), format
                        .replace("%message%", msg)
                );
            }

//            if (ConfigUtils.moduleDEnabled) {
//                if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleChats) {
//                    MessagingUtils.sendDiscordGEBMessage(guild, new DiscordMessage(sender.findSender(), chatTitle,
//                            chatConsole
//                                    .replace("%message%", msg)
//                            , DiscordBotConfUtils.textChannelGuilds));
//                }
//            }

            for (ProxiedPlayer pp : StreamLine.getInstance().getProxy().getPlayers()){
                if (! pp.hasPermission(ConfigUtils.guildView)) continue;

                Player them = PlayerUtils.getOrCreatePlayerStat(pp);

                if (! them.gspy) continue;

                MessagingUtils.sendBPUserMessageFromDiscord(party, user, them.findSender(), spy.replace("%message%", msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MESSAGES...
    // Text.
    public static final String textLeader = StreamLine.config.getMessString("party.text.leader");
    public static final String textModerator = StreamLine.config.getMessString("party.text.moderator");
    public static final String textMember = StreamLine.config.getMessString("party.text.member");
    // Spy.
    public static final String spy = StreamLine.config.getMessString("party.spy");
    // No party.
    public static final String noPartyFound = StreamLine.config.getMessString("party.no-party");
    // Already made.
    public static final String already = StreamLine.config.getMessString("party.already-made");
    // Already in one.
    public static final String alreadyHasOne = StreamLine.config.getMessString("party.already-has");
    // Too big.
    public static final String tooBig = StreamLine.config.getMessString("party.too-big");
    // Not high enough permissions.
    public static final String noPermission = StreamLine.config.getMessString("party.no-permission");
    // Not in a party.
    public static final String notInParty = StreamLine.config.getMessString("party.not-in-a-party");
    public static final String otherNotInParty = StreamLine.config.getMessString("party.other-not-in-party");
    // Not enough space in party.
    public static final String notEnoughSpace = StreamLine.config.getMessString("party.not-enough-space");
    // Chat.
    public static final String chat = StreamLine.config.getMessString("party.chat.message");
    public static final String chatMuted = StreamLine.config.getMessString("party.chat.muted");
    public static final String chatConsole = StreamLine.config.getMessString("party.chat.console");;
    public static final String chatTitle = StreamLine.config.getMessString("party.chat.title");
    // Create.
    public static final String create = StreamLine.config.getMessString("party.create.sender");
    public static final String createConsole = StreamLine.config.getMessString("party.create.console");
    public static final String createTitle = StreamLine.config.getMessString("party.create.title");
    // Join.
    public static final String joinMembers = StreamLine.config.getMessString("party.join.members");
    public static final String joinUser = StreamLine.config.getMessString("party.join.user");
    public static final String joinFailure = StreamLine.config.getMessString("party.join.failure");
    public static final String joinsConsole = StreamLine.config.getMessString("party.join.console");
    public static final String joinsTitle = StreamLine.config.getMessString("party.join.title");
    // Leave.
    public static final String leaveMembers = StreamLine.config.getMessString("party.leave.members");
    public static final String leaveUser = StreamLine.config.getMessString("party.leave.user");
    public static final String leaveFailure = StreamLine.config.getMessString("party.leave.failure");
    public static final String leaveConsole = StreamLine.config.getMessString("party.leave.console");
    public static final String leaveTitle = StreamLine.config.getMessString("party.leave.title");
    // Promote.
    public static final String promoteMembers = StreamLine.config.getMessString("party.promote.members");
    public static final String promoteUser = StreamLine.config.getMessString("party.promote.user");
    public static final String promoteLeader = StreamLine.config.getMessString("party.promote.leader");
    public static final String promoteFailure = StreamLine.config.getMessString("party.promote.failure");
    public static final String promoteConsole = StreamLine.config.getMessString("party.promote.console");
    public static final String promoteTitle = StreamLine.config.getMessString("party.promote.title");
    // Demote.
    public static final String demoteMembers = StreamLine.config.getMessString("party.demote.members");
    public static final String demoteUser = StreamLine.config.getMessString("party.demote.user");
    public static final String demoteLeader = StreamLine.config.getMessString("party.demote.leader");
    public static final String demoteFailure = StreamLine.config.getMessString("party.demote.failure");
    public static final String demoteIsLeader = StreamLine.config.getMessString("party.demote.is-leader");
    public static final String demoteConsole = StreamLine.config.getMessString("party.demote.console");
    public static final String demoteTitle = StreamLine.config.getMessString("party.demote.title");
    // List.
    public static final String listMain = StreamLine.config.getMessString("party.list.main");
    public static final String listLeaderBulk = StreamLine.config.getMessString("party.list.leaderbulk");
    public static final String listModBulkMain = StreamLine.config.getMessString("party.list.moderatorbulk.main");
    public static final String listModBulkNotLast = StreamLine.config.getMessString("party.list.moderatorbulk.moderators.not-last");
    public static final String listModBulkLast = StreamLine.config.getMessString("party.list.moderatorbulk.moderators.last");
    public static final String listModBulkNone = StreamLine.config.getMessString("party.list.moderatorbulk.moderators.if-none");
    public static final String listMemberBulkMain = StreamLine.config.getMessString("party.list.memberbulk.main");
    public static final String listMemberBulkNotLast = StreamLine.config.getMessString("party.list.memberbulk.members.not-last");
    public static final String listMemberBulkLast = StreamLine.config.getMessString("party.list.memberbulk.members.last");
    public static final String listMemberBulkNone = StreamLine.config.getMessString("party.list.memberbulk.members.if-none");
    // Open.
    public static final String openMembers = StreamLine.config.getMessString("party.open.members");
    public static final String openLeader = StreamLine.config.getMessString("party.open.leader");
    public static final String openFailure = StreamLine.config.getMessString("party.open.failure");
    public static final String openConsole = StreamLine.config.getMessString("party.open.console");
    public static final String openTitle = StreamLine.config.getMessString("party.open.title");
    // Close.
    public static final String closeMembers = StreamLine.config.getMessString("party.close.members");
    public static final String closeSender = StreamLine.config.getMessString("party.close.sender");
    public static final String closeFailure = StreamLine.config.getMessString("party.close.failure");
    public static final String closeConsole = StreamLine.config.getMessString("party.close.console");
    public static final String closeTitle = StreamLine.config.getMessString("party.close.title");
    // Disband.
    public static final String disbandMembers = StreamLine.config.getMessString("party.disband.members");
    public static final String disbandLeader = StreamLine.config.getMessString("party.disband.leader");
    public static final String disbandConsole = StreamLine.config.getMessString("party.disband.console");
    public static final String disbandTitle = StreamLine.config.getMessString("party.disband.title");
    // Accept.
    public static final String acceptUser = StreamLine.config.getMessString("party.accept.user");
    public static final String acceptLeader = StreamLine.config.getMessString("party.accept.leader");
    public static final String acceptMembers = StreamLine.config.getMessString("party.accept.members");
    public static final String acceptFailure = StreamLine.config.getMessString("party.accept.failure");
    public static final String acceptConsole = StreamLine.config.getMessString("party.accept.console");
    public static final String acceptTitle = StreamLine.config.getMessString("party.accept.title");
    // Deny.
    public static final String denyUser = StreamLine.config.getMessString("party.deny.user");
    public static final String denyLeader = StreamLine.config.getMessString("party.deny.leader");
    public static final String denyMembers = StreamLine.config.getMessString("party.deny.members");
    public static final String denyFailure = StreamLine.config.getMessString("party.deny.failure");
    public static final String denyConsole = StreamLine.config.getMessString("party.deny.console");
    public static final String denyTitle = StreamLine.config.getMessString("party.deny.title");
    // Invite.
    public static final String inviteUser = StreamLine.config.getMessString("party.invite.user");
    public static final String inviteLeader = StreamLine.config.getMessString("party.invite.leader");
    public static final String inviteMembers = StreamLine.config.getMessString("party.invite.members");
    public static final String inviteFailure = StreamLine.config.getMessString("party.invite.failure");
    public static final String inviteNonSelf = StreamLine.config.getMessString("party.invite.non-self");
    public static final String inviteConsole = StreamLine.config.getMessString("party.invite.console");
    public static final String inviteTitle = StreamLine.config.getMessString("party.invite.title");
    // Kick.
    public static final String kickUser = StreamLine.config.getMessString("party.kick.user");
    public static final String kickSender = StreamLine.config.getMessString("party.kick.sender");
    public static final String kickMembers = StreamLine.config.getMessString("party.kick.members");
    public static final String kickFailure = StreamLine.config.getMessString("party.kick.failure");
    public static final String kickMod = StreamLine.config.getMessString("party.kick.mod");
    public static final String kickSelf = StreamLine.config.getMessString("party.kick.self");
    public static final String kickConsole = StreamLine.config.getMessString("party.kick.console");
    public static final String kickTitle = StreamLine.config.getMessString("party.kick.title");
    // Mute.
    public static final String muteUser = StreamLine.config.getMessString("party.mute.mute.user");
    public static final String muteMembers = StreamLine.config.getMessString("party.mute.mute.members");
    public static final String unmuteUser = StreamLine.config.getMessString("party.mute.unmute.user");
    public static final String unmuteMembers = StreamLine.config.getMessString("party.mute.unmute.members");
    public static final String muteConsole = StreamLine.config.getMessString("party.mute.console");
    public static final String muteTitle = StreamLine.config.getMessString("party.mute.title");
    public static final String muteToggleMuted = StreamLine.config.getMessString("party.mute.toggle.muted");
    public static final String muteToggleUnMuted = StreamLine.config.getMessString("party.mute.toggle.unmuted");
    // Warp.
    public static final String warpSender = StreamLine.config.getMessString("party.warp.sender");
    public static final String warpMembers = StreamLine.config.getMessString("party.warp.members");
    public static final String warpConsole = StreamLine.config.getMessString("party.warp.console");
    public static final String warpTitle = StreamLine.config.getMessString("party.warp.title");
}
