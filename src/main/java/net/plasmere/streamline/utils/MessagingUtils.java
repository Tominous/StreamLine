package net.plasmere.streamline.utils;

import net.md_5.bungee.api.config.ServerInfo;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.dv8tion.jda.api.JDA;
import net.plasmere.streamline.objects.messaging.BungeeMassMessage;
import net.plasmere.streamline.objects.messaging.BungeeMessage;
import net.plasmere.streamline.objects.messaging.DiscordMessage;

import java.util.*;

public class MessagingUtils {
    private static final JDA jda = StreamLine.getJda();
    private static final EmbedBuilder eb = new EmbedBuilder();

    public static void sendStaffMessage(CommandSender sender, String from, String msg){
        Collection<ProxiedPlayer> staff = StreamLine.getInstance().getProxy().getPlayers();
        Set<ProxiedPlayer> staffs = new HashSet<>(staff);

        for (ProxiedPlayer player : staff){
            try {
                if (! player.hasPermission(ConfigUtils.staffPerm)) {
                    staffs.remove(player);
                }

                Player stat = PlayerUtils.getStat(player);

                if (stat == null) continue;
                if (! stat.sc) {
                    staffs.remove(player);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        for (ProxiedPlayer player : staffs) {
            player.sendMessage(TextUtils.codedText(MessageConfUtils.bungeeStaffChatMessage
                            .replace("%user%", sender.getName())
                            .replace("%from%", from)
                            .replace("%message%", msg)
                            .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(sender)).latestVersion)
                    )
            );
        }
    }

    public static void sendStaffChatMessage(CommandSender sender, String from, String msg, StreamLine plugin){
        Collection<ProxiedPlayer> staff = plugin.getProxy().getPlayers();
        Set<ProxiedPlayer> staffs = new HashSet<>(staff);

        for (ProxiedPlayer player : staff){
            try {
                if (! player.hasPermission(ConfigUtils.staffPerm)) {
                    staffs.remove(player);
                    continue;
                }

                try {
                    Player p = PlayerUtils.getStat(player);

                    if (p == null) continue;

                    if (! p.sc) {
                        staffs.remove(player);
                    }
                } catch (Exception e) {
                    // Console, so continue...
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        for (ProxiedPlayer player : staffs) {
            player.sendMessage(TextUtils.codedText(MessageConfUtils.bungeeStaffChatMessage
                            .replace("%user%", sender.getName())
                            .replace("%from%", from)
                            .replace("%message%", msg)
                            .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(sender)).latestVersion)
                    )
            );
        }
    }

    public static void sendBungeeMessage(BungeeMassMessage message){
        Collection<ProxiedPlayer> staff = StreamLine.getInstance().getProxy().getPlayers();
        Set<ProxiedPlayer> people = new HashSet<>(staff);
        List<ProxiedPlayer> ps = new ArrayList<>(people);

        for (ProxiedPlayer player : people){
            try {
                if (! player.hasPermission(message.permission)) {
                    ps.remove(player);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        for (ProxiedPlayer player : ps) {
            sendBungeeMessage(new BungeeMessage(message.sender, player, message.title, message.transition, message.message));
        }
    }

    public static void sendBungeeMessage(BungeeMessage message){
        String version = PlayerUtils.notSet;

        Player player = UUIDFetcher.getPlayer(message.sender);
        if (player != null) version = player.latestVersion;

        message.to.sendMessage(TextUtils.codedText((message.title + message.transition + message.message)
                        .replace("%sender%", message.sender.getName())
                        .replace("%version%", version)
                )
        );
    }

    public static void sendStaffMessageSC(String sender, String from, String msg){
        Collection<ProxiedPlayer> staff = StreamLine.getInstance().getProxy().getPlayers();
        Set<ProxiedPlayer> staffs = new HashSet<>(staff);

        for (ProxiedPlayer player : staff){
            try {
                if (! player.hasPermission(ConfigUtils.staffPerm)) {
                    staffs.remove(player);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        for (ProxiedPlayer player : staffs) {
            player.sendMessage(TextUtils.codedText(MessageConfUtils.bungeeStaffChatMessage
                            .replace("%user%", sender)
                            .replace("%from%", from)
                            .replace("%message%", msg)
                    )
            );
        }
    }

    public static void sendStaffMessageReport(String sender, boolean fromBungee, String report){
        Collection<ProxiedPlayer> staff = StreamLine.getInstance().getProxy().getPlayers();
        Set<ProxiedPlayer> staffs = new HashSet<>(staff);

        for (ProxiedPlayer player : staff){
            try {
                if (! player.hasPermission("streamline.staff.reports")) {
                    staffs.remove(player);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        for (ProxiedPlayer player : staffs) {
            if (fromBungee)
                player.sendMessage(TextUtils.codedText(MessageConfUtils.bToBReportMessage
                                .replace("%reporter%", sender)
                                .replace("%report%", report)
                                .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(sender)).latestVersion)
                        )
                );
            else
                player.sendMessage(TextUtils.codedText(MessageConfUtils.dToBReportMessage
                                .replace("%reporter%", sender)
                                .replace("%report%", report)
                        )
                );
        }
    }

    public static void sendDiscordJoinLeaveMessage(boolean isJoin, Player player){
        try {
            if (isJoin) {
                Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelBJoins))
                        .sendMessage(
                                eb
                                        .setDescription(MessageConfUtils.discordOnline.replace("%player_default%", player.getName()).replace("%player%", PlayerUtils.getOffOnDisplayDiscord(player)))
                                        .setAuthor(MessageConfUtils.discordOnlineEmbed, jda.getSelfUser().getAvatarUrl(), jda.getSelfUser().getAvatarUrl())
                                        .build()
                        ).queue();
            } else {
                Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelBLeaves))
                        .sendMessage(
                                eb
                                        .setDescription(MessageConfUtils.discordOffline.replace("%player_default%", player.getName())
                                                .replace("%player%", PlayerUtils.getOffOnDisplayDiscord(player)))
                                        .setAuthor(MessageConfUtils.discordOfflineEmbed, jda.getSelfUser().getAvatarUrl(), jda.getSelfUser().getAvatarUrl())
                                        .build()
                        ).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendDiscordEBMessage(DiscordMessage message){
        try {
            if (ConfigUtils.moduleUseMCAvatar) {
                if (message.sender instanceof ProxiedPlayer) {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessage(
                                    eb.setTitle(message.title.replace("%sender%", message.sender.getName()))
                                            .setDescription(message.message.replace("%sender%", message.sender.getName()))
                                            .setAuthor(message.sender.getName(), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getStat(message.sender)).latestName), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getStat(message.sender)).latestName))
                                            .build()
                            ).queue();
                } else {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessage(
                                    eb.setTitle(message.title.replace("%sender%", message.sender.getName()))
                                            .setDescription(message.message.replace("%sender%", message.sender.getName()))
                                            .setAuthor("CONSOLE", jda.getSelfUser().getAvatarUrl() , jda.getSelfUser().getAvatarUrl())
                                            .build()
                            ).queue();
                }
            } else {
                Objects.requireNonNull(jda.getTextChannelById(message.channel))
                        .sendMessage(
                                eb.setTitle(message.title.replace("%sender%", message.sender.getName()))
                                        .setDescription(message.message.replace("%sender%", message.sender.getName()))
                                        .build()
                        ).queue();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendDiscordReportMessage(String sender, boolean fromBungee, String report){
        try {
            String replace = MessageConfUtils.dToDReportMessage
                    .replace("%reporter%", sender)
                    .replace("%report%", report);

            String replace1 = MessageConfUtils.bToDReportMessage
                    .replace("%reporter%", sender)
                    .replace("%report%", report);

            if (ConfigUtils.moduleUseMCAvatar) {
                if (fromBungee)
                    Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelReports)).sendMessage(
                            eb.setTitle(MessageConfUtils.reportEmbedTitle)
                                    .setDescription(TextUtils.newLined(
                                            replace1
                                            )
                                    ).setAuthor(sender, FaceFetcher.getFaceAvatarURL(sender), FaceFetcher.getFaceAvatarURL(sender)).build()
                    ).queue();
                else
                    Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelReports)).sendMessage(
                            eb.setTitle(MessageConfUtils.reportEmbedTitle)
                                    .setDescription(TextUtils.newLined(
                                            replace
                                            )
                                    ).setAuthor(sender, FaceFetcher.getFaceAvatarURL(sender), FaceFetcher.getFaceAvatarURL(sender)).build()
                    ).queue();
            } else {
                if (fromBungee)
                    Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelReports)).sendMessage(
                            eb.setTitle(MessageConfUtils.reportEmbedTitle)
                                    .setDescription(TextUtils.newLined(
                                            replace1
                                            )
                                    ).build()
                    ).queue();
                else
                    Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelReports)).sendMessage(
                            eb.setTitle(MessageConfUtils.reportEmbedTitle)
                                    .setDescription(TextUtils.newLined(
                                            replace
                                            )
                                    ).build()
                    ).queue();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendDSelfMessage(MessageReceivedEvent context, String title, String description) {
        context.getChannel().sendMessage(
                eb.setTitle(title)
                .setDescription(TextUtils.newLined(description))
                        .build()
        ).queue();
    }

    public static void sendBPUserMessage(Party party, CommandSender sender, CommandSender to, String msg){
        to.sendMessage(TextUtils.codedText(msg
                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(UUIDFetcher.getPlayer(sender)))
                .replace("%leader%", party.leader.getName())
                .replace("%size%", Integer.toString(party.getSize()))
                .replace("%max%", Integer.toString(party.maxSize))
                .replace("%maxmax%", Integer.toString(party.getMaxSize(party.leader)))
                .replace("%mods_count%", Integer.toString(party.moderators.size()))
                .replace("%members_count%", Integer.toString(party.members.size()))
                .replace("%total_count%", Integer.toString(party.totalMembers.size()))
                .replace("%invites_count%", Integer.toString(party.invites.size()))
                .replace("%mods%", mods(party))
                .replace("%members%", members(party))
                .replace("%totalmembers%", membersT(party))
                .replace("%invites%", invites(party))
                .replace("%ispublic%", getIsPublic(party))
                .replace("%ismuted%", getIsMuted(party))
                .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(sender)).latestVersion)
        ));
    }

    public static void sendBGUserMessage(Guild guild, CommandSender sender, CommandSender to, String msg){
        if (sender instanceof Player) {
            sender = UUIDFetcher.getPPlayerByUUID(((Player) sender).uuid);
        }

        if (to instanceof Player){
            to = UUIDFetcher.getPPlayerByUUID(((Player) to).uuid);
        }

        if (sender == null || to == null) {
            StreamLine.getInstance().getLogger().severe("ERROR CASTING! REPORT \"sendBGUserMessage error\" TO PLUGIN AUTHOR!");
            return;
        }

        to.sendMessage(TextUtils.codedText(msg
                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(UUIDFetcher.getPlayer(sender)))
                .replace("%leader%", Objects.requireNonNull(UUIDFetcher.getPlayerByUUID(guild.leaderUUID, true)).getName())
                .replace("%size%", Integer.toString(guild.getSize()))
                .replace("%max%", Integer.toString(guild.maxSize))
                .replace("%mods_count%", Integer.toString(guild.modsByUUID.size()))
                .replace("%members_count%", Integer.toString(guild.membersByUUID.size()))
                .replace("%total_count%", Integer.toString(guild.totalMembersByUUID.size()))
                .replace("%invites_count%", Integer.toString(guild.invites.size()))
                .replace("%mods%", modsGuild(guild))
                .replace("%members%", membersGuild(guild))
                .replace("%totalmembers%", membersTGuild(guild))
                .replace("%invites%", invitesGuild(guild))
                .replace("%ispublic%", getIsPublicGuild(guild))
                .replace("%ismuted%", getIsMutedGuild(guild))
                .replace("%totalXP%", Integer.toString(guild.xp))
                .replace("%level%", Integer.toString(guild.lvl))
                .replace("%name%", guild.name)
                .replace("%xpneeded%", Integer.toString(guild.getNeededXp()))
                .replace("%xplevel%", Integer.toString(guild.xpUntilNextLevel()))
                .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(sender)).latestVersion)
                .replace("%name%", guild.name)
        ));
    }

    public static void sendStatUserMessage(Player player, CommandSender sender, String msg){
        Guild guild = GuildUtils.getGuild(player);

        sender.sendMessage(TextUtils.codedText(msg
                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(UUIDFetcher.getPlayer(sender)))
                .replace("%player%", PlayerUtils.getOffOnRegBungee(player))
                .replace("%totalxp%", Integer.toString(player.totalXP))
                .replace("%xp%", Integer.toString(player.getCurrentXP()))
                .replace("%level%", Integer.toString(player.lvl))
                .replace("%xpneeded%", Integer.toString(player.getNeededXp(player.lvl + 1)))
                .replace("%xplevel%", Integer.toString(player.xpUntilNextLevel()))
                .replace("%playtime%", TextUtils.truncate(Double.toString(player.getPlayHours()), 3))
                .replace("%version%", player.latestVersion)
                .replace("%points%", Integer.toString(player.points))
                .replace("%points_name%", PlayerUtils.pointsName)
                .replace("%uuid%", player.uuid)
                .replace("%tags%", statTags(player))
                .replace("%ip%", player.latestIP)
                .replace("%ips%", statIPs(player))
                .replace("%display%", player.displayName)
                .replace("%names%", statNames(player))
                .replace("%guild%", (guild != null ? guild.name : PlayerUtils.notSet))
                .replace("%guild_members%", (guild != null ? Integer.toString(guild.totalMembers.size()) : PlayerUtils.notSet))
                .replace("%guild_xp%", (guild != null ? Integer.toString(guild.xp) : PlayerUtils.notSet))
                .replace("%guild_lvl%", (guild != null ? Integer.toString(guild.lvl) : PlayerUtils.notSet))
                .replace("%guild_leader%", (guild != null ? Objects.requireNonNull(UUIDFetcher.getPlayerByUUID(guild.leaderUUID, true)).displayName : PlayerUtils.notSet))
                .replace("%guild_uuid%", (guild != null ? player.guild : PlayerUtils.notSet))
                .replace("%sspy%", (player.sspy ? PlayerUtils.sspyT : PlayerUtils.sspyF))
                .replace("%gspy%", (player.gspy ? PlayerUtils.gspyT : PlayerUtils.gspyF))
                .replace("%pspy%", (player.pspy ? PlayerUtils.pspyT : PlayerUtils.pspyF))
                .replace("%online%", (player.online ? PlayerUtils.onlineT : PlayerUtils.onlineF))
        ));
    }

    public static String statTags(Player player){
        StringBuilder stringBuilder = new StringBuilder();

        int i = 1;
        for (String tag : player.tagList) {
            if (i != player.tagList.size()) {
                stringBuilder.append(PlayerUtils.tagsNLast.replace("%value%", tag));
            } else {
                stringBuilder.append(PlayerUtils.tagsLast.replace("%value%", tag));
            }
        }

        return stringBuilder.toString();
    }

    public static String statIPs(Player player){
        StringBuilder stringBuilder = new StringBuilder();

        int i = 1;
        for (String ip : player.ipList) {
            if (i != player.ipList.size()) {
                stringBuilder.append(PlayerUtils.ipsNLast.replace("%value%", ip));
            } else {
                stringBuilder.append(PlayerUtils.ipsLast.replace("%value%", ip));
            }
        }

        return stringBuilder.toString();
    }

    public static String statNames(Player player){
        StringBuilder stringBuilder = new StringBuilder();

        int i = 1;
        for (String name : player.nameList) {
            if (i != player.nameList.size()) {
                stringBuilder.append(PlayerUtils.namesNLast.replace("%value%", name));
            } else {
                stringBuilder.append(PlayerUtils.namesLast.replace("%value%", name));
            }
        }

        return stringBuilder.toString();
    }

    public static void sendBUserMessage(CommandSender sender, String msg){
        if (sender instanceof ProxiedPlayer) {
            sender.sendMessage(TextUtils.codedText(msg
                    .replace("%sender%", PlayerUtils.getOffOnDisplayBungee((UUIDFetcher.getPlayer(sender))))
                    .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(sender)).latestVersion)
            ));
        } else {
            sender.sendMessage(TextUtils.codedText(msg
                    .replace("%sender%", sender.getName())
            ));
        }
    }

    public static void sendBUserAsMessage(CommandSender as, String msg){
        ServerInfo serverInfo = StreamLine.getInstance().getProxy().getPlayer(as.getName()).getServer().getInfo();

        Collection<ProxiedPlayer> players = serverInfo.getPlayers();

        if (as instanceof ProxiedPlayer) {
            for (ProxiedPlayer player : players) {
            player.sendMessage(TextUtils.codedText(msg
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(UUIDFetcher.getPlayer(as)))
                        .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(as)).latestVersion)
                ));
            }
        } else {
            for (ProxiedPlayer player : players) {
                player.sendMessage(TextUtils.codedText(msg
                        .replace("%sender%", as.getName())
                ));
            }
        }
    }

    public static void sendBBroadcast(CommandSender sender, String msg){
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(TextUtils.codedText(msg));
        }
    }

    public static void sendBCLHBroadcast(CommandSender sender, String msg, String hoverPrefix){
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(TextUtils.clhText(msg, hoverPrefix));
        }
    }

    public static boolean compareWithList(String toCompare, List<String> list) {
        for (String item : list) {
            if (toCompare.equals(item))
                return true;
        }
        return false;
    }

    public static void sendDiscordPingRoleMessage(String channelId, String roleId){
        Objects.requireNonNull(StreamLine.getJda().getTextChannelById(channelId)).sendMessage(Objects.requireNonNull(StreamLine.getJda().getRoleById(roleId)).getAsMention()).queue();
    }

    public static void logInfo(String msg){
        StreamLine.getInstance().getLogger().info(TextUtils.newLined(msg));
    }

    public static void logWarning(String msg){
        StreamLine.getInstance().getLogger().warning(TextUtils.newLined(msg));
    }

    public static void logSevere(String msg){
        StreamLine.getInstance().getLogger().severe(TextUtils.newLined(msg));
    }

    public static String mods(Party party){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (ProxiedPlayer m : party.moderators){
            if (i < party.moderators.size()){
                msg.append(MessageConfUtils.partiesModsNLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(m))))
                        .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(m)).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesModsLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(m))))
                        .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(m)).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String members(Party party){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (ProxiedPlayer m : party.members){
            if (i < party.members.size()){
                msg.append(MessageConfUtils.partiesMemsNLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(m))))
                        .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(m)).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesMemsLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(m))))
                        .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(m)).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String membersT(Party party){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (ProxiedPlayer m : party.totalMembers){
            if (i != party.totalMembers.size()){
                msg.append(MessageConfUtils.partiesTMemsNLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(m))))
                        .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(m)).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesTMemsLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(m))))
                        .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(m)).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String invites(Party party){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (ProxiedPlayer m : party.invites){
            if (i < party.invites.size()){
                msg.append(MessageConfUtils.partiesInvsNLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(m))))
                        .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(m)).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesInvsLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(PlayerUtils.getStat(m))))
                        .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(m)).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String getIsPublic(Party party){
        return party.isPublic ? MessageConfUtils.partiesIsPublicTrue : MessageConfUtils.partiesIsPublicFalse;
    }

    public static String getIsMuted(Party party){
        return party.isMuted ? MessageConfUtils.partiesIsMutedTrue : MessageConfUtils.partiesIsMutedFalse;
    }

    public static String modsGuild(Guild guild){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (String m : guild.modsByUUID){
            Player player;
            try {
                player = Objects.requireNonNull(UUIDFetcher.getPlayerByUUID(m, true));
            } catch (Exception e) {
                continue;
            }

            if (i != guild.modsByUUID.size()){
                msg.append(MessageConfUtils.guildsModsNLast
                        .replace("%user%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%version%", Objects.requireNonNull(player).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.guildsModsLast
                        .replace("%user%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%version%", Objects.requireNonNull(player).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String membersGuild(Guild guild){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (String m : guild.membersByUUID){
            Player player;
            try {
                player = Objects.requireNonNull(UUIDFetcher.getPlayerByUUID(m, true));
            } catch (Exception e) {
                continue;
            }

            if (i != guild.membersByUUID.size()){
                msg.append(MessageConfUtils.guildsMemsNLast
                        .replace("%user%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%version%", Objects.requireNonNull(player).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.guildsMemsLast
                        .replace("%user%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%version%", Objects.requireNonNull(player).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String membersTGuild(Guild guild){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (String m : guild.totalMembersByUUID){
            Player player;
            try {
                player = Objects.requireNonNull(UUIDFetcher.getPlayerByUUID(m, true));
            } catch (Exception e) {
                continue;
            }

            if (i != guild.totalMembersByUUID.size()){
                msg.append(MessageConfUtils.guildsTMemsNLast
                        .replace("%user%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%version%", Objects.requireNonNull(player).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.guildsTMemsLast
                        .replace("%user%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%version%", Objects.requireNonNull(player).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String invitesGuild(Guild guild){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (String m : guild.invitesByUUID){
            Player player;
            try {
                player = Objects.requireNonNull(UUIDFetcher.getPlayerByUUID(m, true));
            } catch (Exception e) {
                continue;
            }

            if (i != guild.invites.size()){
                msg.append(MessageConfUtils.guildsInvsNLast
                        .replace("%user%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%version%", Objects.requireNonNull(player).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.guildsInvsLast
                        .replace("%user%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%version%", player.latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String getIsPublicGuild(Guild guild){
        return guild.isPublic ? MessageConfUtils.guildsIsPublicTrue : MessageConfUtils.guildsIsPublicFalse;
    }

    public static String getIsMutedGuild(Guild guild){
        return guild.isMuted ? MessageConfUtils.guildsIsMutedTrue : MessageConfUtils.guildsIsMutedFalse;
    }
}
