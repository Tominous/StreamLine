package net.plasmere.streamline.utils;

import net.md_5.bungee.api.config.ServerInfo;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.events.EventsHandler;
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
import net.plasmere.streamline.objects.users.ConsolePlayer;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.objects.users.SavableUser;

import java.util.*;

public class MessagingUtils {
    private static final JDA jda = StreamLine.getJda();

    public static void sendStaffMessage(CommandSender sender, String from, String msg){
        sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.bungeeStaffChatMessage
                .replace("%user%", sender.getName())
                .replace("%from%", from)
                .replace("%message%", msg)
                .replace("%server%", PlayerUtils.getOrCreateSavableUser(sender).latestServer)
                .replace("%version%", PlayerUtils.getOrCreateSavableUser(sender).latestVersion)
        );
    }

    public static void sendPermissionedMessage(String toPermission, String message){
        Set<ProxiedPlayer> toPlayers = new HashSet<>();

        for (ProxiedPlayer player : PlayerUtils.getOnlinePPlayers()) {
            if (player.hasPermission(toPermission)) toPlayers.add(player);
        }

        for (ProxiedPlayer player : toPlayers) {
            player.sendMessage(TextUtils.codedText(message));
        }
    }

    public static void sendPermissionedMessageNonSelf(CommandSender sender, String toPermission, String message){
        Set<ProxiedPlayer> toPlayers = new HashSet<>();

        for (ProxiedPlayer player : PlayerUtils.getOnlinePPlayers()) {
            if (player.getName().equals(sender.getName())) continue;
            if (player.hasPermission(toPermission)) toPlayers.add(player);
        }

        for (ProxiedPlayer player : toPlayers) {
            player.sendMessage(TextUtils.codedText(message));
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
        SavableUser player = PlayerUtils.getOrCreateSavableUser(message.sender);

        message.to.sendMessage(TextUtils.codedText((message.title + message.transition + message.message)
                        .replace("%sender%", message.sender.getName())
                        .replace("%version%", player.latestVersion)
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
                                .replace("%version%", PlayerUtils.getOrCreatePlayerStat(player).latestVersion)
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

    public static void sendDiscordJoinLeaveMessagePlain(boolean isJoin, Player player){
        EmbedBuilder eb = new EmbedBuilder();

        try {
            if (isJoin) {
                Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelBJoins))
                        .sendMessage(
                                eb
                                        .setDescription(MessageConfUtils.discordOnline.replace("%player_default%", player.getName())
                                                .replace("%player%", PlayerUtils.getOffOnDisplayDiscord(player)))
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

    public static void sendDiscordJoinLeaveMessageIcon(boolean isJoin, Player player){
        EmbedBuilder eb = new EmbedBuilder();

        try {
            if (isJoin) {
                try {
                    Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelBJoins))
                            .sendMessage(
                                    eb
                                            .setDescription(MessageConfUtils.discordOnline.replace("%player_default%", player.getName())
                                                    .replace("%player%", PlayerUtils.getOffOnDisplayDiscord(player)))
                                            .setAuthor(MessageConfUtils.discordOnlineEmbed, jda.getSelfUser().getAvatarUrl(), FaceFetcher.getFaceAvatarURL(player))
                                            .build()
                            ).queue();
                } catch (NullPointerException e) {
                    StreamLine.getInstance().getLogger().severe("Discord bot is either not in the Discord server, or the bot cannot find " + ConfigUtils.textChannelBJoins);
                }
            } else {
                try {
                Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelBLeaves))
                        .sendMessage(
                                eb
                                        .setDescription(MessageConfUtils.discordOffline.replace("%player_default%", player.getName())
                                                .replace("%player%", PlayerUtils.getOffOnDisplayDiscord(player)))
                                        .setAuthor(MessageConfUtils.discordOfflineEmbed, jda.getSelfUser().getAvatarUrl(), FaceFetcher.getFaceAvatarURL(player))
                                        .build()
                        ).queue();

                } catch (NullPointerException e) {
                    StreamLine.getInstance().getLogger().severe("Discord bot is either not in the Discord server, or the bot cannot find " + ConfigUtils.textChannelBJoins);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendDiscordEBMessage(DiscordMessage message){
        EmbedBuilder eb = new EmbedBuilder();

        try {
            if (ConfigUtils.moduleUseMCAvatar) {
                if (message.sender instanceof ProxiedPlayer) {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessage(
                                    eb.setTitle(message.title.replace("%sender%", message.sender.getName()))
                                            .setDescription(message.message.replace("%sender%", message.sender.getName()))
                                            .setAuthor(message.sender.getName(), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName))
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
        EmbedBuilder eb = new EmbedBuilder();

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
        EmbedBuilder eb = new EmbedBuilder();

        context.getChannel().sendMessage(
                eb.setTitle(title)
                .setDescription(TextUtils.newLined(description))
                        .build()
        ).queue();
    }

    public static void sendBPUserMessage(Party party, CommandSender sender, CommandSender to, String msg){
        to.sendMessage(TextUtils.codedText(msg
                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSavableUser(sender)))
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
                .replace("%version%", PlayerUtils.getOrCreatePlayerStat((ProxiedPlayer) sender).latestVersion)
        ));
    }

    public static void sendBGUserMessage(Guild guild, CommandSender sender, CommandSender to, String msg){
        to.sendMessage(TextUtils.codedText(msg
                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSavableUser(sender)))
                .replace("%leader%", PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID).getName())
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
                .replace("%total_xp%", Integer.toString(guild.totalXP))
                .replace("%current_xp%", Integer.toString(guild.currentXP))
                .replace("%level%", Integer.toString(guild.lvl))
                .replace("%name%", guild.name)
                .replace("%xpneeded%", Integer.toString(guild.getNeededXp(guild.lvl + 1)))
                .replace("%xplevel%", Integer.toString(guild.xpUntilNextLevel()))
                .replace("%version%", PlayerUtils.getOrCreateSavableUser(sender).latestVersion)
                .replace("%name%", guild.name)
                .replace("%length%", String.valueOf(guild.name.length()))
                .replace("%max_length%", String.valueOf(ConfigUtils.guildMaxLength))
                .replace("%codes%", (ConfigUtils.guildIncludeColors ? GuildUtils.withCodes : GuildUtils.withoutCodes))
        ));
    }

    public static void sendStatUserMessage(SavableUser user, CommandSender sender, String msg){
        Guild guild = GuildUtils.getGuild(user);

        if (user instanceof ConsolePlayer) {
            ConsolePlayer player = PlayerUtils.getConsoleStat();

            if (player == null) {
                sendBUserMessage(sender, MessageConfUtils.noPlayer);
                return;
            }

            sender.sendMessage(TextUtils.codedText(msg
                    .replace("%sender%", sender.getName())
                    .replace("%player%", user.displayName)
                    .replace("%version%", player.latestVersion)
                    .replace("%points%", Integer.toString(player.points))
                    .replace("%points_name%", PlayerUtils.pointsName)
                    .replace("%tags%", statTags(player))
                    .replace("%uuid%", player.uuid)
                    .replace("%display%", player.displayName)
                    .replace("%guild%", (guild != null ? guild.name : PlayerUtils.notSet))
                    .replace("%guild_members%", (guild != null ? Integer.toString(guild.totalMembers.size()) : PlayerUtils.notSet))
                    .replace("%guild_xp_total%", (guild != null ? Integer.toString(guild.totalXP) : PlayerUtils.notSet))
                    .replace("%guild_xp_current%", (guild != null ? Integer.toString(guild.currentXP) : PlayerUtils.notSet))
                    .replace("%guild_lvl%", (guild != null ? Integer.toString(guild.lvl) : PlayerUtils.notSet))
                    .replace("%guild_leader%", (guild != null ? PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID).displayName : PlayerUtils.notSet))
                    .replace("%guild_uuid%", (guild != null ? player.guild : PlayerUtils.notSet))
                    .replace("%version%", player.latestVersion)
            ));

            return;
        }

        if (user instanceof Player) {
            Player player = PlayerUtils.getOrGetPlayerStatByUUID(user.uuid);

            if (player == null) {
                sendBUserMessage(sender, MessageConfUtils.noPlayer);
                return;
            }

            sender.sendMessage(TextUtils.codedText(msg
                    .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSavableUser(sender)))
                    .replace("%player%", PlayerUtils.getOffOnRegBungee(player))
                    .replace("%total_xp%", Integer.toString(player.totalXP))
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
                    .replace("%guild_xp_total%", (guild != null ? Integer.toString(guild.totalXP) : PlayerUtils.notSet))
                    .replace("%guild_xp_current%", (guild != null ? Integer.toString(guild.currentXP) : PlayerUtils.notSet))
                    .replace("%guild_lvl%", (guild != null ? Integer.toString(guild.lvl) : PlayerUtils.notSet))
                    .replace("%guild_leader%", (guild != null ? PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID).displayName : PlayerUtils.notSet))
                    .replace("%guild_uuid%", (guild != null ? player.guild : PlayerUtils.notSet))
                    .replace("%sspy%", (player.sspy ? PlayerUtils.sspyT : PlayerUtils.sspyF))
                    .replace("%gspy%", (player.gspy ? PlayerUtils.gspyT : PlayerUtils.gspyF))
                    .replace("%pspy%", (player.pspy ? PlayerUtils.pspyT : PlayerUtils.pspyF))
                    .replace("%online%", (player.online ? PlayerUtils.onlineT : PlayerUtils.onlineF))
            ));
        }
    }

    public static String statTags(SavableUser player){
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
                    .replace("%sender%", PlayerUtils.getOffOnDisplayBungee((PlayerUtils.getOrCreateSavableUser(sender))))
                    .replace("%version%", PlayerUtils.getOrCreatePlayerStat((ProxiedPlayer) sender).latestVersion)
            ));
        } else {
            sender.sendMessage(TextUtils.codedText(msg
                    .replace("%sender%", sender.getName())
            ));
        }
    }

    public static void sendBUserMessage(Player sender, String msg){
        if (sender instanceof ProxiedPlayer) {
            sender.sendMessage(TextUtils.codedText(msg
                    .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(sender))
                    .replace("%version%", Objects.requireNonNull(sender).latestVersion)
            ));
        }
    }

    public static void sendBMessagenging(CommandSender sendTo, SavableUser from, SavableUser to, String playerMessage, String msg) {
        sendTo.sendMessage(TextUtils.codedText(msg
                .replace("%from%", from.displayName)
                .replace("%from_normal%", from.latestName)
                .replace("%from_server%", (from instanceof Player ? ((Player) from).player.getServer().getInfo().getName() : ConfigUtils.consoleServer))
                .replace("%to%", to.displayName)
                .replace("%to_normal%", to.latestName)
                .replace("%to_server%", (to instanceof Player ? ((Player) to).player.getServer().getInfo().getName() : ConfigUtils.consoleServer))
                .replace("%message%", playerMessage)
        ));
    }

    public static void sendBUserAsMessage(CommandSender as, String msg){
        ServerInfo serverInfo = StreamLine.getInstance().getProxy().getPlayer(as.getName()).getServer().getInfo();

        Collection<ProxiedPlayer> players = serverInfo.getPlayers();

        if (as instanceof ProxiedPlayer) {
            for (ProxiedPlayer player : players) {
                player.sendMessage(TextUtils.codedText(msg
                        .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStat(as)))
                        .replace("%version%", PlayerUtils.getOrCreatePlayerStat((ProxiedPlayer) as).latestVersion)
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
        for (Player m : party.moderators){
            if (i < party.moderators.size()){
                msg.append(MessageConfUtils.partiesModsNLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(m)))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesModsLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(m)))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String members(Party party){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (Player m : party.members){
            if (i < party.members.size()){
                msg.append(MessageConfUtils.partiesMemsNLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(m)))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesMemsLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(m)))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String membersT(Party party){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (Player m : party.totalMembers){
            if (i != party.totalMembers.size()){
                msg.append(MessageConfUtils.partiesTMemsNLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(m)))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesTMemsLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(m)))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String invites(Party party){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (Player m : party.invites){
            if (i < party.invites.size()){
                msg.append(MessageConfUtils.partiesInvsNLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(m)))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesInvsLast
                        .replace("%user%", PlayerUtils.getOffOnDisplayBungee(Objects.requireNonNull(m)))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
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
            SavableUser player;
            try {
                player = PlayerUtils.getOrCreateSUByUUID(m);
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
            SavableUser player;
            try {
                player = PlayerUtils.getOrCreateSUByUUID(m);
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
            SavableUser player;
            try {
                player = PlayerUtils.getOrCreateSUByUUID(m);
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
            SavableUser player;
            try {
                player = PlayerUtils.getOrCreateSUByUUID(m);
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
                        .replace("%version%", Objects.requireNonNull(player).latestVersion)
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

    public static void sendInfo(CommandSender sender) {
        sender.sendMessage(TextUtils.codedText(MessageConfUtils.info
                .replace("%name%", StreamLine.getInstance().getDescription().getName())
                .replace("%version%", StreamLine.getInstance().getDescription().getVersion())
                .replace("%author%", StreamLine.getInstance().getDescription().getAuthor())
                .replace("%num_commands%", String.valueOf(PluginUtils.commandsAmount))
                .replace("%num_listeners%", String.valueOf(PluginUtils.listenerAmount))
                .replace("%num_events%", String.valueOf(EventsHandler.getEvents().size()))
                .replace("%discord%", "https://discord.gg/tny494zXfn")
        ));
    }
}
