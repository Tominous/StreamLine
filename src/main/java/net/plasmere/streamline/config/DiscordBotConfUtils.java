package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;

public class DiscordBotConfUtils {
    // Bot Stuff.
    public static String botPrefix = StreamLine.config.getDisBotString("bot.prefix");
    public static String botToken = StreamLine.config.getDisBotString("bot.token");
    public static String botStatusMessage = StreamLine.config.getDisBotString("bot.server-ip");
    // ... Discord.
    // Text Channels.
    public static String textChannelReports = StreamLine.config.getDisBotString("discord.text-channels.reports");
    public static String textChannelStaffChat = StreamLine.config.getDisBotString("discord.text-channels.staffchat");
    public static String textChannelOfflineOnline = StreamLine.config.getDisBotString("discord.text-channels.offline-online");
    public static String textChannelBJoins = StreamLine.config.getDisBotString("discord.text-channels.bungee-joins");
    public static String textChannelBLeaves = StreamLine.config.getDisBotString("discord.text-channels.bungee-leaves");
    public static String textChannelBConsole = StreamLine.config.getDisBotString("discord.text-channels.console");
    public static String textChannelGuilds = StreamLine.config.getDisBotString("discord.text-channels.guilds");
    public static String textChannelParties = StreamLine.config.getDisBotString("discord.text-channels.parties");
    public static String textChannelMutes = StreamLine.config.getDisBotString("discord.text-channels.mutes");
    public static String textChannelKicks = StreamLine.config.getDisBotString("discord.text-channels.kicks");
    public static String textChannelBans = StreamLine.config.getDisBotString("discord.text-channels.bans");
    public static String textChannelIPBans = StreamLine.config.getDisBotString("discord.text-channels.ipbans");
    // Roles.
    public static String roleReports = StreamLine.config.getDisBotString("discord.roles.reports");
    public static String roleStaff = StreamLine.config.getDisBotString("discord.roles.staff");
}
