package net.plasmere.streamline.utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.users.ConsolePlayer;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.users.SavableUser;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GuildUtils {
    private static final List<Guild> guilds = new ArrayList<>();

    public static List<Guild> getGuilds() {
        List<Guild> rem = new ArrayList<>();

        for (Guild g : guilds) {
            if (g.leaderUUID == null) rem.add(g);
        }

        for (Guild g : rem) {
            guilds.remove(g);
        }

        return guilds;
    }
    // Guild , Invites
    public static Map<Guild, List<SavableUser>> invites = new HashMap<>();

    public static void removeInvite(Guild guild, SavableUser player) {
        invites.get(guild).remove(player);
    }

    public static Guild getGuild(SavableUser stat) {
        try {
            for (Guild guild : guilds) {
                if (guild.hasMember(stat)) {
                    return guild;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Guild getGuild(String uuid) {
        try {
            for (Guild guild : guilds) {
                if (guild.hasMember(PlayerUtils.getOrCreateSUByUUID(uuid))) {
                    return guild;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasGuild(SavableUser player) {
        for (Guild guild : guilds) {
            if (guild.hasMember(player)) return true;
        }
        return false;
    }

    public static boolean existsByUUID(String uuid){
        File file = new File(StreamLine.getInstance().getGDir(), uuid + ".properties");

        return file.exists();
    }

    public static boolean exists(String username){
        File file = new File(StreamLine.getInstance().getGDir(), Objects.requireNonNull(PlayerUtils.getOrCreatePlayerStat(username)).guild + ".properties");

        return file.exists();
    }

    public static boolean isGuild(Guild guild){
        return guilds.contains(guild);
    }

    public static boolean pHasGuild(SavableUser player){
        try {
            if (! existsByUUID(player.uuid)) return false;

            Guild guild = new Guild(player.guild, false);

            if (guild.leaderUUID == null) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkPlayer(Guild guild, SavableUser player, SavableUser sender){
        if (! isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
            return false;
        }

        if (! guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
            return false;
        }

        if (hasGuild(player)) {
            MessagingUtils.sendBUserMessage(sender.sender, alreadyHasOneOthers);
            return false;
        }

        return true;
    }

    public static void createGuild(SavableUser sender, String name) {
        Guild g = getGuild(sender);

        if (g != null) {
            MessagingUtils.sendBUserMessage(sender.sender, alreadyMade);
            return;
        }

        if (name.equals("")) {
            MessagingUtils.sendBUserMessage(sender.sender, createNonEmpty);
            return;
        }

        if (ConfigUtils.guildIncludeColors) {
            if (name.length() > ConfigUtils.guildMaxLength) {
                MessagingUtils.sendBUserMessage(sender.sender, nameTooLong
                        .replace("%length%", String.valueOf(name.length()))
                        .replace("%max_length%", String.valueOf(ConfigUtils.guildMaxLength))
                        .replace("%codes%", withCodes)
                );
                return;
            }
        } else {
            if (TextUtils.stripColor(name).length() > ConfigUtils.guildMaxLength) {
                MessagingUtils.sendBUserMessage(sender.sender, nameTooLong
                        .replace("%length%", String.valueOf(TextUtils.stripColor(name).length()))
                        .replace("%max_length%", String.valueOf(ConfigUtils.guildMaxLength))
                        .replace("%codes%", withoutCodes)
                );
                return;
            }
        }

        try {
            //MessagingUtils.logInfo("createGuild Player.uuid > " + sender.uuid);
            Guild guild = new Guild(sender.uuid, name);

            addGuild(guild);

            MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  create);

            if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleCreates) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, createTitle,
                        createConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%size%", String.valueOf(guild.maxSize))
                                .replace("%name%", name)
                        , ConfigUtils.textChannelGuilds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addGuild(Guild guild){
        Guild g;

        try {
            g = getGuild(guild.leaderUUID);
        } catch (Exception e) {
            return;
            // Do nothing.
        }

        if (g != null) return;

        try {
            if (guilds.size() > 0) {
                List<Guild> rem = new ArrayList<>();

                for (Guild gu : guilds) {
                    String s = gu.leaderUUID;

                    if (s == null) {
                        rem.add(gu);
                        continue;
                    }

                    if (s.equals(guild.leaderUUID)) {
                        rem.add(gu);
                    }
                }

                for (Guild gd : rem) {
                    guilds.remove(gd);
                }
            }

            guilds.add(guild);
        } catch (Exception e){
            MessagingUtils.logInfo("Error adding guild...");
            e.printStackTrace();
        }
    }

    public static boolean hasLeader(String leader){
        List<Guild> toRem = new ArrayList<>();

        boolean hasLeader = false;

        for (Guild guild : guilds){
            if (guild.leaderUUID == null) {
                toRem.add(guild);
                continue;
            }
            if (guild.leaderUUID.equals(leader)) hasLeader = true;
        }

        for (Guild guild : toRem) {
            try {
                guild.dispose();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            removeGuild(guild);
        }

        return hasLeader;
    }

    public static void removeGuild(Guild guild){
        try {
            guild.saveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        guilds.remove(guild);
    }

    public static void sendInvite(SavableUser to, SavableUser from) {
        try {
            Guild guild = getGuild(from);

            if (! checkPlayer(guild, to, from)) return;

            if (to.equals(from)) {
                MessagingUtils.sendBUserMessage(from.sender, inviteNonSelf);
                return;
            }

            if (! guild.hasModPerms(from.uuid)) {
                MessagingUtils.sendBUserMessage(from.sender, noPermission);
                return;
            }

            if (isGuild(getGuild(to))) {
                MessagingUtils.sendBUserMessage(from.sender, alreadyHasOneOthers);
                return;
            }

            if (guild.invites.contains(to)) {
                MessagingUtils.sendBUserMessage(from.sender, inviteFailure);
                return;
            }

            if (to instanceof Player && ((Player) to).online) {
                MessagingUtils.sendBGUserMessage(guild, from.sender, to.sender, inviteUser
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                        .replace("%leader_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                );
            }

            for (SavableUser pl : guild.totalMembers) {
                CommandSender member = pl.sender;

                if (member == null) continue;

                if (pl.equals(from)) {
                    MessagingUtils.sendBGUserMessage(guild, from.sender, member, inviteLeader
                            .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%leader_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                    );
                } else {
                    MessagingUtils.sendBGUserMessage(guild, from.sender, member, inviteMembers
                            .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%leader_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                    );
                }
            }

            guild.addInvite(to);
            invites.remove(guild);
            invites.put(guild, guild.invites);

            if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleInvites) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(from.sender, inviteTitle,
                        inviteConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(from))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(from))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%user%", PlayerUtils.getOffOnDisplayDiscord(to))
                                .replace("%user_normal%", PlayerUtils.getOffOnRegDiscord(to))
                                .replace("%size%", String.valueOf(guild.maxSize))
                                .replace("%name%", guild.name)
                        , ConfigUtils.textChannelGuilds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void acceptInvite(SavableUser accepter, SavableUser from) {
        try {
            Guild guild = getGuild(from);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(accepter.sender, acceptFailure);
                return;
            }

            if (! guild.hasMember(from)) {
                MessagingUtils.sendBUserMessage(accepter.sender, otherNotInGuild);
                return;
            }

            if (isGuild(getGuild(accepter))) {
                MessagingUtils.sendBUserMessage(accepter.sender, alreadyHasOneSelf);
                return;
            }

            if (! invites.get(guild).contains(accepter)) {
                MessagingUtils.sendBUserMessage(accepter.sender, acceptFailure);
                return;
            }

            if (guild.invites.contains(accepter)) {
                if (guild.getSize() >= guild.maxSize) {
                    MessagingUtils.sendBGUserMessage(guild, accepter.sender, accepter.sender, notEnoughSpace);
                    return;
                }

                MessagingUtils.sendBGUserMessage(guild, accepter.sender, accepter.sender, acceptUser
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(accepter))
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                );

                for (SavableUser pl : guild.totalMembers){
                    CommandSender m = pl.sender;

                    if (m == null) continue;

                    if (pl.equals(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID))){
                        MessagingUtils.sendBGUserMessage(guild, accepter.sender, m, acceptLeader
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(accepter))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, accepter.sender, m, acceptMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(accepter))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                        );
                    }
                }

                guild.addMember(accepter);
                guild.remFromInvites(from, accepter);

                if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleJoins) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(accepter.sender, joinsTitle,
                            joinsConsole
                                    .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(accepter))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(accepter))
                                    .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%size%", String.valueOf(guild.maxSize))
                                    .replace("%name%", guild.name)
                            , ConfigUtils.textChannelGuilds));
                }

                if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleAccepts) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(accepter.sender, acceptTitle,
                            acceptConsole
                                    .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(accepter))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(accepter))
                                    .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%size%", String.valueOf(guild.maxSize))
                                    .replace("%name%", guild.name)
                            , ConfigUtils.textChannelGuilds));
                }
            } else {
                MessagingUtils.sendBUserMessage(accepter.sender, acceptFailure);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void denyInvite(SavableUser denier, SavableUser from) {
        try {
            Guild guild = getGuild(from);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(denier.sender, denyFailure);
                return;
            }

            if (! guild.hasMember(from)) {
                MessagingUtils.sendBUserMessage(denier.sender, otherNotInGuild);
                return;
            }

            if (! invites.get(guild).contains(denier)) {
                MessagingUtils.sendBUserMessage(denier.sender, denyFailure);
                return;
            }

            if (guild.invites.contains(denier)) {
                MessagingUtils.sendBGUserMessage(guild, denier.sender, denier.sender, denyUser
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(denier))
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                );

                for (SavableUser pl : guild.totalMembers) {
                    CommandSender m = pl.sender;

                    if (m == null) continue;

                    if (pl.equals(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID))) {
                        MessagingUtils.sendBGUserMessage(guild, denier.sender, m, denyLeader
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(denier))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, denier.sender, m, denyMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(denier))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                        );
                    }
                }

                guild.remFromInvites(from, denier);

                if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleDenies) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(denier.sender, denyTitle,
                            denyConsole
                                    .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(denier))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(denier))
                                    .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%size%", String.valueOf(guild.maxSize))
                                    .replace("%name%", guild.name)
                            , ConfigUtils.textChannelGuilds));
                }
            } else {
                MessagingUtils.sendBUserMessage(denier.sender, denyFailure);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void warpGuild(SavableUser sender){
        Guild guild = getGuild(sender);

        if (!isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
            return;
        }

        if (sender instanceof ConsolePlayer) {
            MessagingUtils.sendBUserMessage(sender.sender, MessageConfUtils.onlyPlayers);
            return;
        }

        if (! guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
            return;
        }

        if (! guild.hasModPerms(sender)) {
            MessagingUtils.sendBUserMessage(sender.sender, noPermission);
            return;
        }

        if (sender instanceof Player && ((Player) sender).online) {
            for (SavableUser player : guild.totalMembers) {                
                if (! (player instanceof Player && ((Player) player).online)) continue;

                ProxiedPlayer m = ((Player) player).player;

                if (m == null) continue;

                if (player.equals(sender)) {
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, m, warpSender);
                } else {
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, m, warpMembers);
                }

                m.connect(((Player) sender).player.getServer().getInfo());
            }
        }

        if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleWarps) {
            MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, warpTitle,
                    warpConsole
                            .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                            .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%size%", String.valueOf(guild.maxSize))
                            .replace("%name%", guild.name)
                    , ConfigUtils.textChannelGuilds));
        }
    }

    public static void muteGuild(SavableUser sender){
        Guild guild = getGuild(sender);

        if (!isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
            return;
        }

        if (! guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
            return;
        }

        if (! guild.hasModPerms(sender)) {
            MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender, noPermission);
            return;
        }

        if (guild.isMuted) {
            for (SavableUser player : guild.totalMembers) {
                CommandSender m = player.sender;

                if (m == null) continue;

                if (player.equals(sender)){
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, m, unmuteUser);
                } else {
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, m, unmuteMembers);
                }
            }

        } else {
            for (SavableUser player : guild.totalMembers) {
                CommandSender m = player.sender;

                if (m == null) continue;

                if (player.equals(sender)){
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, m, muteUser);
                } else {
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, m, muteMembers);
                }
            }

        }
        guild.toggleMute();

        if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleMutes) {
            MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, muteTitle,
                    muteConsole
                            .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                            .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%size%", String.valueOf(guild.maxSize))
                            .replace("%name%", guild.name)
                            .replace("%toggle%", guild.isMuted ? muteToggleMuted : muteToggleUnMuted)
                    , ConfigUtils.textChannelGuilds));
        }
    }

    public static void kickMember(SavableUser sender, SavableUser player) {
        Guild guild = getGuild(sender);

        if (!isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(sender.sender, kickFailure);
            return;
        }

        if (!guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
            return;
        }

        if (!guild.hasMember(player)) {
            MessagingUtils.sendBUserMessage(sender.sender, otherNotInGuild);
            return;
        }

        if (!guild.hasModPerms(sender)) {
            MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  noPermission);
            return;
        }

        if (guild.hasModPerms(player)) {
            MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  kickMod);
            return;
        }

        try {
            if (sender.equals(player)) {
                MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  kickSelf);
            } else if (player.equals(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID))) {
                MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  noPermission);
            } else {
                for (SavableUser pl : guild.totalMembers) {
                    

                    CommandSender m = pl.sender;

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, m, kickSender
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(player))
                        );
                    } else if (pl.equals(player)) {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, m, kickUser
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(player))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, m, kickMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(player))
                        );
                    }
                }

                guild.removeMemberFromGuild(player);
            }

            if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleKicks) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, kickTitle,
                        kickConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%user%", PlayerUtils.getOffOnDisplayDiscord(player))
                                .replace("%user_normal%", PlayerUtils.getOffOnRegDiscord(player))
                                .replace("%size%", String.valueOf(guild.maxSize))
                                .replace("%name%", guild.name)
                        , ConfigUtils.textChannelGuilds));
            }
        } catch (Exception e) {
            MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender, MessageConfUtils.bungeeCommandErrorUnd);
            e.printStackTrace();
        }
    }

    public static void info(SavableUser sender){
        Guild guild = getGuild(sender);

        if (!isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
            return;
        }

        if (! guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
            return;
        }

        MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  info);
    }

    public static void disband(SavableUser sender) {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
                return;
            }

            if (! guild.leaderUUID.equals(sender.uuid)) {
                MessagingUtils.sendBUserMessage(sender.sender, noPermission);
                return;
            }

            for (SavableUser pl : guild.totalMembers) {
                

                CommandSender member = pl.sender;

                if (member == null) continue;

                if (! member.equals(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID).sender)) {
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, member, disbandMembers
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                    );
                } else {
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, member, disbandLeader
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                    );
                }

            }

            if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleDisbands) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, disbandTitle,
                        disbandConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%size%", String.valueOf(guild.maxSize))
                                .replace("%name%", guild.name)
                        , ConfigUtils.textChannelGuilds));
            }

            guilds.remove(guild);
            guild.disband();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openGuild(SavableUser sender) {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
                return;
            }

            if (!guild.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, noPermission);
                return;
            }

            if (guild.isPublic) {
                MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  openFailure
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                        .replace("%size%", Integer.toString(guild.getSize()))
                );
            } else {
                guild.setPublic(true);

                for (SavableUser pl : guild.totalMembers) {
                    CommandSender member = pl.sender;

                    if (member == null) continue;

                    if (member.equals(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID).sender)) {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, member, openLeader
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, member, openMembers
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    }
                }

                if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleOpens) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, openTitle,
                            openConsole
                                    .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                    .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%size%", String.valueOf(guild.maxSize))
                                    .replace("%name%", guild.name)
                            , ConfigUtils.textChannelGuilds));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeGuild(SavableUser sender) {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
                return;
            }

            if (!guild.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, noPermission);
                return;
            }

            if (!guild.isPublic) {
                MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  closeFailure
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                        .replace("%size%", Integer.toString(guild.getSize()))
                );
            } else {
                guild.setPublic(false);

                for (SavableUser pl : guild.totalMembers) {
                    

                    CommandSender member = pl.sender;

                    if (member == null) continue;

                    if (member.equals(pl)) {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, member, closeSender
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, member, closeMembers
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    }
                }

                if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleCloses) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, closeTitle,
                            closeConsole
                                    .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                    .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%size%", String.valueOf(guild.maxSize))
                                    .replace("%name%", guild.name)
                            , ConfigUtils.textChannelGuilds));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listGuild(SavableUser sender) {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
                return;
            }

            String leaderBulk = listLeaderBulk
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%size%", Integer.toString(guild.getSize()));
            String moderatorBulk = listModBulkMain
                    .replace("%moderators%", Objects.requireNonNull(moderators(guild)))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                    .replace("%size%", Integer.toString(guild.getSize()));
            String memberBulk = listMemberBulkMain
                    .replace("%members%", Objects.requireNonNull(members(guild)))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                    .replace("%size%", Integer.toString(guild.getSize()));

            MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  listMain
                    .replace("%leaderbulk%", leaderBulk)
                    .replace("%moderatorbulk%", moderatorBulk)
                    .replace("%memberbulk%", memberBulk)
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                    .replace("%size%", Integer.toString(guild.getSize()))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String moderators(Guild guild) {
        try {
            if (! (guild.moderators.size() > 0)) {
                return listModBulkNone;
            }

            StringBuilder mods = new StringBuilder();

            int i = 1;

            for (SavableUser m : guild.moderators) {
                if (i <= guild.moderators.size()) {
                    mods.append(listModBulkNotLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%size%", Integer.toString(guild.getSize()))
                    );
                } else {
                    mods.append(listModBulkLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%size%", Integer.toString(guild.getSize()))
                    );
                }
                i++;
            }

            return mods.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String members(Guild guild) {
        try {
            if (! (guild.members.size() > 0)) {
                return listMemberBulkNone;
            }

            StringBuilder mems = new StringBuilder();

            int i = 1;

            for (SavableUser m : guild.members) {
                if (i <= guild.moderators.size()) {
                    mems.append(listMemberBulkNotLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%size%", Integer.toString(guild.getSize()))
                    );
                } else {
                    mems.append(listMemberBulkLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%size%", Integer.toString(guild.getSize()))
                    );
                }
                i++;
            }

            return mems.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void promotePlayer(SavableUser sender, SavableUser member) {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
                return;
            }

            if (! guild.hasMember(member)) {
                MessagingUtils.sendBUserMessage(sender.sender, otherNotInGuild);
                return;
            }

            if (!guild.leaderUUID.equals(sender.uuid)) {
                MessagingUtils.sendBUserMessage(sender.sender, noPermission);
                return;
            }

            switch (guild.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  promoteFailure
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%level%", textLeader
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%size%", Integer.toString(guild.getSize()))
                            )
                    );
                    return;
                case MODERATOR:
                    guild.replaceLeader(member);

                    for (SavableUser pl : guild.totalMembers) {
                        CommandSender m = pl.sender;

                        if (m == null) continue;

                        if (pl.equals(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID))) {
                            MessagingUtils.sendBGUserMessage(guild, sender.sender, m, promoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else if (pl.equals(member)) {
                            MessagingUtils.sendBGUserMessage(guild, sender.sender, m, promoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBGUserMessage(guild, sender.sender, m, promoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    guild.setModerator(member);

                    for (SavableUser pl : guild.totalMembers) {
                        CommandSender m = pl.sender;

                        if (m == null) continue;

                        if (pl.equals(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID))) {
                            MessagingUtils.sendBGUserMessage(guild, sender.sender, m, promoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else if (pl.equals(member)) {
                            MessagingUtils.sendBGUserMessage(guild, sender.sender, m, promoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBGUserMessage(guild, sender.sender, m, promoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        }
                    }
                    break;
            }

            if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsolePromotes) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, promoteTitle,
                        promoteConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%user%", PlayerUtils.getOffOnDisplayDiscord(member))
                                .replace("%user_normal%", PlayerUtils.getOffOnRegDiscord(member))
                                .replace("%size%", String.valueOf(guild.maxSize))
                                .replace("%name%", guild.name)
                        , ConfigUtils.textChannelGuilds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void demotePlayer(SavableUser sender, SavableUser member) {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
                return;
            }

            if (! guild.hasMember(member)) {
                MessagingUtils.sendBUserMessage(sender.sender, otherNotInGuild);
                return;
            }

            if (!guild.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, noPermission);
                return;
            }

            switch (guild.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  demoteIsLeader
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%level%", textLeader
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%size%", Integer.toString(guild.getSize()))
                            )
                    );
                    return;
                case MODERATOR:
                    guild.setMember(member);

                    for (SavableUser pl : guild.totalMembers) {
                        CommandSender m = pl.sender;

                        if (m == null) continue;

                        if (pl.equals(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID))) {
                            MessagingUtils.sendBGUserMessage(guild, sender.sender, m, demoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%level%", textMember
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else if (pl.equals(member)) {
                            MessagingUtils.sendBGUserMessage(guild, sender.sender, m, demoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%level%", textMember
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBGUserMessage(guild, sender.sender, m, demoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%level%", textMember
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  demoteFailure
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                            .replace("%level%", textMember
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%size%", Integer.toString(guild.getSize()))
                            )
                    );
                    break;
            }

            if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleDemotes) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, demoteTitle,
                        demoteConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%user%", PlayerUtils.getOffOnDisplayDiscord(member))
                                .replace("%user_normal%", PlayerUtils.getOffOnRegDiscord(member))
                                .replace("%size%", String.valueOf(guild.maxSize))
                                .replace("%name%", guild.name)
                        , ConfigUtils.textChannelGuilds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void joinGuild(SavableUser sender, SavableUser from) {
        try {
            Guild guild = getGuild(from);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(from)) {
                MessagingUtils.sendBUserMessage(sender.sender, otherNotInGuild);
                return;
            }

            if (isGuild(getGuild(sender))) {
                MessagingUtils.sendBUserMessage(sender.sender, alreadyHasOneSelf);
                return;
            }

            if (guild.getSize() + 1 > guild.maxSize) {
                MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  notEnoughSpace);
                return;
            }

            if (guild.isPublic) {
                guild.addMember(sender);

                for (SavableUser pl : guild.totalMembers) {
                    CommandSender m = pl.sender;

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, m, joinUser
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, m, joinMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                        );
                    }
                }

                if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleJoins) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, joinsTitle,
                            joinsConsole
                                    .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                    .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%size%", String.valueOf(guild.maxSize))
                                    .replace("%name%", guild.name)
                            , ConfigUtils.textChannelGuilds));
                }
            } else {
                MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  joinFailure);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void leaveGuild(SavableUser sender) {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
                return;
            }

            if (PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID).equals(sender)) {
                for (SavableUser pl : guild.totalMembers) {
                    

                    CommandSender m = pl.sender;

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, m, leaveUser);
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, m, disbandLeader);
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, m, leaveMembers);
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, m, disbandMembers);
                    }
                }

                guilds.remove(guild);
                guild.dispose();
                return;
            }

            if (guild.hasMember(sender)) {
                for (SavableUser pl : guild.totalMembers) {
                    CommandSender m = pl.sender;

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, m, leaveUser
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender.sender, m, leaveMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                        );
                    }
                }

                guild.removeMemberFromGuild(sender);

                if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleLeaves) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, leaveTitle,
                            leaveConsole
                                    .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                    .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                    .replace("%size%", String.valueOf(guild.maxSize))
                                    .replace("%name%", guild.name)
                            , ConfigUtils.textChannelGuilds));
                }
            } else {
                MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  leaveFailure);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void sendChat(SavableUser sender, String msg) {
        try {
            Guild guild = getGuild(sender);

            if (! isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
                return;
            }

            if (guild.isMuted && ! guild.hasModPerms(sender)) {
                MessagingUtils.sendBGUserMessage(guild, sender.sender, sender.sender,  chatMuted
                        .replace("%sender%", sender.displayName)
                        .replace("%message%", msg)
                );
                return;
            }

//            if (ConfigUtils.guildConsoleChats) {
//                MessagingUtils.sendBGUserMessage(guild, sender.sender, StreamLine.getInstance().getProxy().getConsole(), chatConsole
//                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(sender))
//                        .replace("%message%", msg)
//                );
//            }

            for (SavableUser pl : guild.totalMembers) {
                CommandSender m = pl.sender;

                if (m == null) continue;

                MessagingUtils.sendBGUserMessage(guild, sender.sender, m, chat
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(sender))
                        .replace("%message%", msg)
                );
            }

            if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleChats) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, chatTitle,
                        chatConsole
                                .replace("%message%", msg)
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%size%", String.valueOf(guild.maxSize))
                                .replace("%name%", guild.name)
                        , ConfigUtils.textChannelGuilds));
            }

            for (ProxiedPlayer pp : StreamLine.getInstance().getProxy().getPlayers()){
                if (! pp.hasPermission(ConfigUtils.guildView)) continue;

                Player them = PlayerUtils.getOrCreatePlayerStat(pp);

                if (! them.gspy) continue;

                MessagingUtils.sendBGUserMessage(guild, sender.sender, them.sender, spy.replace("%message%", msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rename(SavableUser sender, String newName){
        try {
            Guild guild = getGuild(sender);

            if (! isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender.sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender.sender, notInGuild);
                return;
            }

            if (newName.equals("")) {
                MessagingUtils.sendBUserMessage(sender.sender, renameNonEmpty);
                return;
            }

            String oldName = guild.name;

            if (ConfigUtils.guildIncludeColors) {
                if (newName.length() > ConfigUtils.guildMaxLength) {
                    MessagingUtils.sendBUserMessage(sender.sender, nameTooLong
                            .replace("%length%", String.valueOf(newName.length()))
                            .replace("%max_length%", String.valueOf(ConfigUtils.guildMaxLength))
                            .replace("%codes%", withCodes)
                    );
                    return;
                }
            } else {
                if (TextUtils.stripColor(newName).length() > ConfigUtils.guildMaxLength) {
                    MessagingUtils.sendBUserMessage(sender.sender, nameTooLong
                            .replace("%length%", String.valueOf(TextUtils.stripColor(newName).length()))
                            .replace("%max_length%", String.valueOf(ConfigUtils.guildMaxLength))
                            .replace("%codes%", withoutCodes)
                    );
                    return;
                }
            }

            guild.updateKey("name", newName);

            for (SavableUser player : guild.totalMembers) {
                CommandSender m = player.sender;

                if (m == null) continue;

                if (player.equals(sender)) {
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, m, renameSender
                            .replace("%old%", oldName)
                            .replace("%new%", newName)
                    );
                } else {
                    MessagingUtils.sendBGUserMessage(guild, sender.sender, m, renameMembers
                            .replace("%old%", oldName)
                            .replace("%new%", newName)
                    );
                }
            }

            if (ConfigUtils.guildToDiscord && ConfigUtils.guildConsoleRenames) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender.sender, renameTitle,
                        renameConsole
                                .replace("%sender%", PlayerUtils.getOffOnDisplayDiscord(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(sender))
                                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                                .replace("%size%", String.valueOf(guild.maxSize))
                                .replace("%name%", guild.name)
                                .replace("%old_name%", oldName)
                        , ConfigUtils.textChannelGuilds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAll(){
        List<Guild> gs = new ArrayList<>(getGuilds());

        for (Guild guild : gs) {
            try {
                guild.saveInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // MESSAGES...
    // Text.
    public static final String textLeader = StreamLine.config.getMessString("guild.text.leader");
    public static final String textModerator = StreamLine.config.getMessString("guild.text.moderator");
    public static final String textMember = StreamLine.config.getMessString("guild.text.member");
    // Discord.
    public static final String discordTitle = StreamLine.config.getMessString("guild.discord.title");
    // Spy.
    public static final String spy = StreamLine.config.getMessString("guild.spy");
    // No guild.
    public static final String noGuildFound = StreamLine.config.getMessString("guild.no-guild");
    // Name too long.
    public static final String nameTooLong = StreamLine.config.getMessString("guild.name-too-long");
    public static final String withCodes = StreamLine.config.getMessString("guild.codes.if-true");
    public static final String withoutCodes = StreamLine.config.getMessString("guild.codes.if-false");
    // Already made.
    public static final String alreadyMade = StreamLine.config.getMessString("guild.already-made");
    // Already in one, others.
    public static final String alreadyHasOneOthers = StreamLine.config.getMessString("guild.already-has-others");
    // Already in one self.
    public static final String alreadyHasOneSelf = StreamLine.config.getMessString("guild.already-has-self");
    // Not high enough permissions.
    public static final String noPermission = StreamLine.config.getMessString("guild.no-permission");
    // Not in a guild.
    public static final String notInGuild = StreamLine.config.getMessString("guild.not-in-a-guild");
    public static final String otherNotInGuild = StreamLine.config.getMessString("guild.other-not-in-guild");
    // Not enough space in guild.
    public static final String notEnoughSpace = StreamLine.config.getMessString("guild.not-enough-space");
    // Chat.
    public static final String chat = StreamLine.config.getMessString("guild.chat.message");
    public static final String chatMuted = StreamLine.config.getMessString("guild.chat.muted");
    public static final String chatConsole = StreamLine.config.getMessString("guild.chat.console");;
    public static final String chatTitle = StreamLine.config.getMessString("guild.chat.title");
    // Create.
    public static final String create = StreamLine.config.getMessString("guild.create.sender");
    public static final String createNonEmpty = StreamLine.config.getMessString("guild.create.non-empty");
    public static final String createConsole = StreamLine.config.getMessString("guild.create.console");
    public static final String createTitle = StreamLine.config.getMessString("guild.create.title");
    // Join.
    public static final String joinMembers = StreamLine.config.getMessString("guild.join.members");
    public static final String joinUser = StreamLine.config.getMessString("guild.join.user");
    public static final String joinFailure = StreamLine.config.getMessString("guild.join.failure");
    public static final String joinsConsole = StreamLine.config.getMessString("guild.join.console");
    public static final String joinsTitle = StreamLine.config.getMessString("guild.join.title");
    // Leave.
    public static final String leaveMembers = StreamLine.config.getMessString("guild.leave.members");
    public static final String leaveUser = StreamLine.config.getMessString("guild.leave.user");
    public static final String leaveFailure = StreamLine.config.getMessString("guild.leave.failure");
    public static final String leaveConsole = StreamLine.config.getMessString("guild.leave.console");
    public static final String leaveTitle = StreamLine.config.getMessString("guild.leave.title");
    // Promote.
    public static final String promoteMembers = StreamLine.config.getMessString("guild.promote.members");
    public static final String promoteUser = StreamLine.config.getMessString("guild.promote.user");
    public static final String promoteLeader = StreamLine.config.getMessString("guild.promote.leader");
    public static final String promoteFailure = StreamLine.config.getMessString("guild.promote.failure");
    public static final String promoteConsole = StreamLine.config.getMessString("guild.promote.console");
    public static final String promoteTitle = StreamLine.config.getMessString("guild.promote.title");
    // Demote.
    public static final String demoteMembers = StreamLine.config.getMessString("guild.demote.members");
    public static final String demoteUser = StreamLine.config.getMessString("guild.demote.user");
    public static final String demoteLeader = StreamLine.config.getMessString("guild.demote.leader");
    public static final String demoteFailure = StreamLine.config.getMessString("guild.demote.failure");
    public static final String demoteIsLeader = StreamLine.config.getMessString("guild.demote.is-leader");
    public static final String demoteConsole = StreamLine.config.getMessString("guild.demote.console");
    public static final String demoteTitle = StreamLine.config.getMessString("guild.demote.title");
    // List.
    public static final String listMain = StreamLine.config.getMessString("guild.list.main");
    public static final String listLeaderBulk = StreamLine.config.getMessString("guild.list.leaderbulk");
    public static final String listModBulkMain = StreamLine.config.getMessString("guild.list.moderatorbulk.main");
    public static final String listModBulkNotLast = StreamLine.config.getMessString("guild.list.moderatorbulk.moderators.not-last");
    public static final String listModBulkLast = StreamLine.config.getMessString("guild.list.moderatorbulk.moderators.last");
    public static final String listModBulkNone = StreamLine.config.getMessString("guild.list.moderatorbulk.moderators.if-none");
    public static final String listMemberBulkMain = StreamLine.config.getMessString("guild.list.memberbulk.main");
    public static final String listMemberBulkNotLast = StreamLine.config.getMessString("guild.list.memberbulk.members.not-last");
    public static final String listMemberBulkLast = StreamLine.config.getMessString("guild.list.memberbulk.members.last");
    public static final String listMemberBulkNone = StreamLine.config.getMessString("guild.list.memberbulk.members.if-none");
    // Open.
    public static final String openMembers = StreamLine.config.getMessString("guild.open.members");
    public static final String openLeader = StreamLine.config.getMessString("guild.open.sender");
    public static final String openFailure = StreamLine.config.getMessString("guild.open.failure");
    public static final String openConsole = StreamLine.config.getMessString("guild.open.console");
    public static final String openTitle = StreamLine.config.getMessString("guild.open.title");
    // Close.
    public static final String closeMembers = StreamLine.config.getMessString("guild.close.members");
    public static final String closeSender = StreamLine.config.getMessString("guild.close.sender");
    public static final String closeFailure = StreamLine.config.getMessString("guild.close.failure");
    public static final String closeConsole = StreamLine.config.getMessString("guild.close.console");
    public static final String closeTitle = StreamLine.config.getMessString("guild.close.title");
    // Disband.
    public static final String disbandMembers = StreamLine.config.getMessString("guild.disband.members");
    public static final String disbandLeader = StreamLine.config.getMessString("guild.disband.leader");
    public static final String disbandConsole = StreamLine.config.getMessString("guild.disband.console");
    public static final String disbandTitle = StreamLine.config.getMessString("guild.disband.title");
    // Accept.
    public static final String acceptUser = StreamLine.config.getMessString("guild.accept.user");
    public static final String acceptLeader = StreamLine.config.getMessString("guild.accept.leader");
    public static final String acceptMembers = StreamLine.config.getMessString("guild.accept.members");
    public static final String acceptFailure = StreamLine.config.getMessString("guild.accept.failure");
    public static final String acceptConsole = StreamLine.config.getMessString("guild.accept.console");
    public static final String acceptTitle = StreamLine.config.getMessString("guild.accept.title");
    // Deny.
    public static final String denyUser = StreamLine.config.getMessString("guild.deny.user");
    public static final String denyLeader = StreamLine.config.getMessString("guild.deny.leader");
    public static final String denyMembers = StreamLine.config.getMessString("guild.deny.members");
    public static final String denyFailure = StreamLine.config.getMessString("guild.deny.failure");
    public static final String denyConsole = StreamLine.config.getMessString("guild.deny.console");
    public static final String denyTitle = StreamLine.config.getMessString("guild.deny.title");
    // Invite.
    public static final String inviteUser = StreamLine.config.getMessString("guild.invite.user");
    public static final String inviteLeader = StreamLine.config.getMessString("guild.invite.leader");
    public static final String inviteMembers = StreamLine.config.getMessString("guild.invite.members");
    public static final String inviteFailure = StreamLine.config.getMessString("guild.invite.failure");
    public static final String inviteNonSelf = StreamLine.config.getMessString("guild.invite.non-self");
    public static final String inviteConsole = StreamLine.config.getMessString("guild.invite.console");
    public static final String inviteTitle = StreamLine.config.getMessString("guild.invite.title");
    // Kick.
    public static final String kickUser = StreamLine.config.getMessString("guild.kick.user");
    public static final String kickSender = StreamLine.config.getMessString("guild.kick.sender");
    public static final String kickMembers = StreamLine.config.getMessString("guild.kick.members");
    public static final String kickFailure = StreamLine.config.getMessString("guild.kick.failure");
    public static final String kickMod = StreamLine.config.getMessString("guild.kick.mod");
    public static final String kickSelf = StreamLine.config.getMessString("guild.kick.self");
    public static final String kickConsole = StreamLine.config.getMessString("guild.kick.console");
    public static final String kickTitle = StreamLine.config.getMessString("guild.kick.title");
    // Mute.
    public static final String muteUser = StreamLine.config.getMessString("guild.mute.mute.user");
    public static final String muteMembers = StreamLine.config.getMessString("guild.mute.mute.members");
    public static final String unmuteUser = StreamLine.config.getMessString("guild.mute.unmute.user");
    public static final String unmuteMembers = StreamLine.config.getMessString("guild.mute.unmute.members");
    public static final String muteConsole = StreamLine.config.getMessString("guild.mute.console");
    public static final String muteTitle = StreamLine.config.getMessString("guild.mute.title");
    public static final String muteToggleMuted = StreamLine.config.getMessString("guild.mute.toggle.muted");
    public static final String muteToggleUnMuted = StreamLine.config.getMessString("guild.mute.toggle.unmuted");
    // Warp.
    public static final String warpSender = StreamLine.config.getMessString("guild.warp.sender");
    public static final String warpMembers = StreamLine.config.getMessString("guild.warp.members");
    public static final String warpConsole = StreamLine.config.getMessString("guild.warp.console");
    public static final String warpTitle = StreamLine.config.getMessString("guild.warp.title");
    // Info.
    public static final String info = StreamLine.config.getMessString("guild.info");
    // Rename.
    public static final String renameSender = StreamLine.config.getMessString("guild.rename.sender");
    public static final String renameMembers = StreamLine.config.getMessString("guild.rename.members");
    public static final String renameNonEmpty = StreamLine.config.getMessString("guild.rename.non-empty");
    public static final String renameConsole = StreamLine.config.getMessString("guild.rename.console");
    public static final String renameTitle = StreamLine.config.getMessString("guild.rename.title");
}
