package net.plasmere.streamline.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.Guild;

import java.util.*;

public class GuildUtils {
    private static final List<Guild> guilds = new ArrayList<>();
    private static final Configuration message = Config.getMess();

    private static final LuckPerms api = LuckPermsProvider.get();

    public static List<Guild> getGuilds() {
        return guilds;
    }
    // Guild , Invites
    public static Map<Guild, List<ProxiedPlayer>> invites = new HashMap<>();

    public static Guild getGuild(ProxiedPlayer player) throws Exception {
        try {
            Guild it = null;
            for (Guild guild : guilds) {
                if (guild.hasMember(player.getUniqueId()))
                    it = guild;
            }
            return it;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static Guild getGuild(CommandSender player) throws Exception {
        try {
            Guild it = null;
            for (Guild guild : guilds) {
                if (guild.hasMember((ProxiedPlayer) player))
                    it = guild;
            }
            return it;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static boolean isGuild(Guild guild){
        return guilds.contains(guild);
    }

    public static void reloadGuild(Guild guild) throws Exception {
        guilds.remove(getGuild(UUIDFetcher.getProxiedPlayer(guild.leaderUUID)));
        guilds.add(guild);
    }

    public static void createGuild(ProxiedPlayer player, String name) throws Exception {
        try {
            Guild guild = new Guild((guilds.size() + 1), player.getUniqueId(), name);

            addGuild(guild);

            MessagingUtils.sendBGUserMessage(guild, player, player, create);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void addGuild(Guild guild){ guilds.add(guild); }

    public static void removeGuild(Guild guild){ guilds.remove(guild); }

    public static void sendInvite(ProxiedPlayer to, ProxiedPlayer from) throws Exception {
        try {
            Guild guild = getGuild(from);

            if (! isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(to, noGuildFound);
                return;
            }

            if (! guild.hasMember(from.getUniqueId())) {
                MessagingUtils.sendBUserMessage(from, notInGuild);
                return;
            }

            if (! guild.hasModPerms(from.getUniqueId())) {
                MessagingUtils.sendBUserMessage(from, noPermission);
                return;
            }

            if (guild.invites.contains(to.getUniqueId())){
                MessagingUtils.sendBUserMessage(from, inviteFailure);
                return;
            }

            MessagingUtils.sendBGUserMessage(guild, from, to, inviteUser
                    .replace("%sender%", from.getDisplayName())
                    .replace("%user%", to.getDisplayName())
                    .replace("%leader%", Objects.requireNonNull(UUIDFetcher.getProxiedPlayer(getGuild(from).leaderUUID)).getDisplayName())
                    .replace("%leaderdefault%", Objects.requireNonNull(UUIDFetcher.getProxiedPlayer(getGuild(from).leaderUUID)).getName())
            );

            for (UUID member : guild.totalMembersByUUID) {
                if (member.equals(from.getUniqueId())) {
                    MessagingUtils.sendBGUserMessage(guild, from, guild.getMember(member), inviteLeader
                            .replace("%sender%", from.getDisplayName())
                            .replace("%user%", to.getDisplayName())
                            .replace("%leader%", Objects.requireNonNull(UUIDFetcher.getProxiedPlayer(getGuild(from).leaderUUID)).getDisplayName())
                            .replace("%leaderdefault%", Objects.requireNonNull(UUIDFetcher.getProxiedPlayer(getGuild(from).leaderUUID)).getName())
                    );
                } else {
                    MessagingUtils.sendBGUserMessage(guild, from, guild.getMember(member), inviteMembers
                            .replace("%sender%", from.getDisplayName())
                            .replace("%user%", to.getDisplayName())
                            .replace("%leader%", guild.getMember(getGuild(from).leaderUUID).getDisplayName())
                            .replace("%leaderdefault%", guild.getMember(getGuild(from).leaderUUID).getName())
                    );
                }
            }

            reloadGuild(guild);

            guild.addInvite(to);
            invites.remove(guild);
            invites.put(guild, guild.invites);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void acceptInvite(ProxiedPlayer accepter, ProxiedPlayer from) throws Exception {
        try {
            Guild guild = getGuild(from);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(accepter, acceptFailure);
                return;
            }

            if (! guild.hasMember(from)) {
                MessagingUtils.sendBUserMessage(accepter, otherNotInGuild);
                return;
            }

            if (guild.invites.contains(accepter)) {
                if (guild.getSize() >= guild.maxSize) {
                    MessagingUtils.sendBGUserMessage(guild, accepter, accepter, notEnoughSpace);
                    return;
                }

                MessagingUtils.sendBGUserMessage(guild, accepter, accepter, acceptUser
                        .replace("%user%", accepter.getDisplayName())
                        .replace("%leader%", from.getDisplayName())
                );

                for (ProxiedPlayer m : guild.totalMembers){
                    if (m.equals(guild.getMember(guild.leaderUUID))){
                        MessagingUtils.sendBGUserMessage(guild, accepter, m, acceptLeader
                                .replace("%user%", accepter.getDisplayName())
                                .replace("%leader%", from.getDisplayName())
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, accepter, m, acceptMembers
                                .replace("%user%", accepter.getDisplayName())
                                .replace("%leader%", from.getDisplayName())
                        );
                    }
                }

                guild.addMember(accepter);
                guild.removeInvite(accepter);
            }

            reloadGuild(guild);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void denyInvite(ProxiedPlayer denier, ProxiedPlayer from) throws Exception {
        try {
            Guild guild = getGuild(from);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(denier, denyFailure);
                return;
            }

            if (! guild.hasMember(from)) {
                MessagingUtils.sendBUserMessage(denier, otherNotInGuild);
                return;
            }

            guild.removeInvite(denier);

            MessagingUtils.sendBGUserMessage(guild, denier, denier, denyUser
                    .replace("%user%", denier.getDisplayName())
                    .replace("%leader%", from.getDisplayName())
            );

            for (ProxiedPlayer m : guild.totalMembers){
                if (m.equals(guild.getMember(guild.leaderUUID))){
                    MessagingUtils.sendBGUserMessage(guild, denier, m, denyLeader
                            .replace("%user%", denier.getDisplayName())
                            .replace("%leader%", from.getDisplayName())
                    );
                } else {
                    MessagingUtils.sendBGUserMessage(guild, denier, m, denyMembers
                            .replace("%user%", denier.getDisplayName())
                            .replace("%leader%", from.getDisplayName())
                    );
                }
            }

            reloadGuild(guild);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void warpGuild(ProxiedPlayer sender) throws Exception{
        Guild guild = getGuild(sender);

        if (!isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(sender, noGuildFound);
            return;
        }

        if (! guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(sender, notInGuild);
            return;
        }

        if (! guild.getMember(guild.leaderUUID).equals(sender)) {
            MessagingUtils.sendBGUserMessage(guild, sender, sender, noPermission);
            return;
        }

        for (ProxiedPlayer m : guild.totalMembers){
            if (m.equals(sender)) {
                MessagingUtils.sendBGUserMessage(guild, sender, m, warpLeader);
            } else {
                MessagingUtils.sendBGUserMessage(guild, sender, m, warpMembers);
            }

            m.connect(sender.getServer().getInfo());
        }

        reloadGuild(guild);
    }

    public static void muteGuild(ProxiedPlayer sender) throws Exception{
        Guild guild = getGuild(sender);

        if (!isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(sender, noGuildFound);
            return;
        }

        if (! guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(sender, notInGuild);
            return;
        }

        if (! guild.hasModPerms(sender)) {
            MessagingUtils.sendBGUserMessage(guild, sender, sender, noPermission);
            return;
        }

        if (guild.isMuted) {
            for (ProxiedPlayer m : guild.totalMembers) {
                if (m.equals(sender)){
                    MessagingUtils.sendBGUserMessage(guild, sender, m, unmuteUser);
                } else {
                    MessagingUtils.sendBGUserMessage(guild, sender, m, unmuteMembers);
                }
            }

        } else {
            for (ProxiedPlayer m : guild.totalMembers) {
                if (m.equals(sender)){
                    MessagingUtils.sendBGUserMessage(guild, sender, m, muteUser);
                } else {
                    MessagingUtils.sendBGUserMessage(guild, sender, m, muteMembers);
                }
            }

        }
        guild.toggleMute();

        reloadGuild(guild);
    }

    public static void kickMember(ProxiedPlayer sender, ProxiedPlayer player) throws Exception{
        Guild guild = getGuild(sender);

        if (!isGuild(guild) || guild == null) {
            MessagingUtils.sendBUserMessage(sender, noGuildFound);
            return;
        }

        if (! guild.hasMember(sender)) {
            MessagingUtils.sendBUserMessage(sender, notInGuild);
            return;
        }

        if (! guild.hasMember(player)) {
            MessagingUtils.sendBUserMessage(sender, otherNotInGuild);
            return;
        }

        if (! guild.hasModPerms(sender)) {
            MessagingUtils.sendBGUserMessage(guild, sender, sender, noPermission);
        } else {
            if (sender.equals(player)) {
                MessagingUtils.sendBGUserMessage(guild, sender, sender, kickSelf);
            } else if (player.equals(guild.getMember(guild.leaderUUID))) {
                MessagingUtils.sendBGUserMessage(guild, sender, sender, noPermission);
            } else {
                for (ProxiedPlayer m : guild.totalMembers){
                    if (m.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, sender, m, kickSender
                                .replace("%user%", player.getDisplayName())
                        );
                    } else if (m.equals(player)) {
                        MessagingUtils.sendBGUserMessage(guild, sender, m, kickUser
                                .replace("%user%", player.getDisplayName())
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender, m, kickMembers
                                .replace("%user%", player.getDisplayName())
                        );
                    }
                }

                guild.removeMemberFromGuild(player);
            }
        }

        reloadGuild(guild);
    }

    public static void disband(ProxiedPlayer sender) throws Throwable {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender, notInGuild);
                return;
            }

            if (!guild.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender, noPermission);
                return;
            }

            for (ProxiedPlayer member : guild.totalMembers) {
                if (!member.equals(guild.getMember(guild.leaderUUID))) {
                    MessagingUtils.sendBGUserMessage(guild, sender, member, disbandMembers
                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                    );
                } else {
                    MessagingUtils.sendBGUserMessage(guild, sender, member, disbandLeader
                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                    );
                }

            }

            removeGuild(guild);

            guild.dispose();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void openGuild(ProxiedPlayer sender) throws Exception {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender, notInGuild);
                return;
            }

            if (!guild.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender, noPermission);
                return;
            }

            if (guild.isPublic) {
                MessagingUtils.sendBGUserMessage(guild, sender, guild.getMember(guild.leaderUUID), openFailure
                        .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                        .replace("%size%", Integer.toString(guild.getSize()))
                );
            } else {
                guild.setPublic(true);

                for (ProxiedPlayer member : guild.totalMembers) {
                    if (member.equals(guild.getMember(guild.leaderUUID))) {
                        MessagingUtils.sendBGUserMessage(guild, sender, guild.getMember(guild.leaderUUID), openLeader
                                .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender, guild.getMember(guild.leaderUUID), openMembers
                                .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    }
                }
            }

            reloadGuild(guild);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void closeGuild(ProxiedPlayer sender) throws Exception {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender, notInGuild);
                return;
            }

            if (!guild.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender, noPermission);
                return;
            }

            if (!guild.isPublic) {
                MessagingUtils.sendBGUserMessage(guild, sender, guild.getMember(guild.leaderUUID), closeFailure
                        .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                        .replace("%size%", Integer.toString(guild.getSize()))
                );
            } else {
                guild.setPublic(false);

                for (ProxiedPlayer member : guild.totalMembers) {
                    if (member.equals(guild.getMember(guild.leaderUUID))) {
                        MessagingUtils.sendBGUserMessage(guild, sender, guild.getMember(guild.leaderUUID), closeLeader
                                .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender, guild.getMember(guild.leaderUUID), closeMembers
                                .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                .replace("%size%", Integer.toString(guild.getSize()))
                        );
                    }
                }
            }

            reloadGuild(guild);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void listGuild(ProxiedPlayer sender) throws Exception {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender, notInGuild);
                return;
            }

            String leaderBulk = listLeaderBulk
                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                    .replace("%user%", sender.getDisplayName())
                    .replace("%size%", Integer.toString(guild.getSize()));
            String moderatorBulk = listModBulkMain
                    .replace("%moderators%", moderators(guild))
                    .replace("%user%", sender.getDisplayName())
                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                    .replace("%size%", Integer.toString(guild.getSize()));
            String memberBulk = listMemberBulkMain
                    .replace("%members%", members(guild))
                    .replace("%user%", sender.getDisplayName())
                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                    .replace("%size%", Integer.toString(guild.getSize()));

            MessagingUtils.sendBGUserMessage(guild, sender, sender, listMain
                    .replace("%leaderbulk%", leaderBulk)
                    .replace("%moderatorbulk%", moderatorBulk)
                    .replace("%memberbulk%", memberBulk)
                    .replace("%user%", sender.getDisplayName())
                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                    .replace("%size%", Integer.toString(guild.getSize()))
            );

            reloadGuild(guild);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static String moderators(Guild guild) throws Exception {
        try {
            if (! (guild.moderators.size() > 0)) {
                return listModBulkNone;
            }

            StringBuilder mods = new StringBuilder();

            int i = 1;

            for (ProxiedPlayer m : guild.moderators) {
                if (i <= guild.moderators.size()) {
                    mods.append(listModBulkNotLast
                            .replace("%user%", m.getDisplayName())
                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                            .replace("%size%", Integer.toString(guild.getSize()))
                    );
                } else {
                    mods.append(listModBulkLast
                            .replace("%user%", m.getDisplayName())
                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                            .replace("%size%", Integer.toString(guild.getSize()))
                    );
                }
                i++;
            }

            return mods.toString();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static String members(Guild guild) throws Exception {
        try {
            if (! (guild.members.size() > 0)) {
                return listMemberBulkNone;
            }

            StringBuilder mems = new StringBuilder();

            int i = 1;

            for (ProxiedPlayer m : guild.members) {
                if (i <= guild.moderators.size()) {
                    mems.append(listMemberBulkNotLast
                            .replace("%user%", m.getDisplayName())
                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                            .replace("%size%", Integer.toString(guild.getSize()))
                    );
                } else {
                    mems.append(listMemberBulkLast
                            .replace("%user%", m.getDisplayName())
                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                            .replace("%size%", Integer.toString(guild.getSize()))
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
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender, notInGuild);
                return;
            }

            if (! guild.hasMember(member)) {
                MessagingUtils.sendBUserMessage(sender, otherNotInGuild);
                return;
            }

            if (!guild.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender, noPermission);
                return;
            }

            switch (guild.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBGUserMessage(guild, sender, guild.getMember(guild.leaderUUID), promoteFailure
                            .replace("%user%", member.getDisplayName())
                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                            .replace("%level%", textLeader
                                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                    .replace("%size%", Integer.toString(guild.getSize()))
                            )
                    );
                    return;
                case MODERATOR:
                    guild.replaceLeader(member);

                    for (ProxiedPlayer m : guild.totalMembers) {
                        if (m.equals(guild.getMember(guild.leaderUUID))) {
                            MessagingUtils.sendBGUserMessage(guild, sender, m, promoteLeader
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                    .replace("%level%", textLeader
                                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBGUserMessage(guild, sender, m, promoteUser
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                    .replace("%level%", textLeader
                                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBGUserMessage(guild, sender, m, promoteMembers
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                    .replace("%level%", textLeader
                                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    guild.setModerator(member);

                    for (ProxiedPlayer m : guild.totalMembers) {
                        if (m.equals(guild.getMember(guild.leaderUUID))) {
                            MessagingUtils.sendBGUserMessage(guild, sender, m, promoteLeader
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                    .replace("%level%", textModerator
                                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBGUserMessage(guild, sender, m, promoteUser
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                    .replace("%level%", textModerator
                                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        } else {
                            MessagingUtils.sendBGUserMessage(guild, sender, m, promoteMembers
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                    .replace("%level%", textModerator
                                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                            .replace("%size%", Integer.toString(guild.getSize()))
                                    )
                            );
                        }
                    }
                    break;
            }

            reloadGuild(guild);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void demotePlayer(ProxiedPlayer sender, ProxiedPlayer member) throws Exception {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender, notInGuild);
                return;
            }

            if (! guild.hasMember(member)) {
                MessagingUtils.sendBUserMessage(sender, otherNotInGuild);
                return;
            }

            if (!guild.hasModPerms(sender)) {
                MessagingUtils.sendBUserMessage(sender, noPermission);
                return;
            }

            switch (guild.getLevel(member)) {
                case LEADER:
                    MessagingUtils.sendBGUserMessage(guild, sender, guild.getMember(guild.leaderUUID), demoteIsLeader
                            .replace("%user%", member.getDisplayName())
                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                            .replace("%level%", textLeader)
                    );
                    return;
                case MODERATOR:
                    guild.setMember(member);

                    for (ProxiedPlayer m : guild.totalMembers) {
                        if (m.equals(guild.getMember(guild.leaderUUID))) {
                            MessagingUtils.sendBGUserMessage(guild, sender, m, demoteLeader
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                    .replace("%level%", textMember)
                            );
                        } else if (m.equals(member)) {
                            MessagingUtils.sendBGUserMessage(guild, sender, m, demoteUser
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                    .replace("%level%", textMember)
                            );
                        } else {
                            MessagingUtils.sendBGUserMessage(guild, sender, m, demoteMembers
                                    .replace("%user%", member.getDisplayName())
                                    .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                                    .replace("%level%", textMember)
                            );
                        }
                    }
                    return;
                case MEMBER:
                default:
                    MessagingUtils.sendBGUserMessage(guild, sender, guild.getMember(guild.leaderUUID), demoteFailure
                            .replace("%user%", member.getDisplayName())
                            .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                            .replace("%level%", textMember)
                    );
                    break;
            }

            reloadGuild(guild);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void joinGuild(ProxiedPlayer sender, ProxiedPlayer from) throws Exception {
        try {
            Guild guild = getGuild(from);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(from)) {
                MessagingUtils.sendBUserMessage(sender, otherNotInGuild);
                return;
            }

            if (guild.getSize() >= guild.maxSize) {
                MessagingUtils.sendBGUserMessage(guild, sender, sender, notEnoughSpace);
                return;
            }

            if (guild.isPublic) {
                guild.addMember(sender);

                for (ProxiedPlayer m : guild.totalMembers) {
                    if (m.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, sender, m, joinUser
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender, m, joinMembers
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                        );
                    }
                }
            } else {
                MessagingUtils.sendBGUserMessage(guild, sender, sender, joinFailure);
            }

            reloadGuild(guild);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void leaveGuild(ProxiedPlayer sender) throws Exception {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender, notInGuild);
                return;
            }

            if (guild.getMember(guild.leaderUUID).equals(sender)) {
                for (ProxiedPlayer m : guild.totalMembers) {
                    MessagingUtils.sendBGUserMessage(guild, sender, m, disbandLeader);
                }
                for (ProxiedPlayer m : guild.totalMembers) {
                    if (m.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, sender, m, leaveUser);
                        MessagingUtils.sendBGUserMessage(guild, sender, m, disbandLeader);
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender, m, leaveMembers);
                        MessagingUtils.sendBGUserMessage(guild, sender, m, disbandMembers);
                    }
                }

                guilds.remove(guild);
                guild.dispose();
                return;
            }

            if (guild.hasMember(sender)) {
                for (ProxiedPlayer m : guild.totalMembers) {
                    if (m.equals(sender)) {
                        MessagingUtils.sendBGUserMessage(guild, sender, m, leaveUser
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                        );
                    } else {
                        MessagingUtils.sendBGUserMessage(guild, sender, m, leaveMembers
                                .replace("%user%", sender.getDisplayName())
                                .replace("%leader%", guild.getMember(guild.leaderUUID).getDisplayName())
                        );
                    }
                }

                guild.removeMemberFromGuild(sender);
            } else {
                MessagingUtils.sendBGUserMessage(guild, sender, sender, leaveFailure);
            }

            reloadGuild(guild);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static void sendChat(ProxiedPlayer sender, String msg) throws Exception {
        try {
            Guild guild = getGuild(sender);

            if (!isGuild(guild) || guild == null) {
                MessagingUtils.sendBUserMessage(sender, noGuildFound);
                return;
            }

            if (! guild.hasMember(sender)) {
                MessagingUtils.sendBUserMessage(sender, notInGuild);
                return;
            }

            if (guild.isMuted && ! guild.hasModPerms(sender)) {
                MessagingUtils.sendBGUserMessage(guild, sender, sender, chatMuted
                        .replace("%sender%", sender.getDisplayName())
                        .replace("%message%", msg)
                );
                return;
            }

            if (ConfigUtils.guildConsole) {
                MessagingUtils.sendBGUserMessage(guild, sender, StreamLine.getInstance().getProxy().getConsole(), chatConsole
                        .replace("%sender%", sender.getDisplayName())
                        .replace("%message%", msg)
                );
            }

            for (ProxiedPlayer member : guild.totalMembers) {
                MessagingUtils.sendBGUserMessage(guild, sender, member, chat
                        .replace("%sender%", sender.getDisplayName())
                        .replace("%message%", msg)
                );
            }

            reloadGuild(guild);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    // MESSAGES...
    // Text.
    public static final String textLeader = message.getString("guild.text.leader");
    public static final String textModerator = message.getString("guild.text.moderator");
    public static final String textMember = message.getString("guild.text.member");
    // No guild.
    public static final String noGuildFound = message.getString("guild.no-guild");
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
    public static final String closeLeader = message.getString("guild.close.leader");
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
    // Kick.
    public static final String kickUser = message.getString("guild.kick.user");
    public static final String kickSender = message.getString("guild.kick.sender");
    public static final String kickMembers = message.getString("guild.kick.members");
    public static final String kickFailure = message.getString("guild.kick.failure");
    public static final String kickSelf = message.getString("guild.kick.self");
    // Mute.
    public static final String muteUser = message.getString("guild.mute.mute.user");
    public static final String muteMembers = message.getString("guild.mute.mute.members");
    public static final String unmuteUser = message.getString("guild.mute.unmute.user");
    public static final String unmuteMembers = message.getString("guild.mute.unmute.members");
    // Warp.
    public static final String warpLeader = message.getString("guild.warp.leader");
    public static final String warpMembers = message.getString("guild.warp.members");
}
