package net.plasmere.streamline.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.DiscordBotConfUtils;
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
import net.plasmere.streamline.objects.savable.users.ConsolePlayer;
import net.plasmere.streamline.objects.savable.users.Player;
import net.plasmere.streamline.objects.savable.users.SavableUser;

import java.util.*;

public class MessagingUtils {
    public static HashMap<ProxiedPlayer, String> serveredUsernames = new HashMap<>();

    public static void sendStaffMessage(CommandSender sender, String from, String msg){
        List<SavableUser> toExclude = new ArrayList<>();

        for (SavableUser user : PlayerUtils.getJustStaffOnline()) {
            if (! user.scvs || ! user.viewsc) toExclude.add(user);
        }

        sendPermissionedMessageExcludePlayers(toExclude, ConfigUtils.staffPerm, MessageConfUtils.bungeeStaffChatMessage()
                .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrGetSavableUser(sender)))
                .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrGetSavableUser(sender)))
                .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrGetSavableUser(sender)))
                .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrGetSavableUser(sender)))
                .replace("%from_server%", from)
                .replace("%message%", msg)
                .replace("%server%", PlayerUtils.getOrCreateSavableUser(sender).findServer())
                .replace("%version%", PlayerUtils.getOrCreateSavableUser(sender).latestVersion)
        );
    }

    public static void sendStaffMessageExcludeSelf(CommandSender sender, String from, String msg){
        sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.bungeeStaffChatMessage()
                .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrGetSavableUser(sender)))
                .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrGetSavableUser(sender)))
                .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrGetSavableUser(sender)))
                .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrGetSavableUser(sender)))
                .replace("%from_server%", from)
                .replace("%message%", msg)
                .replace("%server%", PlayerUtils.getOrCreateSavableUser(sender).findServer())
                .replace("%version%", PlayerUtils.getOrCreateSavableUser(sender).latestVersion)
        );
    }

    public static void sendPermissionedMessageExcludePlayers(List<SavableUser> toExclude, String toPermission, String message){
        List<SavableUser> toUsers = new ArrayList<>();
        List<String> excludedUUIDs = new ArrayList<>();

        for (SavableUser user : toExclude) {
            excludedUUIDs.add(user.uuid);
        }

        for (SavableUser user : PlayerUtils.getStatsOnline()) {
            if (excludedUUIDs.contains(user.uuid)) continue;
            if (user.hasPermission(toPermission)) toUsers.add(user);
        }

        for (SavableUser player : toUsers) {
            player.sendMessage(TextUtils.codedText(message));
        }
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

        message.to.sendMessage(TextUtils.codedText(TextUtils.replaceAllSender((message.title + message.transition + message.message)
                        .replace("%sender_display%", message.sender.getName()), message.sender)
                        .replace("%version%", player.latestVersion)
                )
        );
    }

    public static void sendServerMessageFromUser(ProxiedPlayer player, Server server, String format, String message) {
        for (ProxiedPlayer p : PlayerUtils.getServeredPPlayers(server.getInfo().getName())) {
            p.sendMessage(TextUtils.codedText(format
                    .replace("%sender_servered%", getPlayerDisplayName(player))
                    .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrGetSavableUser(player)))
                    .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrGetSavableUser(player)))
                    .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrGetSavableUser(player)))
                    .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrGetSavableUser(player)))
                    .replace("%message%", message)
                    .replace("%server%", server.getInfo().getName())
            ));
        }
    }

    public static void sendGlobalMessageFromUser(ProxiedPlayer player, Server server, String format, String message) {
        for (ProxiedPlayer p : PlayerUtils.getOnlinePPlayers()) {
            p.sendMessage(TextUtils.codedText(format
                    .replace("%sender_servered%", getPlayerDisplayName(player))
                    .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrGetSavableUser(player)))
                    .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrGetSavableUser(player)))
                    .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrGetSavableUser(player)))
                    .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrGetSavableUser(player)))
                    .replace("%message%", message)
                    .replace("%server%", server.getInfo().getName())
            ));
        }
    }

    public static void sendServerMessageFromDiscord(String nameUsed, ServerInfo server, String format, String message) {
        for (ProxiedPlayer p : PlayerUtils.getServeredPPlayers(server.getName())) {
            p.sendMessage(TextUtils.codedText(format
                    .replace("%sender_servered%", nameUsed)
                    .replace("%sender_display%", nameUsed)
                    .replace("%sender_normal%", nameUsed)
                    .replace("%sender_absolute%", nameUsed)
                    .replace("%sender_formatted%", nameUsed)
                    .replace("%message%", message)
                    .replace("%server%", server.getName())
            ));
        }
    }

    public static void sendGlobalMessageFromDiscord(String nameUsed, String format, String message) {
        for (ProxiedPlayer p : PlayerUtils.getOnlinePPlayers()) {
            p.sendMessage(TextUtils.codedText(format
                    .replace("%sender_servered%", nameUsed)
                    .replace("%sender_display%", nameUsed)
                    .replace("%sender_normal%", nameUsed)
                    .replace("%sender_absolute%", nameUsed)
                    .replace("%sender_formatted%", nameUsed)
                    .replace("%message%", message)
            ));
        }
    }

    public static void sendServerMessageFromDiscord(SavableUser user, ServerInfo server, String format, String message) {
        for (ProxiedPlayer p : PlayerUtils.getServeredPPlayers(server.getName())) {
            p.sendMessage(TextUtils.codedText(format
                    .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(user))
                    .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(user))
                    .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(user))
                    .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(user))
                    .replace("%message%", message)
                    .replace("%server%", server.getName())
            ));
        }
    }

    public static void sendGlobalMessageFromDiscord(SavableUser user, String format, String message) {
        for (ProxiedPlayer p : PlayerUtils.getOnlinePPlayers()) {
            p.sendMessage(TextUtils.codedText(format
                    .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(user))
                    .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(user))
                    .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(user))
                    .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(user))
                    .replace("%message%", message)
            ));
        }
    }

    public static void sendMessageFromUserToConsole(ProxiedPlayer player, Server server, String format, String message) {
        PlayerUtils.getConsoleStat().sendMessage(TextUtils.codedText(format
                .replace("%sender_servered%", getPlayerDisplayName(player))
                .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrGetSavableUser(player)))
                .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrGetSavableUser(player)))
                .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrGetSavableUser(player)))
                .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrGetSavableUser(player)))
                .replace("%message%", message)
                .replace("%server%", server.getInfo().getName())
        ));
    }

    public static String getPlayerDisplayName(ProxiedPlayer player) {
        sendDisplayPluginMessageRequest(player);
        String string = serveredUsernames.get(player);

        return (string == null) ? PlayerUtils.getOrCreatePlayerStat(player).displayName : string;
    }

    public static void sendDisplayPluginMessageRequest(ProxiedPlayer player) {
        if (PlayerUtils.getServeredPPlayers(player.getServer().getInfo().getName()).size() <= 0) return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("request.displayname"); // the channel could be whatever you want

        // we send the data to the server
        // using ServerInfo the packet is being queued if there are no players in the server
        // using only the server to send data the packet will be lost if no players are in it
        player.getServer().getInfo().sendData(StreamLine.customChannel, out.toByteArray());
    }

    public static void sendTeleportPluginMessageRequest(ProxiedPlayer sender, ProxiedPlayer to) {
        if (PlayerUtils.getServeredPPlayers(sender.getServer().getInfo().getName()).size() <= 0) return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("teleport"); // the channel could be whatever you want
        out.writeUTF(sender.getUniqueId().toString()); // this data could be whatever you want // THIS IS THE SENDER PLAYER
        out.writeUTF(to.getUniqueId().toString()); // this data could be whatever you want // THIS IS THE TO PLAYER

        // we send the data to the server
        // using ServerInfo the packet is being queued if there are no players in the server
        // using only the server to send data the packet will be lost if no players are in it
        sender.getServer().getInfo().sendData(StreamLine.customChannel, out.toByteArray());
    }

    public static void sendTagPingPluginMessageRequest(ProxiedPlayer toPing) {
        if (PlayerUtils.getServeredPPlayers(toPing.getServer().getInfo().getName()).size() <= 0) return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("tag-ping"); // the channel could be whatever you want
        out.writeUTF(toPing.getUniqueId().toString()); // this data could be whatever you want // THIS IS THE SENDER PLAYER

        // we send the data to the server
        // using ServerInfo the packet is being queued if there are no players in the server
        // using only the server to send data the packet will be lost if no players are in it
        toPing.getServer().getInfo().sendData(StreamLine.customChannel, out.toByteArray());
    }

    public static void sendGuildConfigPluginMessage(ProxiedPlayer to, Guild guild) {
        if (PlayerUtils.getServeredPPlayers(to.getServer().getInfo().getName()).size() <= 0) return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("config.guild");
        out.writeUTF(guild.leaderUUID);

        try {
            Scanner reader = new Scanner(guild.file);

            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                while (data.startsWith("#")) {
                    data = reader.nextLine();
                }
                out.writeUTF(data);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        to.getServer().getInfo().sendData(StreamLine.customChannel, out.toByteArray());
    }

    public static void sendStaffMessageFromDiscord(String sender, String from, String msg){
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
            player.sendMessage(TextUtils.codedText(MessageConfUtils.bungeeStaffChatMessage()
                            .replace("%player_display%", sender)
                            .replace("%from_display%", from)
                            .replace("%message%", msg)
                            .replace("%server%", ConfigUtils.moduleStaffChatServer)
                            .replace("%version%", "JDA")
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
                player.sendMessage(TextUtils.codedText(MessageConfUtils.bToBReportMessage()
                                .replace("%reporter%", sender)
                                .replace("%report%", report)
                                .replace("%version%", PlayerUtils.getOrCreatePlayerStat(player).latestVersion)
                        )
                );
            else
                player.sendMessage(TextUtils.codedText(MessageConfUtils.dToBReportMessage()
                                .replace("%reporter%", sender)
                                .replace("%report%", report)
                        )
                );
        }
    }

    public static void sendDiscordJoinLeaveMessagePlain(boolean isJoin, Player player){
        if (! ConfigUtils.moduleDEnabled) {
            return;
        }

        JDA jda = StreamLine.getJda();
        EmbedBuilder eb = new EmbedBuilder();

        try {
            if (isJoin) {
                Objects.requireNonNull(jda.getTextChannelById(DiscordBotConfUtils.textChannelBJoins))
                        .sendMessageEmbeds(
                                eb
                                        .setDescription(MessageConfUtils.discordOnline()
                                                .replace("%player_absolute%", player.getName())
                                                .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                                .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                                .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player)))
                                        .setAuthor(MessageConfUtils.discordOnlineEmbed(), jda.getSelfUser().getAvatarUrl(), jda.getSelfUser().getAvatarUrl())
                                        .build()
                        ).queue();
            } else {
                Objects.requireNonNull(jda.getTextChannelById(DiscordBotConfUtils.textChannelBLeaves))
                        .sendMessageEmbeds(
                                eb
                                        .setDescription(MessageConfUtils.discordOffline()
                                                .replace("%player_absolute%", player.getName())
                                                .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                                .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                                .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player)))
                                        .setAuthor(MessageConfUtils.discordOfflineEmbed(), jda.getSelfUser().getAvatarUrl(), jda.getSelfUser().getAvatarUrl())
                                        .build()
                        ).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendDiscordJoinLeaveMessageIcon(boolean isJoin, Player player){
        if (! ConfigUtils.moduleDEnabled) {
            return;
        }

        JDA jda = StreamLine.getJda();
        EmbedBuilder eb = new EmbedBuilder();

        try {
            if (isJoin) {
                try {
                    Objects.requireNonNull(jda.getTextChannelById(DiscordBotConfUtils.textChannelBJoins))
                            .sendMessageEmbeds(
                                    eb
                                            .setDescription(MessageConfUtils.discordOnline()
                                                    .replace("%player_absolute%", player.getName())
                                                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                                    .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player)))
                                            .setAuthor(MessageConfUtils.discordOnlineEmbed(), jda.getSelfUser().getAvatarUrl(), FaceFetcher.getFaceAvatarURL(player))
                                            .build()
                            ).queue();
                } catch (NullPointerException e) {
                    MessagingUtils.logSevere("Discord bot is either not in the Discord server, or the bot cannot find " + DiscordBotConfUtils.textChannelBJoins);
                }
            } else {
                try {
                Objects.requireNonNull(jda.getTextChannelById(DiscordBotConfUtils.textChannelBLeaves))
                        .sendMessageEmbeds(
                                eb
                                        .setDescription(MessageConfUtils.discordOffline()
                                                .replace("%player_absolute%", player.getName())
                                                .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                                                .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                                                .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player)))
                                        .setAuthor(MessageConfUtils.discordOfflineEmbed(), jda.getSelfUser().getAvatarUrl(), FaceFetcher.getFaceAvatarURL(player))
                                        .build()
                        ).queue();

                } catch (NullPointerException e) {
                    MessagingUtils.logSevere("Discord bot is either not in the Discord server, or the bot cannot find " + DiscordBotConfUtils.textChannelBJoins);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendDiscordEBMessage(DiscordMessage message){
        if (! ConfigUtils.moduleDEnabled) {
            return;
        }

        JDA jda = StreamLine.getJda();
        EmbedBuilder eb = new EmbedBuilder();

        try {
            if (ConfigUtils.moduleAvatarUse) {
                if (message.sender instanceof ProxiedPlayer) {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessageEmbeds(
                                    eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                            .setDescription(TextUtils.replaceAllSender(message.message, message.sender))
                                            .setAuthor(message.sender.getName(), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName))
                                            .build()
                            ).queue();
                } else {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessageEmbeds(
                                    eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                            .setDescription(TextUtils.replaceAllSender(message.message, message.sender))
                                            .setAuthor("CONSOLE", jda.getSelfUser().getAvatarUrl() , jda.getSelfUser().getAvatarUrl())
                                            .build()
                            ).queue();
                }
            } else {
                Objects.requireNonNull(jda.getTextChannelById(message.channel))
                        .sendMessageEmbeds(
                                eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                        .setDescription(TextUtils.replaceAllSender(message.message, message.sender))
                                        .build()
                        ).queue();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendDiscordEBMessage(DiscordMessage message, boolean useAvatar){
        if (! ConfigUtils.moduleDEnabled) {
            return;
        }

        JDA jda = StreamLine.getJda();
        EmbedBuilder eb = new EmbedBuilder();

        try {
            if (useAvatar) {
                if (message.sender instanceof ProxiedPlayer) {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessageEmbeds(
                                    eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                            .setDescription(TextUtils.replaceAllSender(message.message, message.sender))
                                            .setAuthor(message.sender.getName(), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName))
                                            .build()
                            ).queue();
                } else {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessageEmbeds(
                                    eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                            .setDescription(TextUtils.replaceAllSender(message.message, message.sender))
                                            .setAuthor("CONSOLE", jda.getSelfUser().getAvatarUrl() , jda.getSelfUser().getAvatarUrl())
                                            .build()
                            ).queue();
                }
            } else {
                Objects.requireNonNull(jda.getTextChannelById(message.channel))
                        .sendMessageEmbeds(
                                eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                        .setDescription(TextUtils.replaceAllSender(message.message, message.sender))
                                        .build()
                        ).queue();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendDiscordEBMessage(JDA jda, DiscordMessage message){
        EmbedBuilder eb = new EmbedBuilder();

        try {
            if (ConfigUtils.moduleAvatarUse) {
                if (message.sender instanceof ProxiedPlayer) {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessageEmbeds(
                                    eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                            .setDescription(TextUtils.replaceAllSender(message.message, message.sender))
                                            .setAuthor(message.sender.getName(), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName))
                                            .build()
                            ).queue();
                } else {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessageEmbeds(
                                    eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                            .setDescription(TextUtils.replaceAllSender(message.message, message.sender))
                                            .setAuthor("CONSOLE", jda.getSelfUser().getAvatarUrl() , jda.getSelfUser().getAvatarUrl())
                                            .build()
                            ).queue();
                }
            } else {
                Objects.requireNonNull(jda.getTextChannelById(message.channel))
                        .sendMessageEmbeds(
                                eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                        .setDescription(TextUtils.replaceAllSender(message.message, message.sender))
                                        .build()
                        ).queue();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendDiscordReportMessage(String sender, boolean fromBungee, String report){
        if (! ConfigUtils.moduleDEnabled) {
            return;
        }

        JDA jda = StreamLine.getJda();
        EmbedBuilder eb = new EmbedBuilder();

        try {
            String replace = MessageConfUtils.dToDReportMessage()
                    .replace("%reporter%", sender)
                    .replace("%report%", report);

            String replace1 = MessageConfUtils.bToDReportMessage()
                    .replace("%reporter%", sender)
                    .replace("%report%", report);

            if (ConfigUtils.moduleAvatarUse) {
                if (fromBungee)
                    Objects.requireNonNull(jda.getTextChannelById(DiscordBotConfUtils.textChannelReports)).sendMessageEmbeds(
                            eb.setTitle(MessageConfUtils.reportEmbedTitle())
                                    .setDescription(TextUtils.newLined(
                                            replace1
                                            )
                                    ).setAuthor(sender, FaceFetcher.getFaceAvatarURL(sender), FaceFetcher.getFaceAvatarURL(sender)).build()
                    ).queue();
                else
                    Objects.requireNonNull(jda.getTextChannelById(DiscordBotConfUtils.textChannelReports)).sendMessageEmbeds(
                            eb.setTitle(MessageConfUtils.reportEmbedTitle())
                                    .setDescription(TextUtils.newLined(
                                            replace
                                            )
                                    ).setAuthor(sender, FaceFetcher.getFaceAvatarURL(sender), FaceFetcher.getFaceAvatarURL(sender)).build()
                    ).queue();
            } else {
                if (fromBungee)
                    Objects.requireNonNull(jda.getTextChannelById(DiscordBotConfUtils.textChannelReports)).sendMessageEmbeds(
                            eb.setTitle(MessageConfUtils.reportEmbedTitle())
                                    .setDescription(TextUtils.newLined(
                                            replace1
                                            )
                                    ).build()
                    ).queue();
                else
                    Objects.requireNonNull(jda.getTextChannelById(DiscordBotConfUtils.textChannelReports)).sendMessageEmbeds(
                            eb.setTitle(MessageConfUtils.reportEmbedTitle())
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

        context.getChannel().sendMessageEmbeds(
                eb.setTitle(title)
                .setDescription(TextUtils.newLined(description))
                        .build()
        ).queue();
    }

    public static void sendBPUserMessage(Party party, CommandSender sender, CommandSender to, String msg){
        to.sendMessage(TextUtils.codedText(msg
                .replace("%size%", Integer.toString(party.getSize()))
                .replace("%max%", Integer.toString(party.maxSize))
                .replace("%maxmax%", party.leader == null ? MessageConfUtils.nullB() : Integer.toString(party.getMaxSize(party.leader)))
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
                .replace("%version%", PlayerUtils.getOrCreateSavableUser(sender).latestVersion)
                .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSavableUser(sender)))
                .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreateSavableUser(sender)))
                .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrCreateSavableUser(sender)))
                .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrCreateSavableUser(sender)))
                .replace("%leader_display%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnDisplayBungee(party.leader))
                .replace("%leader_normal%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnRegBungee(party.leader))
                .replace("%leader_absolute%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getAbsoluteBungee(party.leader))
                .replace("%leader_formatted%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getJustDisplayBungee(party.leader))
                .replace("%size%", Integer.toString(party.getSize()))
        ));
    }

    public static void sendBPUserMessageFromDiscord(Party party, String nameUsed, CommandSender to, String msg){
        to.sendMessage(TextUtils.codedText(msg
                .replace("%size%", Integer.toString(party.getSize()))
                .replace("%max%", Integer.toString(party.maxSize))
                .replace("%maxmax%", party.leader == null ? MessageConfUtils.nullB() : Integer.toString(party.getMaxSize(party.leader)))
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
                .replace("%sender_display%", nameUsed)
                .replace("%sender_normal%", nameUsed)
                .replace("%sender_absolute%", nameUsed)
                .replace("%sender_formatted%", nameUsed)
                .replace("%leader_display%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnDisplayBungee(party.leader))
                .replace("%leader_normal%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnRegBungee(party.leader))
                .replace("%leader_absolute%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getAbsoluteBungee(party.leader))
                .replace("%leader_formatted%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getJustDisplayBungee(party.leader))
                .replace("%size%", Integer.toString(party.getSize()))
        ));
    }

    public static void sendBPUserMessageFromDiscord(Party party, SavableUser sender, CommandSender to, String msg){
        to.sendMessage(TextUtils.codedText(msg
                .replace("%size%", Integer.toString(party.getSize()))
                .replace("%max%", Integer.toString(party.maxSize))
                .replace("%maxmax%", party.leader == null ? MessageConfUtils.nullB() : Integer.toString(party.getMaxSize(party.leader)))
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
                .replace("%version%", sender.latestVersion)
                .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(sender))
                .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(sender))
                .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(sender))
                .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(sender))
                .replace("%leader_display%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnDisplayBungee(party.leader))
                .replace("%leader_normal%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnRegBungee(party.leader))
                .replace("%leader_absolute%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getAbsoluteBungee(party.leader))
                .replace("%leader_formatted%", party.leader == null ? MessageConfUtils.nullB() : PlayerUtils.getJustDisplayBungee(party.leader))
                .replace("%size%", Integer.toString(party.getSize()))
        ));
    }

    public static void sendDiscordPEBMessage(Party party, DiscordMessage message){
        if (! ConfigUtils.moduleDEnabled) {
            return;
        }

        JDA jda = StreamLine.getJda();
        EmbedBuilder eb = new EmbedBuilder();

        String msg = message.message
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
                .replace("%version%", PlayerUtils.getOrCreateSavableUser(message.sender).latestVersion)
                .replace("%version%", PlayerUtils.getOrCreatePlayerStat((ProxiedPlayer) message.sender).latestVersion)
                .replace("%sender_display%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSavableUser(message.sender)))
                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSavableUser(message.sender)))
                .replace("%sender_absolute%", PlayerUtils.getAbsoluteDiscord(PlayerUtils.getOrCreateSavableUser(message.sender)))
                .replace("%sender_formatted%", PlayerUtils.getJustDisplayDiscord(PlayerUtils.getOrCreateSavableUser(message.sender)))
                .replace("%leader_display%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(party.leaderUUID)))
                .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(party.leaderUUID)))
                .replace("%leader_absolute%", PlayerUtils.getAbsoluteDiscord(PlayerUtils.getOrCreateSavableUserByUUID(party.leaderUUID)))
                .replace("%leader_formatted%", PlayerUtils.getJustDisplayDiscord(PlayerUtils.getOrCreateSavableUserByUUID(party.leaderUUID)))
                .replace("%size%", Integer.toString(party.getSize()));

        try {
            if (ConfigUtils.moduleAvatarUse) {
                if (message.sender instanceof ProxiedPlayer) {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessageEmbeds(
                                    eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                            .setDescription(TextUtils.replaceAllSender(msg, message.sender))
                                            .setAuthor(message.sender.getName(), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName))
                                            .build()
                            ).queue();
                } else {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessageEmbeds(
                                    eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                            .setDescription(TextUtils.replaceAllSender(msg, message.sender))
                                            .setAuthor("CONSOLE", jda.getSelfUser().getAvatarUrl() , jda.getSelfUser().getAvatarUrl())
                                            .build()
                            ).queue();
                }
            } else {
                Objects.requireNonNull(jda.getTextChannelById(message.channel))
                        .sendMessageEmbeds(
                                eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                        .setDescription(TextUtils.replaceAllSender(msg, message.sender))
                                        .build()
                        ).queue();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendBGUserMessage(Guild guild, CommandSender sender, CommandSender to, String msg){
        to.sendMessage(TextUtils.codedText(msg
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
                .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSavableUser(sender)))
                .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreateSavableUser(sender)))
                .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrCreateSavableUser(sender)))
                .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrCreateSavableUser(sender)))
                .replace("%leader_display%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                .replace("%leader_normal%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                .replace("%leader_absolute%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrCreateSavableUserByUUID(guild.leaderUUID)))
                .replace("%leader_formatted%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrCreateSavableUserByUUID(guild.leaderUUID)))
        ));
    }

    public static void sendBGUserMessageFromDiscord(Guild guild, String nameUsed, CommandSender to, String msg){
        to.sendMessage(TextUtils.codedText(msg
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
                .replace("%name%", guild.name)
                .replace("%length%", String.valueOf(guild.name.length()))
                .replace("%max_length%", String.valueOf(ConfigUtils.guildMaxLength))
                .replace("%codes%", (ConfigUtils.guildIncludeColors ? GuildUtils.withCodes : GuildUtils.withoutCodes))
                .replace("%sender_display%", nameUsed)
                .replace("%sender_normal%", nameUsed)
                .replace("%sender_absolute%", nameUsed)
                .replace("%sender_formatted%", nameUsed)
                .replace("%leader_display%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                .replace("%leader_normal%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                .replace("%leader_absolute%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrCreateSavableUserByUUID(guild.leaderUUID)))
                .replace("%leader_formatted%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrCreateSavableUserByUUID(guild.leaderUUID)))
        ));
    }

    public static void sendBGUserMessageFromDiscord(Guild guild, SavableUser sender, CommandSender to, String msg){
        to.sendMessage(TextUtils.codedText(msg
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
                .replace("%version%", sender.latestVersion)
                .replace("%name%", guild.name)
                .replace("%length%", String.valueOf(guild.name.length()))
                .replace("%max_length%", String.valueOf(ConfigUtils.guildMaxLength))
                .replace("%codes%", (ConfigUtils.guildIncludeColors ? GuildUtils.withCodes : GuildUtils.withoutCodes))
                .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(sender))
                .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(sender))
                .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(sender))
                .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(sender))
                .replace("%leader_display%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                .replace("%leader_normal%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                .replace("%leader_absolute%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrCreateSavableUserByUUID(guild.leaderUUID)))
                .replace("%leader_formatted%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrCreateSavableUserByUUID(guild.leaderUUID)))
        ));
    }

    public static void sendDiscordGEBMessage(Guild guild, DiscordMessage message){
        if (! ConfigUtils.moduleDEnabled) {
            return;
        }

        JDA jda = StreamLine.getJda();
        EmbedBuilder eb = new EmbedBuilder();

        String msg = message.message
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
                .replace("%version%", PlayerUtils.getOrCreateSavableUser(message.sender).latestVersion)
                .replace("%name%", guild.name)
                .replace("%length%", String.valueOf(guild.name.length()))
                .replace("%max_length%", String.valueOf(ConfigUtils.guildMaxLength))
                .replace("%codes%", (ConfigUtils.guildIncludeColors ? GuildUtils.withCodes : GuildUtils.withoutCodes))
                .replace("%sender_display%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSavableUser(message.sender)))
                .replace("%sender_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSavableUser(message.sender)))
                .replace("%sender_absolute%", PlayerUtils.getAbsoluteDiscord(PlayerUtils.getOrCreateSavableUser(message.sender)))
                .replace("%sender_formatted%", PlayerUtils.getJustDisplayDiscord(PlayerUtils.getOrCreateSavableUser(message.sender)))
                .replace("%leader_display%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                .replace("%leader_normal%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreateSUByUUID(guild.leaderUUID)))
                .replace("%leader_absolute%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getAbsoluteDiscord(PlayerUtils.getOrCreateSavableUserByUUID(guild.leaderUUID)))
                .replace("%leader_formatted%", guild.leaderUUID == null ? MessageConfUtils.nullB() : PlayerUtils.getJustDisplayDiscord(PlayerUtils.getOrCreateSavableUserByUUID(guild.leaderUUID)));

        try {
            if (ConfigUtils.moduleAvatarUse) {
                if (message.sender instanceof ProxiedPlayer) {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessageEmbeds(
                                    eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                            .setDescription(TextUtils.replaceAllSender(msg, message.sender))
                                            .setAuthor(message.sender.getName(), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName), FaceFetcher.getFaceAvatarURL(Objects.requireNonNull(PlayerUtils.getPlayerStat(message.sender)).latestName))
                                            .build()
                            ).queue();
                } else {
                    Objects.requireNonNull(jda.getTextChannelById(message.channel))
                            .sendMessageEmbeds(
                                    eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                            .setDescription(TextUtils.replaceAllSender(msg, message.sender))
                                            .setAuthor("CONSOLE", jda.getSelfUser().getAvatarUrl() , jda.getSelfUser().getAvatarUrl())
                                            .build()
                            ).queue();
                }
            } else {
                Objects.requireNonNull(jda.getTextChannelById(message.channel))
                        .sendMessageEmbeds(
                                eb.setTitle(TextUtils.replaceAllSender(message.title, message.sender))
                                        .setDescription(TextUtils.replaceAllSender(msg, message.sender))
                                        .build()
                        ).queue();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendStatUserMessage(SavableUser user, CommandSender sender, String msg){
        Guild guild = GuildUtils.getGuild(user);

        if (user instanceof ConsolePlayer) {
            ConsolePlayer player = PlayerUtils.getConsoleStat();

            if (player == null) {
                sendBUserMessage(sender, MessageConfUtils.noPlayer());
                return;
            }

            sender.sendMessage(TextUtils.codedText(msg
                    .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStat(sender)))
                    .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreatePlayerStat(sender)))
                    .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrCreatePlayerStat(sender)))
                    .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrCreatePlayerStat(sender)))
                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(user))
                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(user))
                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(user))
                    .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(user))
                    .replace("%version%", player.latestVersion)
                    .replace("%points%", Integer.toString(player.points))
                    .replace("%points_name%", PlayerUtils.pointsName)
                    .replace("%tags%", statTags(player))
                    .replace("%uuid%", player.uuid)
                    .replace("%player_display%", player.displayName)
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
                sendBUserMessage(sender, MessageConfUtils.noPlayer());
                return;
            }

            sender.sendMessage(TextUtils.codedText(msg
                    .replace("%sender_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStat(sender)))
                    .replace("%sender_normal%", PlayerUtils.getOffOnRegBungee(PlayerUtils.getOrCreatePlayerStat(sender)))
                    .replace("%sender_absolute%", PlayerUtils.getAbsoluteBungee(PlayerUtils.getOrCreatePlayerStat(sender)))
                    .replace("%sender_formatted%", PlayerUtils.getJustDisplayBungee(PlayerUtils.getOrCreatePlayerStat(sender)))
                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(user))
                    .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(user))
                    .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(user))
                    .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(user))
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
                    .replace("%player_display%", player.displayName)
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

    public static void sendEventUserMessage(SavableUser from, SavableUser to, String msg) {
        to.sendMessage(TextUtils.codedText(msg
                .replace("%from_display%", PlayerUtils.getOffOnDisplayBungee(from))
                .replace("%from_normal%", PlayerUtils.getOffOnRegBungee(from))
                .replace("%from_absolute%", PlayerUtils.getAbsoluteBungee(from))
                .replace("%from_server%", from.findServer())
                .replace("%to_display%", PlayerUtils.getOffOnDisplayBungee(to))
                .replace("%to_normal%", PlayerUtils.getOffOnRegBungee(to))
                .replace("%to_absolute%", PlayerUtils.getAbsoluteBungee(to))
                .replace("%to_server%", to.findServer())
        ));
    }

    public static void sendBUserMessage(CommandSender sender, String msg){
        if (sender instanceof ProxiedPlayer) {
            sender.sendMessage(TextUtils.codedText(TextUtils.replaceAllSender(msg, sender)
                    .replace("%version%", PlayerUtils.getOrCreatePlayerStat((ProxiedPlayer) sender).latestVersion)
            ));
        } else {
            sender.sendMessage(TextUtils.codedText(TextUtils.replaceAllSender(msg, sender)));
        }
    }

    public static void sendBUserMessage(Player sender, String msg){
        if (sender instanceof ProxiedPlayer) {
            sender.sendMessage(TextUtils.codedText(TextUtils.replaceAllSender(msg, sender)
                    .replace("%version%", Objects.requireNonNull(sender).latestVersion)
            ));
        }
    }

    public static void sendBMessagenging(CommandSender sendTo, SavableUser from, SavableUser to, String playerMessage, String msg) {
        sendTo.sendMessage(TextUtils.codedText(msg
                .replace("%from_formatted%", PlayerUtils.getJustDisplayBungee(from))
                .replace("%from_display%", PlayerUtils.getOffOnDisplayBungee(from))
                .replace("%from_normal%", PlayerUtils.getOffOnRegBungee(from))
                .replace("%from_absolute%", PlayerUtils.getAbsoluteBungee(from))
                .replace("%from_server%", from.findServer())
                .replace("%to_formatted%", PlayerUtils.getJustDisplayBungee(to))
                .replace("%to_display%", PlayerUtils.getOffOnDisplayBungee(to))
                .replace("%to_normal%", PlayerUtils.getOffOnRegBungee(to))
                .replace("%to_absolute%", PlayerUtils.getAbsoluteBungee(to))
                .replace("%to_server%", to.findServer())
                .replace("%message%", playerMessage)
        ));
    }

    public static void sendBUserAsMessage(CommandSender as, String msg){
        ServerInfo serverInfo = StreamLine.getInstance().getProxy().getPlayer(as.getName()).getServer().getInfo();

        Collection<ProxiedPlayer> players = serverInfo.getPlayers();

        if (as instanceof ProxiedPlayer) {
            for (ProxiedPlayer player : players) {
                player.sendMessage(TextUtils.codedText(TextUtils.replaceAllSender(msg, as)
                        .replace("%version%", PlayerUtils.getOrCreatePlayerStat((ProxiedPlayer) as).latestVersion)
                ));
            }
        } else {
            for (ProxiedPlayer player : players) {
                player.sendMessage(TextUtils.codedText(TextUtils.replaceAllSender(msg, as)));
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
        if (msg == null) msg = "";
        StreamLine.getInstance().getLogger().info(TextUtils.newLined(msg));
    }

    public static void logWarning(String msg){
        if (msg == null) msg = "";
        StreamLine.getInstance().getLogger().warning(TextUtils.newLined(msg));
    }

    public static void logSevere(String msg){
        if (msg == null) msg = "";
        StreamLine.getInstance().getLogger().severe(TextUtils.newLined(msg));
    }

    public static String mods(Party party){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (Player m : party.moderators){
            if (i < party.moderators.size()){
                msg.append(MessageConfUtils.partiesModsNLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesModsLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
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
                msg.append(MessageConfUtils.partiesMemsNLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesMemsLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
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
                msg.append(MessageConfUtils.partiesTMemsNLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesTMemsLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
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
                msg.append(MessageConfUtils.partiesInvsNLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.partiesInvsLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(m))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(m))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(m))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(m))
                        .replace("%version%", Objects.requireNonNull(m).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String getIsPublic(Party party){
        return party.isPublic ? MessageConfUtils.partiesIsPublicTrue() : MessageConfUtils.partiesIsPublicFalse();
    }

    public static String getIsMuted(Party party){
        return party.isMuted ? MessageConfUtils.partiesIsMutedTrue() : MessageConfUtils.partiesIsMutedFalse();
    }

    public static String modsGuild(Guild guild){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (String m : guild.modsByUUID){
            SavableUser player;
            try {
                player = PlayerUtils.getOrGetPlayerStatByUUID(m);
            } catch (Exception e) {
                continue;
            }

//            if (player == null) continue;

            if (i < guild.modsByUUID.size()){
                msg.append(MessageConfUtils.guildsModsNLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player))
                        .replace("%version%", player == null ? MessageConfUtils.nullB() : Objects.requireNonNull(player).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.guildsModsLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player))
                        .replace("%version%", player == null ? MessageConfUtils.nullB() : Objects.requireNonNull(player).latestVersion)
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
                player = PlayerUtils.getOrGetPlayerStatByUUID(m);
            } catch (Exception e) {
                continue;
            }

//            if (player == null) continue;

            if (i < guild.membersByUUID.size()){
                msg.append(MessageConfUtils.guildsMemsNLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player))
                        .replace("%version%", player == null ? MessageConfUtils.nullB() : Objects.requireNonNull(player).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.guildsMemsLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player))
                        .replace("%version%", player == null ? MessageConfUtils.nullB() : Objects.requireNonNull(player).latestVersion)
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
                player = PlayerUtils.getOrGetPlayerStatByUUID(m);
            } catch (Exception e) {
                continue;
            }

//            if (player == null) continue;

            if (i < guild.totalMembersByUUID.size()){
                msg.append(MessageConfUtils.guildsTMemsNLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player))
                        .replace("%version%", player == null ? MessageConfUtils.nullB() : Objects.requireNonNull(player).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.guildsTMemsLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player))
                        .replace("%version%", player == null ? MessageConfUtils.nullB() : Objects.requireNonNull(player).latestVersion)
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
                player = PlayerUtils.getOrGetPlayerStatByUUID(m);
            } catch (Exception e) {
                continue;
            }

//            if (player == null) continue;

            if (i < guild.invites.size()){
                msg.append(MessageConfUtils.guildsInvsNLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player))
                        .replace("%version%", player == null ? MessageConfUtils.nullB() : Objects.requireNonNull(player).latestVersion)
                );
            } else {
                msg.append(MessageConfUtils.guildsInvsLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player))
                        .replace("%version%", player == null ? MessageConfUtils.nullB() : Objects.requireNonNull(player).latestVersion)
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String getIsPublicGuild(Guild guild){
        return guild.isPublic ? MessageConfUtils.guildsIsPublicTrue() : MessageConfUtils.guildsIsPublicFalse();
    }

    public static String getIsMutedGuild(Guild guild){
        return guild.isMuted ? MessageConfUtils.guildsIsMutedTrue() : MessageConfUtils.guildsIsMutedFalse();
    }

    public static void sendInfo(CommandSender sender) {
        sender.sendMessage(TextUtils.codedText(MessageConfUtils.info()
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
