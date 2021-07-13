package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConfigUtils {
    // Config //
//    public static String s = StreamLine.getConfig().getConfString("");
    // Important.
    public static String version = StreamLine.getConfig().getConfString("version");
    // Debug.
    public static boolean debug = StreamLine.getConfig().getConfBoolean("debug");
    // ... Basics.
    // Links.
    public static String linkPre = StreamLine.getConfig().getConfString("link-prefix");
//    public static String linkSuff = StreamLine.getConfig().getConfString("link.suffix");
    // Bot Stuff.
    public static String botPrefix = StreamLine.getConfig().getConfString("bot.prefix");
    public static String botToken = StreamLine.getConfig().getConfString("bot.token");
    public static String botStatusMessage = StreamLine.getConfig().getConfString("bot.server-ip");
    // ... Discord.
    // Text Channels.
    public static String textChannelReports = StreamLine.getConfig().getConfString("discord.text-channels.reports");
    public static String textChannelStaffChat = StreamLine.getConfig().getConfString("discord.text-channels.staffchat");
    public static String textChannelOfflineOnline = StreamLine.getConfig().getConfString("discord.text-channels.offline-online");
    public static String textChannelBJoins = StreamLine.getConfig().getConfString("discord.text-channels.bungee-joins");
    public static String textChannelBLeaves = StreamLine.getConfig().getConfString("discord.text-channels.bungee-leaves");
    public static String textChannelBConsole = StreamLine.getConfig().getConfString("discord.text-channels.console");
    public static String textChannelGuilds = StreamLine.getConfig().getConfString("discord.text-channels.guilds");
    public static String textChannelParties = StreamLine.getConfig().getConfString("discord.text-channels.parties");
    public static String textChannelMutes = StreamLine.getConfig().getConfString("discord.text-channels.mutes");
    public static String textChannelKicks = StreamLine.getConfig().getConfString("discord.text-channels.kicks");
    public static String textChannelBans = StreamLine.getConfig().getConfString("discord.text-channels.bans");
    public static String textChannelIPBans = StreamLine.getConfig().getConfString("discord.text-channels.ipbans");
    // Roles.
    public static String roleReports = StreamLine.getConfig().getConfString("discord.roles.reports");
    public static String roleStaff = StreamLine.getConfig().getConfString("discord.roles.staff");
    // ... ... ... Commands.
    // ... ... Discord Stuff.
    // Commands.
    public static boolean comDCommands = StreamLine.getConfig().getConfBoolean("commands.discord.help.enabled");
    public static List<String> comDCommandsAliases = StreamLine.getConfig().getConfStringList("commands.discord.help.aliases");
    public static String comDCommandsPerm = StreamLine.getConfig().getConfString("commands.discord.help.permission");
    // Online.
    public static boolean comDOnline = StreamLine.getConfig().getConfBoolean("commands.discord.online.enabled");
    public static List<String> comDOnlineAliases = StreamLine.getConfig().getConfStringList("commands.discord.online.aliases");
    public static String comDOnlinePerm = StreamLine.getConfig().getConfString("commands.discord.online.permission");
    // Report.
    public static boolean comDReport = StreamLine.getConfig().getConfBoolean("commands.discord.report.enabled");
    public static List<String> comDReportAliases = StreamLine.getConfig().getConfStringList("commands.discord.report.aliases");
    public static String comDReportPerm = StreamLine.getConfig().getConfString("commands.discord.report.permission");
    // StaffChat.
    public static boolean comDStaffChat = StreamLine.getConfig().getConfBoolean("commands.discord.staffchat.enabled");
    public static List<String> comDStaffChatAliases = StreamLine.getConfig().getConfStringList("commands.discord.staffchat.aliases");
    public static String comDStaffChatPerm = StreamLine.getConfig().getConfString("commands.discord.staffchat.permission");
    // StaffOnline.
    public static boolean comDStaffOnline = StreamLine.getConfig().getConfBoolean("commands.discord.staffonline.enabled");
    public static List<String> comDStaffOnlineAliases = StreamLine.getConfig().getConfStringList("commands.discord.staffonline.aliases");
    public static String comDStaffOnlinePerm = StreamLine.getConfig().getConfString("commands.discord.staffonline.permission");
    // ... ... Bungee Stuff.
    // Ping.
    public static boolean comBPing = StreamLine.getConfig().getConfBoolean("commands.bungee.ping.enabled");
    public static String comBPingBase = StreamLine.getConfig().getConfString("commands.bungee.ping.base");
    public static List<String> comBPingAliases = StreamLine.getConfig().getConfStringList("commands.bungee.ping.aliases");
    public static String comBPingPerm = StreamLine.getConfig().getConfString("commands.bungee.ping.permission");
    // Plugins.
    public static boolean comBPlugins = StreamLine.getConfig().getConfBoolean("commands.bungee.plugins.enabled");
    public static String comBPluginsBase = StreamLine.getConfig().getConfString("commands.bungee.plugins.base");
    public static List<String> comBPluginsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.plugins.aliases");
    public static String comBPluginsPerm = StreamLine.getConfig().getConfString("commands.bungee.plugins.permission");
    // Stream.
    public static boolean comBStream = StreamLine.getConfig().getConfBoolean("commands.bungee.stream.enabled");
    public static String comBStreamBase = StreamLine.getConfig().getConfString("commands.bungee.stream.base");
    public static List<String> comBStreamAliases = StreamLine.getConfig().getConfStringList("commands.bungee.stream.aliases");
    public static String comBStreamPerm = StreamLine.getConfig().getConfString("commands.bungee.stream.permission");
    // Report.
    public static boolean comBReport = StreamLine.getConfig().getConfBoolean("commands.bungee.report.enabled");
    public static String comBReportBase = StreamLine.getConfig().getConfString("commands.bungee.report.base");
    public static List<String> comBReportAliases = StreamLine.getConfig().getConfStringList("commands.bungee.report.aliases");
    public static String comBReportPerm = StreamLine.getConfig().getConfString("commands.bungee.report.permission");
    // StatsCommand
    public static boolean comBStats = StreamLine.getConfig().getConfBoolean("commands.bungee.stats.enabled");
    public static String comBStatsBase = StreamLine.getConfig().getConfString("commands.bungee.stats.base");
    public static List<String> comBStatsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.stats.aliases");
    public static String comBStatsPerm = StreamLine.getConfig().getConfString("commands.bungee.stats.permission");
    public static boolean comBStatsOthers = StreamLine.getConfig().getConfBoolean("commands.bungee.stats.view-others.enabled");
    public static String comBStatsPermOthers = StreamLine.getConfig().getConfString("commands.bungee.stats.view-others.permission");
    // ... Party.
    //
    public static boolean comBParty = StreamLine.getConfig().getConfBoolean("commands.bungee.party.enabled");
    public static String comBPartyBase = StreamLine.getConfig().getConfString("commands.bungee.party.base");
    public static boolean comBParQuick = StreamLine.getConfig().getConfBoolean("commands.bungee.party.quick-chat");
    public static List<String> comBParMainAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.main");
    public static String comBParPerm = StreamLine.getConfig().getConfString("commands.bungee.party.permission");
    // Join.
    public static List<String> comBParJoinAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.join");
    // Leave.
    public static List<String> comBParLeaveAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.leave");
    // Create.
    public static List<String> comBParCreateAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.create");
    // Promote.
    public static List<String> comBParPromoteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.promote");
    // Demote.
    public static List<String> comBParDemoteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.demote");
    // Chat.
    public static List<String> comBParChatAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.chat");
    // List.
    public static List<String> comBParListAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.list");
    // Open.
    public static List<String> comBParOpenAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.open");
    // Close.
    public static List<String> comBParCloseAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.close");
    // Disband.
    public static List<String> comBParDisbandAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.disband");
    // Accept.
    public static List<String> comBParAcceptAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.accept");
    // Deny.
    public static List<String> comBParDenyAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.deny");
    // Invite.
    public static List<String> comBParInvAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.invite");
    // Kick.
    public static List<String> comBParKickAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.kick");
    // Mute.
    public static List<String> comBParMuteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.mute");
    // Warp.
    public static List<String> comBParWarpAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.warp");
    // ... Guild.
    //
    public static boolean comBGuild = StreamLine.getConfig().getConfBoolean("commands.bungee.guild.enabled");
    public static String comBGuildBase = StreamLine.getConfig().getConfString("commands.bungee.guild.base");
    public static boolean comBGuildQuick = StreamLine.getConfig().getConfBoolean("commands.bungee.guild.quick-chat");
    public static String comBGuildPerm = StreamLine.getConfig().getConfString("commands.bungee.guild.permission");
    public static List<String> comBGuildMainAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.main");
    // Join.
    public static List<String> comBGuildJoinAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.join");
    // Leave.
    public static List<String> comBGuildLeaveAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.leave");
    // Create.
    public static List<String> comBGuildCreateAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.create");
    // Promote.
    public static List<String> comBGuildPromoteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.promote");
    // Demote.
    public static List<String> comBGuildDemoteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.demote");
    // Chat.
    public static List<String> comBGuildChatAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.chat");
    // List.
    public static List<String> comBGuildListAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.list");
    // Open.
    public static List<String> comBGuildOpenAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.open");
    // Close.
    public static List<String> comBGuildCloseAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.close");
    // Disband.
    public static List<String> comBGuildDisbandAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.disband");
    // Accept.
    public static List<String> comBGuildAcceptAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.accept");
    // Deny.
    public static List<String> comBGuildDenyAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.deny");
    // Invite.
    public static List<String> comBGuildInvAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.invite");
    // Kick.
    public static List<String> comBGuildKickAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.kick");
    // Mute.
    public static List<String> comBGuildMuteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.mute");
    // Warp.
    public static List<String> comBGuildWarpAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.warp");
    // Info.
    public static List<String> comBGuildInfoAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.info");
    // Rename.
    public static List<String> comBGuildRenameAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.rename");
    // ... Servers.
    // Lobby.
    public static boolean comBLobby = StreamLine.getConfig().getConfBoolean("commands.bungee.servers.lobby.enabled");
    public static String comBLobbyBase = StreamLine.getConfig().getConfString("commands.bungee.staff.lobby.base");
    public static List<String> comBLobbyAliases = StreamLine.getConfig().getConfStringList("commands.bungee.servers.lobby.aliases");
    public static String comBLobbyEnd = StreamLine.getConfig().getConfString("commands.bungee.servers.lobby.points-to");
    public static String comBLobbyPerm = StreamLine.getConfig().getConfString("commands.bungee.servers.lobby.permission");
    // Fabric Fix.
    public static boolean comBFabric = StreamLine.getConfig().getConfBoolean("commands.bungee.servers.fabric-fix.enabled");
    public static String comBFabricEnd = StreamLine.getConfig().getConfString("commands.bungee.servers.fabric-fix.points-to");
    public static String comBFabricPerm = StreamLine.getConfig().getConfString("commands.bungee.servers.fabric-fix.permission");
    // ... Staff.
    // GlobalOnline.
    public static boolean comBGlobalOnline = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.globalonline.enabled");
    public static String comBGlobalOnlineBase = StreamLine.getConfig().getConfString("commands.bungee.staff.globalonline.base");
    public static List<String> comBGlobalOnlineAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.globalonline.aliases");
    public static String comBGlobalOnlinePerm = StreamLine.getConfig().getConfString("commands.bungee.staff.globalonline.permission");
    // StaffChat.
    public static boolean comBStaffChat = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.staffchat.enabled");
    public static String comBStaffChatBase = StreamLine.getConfig().getConfString("commands.bungee.staff.staffchat.base");
    public static List<String> comBStaffChatAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.staffchat.aliases");
    public static String comBStaffChatPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.staffchat.permission");
    // StaffOnline.
    public static boolean comBStaffOnline = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.staffonline.enabled");
    public static String comBStaffOnlineBase = StreamLine.getConfig().getConfString("commands.bungee.staff.staffonline.base");
    public static List<String> comBStaffOnlineAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.staffonline.aliases");
    public static String comBStaffOnlinePerm = StreamLine.getConfig().getConfString("commands.bungee.staff.staffonline.permission");
    // Reload.
    public static String comBReloadBase = StreamLine.getConfig().getConfString("commands.bungee.staff.slreload.base");
    public static List<String> comBReloadAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.slreload.aliases");
    public static String comBReloadPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.slreload.permission");
    // Parties.
    public static boolean comBParties = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.parties.enabled");
    public static String comBPartiesBase = StreamLine.getConfig().getConfString("commands.bungee.staff.parties.base");
    public static List<String> comBPartiesAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.parties.aliases");
    public static String comBPartiesPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.parties.permission");
    // Guilds.
    public static boolean comBGuilds = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.guilds.enabled");
    public static String comBGuildsBase = StreamLine.getConfig().getConfString("commands.bungee.staff.guilds.base");
    public static List<String> comBGuildsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.guilds.aliases");
    public static String comBGuildsPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.guilds.permission");
    // GetStats.
    public static boolean comBGetStats = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.getstats.enabled");
    public static String comBGetStatsBase = StreamLine.getConfig().getConfString("commands.bungee.staff.getstats.base");
    public static List<String> comBGetStatsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.getstats.aliases");
    public static String comBGetStatsPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.getstats.permission");
    // BSudo.
    public static boolean comBSudo = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.bsudo.enabled");
    public static String comBSudoBase = StreamLine.getConfig().getConfString("commands.bungee.staff.bsudo.base");
    public static List<String> comBSudoAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.bsudo.aliases");
    public static String comBSudoPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.bsudo.permission");
    // SSPY.
    public static boolean comBSSPY = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.sspy.enabled");
    public static String comBSSPYBase = StreamLine.getConfig().getConfString("commands.bungee.staff.sspy.base");
    public static List<String> comBSSPYAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.sspy.aliases");
    public static String comBSSPYPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.sspy.permission");
    // GSPY.
    public static boolean comBGSPY = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.gspy.enabled");
    public static String comBGSPYBase = StreamLine.getConfig().getConfString("commands.bungee.staff.gspy.base");
    public static List<String> comBGSPYAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.gspy.aliases");
    public static String comBGSPYPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.gspy.permission");
    // PSPY.
    public static boolean comBPSPY = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.pspy.enabled");
    public static String comBPSPYBase = StreamLine.getConfig().getConfString("commands.bungee.staff.pspy.base");
    public static List<String> comBPSPYAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.pspy.aliases");
    public static String comBPSPYPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.pspy.permission");
    // SCView.
    public static boolean comBSCView = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.scview.enabled");
    public static String comBSCViewBase = StreamLine.getConfig().getConfString("commands.bungee.staff.scview.base");
    public static List<String> comBSCViewAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.scview.aliases");
    public static String comBSCViewPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.scview.permission");
    // BTag.
    public static boolean comBBTag = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.btag.enabled");
    public static String comBBTagBase = StreamLine.getConfig().getConfString("commands.bungee.staff.btag.base");
    public static List<String> comBBTagAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.btag.aliases");
    public static String comBBTagPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.btag.permission");
    public static String comBBTagOPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.btag.other-perm");
    public static String comBBTagChPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.btag.change-perm");
    // Event Reload.
    public static boolean comBEReload = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.evreload.enabled");
    public static String comBEReloadBase = StreamLine.getConfig().getConfString("commands.bungee.staff.evreload.base");
    public static List<String> comBEReloadAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.evreload.aliases");
    public static String comBEReloadPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.evreload.permission");
    // Network Points.
    public static boolean comBPoints = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.points.enabled");
    public static String comBPointsBase = StreamLine.getConfig().getConfString("commands.bungee.staff.points.base");
    public static List<String> comBPointsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.points.aliases");
    public static String comBPointsPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.points.permission");
    public static String comBPointsOPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.points.other-perm");
    public static String comBPointsChPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.points.change-perm");
    // Server Ping.
    public static boolean comBSPing = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.serverping.enabled");
    public static String comBSPingBase = StreamLine.getConfig().getConfString("commands.bungee.staff.serverping.base");
    public static List<String> comBSPingAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.serverping.aliases");
    public static String comBSPingPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.serverping.permission");
    // Mute.
    public static boolean comBMute = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.mute.enabled");
    public static String comBMuteBase = StreamLine.getConfig().getConfString("commands.bungee.staff.mute.base");
    public static List<String> comBMuteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.mute.aliases");
    public static String comBMutePerm = StreamLine.getConfig().getConfString("commands.bungee.staff.mute.permission");
    // Kick.
    public static boolean comBKick = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.kick.enabled");
    public static String comBKickBase = StreamLine.getConfig().getConfString("commands.bungee.staff.kick.base");
    public static List<String> comBKickAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.kick.aliases");
    public static String comBKickPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.kick.permission");
    // Ban.
    public static boolean comBBan = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.ban.enabled");
    public static String comBBanBase = StreamLine.getConfig().getConfString("commands.bungee.staff.ban.base");
    public static List<String> comBBanAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.ban.aliases");
    public static String comBBanPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.ban.permission");
    // Ban.
    public static boolean comBIPBan = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.ban.enabled");
    public static String comBIPBanBase = StreamLine.getConfig().getConfString("commands.bungee.staff.ban.base");
    public static List<String> comBIPBanAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.ban.aliases");
    public static String comBIPBanPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.ban.permission");
    // Info.
    public static boolean comBInfo = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.info.enabled");
    public static String comBInfoBase = StreamLine.getConfig().getConfString("commands.bungee.staff.info.base");
    public static List<String> comBInfoAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.info.aliases");
    public static String comBInfoPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.info.permission");
    // Settings.
    public static boolean comBSettings = StreamLine.getConfig().getConfBoolean("commands.bungee.configs.settings.enabled");
    public static String comBSettingsBase = StreamLine.getConfig().getConfString("commands.bungee.configs.settings.base");
    public static List<String> comBSettingsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.configs.settings.aliases");
    public static String comBSettingsPerm = StreamLine.getConfig().getConfString("commands.bungee.configs.settings.permission");
    // ... Messaging.
    // Ignore.
    public static boolean comBIgnore = StreamLine.getConfig().getConfBoolean("commands.bungee.messaging.ignore.enabled");
    public static String comBIgnoreBase = StreamLine.getConfig().getConfString("commands.bungee.messaging.ignore.base");
    public static List<String> comBIgnoreAliases = StreamLine.getConfig().getConfStringList("commands.bungee.messaging.ignore.aliases");
    public static String comBIgnorePerm = StreamLine.getConfig().getConfString("commands.bungee.messaging.ignore.permission");
    // Message.
    public static boolean comBMessage = StreamLine.getConfig().getConfBoolean("commands.bungee.messaging.message.enabled");
    public static String comBMessageBase = StreamLine.getConfig().getConfString("commands.bungee.messaging.message.base");
    public static List<String> comBMessageAliases = StreamLine.getConfig().getConfStringList("commands.bungee.messaging.message.aliases");
    public static String comBMessagePerm = StreamLine.getConfig().getConfString("commands.bungee.messaging.message.permission");
    // Reply.
    public static boolean comBReply = StreamLine.getConfig().getConfBoolean("commands.bungee.messaging.reply.enabled");
    public static String comBReplyBase = StreamLine.getConfig().getConfString("commands.bungee.messaging.reply.base");
    public static List<String> comBReplyAliases = StreamLine.getConfig().getConfStringList("commands.bungee.messaging.reply.aliases");
    public static String comBReplyPerm = StreamLine.getConfig().getConfString("commands.bungee.messaging.reply.permission");
    // Friend.
    public static boolean comBFriend = StreamLine.getConfig().getConfBoolean("commands.bungee.messaging.friend.enabled");
    public static String comBFriendBase = StreamLine.getConfig().getConfString("commands.bungee.messaging.friend.base");
    public static List<String> comBFriendAliases = StreamLine.getConfig().getConfStringList("commands.bungee.messaging.friend.aliases");
    public static String comBFriendPerm = StreamLine.getConfig().getConfString("commands.bungee.messaging.friend.permission");
    // ... ... Modules.
    public static String staffPerm = StreamLine.getConfig().getConfString("modules.staff-permission");
    // ... Discord.
    // Basics.
    public static boolean moduleDMainConsole = StreamLine.getConfig().getConfBoolean("modules.discord.main-console");
    public static boolean moduleUseMCAvatar = StreamLine.getConfig().getConfBoolean("modules.discord.use-mc-avatar");
    public static boolean joinsLeavesIcon = StreamLine.getConfig().getConfBoolean("modules.discord.joins-leaves.use-bot-icon");
    public static boolean joinsLeavesAsConsole = StreamLine.getConfig().getConfBoolean("modules.discord.joins-leaves.send-as-console");
    // Reports.
    public static boolean moduleReportsDConfirmation = StreamLine.getConfig().getConfBoolean("modules.discord.reports.send-confirmation");
    public static boolean moduleReportToChannel = StreamLine.getConfig().getConfBoolean("modules.discord.reports.report-to-channel");
    public static boolean moduleReportsDToMinecraft = StreamLine.getConfig().getConfBoolean("modules.discord.reports.discord-to-minecraft");
    public static boolean moduleReportChannelPingsRole = StreamLine.getConfig().getConfBoolean("modules.discord.report-channel-pings-a-role");
    // StaffChat.
    public static boolean moduleStaffChatToMinecraft = StreamLine.getConfig().getConfBoolean("modules.discord.staffchat-to-minecraft");
    public static boolean moduleSCOnlyStaffRole = StreamLine.getConfig().getConfBoolean("modules.discord.staffchat-to-minecraft-only-staff-role");
    // Startup / Shutdowns.
    public static boolean moduleStartups = StreamLine.getConfig().getConfBoolean("modules.discord.startup-messages");
    public static boolean moduleShutdowns = StreamLine.getConfig().getConfBoolean("modules.discord.shutdown-messages");
    // Say if...
    public static String moduleSayNotACommand = StreamLine.getConfig().getConfString("modules.discord.say-if-not-a-command");
    public static String moduleSayCommandDisabled = StreamLine.getConfig().getConfString("modules.discord.say-if-command-disabled");
    // Player logins / logouts.
    public static String moduleDPlayerJoins = StreamLine.getConfig().getConfString("modules.discord.player-joins");
    public static String moduleDPlayerLeaves = StreamLine.getConfig().getConfString("modules.discord.player-leaves");
    // ... Bungee.
    // Reports.
    public static boolean moduleReportsBConfirmation = StreamLine.getConfig().getConfBoolean("modules.bungee.reports.send-confirmation");
    public static boolean moduleReportsMToDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.reports.minecraft-to-discord");
    public static boolean moduleReportsSendChat = StreamLine.getConfig().getConfBoolean("modules.bungee.reports.send-in-chat");
    // StaffChat.
    public static boolean moduleStaffChat = StreamLine.getConfig().getConfBoolean("modules.bungee.staffchat.enabled");
    public static boolean moduleStaffChatDoPrefix = StreamLine.getConfig().getConfBoolean("modules.bungee.staffchat.enable-prefix");
    public static String moduleStaffChatPrefix = StreamLine.getConfig().getConfString("modules.bungee.staffchat.prefix");
    public static boolean moduleStaffChatMToDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.staffchat.minecraft-to-discord");
    // Player logins / logouts.
//    public static String moduleBPlayerJoins = StreamLine.getConfig().getConfString("modules.bungee.player-joins");
//    public static String moduleBPlayerJoinsPerm = StreamLine.getConfig().getConfString("modules.bungee.joins-permission");
//    public static String moduleBPlayerLeaves = StreamLine.getConfig().getConfString("modules.bungee.player-leaves");
//    public static String moduleBPlayerLeavesPerm = StreamLine.getConfig().getConfString("modules.bungee.leaves-permission");
    public static String moduleBPlayerJoins = StreamLine.getConfig().getConfString("modules.bungee.player-joins-order");
    public static String moduleBPlayerJoinsPerm = StreamLine.getConfig().getConfString("modules.bungee.joins-permission");
    public static String moduleBPlayerLeaves = StreamLine.getConfig().getConfString("modules.bungee.player-leaves-order");
    public static String moduleBPlayerLeavesPerm = StreamLine.getConfig().getConfString("modules.bungee.leaves-permission");
    // ... Parties.
    public static boolean partyToDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.to-discord");
    public static int partyMax = StreamLine.getConfig().getConfInteger("modules.bungee.parties.max-size");
    public static String partyMaxPerm = StreamLine.getConfig().getConfString("modules.bungee.parties.base-permission");
    public static boolean partyConsoleChats = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.chat");
    public static boolean partyConsoleCreates = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.creates");
    public static boolean partyConsoleDisbands = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.disbands");
    public static boolean partyConsoleOpens = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.opens");
    public static boolean partyConsoleCloses = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.closes");
    public static boolean partyConsoleJoins = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.joins");
    public static boolean partyConsoleLeaves = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.leaves");
    public static boolean partyConsoleAccepts = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.accepts");
    public static boolean partyConsoleDenies = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.denies");
    public static boolean partyConsolePromotes = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.promotes");
    public static boolean partyConsoleDemotes = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.demotes");
    public static boolean partyConsoleInvites = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.invites");
    public static boolean partyConsoleKicks = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.kicks");
    public static boolean partyConsoleMutes = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.mutes");
    public static boolean partyConsoleWarps = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.warps");
    public static String partyView = StreamLine.getConfig().getConfString("modules.bungee.parties.view-permission");
    public static boolean partySendJoins = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.send.join");
    public static boolean partySendLeaves = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.send.leave");
    // ... Guilds.
    public static boolean guildToDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.to-discord");
    public static int guildMax = StreamLine.getConfig().getConfInteger("modules.bungee.guilds.max-size");
    public static boolean guildConsoleChats = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.chats");
    public static boolean guildConsoleCreates = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.creates");
    public static boolean guildConsoleDisbands = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.disbands");
    public static boolean guildConsoleOpens = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.opens");
    public static boolean guildConsoleCloses = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.closes");
    public static boolean guildConsoleJoins = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.joins");
    public static boolean guildConsoleLeaves = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.leaves");
    public static boolean guildConsoleAccepts = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.accepts");
    public static boolean guildConsoleDenies = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.denies");
    public static boolean guildConsolePromotes = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.promotes");
    public static boolean guildConsoleDemotes = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.demotes");
    public static boolean guildConsoleInvites = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.invites");
    public static boolean guildConsoleKicks = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.kicks");
    public static boolean guildConsoleMutes = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.mutes");
    public static boolean guildConsoleWarps = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.warps");
    public static boolean guildConsoleRenames = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.renames");
    public static int xpPerGiveG = StreamLine.getConfig().getConfInteger("modules.bungee.guilds.totalXP.amount-per");
    public static int timePerGiveG = StreamLine.getConfig().getConfInteger("modules.bungee.guilds.totalXP.time-per");
    public static String guildView = StreamLine.getConfig().getConfString("modules.bungee.guilds.view-permission");
    public static int guildMaxLength = StreamLine.getConfig().getConfInteger("modules.bungee.guilds.name.max-length");
    public static boolean guildIncludeColors = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.name.max-includes-colors");
    public static boolean guildSendJoins = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.send.join");
    public static boolean guildSendLeaves = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.send.leave");
    // ... Sudo.
    public static String noSudoPerm = StreamLine.getConfig().getConfString("modules.bungee.sudo.no-sudo-permission");
    // ... Stats.
    public static boolean statsTell = StreamLine.getConfig().getConfBoolean("modules.bungee.stats.tell-when-create");
    public static int xpPerGiveP = StreamLine.getConfig().getConfInteger("modules.bungee.stats.totalXP.amount-per");
    public static int timePerGiveP = StreamLine.getConfig().getConfInteger("modules.bungee.stats.totalXP.time-per");
    public static int cachedPClear = StreamLine.getConfig().getConfInteger("modules.bungee.stats.cache-clear");
    public static boolean updateDisplayNames = StreamLine.getConfig().getConfBoolean("modules.bungee.stats.update-display-names");
    // ... Redirect.
    public static boolean redirectEnabled = StreamLine.getConfig().getConfBoolean("modules.bungee.redirect.enabled");
    public static String redirectPre = StreamLine.getConfig().getConfString("modules.bungee.redirect.permission-prefix");
    public static String redirectMain = StreamLine.getConfig().getConfString("modules.bungee.redirect.main");
    // Version Block.
    public static boolean vbEnabled = StreamLine.getConfig().getConfBoolean("modules.bungee.redirect.version-block.enabled");
    public static String vbOverridePerm = StreamLine.getConfig().getConfString("modules.bungee.redirect.version-block.override-permission");
    public static String vbServerFile = StreamLine.getConfig().getConfString("modules.bungee.redirect.version-block.server-permission-file");
    // Lobbies.
    public static boolean lobbies = StreamLine.getConfig().getConfBoolean("modules.bungee.redirect.lobbies.enabled");
    public static String lobbiesFile = StreamLine.getConfig().getConfString("modules.bungee.redirect.lobbies.file");
    public static int lobbyTimeOut = StreamLine.getConfig().getConfInteger("modules.bungee.redirect.lobbies.time-out");
    // Points.
    public static int pointsDefault = StreamLine.getConfig().getConfInteger("modules.bungee.points.default");
    // Tags.
    public static List<String> tagsDefaults = StreamLine.getConfig().getConfStringList("modules.bungee.tags.defaults");
    // Events.
    public static boolean events = StreamLine.getConfig().getConfBoolean("modules.bungee.events.enabled");
    public static String eventsFolder = StreamLine.getConfig().getConfString("modules.bungee.events.folder");
    public static boolean eventsWhenEmpty = StreamLine.getConfig().getConfBoolean("modules.bungee.events.add-default-when-empty");
    // Errors.
    public static boolean errSendToConsole = StreamLine.getConfig().getConfBoolean("modules.bungee.user-errors.send-to-console");
    // ... Punishments.
    // Mutes.
    public static boolean punMutes = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.mutes.enabled");
    public static boolean punMutesHard = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.mutes.hard-mutes");
    public static boolean punMutesReplaceable = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.mutes.replaceable");
    public static boolean punMutesDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.mutes.discord");
    public static String punMutesBypass = StreamLine.getConfig().getConfString("modules.bungee.punishments.mutes.bypass");
    // Kicks
    public static boolean punKicksDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.kicks.discord");
    public static String punKicksBypass = StreamLine.getConfig().getConfString("modules.bungee.punishments.kicks.bypass");
    // Bans.
    public static boolean punBans = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.bans.enabled");
    public static boolean punBansReplaceable = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.bans.replaceable");
    public static boolean punBansDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.bans.discord");
    public static String punBansBypass = StreamLine.getConfig().getConfString("modules.bungee.punishments.bans.bypass");
    // IPBans.
    public static boolean punIPBans = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.ipbans.enabled");
    public static boolean punIPBansReplaceable = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.ipbans.replaceable");
    public static boolean punIPBansDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.ipbans.discord");
    public static String punIPBansBypass = StreamLine.getConfig().getConfString("modules.bungee.punishments.ipbans.bypass");
    // Messaging.
    public static String messViewPerm = StreamLine.getConfig().getConfString("modules.bungee.messaging.view-permission");
    public static String messReplyTo = StreamLine.getConfig().getConfString("modules.bungee.messaging.reply-to");
    // Server Config.
    public static boolean sc = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.enabled");
    public static boolean scMakeDefault = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.make-if-not-exist");
    public static boolean scMOTD = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.motd");
    public static boolean scVersion = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.version");
    public static boolean scSample = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.sample");
    public static boolean scMaxPlayers = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.max-players");
    public static boolean scOnlinePlayers = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.online-players");
    // Console.
    public static String consoleName = StreamLine.getConfig().getConfString("modules.bungee.console.name");
    public static String consoleDisplayName = StreamLine.getConfig().getConfString("modules.bungee.console.display-name");
    public static List<String> consoleDefaultTags = StreamLine.getConfig().getConfStringList("modules.bungee.console.default-tags");
    public static int consoleDefaultPoints = StreamLine.getConfig().getConfInteger("modules.bungee.console.default-points");
    public static String consoleServer = StreamLine.getConfig().getConfString("modules.bungee.console.server");
    // On Close.
    public static boolean onCloseSettings = StreamLine.getConfig().getConfBoolean("modules.bungee.on-close.save-settings");
    public static boolean onCloseMain = StreamLine.getConfig().getConfBoolean("modules.bungee.on-close.save-main");
    public static boolean onCloseSafeKick = StreamLine.getConfig().getConfBoolean("modules.bungee.on-close.safe-kick");
    public static boolean onCloseKickMessage = StreamLine.getConfig().getConfBoolean("modules.bungee.on-close.kick-message");
}
