package net.plasmere.streamline.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.objects.messaging.DiscordMessage;

import java.io.IOException;
import java.util.*;

public class GuildUtils {
    private static final List<Guild> guilds = new ArrayList<>();
    private static final Configuration message = Config.getMess();

    private static final LuckPerms api = LuckPermsProvider.get();

    public static List<Guild> getGuilds() {
        return guilds;
    }
    // Guild , Invites
    public static Map<Guild, List<Player>> invites = new HashMap<>();

    public static Guild getGuild(Player player) {
        try {
            for (Guild guild : guilds) {
                if (guild.hasMember(player)) {
                    return guild;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Guild getGuild(UUID uuid) {
        try {
            for (Guild guild : guilds) {
                if (guild.hasMember(UUIDFetcher.getPlayer(uuid))) {
                    return guild;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasGuild(Player player) {
        for (Guild guild : guilds) {
            if (guild.hasMember(player)) return true;
        }
        return false;
    }

    public static boolean isGuild(Guild guild){
        return guilds.contains(guild);
    }

    public static boolean pHasGuild(Player player){
        Guild guild = new Guild(player.uuid, false);

        if (guild.leaderUUID == null) {
            return false;
        }

        return true;
    }

    public static boolean checkPlayer(Guild guild, Player player, Player sender){
        if (! isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(sender, noGuildFound);
            return false;
        }

        if (! guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(sender, notInGuild);
            return false;
        }

        if (hasGuild(player)) {
            MessagingUtils.sendBUserMessage(sender, alreadyHasOne);
            return false;
        }

        return true;
    }

    public static void createGuild(Player player, String name) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(player.uuid);

        if (p == null) return;

        Guild g = getGuild(player);

        if (g != null) {
            MessagingUtils.sendBUserMessage(p, already);
            return;
        }

        try {
            Guild guild = new Guild(player.uuid, name);

            addGuild(guild);

            MessagingUtils.sendBGUserMessage(guild, p, p, create);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addGuild(Guild guild){
        try {
            Guild g = getGuild(guild.leaderUUID);

            if (g != null) return;
        } catch (Exception e) {
            // Do nothing.
        }

        try {
            guilds.add(guild);
        } catch (Exception e){
            StreamLine.getInstance().getLogger().info("Error adding guild...");
            e.printStackTrace();
        }
    }

    public static boolean hasLeader(UUID leader){
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

    public static void sendInvite(Player to, Player from) {
        ProxiedPlayer player = UUIDFetcher.getPPlayer(from.uuid);

        if (player == null) return;

        try {
            Guild guild = getGuild(from);

            if (!checkPlayer(guild, to, from)) return;

            if (to.equals(from)) {
                MessagingUtils.sendBUserMessage(player, inviteNonSelf);
                return;
            }

            if (!guild.hasModPerms(from.getUniqueId())) {
                MessagingUtils.sendBUserMessage(player, noPermission);
                return;
            }

            if (guild.invites.contains(to)) {
                MessagingUtils.sendBUserMessage(player, inviteFailure);
                return;
            }

            if (to.online) {
                MessagingUtils.sendBGUserMessage(guild, player, to, inviteUser
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                        .replace("%leaderdefault%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                );
            }

            for (Player pl : guild.totalMembers) {
                if (! pl.online) continue;

                ProxiedPlayer member = UUIDFetcher.getPPlayer(pl.uuid);

                if (member == null) continue;

                if (pl.equals(from)) {
                    MessagingUtils.sendBGUserMessage(guild, player, member, inviteLeader
                            .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                            .replace("%leaderdefault%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                    );
                } else {
                    MessagingUtils.sendBGUserMessage(guild, player, member, inviteMembers
                            .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(from))
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(to))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                            .replace("%leaderdefault%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                    );
                }
            }

            guild.addInvite(to);
            invites.remove(guild);
            invites.put(guild, guild.invites);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void acceptInvite(Player accepter, Player from) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(accepter.uuid);

        if (p == null) return;

        try {
            Guild guild = getGuild(from);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(p, acceptFailure);
                return;
            }

            if (! guild.hasMember(from)) {
                MessagingUtils.sendBUserMessage(p, otherNotInGuild);
                return;
            }

            if (guild.invites.contains(accepter)) {
                if (guild.getSize() >= guild.maxSize) {
                    MessagingUtils.sendBGUserMessage(guild, p, p, notEnoughSpace);
                    return;
                }

                MessagingUtils.sendBGUserMessage(guild, p, p, acceptUser
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(accepter))
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                );

                for (Player pl : guild.totalMembers){
                    if (! pl.online) continue;

                    ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                    if (m == null) continue;

                    if (pl.equals(UUIDFetcher.getPlayer(guild.leaderUUID))){
                        MessagingUtils.sendBGUserMessage(guild, p, m, acceptLeader
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(accepter))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, p, m, acceptMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(accepter))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                        );
                    }
                }

                guild.addMember(accepter);
                guild.remFromInvites(accepter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void denyInvite(Player denier, Player from) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(denier.uuid);

        if (p == null) return;

        try {
            Guild guild = getGuild(from);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(p, denyFailure);
                return;
            }

            if (! guild.hasMember(from)) {
                MessagingUtils.sendBUserMessage(p, otherNotInGuild);
                return;
            }

            guild.remFromInvites(denier);

            MessagingUtils.sendBGUserMessage(guild, p, p, denyUser
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(denier))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
            );

            for (Player pl : guild.totalMembers){
                if (! pl.online) continue;

                ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                if (m == null) continue;

                if (pl.equals(UUIDFetcher.getPlayer(guild.leaderUUID))){
                    MessagingUtils.sendBGUserMessage(guild, p, m, denyLeader
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(denier))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                    );
                } else {
                    MessagingUtils.sendBGUserMessage(guild, p, m, denyMembers
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(denier))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(from))
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void warpGuild(Player sender){
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        Guild guild = getGuild(sender);

        if (!isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(p, noGuildFound);
            return;
        }

        if (! guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(p, notInGuild);
            return;
        }

        if (! guild.hasModPerms(sender)) {
            MessagingUtils.sendBUserMessage(p, noPermission);
            return;
        }

        for (Player player : guild.totalMembers){
            if (! player.online) continue;

            ProxiedPlayer m = UUIDFetcher.getPPlayer(player.uuid);

            if (m == null) continue;

            if (player.equals(sender)) {
                MessagingUtils.sendBGUserMessage(guild, p, m, warpSender);
            } else {
                MessagingUtils.sendBGUserMessage(guild, p, m, warpMembers);
            }

            m.connect(sender.getServer().getInfo());
        }
    }

    public static void muteGuild(Player sender){
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        Guild guild = getGuild(sender);

        if (!isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(p, noGuildFound);
            return;
        }

        if (! guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(p, notInGuild);
            return;
        }

        if (! guild.hasModPerms(sender)) {
            MessagingUtils.sendBGUserMessage(guild, p, p, noPermission);
            return;
        }

        if (guild.isMuted) {
            for (Player player : guild.totalMembers) {
                if (! player.online) continue;

                ProxiedPlayer m = UUIDFetcher.getPPlayer(player.uuid);

                if (m == null) continue;

                if (player.equals(sender)){
                    MessagingUtils.sendBGUserMessage(guild, p, m, unmuteUser);
                } else {
                    MessagingUtils.sendBGUserMessage(guild, p, m, unmuteMembers);
                }
            }

        } else {
            for (Player player : guild.totalMembers) {
                if (! player.online) continue;

                ProxiedPlayer m = UUIDFetcher.getPPlayer(player.uuid);

                if (m == null) continue;

                if (player.equals(sender)){
                    MessagingUtils.sendBGUserMessage(guild, p, m, muteUser);
                } else {
                    MessagingUtils.sendBGUserMessage(guild, p, m, muteMembers);
                }
            }

        }
        guild.toggleMute();
    }

    public static void kickMember(Player sender, Player player) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        Guild guild = getGuild(sender);

        if (!isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(p, kickFailure);
            return;
        }

        if (!guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(p, notInGuild);
            return;
        }

        if (!guild.hasMember(player)) {
            MessagingUtils.sendBUserMessage(p, otherNotInGuild);
            return;
        }

        if (!guild.hasModPerms(sender)) {
            MessagingUtils.sendBGUserMessage(guild, p, p, noPermission);
            return;
        }

        if (guild.hasModPerms(player)) {
            MessagingUtils.sendBGUserMessage(guild, p, p, kickMod);
            return;
        }

        try {
            if (sender.equals(player)) {
                MessagingUtils.sendBGUserMessage(guild, p, p, kickSelf);
            } else if (player.equals(UUIDFetcher.getPlayer(guild.leaderUUID))) {
                MessagingUtils.sendBGUserMessage(guild, p, p, noPermission);
            } else {
                for (Player pl : guild.totalMembers) {
                    if (!pl.online) continue;

                    ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, p, m, kickSender
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(player))
                        );
                    } else if (pl.equals(player)) {
                        MessagingUtils.sendBGUserMessage(guild, p, m, kickUser
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(player))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, p, m, kickMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(player))
                        );
                    }
                }

                guild.removeMemberFromGuild(player);
            }
        } catch (Exception e) {
            MessagingUtils.sendBGUserMessage(guild, sender, sender, MessageConfUtils.bungeeCommandError);
            e.printStackTrace();
        }
    }

    public static void info(Player sender){
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        Guild guild = getGuild(sender);

        if (!isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(p, noGuildFound);
            return;
        }

        if (! guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(p, notInGuild);
            return;
        }

        MessagingUtils.sendBGUserMessage(guild, p, p, info);
    }

    public static void disband(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(p, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInGuild);
                return;
            }

            if (! guild.leaderUUID.equals(sender.uuid)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            for (Player pl : guild.totalMembers) {
                if (! pl.online) continue;

                ProxiedPlayer member = UUIDFetcher.getPPlayer(pl.uuid);

                if (member == null) continue;

                if (! member.equals(UUIDFetcher.getPlayer(guild.leaderUUID))) {
                    MessagingUtils.sendBGUserMessage(guild, p, member, disbandMembers
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                    );
                } else {
                    MessagingUtils.sendBGUserMessage(guild, p, member, disbandLeader
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                    );
                }

            }

            guild.disband();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openGuild(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(p, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInGuild);
                return;
            }

            if (!guild.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            if (guild.isPublic) {
                MessagingUtils.sendBGUserMessage(guild, p, p, openFailure
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                        .replace("%size%", Integer.toString(guild.getSize()))
                );
            } else {
                guild.setPublic(true);

                for (Player pl : guild.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer member = UUIDFetcher.getPPlayer(pl.uuid);

                    if (member == null) continue;

                    if (member.equals(UUIDFetcher.getPlayer(guild.leaderUUID))) {
                        MessagingUtils.sendBGUserMessage(guild, p, member, openLeader
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, p, member, openMembers
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeGuild(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(p, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInGuild);
                return;
            }

            if (!guild.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            if (!guild.isPublic) {
                MessagingUtils.sendBGUserMessage(guild, p, p, closeFailure
                        .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                        .replace("%size%", Integer.toString(guild.getSize()))
                );
            } else {
                guild.setPublic(false);

                for (Player pl : guild.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer member = UUIDFetcher.getPPlayer(pl.uuid);

                    if (member == null) continue;
                    
                    if (member.equals(pl)) {
                        MessagingUtils.sendBGUserMessage(guild, p, member, closeSender
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, p, member, closeMembers
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listGuild(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(p, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInGuild);
                return;
            }

            String leaderBulk = listLeaderBulk
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%size%", Integer.toString(guild.getSize()));
            String moderatorBulk = listModBulkMain
                    .replace("%moderators%", Objects.requireNonNull(moderators(guild)))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                    .replace("%size%", Integer.toString(guild.getSize()));
            String memberBulk = listMemberBulkMain
                    .replace("%members%", Objects.requireNonNull(members(guild)))
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                    .replace("%size%", Integer.toString(guild.getSize()));

            MessagingUtils.sendBGUserMessage(guild, p, p, listMain
                    .replace("%leaderbulk%", leaderBulk)
                    .replace("%moderatorbulk%", moderatorBulk)
                    .replace("%memberbulk%", memberBulk)
                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
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

            for (Player m : guild.moderators) {
                if (i <= guild.moderators.size()) {
                    mods.append(listModBulkNotLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                            .replace("%size%", Integer.toString(guild.getSize()))
                    );
                } else {
                    mods.append(listModBulkLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
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

            for (Player m : guild.members) {
                if (i <= guild.moderators.size()) {
                    mems.append(listMemberBulkNotLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                            .replace("%size%", Integer.toString(guild.getSize()))
                    );
                } else {
                    mems.append(listMemberBulkLast
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(m))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
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

    public static void promotePlayer(Player sender, Player member) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(p, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInGuild);
                return;
            }

            if (! guild.hasMember(member)) {
                MessagingUtils.sendBUserMessage(p, otherNotInGuild);
                return;
            }

            if (!guild.leaderUUID.equals(sender.uuid)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            switch (guild.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBGUserMessage(guild, p, p, promoteFailure
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                            .replace("%level%", textLeader
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%size%", Integer.toString(guild.getSize()))
                            )
                    );
                    return;
                case MODERATOR:
                    guild.replaceLeader(member);

                    for (Player pl : guild.totalMembers) {
                        if (! pl.online) continue;

                        ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                        if (m == null) continue;

                        if (pl.equals(UUIDFetcher.getPlayer(guild.leaderUUID))) {
                            MessagingUtils.sendBGUserMessage(guild, p, m, promoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else if (pl.equals(member)) {
                            MessagingUtils.sendBGUserMessage(guild, p, m, promoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBGUserMessage(guild, p, m, promoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%level%", textLeader
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    guild.setModerator(member);

                    for (Player pl : guild.totalMembers) {
                        if (! pl.online) continue;

                        ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                        if (m == null) continue;

                        if (pl.equals(UUIDFetcher.getPlayer(guild.leaderUUID))) {
                            MessagingUtils.sendBGUserMessage(guild, p, m, promoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else if (pl.equals(member)) {
                            MessagingUtils.sendBGUserMessage(guild, p, m, promoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBGUserMessage(guild, p, m, promoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%level%", textModerator
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                            .replace("%size%", Integer.toString(guild.getSize()))
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
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(p, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInGuild);
                return;
            }

            if (! guild.hasMember(member)) {
                MessagingUtils.sendBUserMessage(p, otherNotInGuild);
                return;
            }

            if (!guild.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(p, noPermission);
                return;
            }

            switch (guild.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBGUserMessage(guild, p, p, demoteIsLeader
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                            .replace("%level%", textLeader
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%size%", Integer.toString(guild.getSize()))
                            )
                    );
                    return;
                case MODERATOR:
                    guild.setMember(member);

                    for (Player pl : guild.totalMembers) {
                        if (! pl.online) continue;

                        ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                        if (m == null) continue;

                        if (pl.equals(UUIDFetcher.getPlayer(guild.leaderUUID))) {
                            MessagingUtils.sendBGUserMessage(guild, p, m, demoteLeader
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%level%", textMember
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else if (pl.equals(member)) {
                            MessagingUtils.sendBGUserMessage(guild, p, m, demoteUser
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%level%", textMember
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBGUserMessage(guild, p, m, demoteMembers
                                    .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%level%", textMember
                                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    MessagingUtils.sendBGUserMessage(guild, p, p, demoteFailure
                            .replace("%user%", PlayerUtils.getOffOnDisplayBungee(member))
                            .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                            .replace("%level%", textMember
                                    .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                                    .replace("%size%", Integer.toString(guild.getSize()))
                            )
                    );
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void joinGuild(Player sender, Player from) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        try {
            Guild guild = getGuild(from);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(p, noGuildFound);
                return;
            }

            if (! guild.hasMember(from)) {
                MessagingUtils.sendBUserMessage(p, otherNotInGuild);
                return;
            }

            if (guild.getSize() >= guild.maxSize) {
                MessagingUtils.sendBGUserMessage(guild, p, p, notEnoughSpace);
                return;
            }

            if (guild.isPublic) {
                guild.addMember(sender);

                for (Player pl : guild.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, p, m, joinUser
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, p, m, joinMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                        );
                    }
                }
            } else {
                MessagingUtils.sendBGUserMessage(guild, p, p, joinFailure);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void leaveGuild(Player sender) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(p, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInGuild);
                return;
            }

            if (UUIDFetcher.getPlayer(guild.leaderUUID).equals(sender)) {
                for (Player pl : guild.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, p, m, leaveUser);
                        MessagingUtils.sendBGUserMessage(guild, p, m, disbandLeader);
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, p, m, leaveMembers);
                        MessagingUtils.sendBGUserMessage(guild, p, m, disbandMembers);
                    }
                }

                guilds.remove(guild);
                guild.dispose();
                return;
            }

            if (guild.hasMember(sender)) {
                for (Player pl : guild.totalMembers) {
                    if (! pl.online) continue;

                    ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                    if (m == null) continue;

                    if (pl.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, p, m, leaveUser
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, p, m, leaveMembers
                                .replace("%user%", PlayerUtils.getOffOnDisplayBungee(sender))
                                .replace("%leader%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID))))
                        );
                    }
                }

                guild.removeMemberFromGuild(sender);
            } else {
                MessagingUtils.sendBGUserMessage(guild, p, p, leaveFailure);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void sendChat(Player sender, String msg) {
        ProxiedPlayer p = UUIDFetcher.getPPlayer(sender.uuid);

        if (p == null) return;

        try {
            Guild guild = getGuild(sender);

            if (! isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(p, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(p, notInGuild);
                return;
            }

            if (guild.isMuted && ! guild.hasModPerms(sender)) {
                MessagingUtils.sendBGUserMessage(guild, p, p, chatMuted
                        .replace("%sender%", sender.displayName)
                        .replace("%message%", msg)
                );
                return;
            }

            if (ConfigUtils.guildConsole) {
                MessagingUtils.sendBGUserMessage(guild, p, StreamLine.getInstance().getProxy().getConsole(), chatConsole
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(sender))
                        .replace("%message%", msg)
                );
            }

            for (Player pl : guild.totalMembers) {
                if (! pl.online) continue;

                ProxiedPlayer m = UUIDFetcher.getPPlayer(pl.uuid);

                if (m == null) continue;

                MessagingUtils.sendBGUserMessage(guild, p, m, chat
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(sender))
                        .replace("%message%", msg)
                );

                if (ConfigUtils.guildToDiscord) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(p, discordTitle, msg, ConfigUtils.textChannelGuilds));
                }

                for (ProxiedPlayer pp : StreamLine.getInstance().getProxy().getPlayers()){
                    if (! pp.hasPermission(ConfigUtils.partyView)) continue;

                    Player them = PlayerUtils.getStat(pp);

                    if (them == null) continue;

                    if (! them.pspy) continue;

                    MessagingUtils.sendBGUserMessage(guild, p, pp, spy.replace("%message%", msg));
                }
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
    // No guild.
    public static final String noGuildFound = message.getString("guild.no-guild");
    // Already made.
    public static final String already = message.getString("guild.already-made");
    // Already in one.
    public static final String alreadyHasOne = message.getString("guild.already-has");
    // Not high enough permissions.
    public static final String noPermission = message.getString("guild.no-permission");
    // Not in a guild.
    public static final String notInGuild = message.getString("guild.not-in-a-guild");
    public static final String otherNotInGuild = message.getString("guild.other-not-in-guild");
    // Not enough space in guild.
    public static final String notEnoughSpace = message.getString("guild.not-enough-space");
    // Chat.
    public static final String chat = message.getString("guild.chat.message");
    public static final String chatMuted = message.getString("guild.chat.muted");
    public static final String chatConsole = message.getString("guild.chat.console");
    // Create.
    public static final String create = message.getString("guild.create");
    // Join.
    public static final String joinMembers = message.getString("guild.join.members");
    public static final String joinUser = message.getString("guild.join.user");
    public static final String joinFailure = message.getString("guild.join.failure");
    // Leave.
    public static final String leaveMembers = message.getString("guild.leave.members");
    public static final String leaveUser = message.getString("guild.leave.user");
    public static final String leaveFailure = message.getString("guild.leave.failure");
    // Promote.
    public static final String promoteMembers = message.getString("guild.promote.members");
    public static final String promoteUser = message.getString("guild.promote.user");
    public static final String promoteLeader = message.getString("guild.promote.leader");
    public static final String promoteFailure = message.getString("guild.promote.failure");
    // Demote.
    public static final String demoteMembers = message.getString("guild.demote.members");
    public static final String demoteUser = message.getString("guild.demote.user");
    public static final String demoteLeader = message.getString("guild.demote.leader");
    public static final String demoteFailure = message.getString("guild.demote.failure");
    public static final String demoteIsLeader = message.getString("guild.demote.is-leader");
    // List.
    public static final String listMain = message.getString("guild.list.main");
    public static final String listLeaderBulk = message.getString("guild.list.leaderbulk");
    public static final String listModBulkMain = message.getString("guild.list.moderatorbulk.main");
    public static final String listModBulkNotLast = message.getString("guild.list.moderatorbulk.moderators.not-last");
    public static final String listModBulkLast = message.getString("guild.list.moderatorbulk.moderators.last");
    public static final String listModBulkNone = message.getString("guild.list.moderatorbulk.moderators.if-none");
    public static final String listMemberBulkMain = message.getString("guild.list.memberbulk.main");
    public static final String listMemberBulkNotLast = message.getString("guild.list.memberbulk.members.not-last");
    public static final String listMemberBulkLast = message.getString("guild.list.memberbulk.members.last");
    public static final String listMemberBulkNone = message.getString("guild.list.memberbulk.members.if-none");
    // Open.
    public static final String openMembers = message.getString("guild.open.members");
    public static final String openLeader = message.getString("guild.open.leader");
    public static final String openFailure = message.getString("guild.open.failure");
    // Close.
    public static final String closeMembers = message.getString("guild.close.members");
    public static final String closeSender = message.getString("guild.close.sender");
    public static final String closeFailure = message.getString("guild.close.failure");
    // Disband.
    public static final String disbandMembers = message.getString("guild.disband.members");
    public static final String disbandLeader = message.getString("guild.disband.leader");
    // Accept.
    public static final String acceptUser = message.getString("guild.accept.user");
    public static final String acceptLeader = message.getString("guild.accept.leader");
    public static final String acceptMembers = message.getString("guild.accept.members");
    public static final String acceptFailure = message.getString("guild.accept.failure");
    // Deny.
    public static final String denyUser = message.getString("guild.deny.user");
    public static final String denyLeader = message.getString("guild.deny.leader");
    public static final String denyMembers = message.getString("guild.deny.members");
    public static final String denyFailure = message.getString("guild.deny.failure");
    // Invite.
    public static final String inviteUser = message.getString("guild.invite.user");
    public static final String inviteLeader = message.getString("guild.invite.leader");
    public static final String inviteMembers = message.getString("guild.invite.members");
    public static final String inviteFailure = message.getString("guild.invite.failure");
    public static final String inviteNonSelf = message.getString("guild.invite.non-self");
    // Kick.
    public static final String kickUser = message.getString("guild.kick.user");
    public static final String kickSender = message.getString("guild.kick.sender");
    public static final String kickMembers = message.getString("guild.kick.members");
    public static final String kickFailure = message.getString("guild.kick.failure");
    public static final String kickMod = message.getString("guild.kick.mod");
    public static final String kickSelf = message.getString("guild.kick.self");
    // Mute.
    public static final String muteUser = message.getString("guild.mute.mute.user");
    public static final String muteMembers = message.getString("guild.mute.mute.members");
    public static final String unmuteUser = message.getString("guild.mute.unmute.user");
    public static final String unmuteMembers = message.getString("guild.mute.unmute.members");
    // Warp.
    public static final String warpSender = message.getString("guild.warp.sender");
    public static final String warpMembers = message.getString("guild.warp.members");
    // Info.
    public static final String info = message.getString("guild.info");
}
