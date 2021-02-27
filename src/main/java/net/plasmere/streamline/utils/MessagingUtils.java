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
    private static final StreamLine plugin = StreamLine.getInstance();

    public static void sendStaffMessage(CommandSender sender, String from, String msg, StreamLine plugin){
        Collection<ProxiedPlayer> staff = plugin.getProxy().getPlayers();
        Set<ProxiedPlayer> staffs = new HashSet<>(staff);

        for (ProxiedPlayer player : staff){
            try {
                if (! player.hasPermission("streamline.staff")) {
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

    public static void sendBungeeMessage(BungeeMassMessage message){
        Collection<ProxiedPlayer> staff = plugin.getProxy().getPlayers();
        Set<ProxiedPlayer> people = new HashSet<>(staff);

        for (ProxiedPlayer player : people){
            try {
                if (! player.hasPermission(message.permission)) {
                    people.remove(player);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        for (ProxiedPlayer player : people) {
            sendBungeeMessage(new BungeeMessage(message.sender, player, message.title, message.transition, message.message));
        }
    }

    public static void sendBungeeMessage(BungeeMessage message){
        message.to.sendMessage(TextUtils.codedText((message.title + message.transition + message.message)
                        .replace("%sender%", message.sender.getName())
                        .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(message.sender.getName())).latestVersion)
                )
        );
    }

    public static void sendStaffMessageSC(String sender, String from, String msg, StreamLine plugin){
        Collection<ProxiedPlayer> staff = plugin.getProxy().getPlayers();
        Set<ProxiedPlayer> staffs = new HashSet<>(staff);

        for (ProxiedPlayer player : staff){
            try {
                if (! player.hasPermission("streamline.staff")) {
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

    public static void sendStaffMessageReport(String sender, boolean fromBungee, String report, StreamLine plugin){
        Collection<ProxiedPlayer> staff = plugin.getProxy().getPlayers();
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
            sender = UUIDFetcher.getPPlayer(((Player) sender).uuid);
        }

        if (to instanceof Player){
            to = UUIDFetcher.getPPlayer(((Player) to).uuid);
        }

        if (sender == null || to == null) {
            StreamLine.getInstance().getLogger().severe("ERROR CASTING! REPORT \"sendBGUserMessage error\" TO PLUGIN AUTHOR!");
            return;
        }

        to.sendMessage(TextUtils.codedText(msg
                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(UUIDFetcher.getPlayer(sender)))
                .replace("%leader%", Objects.requireNonNull(UUIDFetcher.getPlayer(guild.leaderUUID)).getName())
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
                .replace("%xp%", Integer.toString(guild.xp))
                .replace("%level%", Integer.toString(guild.lvl))
                .replace("%name%", guild.name)
                .replace("%xpneeded%", Integer.toString(guild.getNeededXp()))
                .replace("%xplevel%", Integer.toString(guild.xpUntilNextLevel()))
                .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(sender)).latestVersion)
        ));
    }

    public static void sendStatUserMessage(Player player, CommandSender sender, String msg){
        sender.sendMessage(TextUtils.codedText(msg
                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(UUIDFetcher.getPlayer(sender)))
                .replace("%player%", PlayerUtils.getOffOnRegBungee(player))
                .replace("%xp%", Integer.toString(player.xp))
                .replace("%level%", Integer.toString(player.lvl))
                .replace("%xpneeded%", Integer.toString(player.getNeededXp()))
                .replace("%xplevel%", Integer.toString(player.xpUntilNextLevel()))
                .replace("%playtime%", TextUtils.truncate(Double.toString(player.getPlayHours()), 3))
                .replace("%version%", Objects.requireNonNull(PlayerUtils.getStat(sender)).latestVersion)
        ));
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
        for (UUID m : guild.modsByUUID){
            Player player;
            try {
                player = Objects.requireNonNull(UUIDFetcher.getPlayer(m));
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
        for (UUID m : guild.membersByUUID){
            Player player;
            try {
                player = Objects.requireNonNull(UUIDFetcher.getPlayer(m));
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
        for (UUID m : guild.totalMembersByUUID){
            Player player;
            try {
                player = Objects.requireNonNull(UUIDFetcher.getPlayer(m));
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
        for (UUID m : guild.invitesByUUID){
            Player player;
            try {
                player = Objects.requireNonNull(UUIDFetcher.getPlayer(m));
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
