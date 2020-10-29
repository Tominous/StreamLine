package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.md_5.bungee.config.Configuration;

public class MessageConfUtils {
    private static final Configuration message = Config.getMess();
    // Messages:
//    public static final String s = message.getString("");
    // Basics.
    public static final String version = message.getString("version");
    public static final String prefix = message.getString("message-prefix");
    public static final String noPerm = message.getString("no-permission");
    public static final String reload = message.getString("reload-message");
    public static final String onlyPlayers = message.getString("only-players");
    public static final String noPlayer = message.getString("no-player");
    public static final String discordErrTitle = message.getString("discord-err-title");
    // Command Error.
    public static final String discordCommandError = message.getString("command-error.discord");
    public static final String bungeeCommandError = message.getString("command-error.bungee");
    // Command Disabled.
    public static final String discordCommandDisabled = message.getString("command-disabled.discord");
    public static final String bungeeCommandDisabled = message.getString("command-disabled.bungee");
    // Module Disabled.
    public static final String discordModuleDisabled = message.getString("module-disabled.discord");
    public static final String bungeeModuleDisabled = message.getString("module-disabled.bungee");
    // Not command / improper usage.
    public static final String discordNotACommand = message.getString("not-a-command.discord");
    public static final String bungeeImproperUsage = message.getString("improper-usage.bungee");
    // Command needs more args.
    public static final String discordNeedsMore = message.getString("command-needs-args.discord");
    public static final String bungeeNeedsMore = message.getString("command-needs-args.bungee");
    // Reports.
    public static final String reportEmbedTitle = message.getString("report-message.embed-title");
    public static final String dToDReportMessage = message.getString("report-message.from-discord.discord");
    public static final String dToBReportMessage = message.getString("report-message.from-discord.bungee");
    public static final String dConfirmReportMessage = message.getString("report-message.from-discord.confirmation");
    public static final String bToDReportMessage = message.getString("report-message.from-bungee.discord");
    public static final String bToBReportMessage = message.getString("report-message.from-bungee.bungee");
    public static final String bConfirmReportMessage = message.getString("report-message.from-bungee.confirmation");
    // StaffChat.
    public static final String staffChatEmbedTitle = message.getString("staffchat.message.embed-title");
    public static final String discordStaffChatMessage = message.getString("staffchat.message.discord");
    public static final String bungeeStaffChatMessage = message.getString("staffchat.message.bungee");
    public static final String discordStaffChatFrom = message.getString("staffchat.message.from.discord");
    public static final String bungeeStaffChatFrom = message.getString("staffchat.message.from.bungee");
    public static final String staffChatWrongPrefix = message.getString("staffchat.wrong-prefix");
    // Online.
    public static final String onlineMessageEmbedTitle = message.getString("online.message.embed-title");
    public static final String onlineMessageDiscord = message.getString("online.message.discord");
    public static final String onlineMessageBMain = message.getString("online.message.bungee.main");
    public static final String onlineMessageBServers = message.getString("online.message.bungee.servers");
    public static final String onlineMessageBPlayersMain = message.getString("online.message.bungee.players.main");
    public static final String onlineMessageBPlayersBulkNotLast = message.getString("online.message.bungee.players.playerbulk.if-not-last");
    public static final String onlineMessageBPlayersBulkLast = message.getString("online.message.bungee.players.playerbulk.if-last");
    // ... StaffOnline.
    // Discord.
    public static final String sOnlineMessageEmbedTitle = message.getString("staffonline.message.embed-title");
    public static final String sOnlineDiscordOnline = message.getString("staffonline.message.discord.online");
    public static final String sOnlineDiscordOffline = message.getString("staffonline.message.discord.offline");
    public static final String sOnlineDiscordMain = message.getString("staffonline.message.discord.main");
    public static final String sOnlineDiscordBulkNotLast = message.getString("staffonline.message.discord.staffbulk.if-not-last");
    public static final String sOnlineDiscordBulkLast = message.getString("staffonline.message.discord.staffbulk.if-last");
    // Bungee.
    public static final String sOnlineBungeeOnline = message.getString("staffonline.message.bungee.online");
    public static final String sOnlineBungeeOffline = message.getString("staffonline.message.bungee.offline");
    public static final String sOnlineBungeeMain = message.getString("staffonline.message.bungee.main");
    public static final String sOnlineBungeeBulkNotLast = message.getString("staffonline.message.bungee.staffbulk.if-not-last");
    public static final String sOnlineBungeeBulkLast = message.getString("staffonline.message.bungee.staffbulk.if-last");
    // Stream.
    public static final String streamNeedLink = message.getString("stream.need-link");
    public static final String streamNotLink = message.getString("stream.not-link");
    public static final String streamMessage = message.getString("stream.message");
    public static final String streamHoverPrefix = message.getString("stream.hover-prefix");
}