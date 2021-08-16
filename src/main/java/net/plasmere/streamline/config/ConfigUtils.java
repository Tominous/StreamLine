package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;

import java.util.List;

public class ConfigUtils {
    // ConfigHandler //
//    public static String s = StreamLine.config.getConfString("");
    // Important.
    public static String version = StreamLine.config.getConfString("version");
    // Debug.
    public static boolean debug = StreamLine.config.getConfBoolean("debug");
    // ... Basics.
    // Links.
    public static String linkPre = StreamLine.config.getConfString("link-prefix");
//    public static String linkSuff = StreamLine.config.getConfString("link.suffix");
    // Bot Stuff.
    public static String botPrefix = StreamLine.config.getConfString("bot.prefix");
    public static String botToken = StreamLine.config.getConfString("bot.token");
    public static String botStatusMessage = StreamLine.config.getConfString("bot.server-ip");
    // ... Discord.
    // Text Channels.
    public static String textChannelReports = StreamLine.config.getConfString("discord.text-channels.reports");
    public static String textChannelStaffChat = StreamLine.config.getConfString("discord.text-channels.staffchat");
    public static String textChannelOfflineOnline = StreamLine.config.getConfString("discord.text-channels.offline-online");
    public static String textChannelBJoins = StreamLine.config.getConfString("discord.text-channels.bungee-joins");
    public static String textChannelBLeaves = StreamLine.config.getConfString("discord.text-channels.bungee-leaves");
    public static String textChannelBConsole = StreamLine.config.getConfString("discord.text-channels.console");
    public static String textChannelGuilds = StreamLine.config.getConfString("discord.text-channels.guilds");
    public static String textChannelParties = StreamLine.config.getConfString("discord.text-channels.parties");
    public static String textChannelMutes = StreamLine.config.getConfString("discord.text-channels.mutes");
    public static String textChannelKicks = StreamLine.config.getConfString("discord.text-channels.kicks");
    public static String textChannelBans = StreamLine.config.getConfString("discord.text-channels.bans");
    public static String textChannelIPBans = StreamLine.config.getConfString("discord.text-channels.ipbans");
    // Roles.
    public static String roleReports = StreamLine.config.getConfString("discord.roles.reports");
    public static String roleStaff = StreamLine.config.getConfString("discord.roles.staff");
    // ... ... ... Commands.
    // ... ... Discord Stuff.
    // Commands.
    public static boolean comDCommands = StreamLine.config.getConfBoolean("commands.discord.help.enabled");
    public static List<String> comDCommandsAliases = StreamLine.config.getConfStringList("commands.discord.help.aliases");
    public static String comDCommandsPerm = StreamLine.config.getConfString("commands.discord.help.permission");
    // Online.
    public static boolean comDOnline = StreamLine.config.getConfBoolean("commands.discord.online.enabled");
    public static List<String> comDOnlineAliases = StreamLine.config.getConfStringList("commands.discord.online.aliases");
    public static String comDOnlinePerm = StreamLine.config.getConfString("commands.discord.online.permission");
    // Report.
    public static boolean comDReport = StreamLine.config.getConfBoolean("commands.discord.report.enabled");
    public static List<String> comDReportAliases = StreamLine.config.getConfStringList("commands.discord.report.aliases");
    public static String comDReportPerm = StreamLine.config.getConfString("commands.discord.report.permission");
    // StaffChat.
    public static boolean comDStaffChat = StreamLine.config.getConfBoolean("commands.discord.staffchat.enabled");
    public static List<String> comDStaffChatAliases = StreamLine.config.getConfStringList("commands.discord.staffchat.aliases");
    public static String comDStaffChatPerm = StreamLine.config.getConfString("commands.discord.staffchat.permission");
    // StaffOnline.
    public static boolean comDStaffOnline = StreamLine.config.getConfBoolean("commands.discord.staffonline.enabled");
    public static List<String> comDStaffOnlineAliases = StreamLine.config.getConfStringList("commands.discord.staffonline.aliases");
    public static String comDStaffOnlinePerm = StreamLine.config.getConfString("commands.discord.staffonline.permission");
    // ... ... Bungee Stuff.
    // Ping.
    public static boolean comBPing = StreamLine.config.getConfBoolean("commands.bungee.ping.enabled");
    public static String comBPingBase = StreamLine.config.getConfString("commands.bungee.ping.base");
    public static List<String> comBPingAliases = StreamLine.config.getConfStringList("commands.bungee.ping.aliases");
    public static String comBPingPerm = StreamLine.config.getConfString("commands.bungee.ping.permission");
    // Plugins.
    public static boolean comBPlugins = StreamLine.config.getConfBoolean("commands.bungee.plugins.enabled");
    public static String comBPluginsBase = StreamLine.config.getConfString("commands.bungee.plugins.base");
    public static List<String> comBPluginsAliases = StreamLine.config.getConfStringList("commands.bungee.plugins.aliases");
    public static String comBPluginsPerm = StreamLine.config.getConfString("commands.bungee.plugins.permission");
    // Stream.
    public static boolean comBStream = StreamLine.config.getConfBoolean("commands.bungee.stream.enabled");
    public static String comBStreamBase = StreamLine.config.getConfString("commands.bungee.stream.base");
    public static List<String> comBStreamAliases = StreamLine.config.getConfStringList("commands.bungee.stream.aliases");
    public static String comBStreamPerm = StreamLine.config.getConfString("commands.bungee.stream.permission");
    // Report.
    public static boolean comBReport = StreamLine.config.getConfBoolean("commands.bungee.report.enabled");
    public static String comBReportBase = StreamLine.config.getConfString("commands.bungee.report.base");
    public static List<String> comBReportAliases = StreamLine.config.getConfStringList("commands.bungee.report.aliases");
    public static String comBReportPerm = StreamLine.config.getConfString("commands.bungee.report.permission");
    // StatsCommand
    public static boolean comBStats = StreamLine.config.getConfBoolean("commands.bungee.stats.enabled");
    public static String comBStatsBase = StreamLine.config.getConfString("commands.bungee.stats.base");
    public static List<String> comBStatsAliases = StreamLine.config.getConfStringList("commands.bungee.stats.aliases");
    public static String comBStatsPerm = StreamLine.config.getConfString("commands.bungee.stats.permission");
    public static boolean comBStatsOthers = StreamLine.config.getConfBoolean("commands.bungee.stats.view-others.enabled");
    public static String comBStatsPermOthers = StreamLine.config.getConfString("commands.bungee.stats.view-others.permission");
    // ... Party.
    //
    public static boolean comBParty = StreamLine.config.getConfBoolean("commands.bungee.party.enabled");
    public static String comBPartyBase = StreamLine.config.getConfString("commands.bungee.party.base");
    public static boolean comBParQuick = StreamLine.config.getConfBoolean("commands.bungee.party.quick-chat");
    public static List<String> comBParMainAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.main");
    public static String comBParPerm = StreamLine.config.getConfString("commands.bungee.party.permission");
    // Join.
    public static List<String> comBParJoinAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.join");
    // Leave.
    public static List<String> comBParLeaveAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.leave");
    // Create.
    public static List<String> comBParCreateAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.create");
    // Promote.
    public static List<String> comBParPromoteAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.promote");
    // Demote.
    public static List<String> comBParDemoteAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.demote");
    // Chat.
    public static List<String> comBParChatAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.chat");
    // List.
    public static List<String> comBParListAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.list");
    // Open.
    public static List<String> comBParOpenAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.open");
    // Close.
    public static List<String> comBParCloseAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.close");
    // Disband.
    public static List<String> comBParDisbandAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.disband");
    // Accept.
    public static List<String> comBParAcceptAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.accept");
    // Deny.
    public static List<String> comBParDenyAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.deny");
    // Invite.
    public static List<String> comBParInvAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.invite");
    // Kick.
    public static List<String> comBParKickAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.kick");
    // Mute.
    public static List<String> comBParMuteAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.mute");
    // Warp.
    public static List<String> comBParWarpAliases = StreamLine.config.getConfStringList("commands.bungee.party.aliases.warp");
    // ... Guild.
    //
    public static boolean comBGuild = StreamLine.config.getConfBoolean("commands.bungee.guild.enabled");
    public static String comBGuildBase = StreamLine.config.getConfString("commands.bungee.guild.base");
    public static boolean comBGuildQuick = StreamLine.config.getConfBoolean("commands.bungee.guild.quick-chat");
    public static String comBGuildPerm = StreamLine.config.getConfString("commands.bungee.guild.permission");
    public static List<String> comBGuildMainAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.main");
    // Join.
    public static List<String> comBGuildJoinAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.join");
    // Leave.
    public static List<String> comBGuildLeaveAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.leave");
    // Create.
    public static List<String> comBGuildCreateAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.create");
    // Promote.
    public static List<String> comBGuildPromoteAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.promote");
    // Demote.
    public static List<String> comBGuildDemoteAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.demote");
    // Chat.
    public static List<String> comBGuildChatAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.chat");
    // List.
    public static List<String> comBGuildListAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.list");
    // Open.
    public static List<String> comBGuildOpenAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.open");
    // Close.
    public static List<String> comBGuildCloseAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.close");
    // Disband.
    public static List<String> comBGuildDisbandAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.disband");
    // Accept.
    public static List<String> comBGuildAcceptAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.accept");
    // Deny.
    public static List<String> comBGuildDenyAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.deny");
    // Invite.
    public static List<String> comBGuildInvAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.invite");
    // Kick.
    public static List<String> comBGuildKickAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.kick");
    // Mute.
    public static List<String> comBGuildMuteAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.mute");
    // Warp.
    public static List<String> comBGuildWarpAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.warp");
    // Info.
    public static List<String> comBGuildInfoAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.info");
    // Rename.
    public static List<String> comBGuildRenameAliases = StreamLine.config.getConfStringList("commands.bungee.guild.aliases.rename");
    // ... Servers.
    // Lobby.
    public static boolean comBLobby = StreamLine.config.getConfBoolean("commands.bungee.servers.lobby.enabled");
    public static String comBLobbyBase = StreamLine.config.getConfString("commands.bungee.staff.lobby.base");
    public static List<String> comBLobbyAliases = StreamLine.config.getConfStringList("commands.bungee.servers.lobby.aliases");
    public static String comBLobbyEnd = StreamLine.config.getConfString("commands.bungee.servers.lobby.points-to");
    public static String comBLobbyPerm = StreamLine.config.getConfString("commands.bungee.servers.lobby.permission");
    // Fabric Fix.
    public static boolean comBFabric = StreamLine.config.getConfBoolean("commands.bungee.servers.fabric-fix.enabled");
    public static String comBFabricEnd = StreamLine.config.getConfString("commands.bungee.servers.fabric-fix.points-to");
    public static String comBFabricPerm = StreamLine.config.getConfString("commands.bungee.servers.fabric-fix.permission");
    // ... Staff.
    // GlobalOnline.
    public static boolean comBGlobalOnline = StreamLine.config.getConfBoolean("commands.bungee.staff.globalonline.enabled");
    public static String comBGlobalOnlineBase = StreamLine.config.getConfString("commands.bungee.staff.globalonline.base");
    public static List<String> comBGlobalOnlineAliases = StreamLine.config.getConfStringList("commands.bungee.staff.globalonline.aliases");
    public static String comBGlobalOnlinePerm = StreamLine.config.getConfString("commands.bungee.staff.globalonline.permission");
    // StaffChat.
    public static boolean comBStaffChat = StreamLine.config.getConfBoolean("commands.bungee.staff.staffchat.enabled");
    public static String comBStaffChatBase = StreamLine.config.getConfString("commands.bungee.staff.staffchat.base");
    public static List<String> comBStaffChatAliases = StreamLine.config.getConfStringList("commands.bungee.staff.staffchat.aliases");
    public static String comBStaffChatPerm = StreamLine.config.getConfString("commands.bungee.staff.staffchat.permission");
    // StaffOnline.
    public static boolean comBStaffOnline = StreamLine.config.getConfBoolean("commands.bungee.staff.staffonline.enabled");
    public static String comBStaffOnlineBase = StreamLine.config.getConfString("commands.bungee.staff.staffonline.base");
    public static List<String> comBStaffOnlineAliases = StreamLine.config.getConfStringList("commands.bungee.staff.staffonline.aliases");
    public static String comBStaffOnlinePerm = StreamLine.config.getConfString("commands.bungee.staff.staffonline.permission");
    // Reload.
    public static String comBReloadBase = StreamLine.config.getConfString("commands.bungee.staff.slreload.base");
    public static List<String> comBReloadAliases = StreamLine.config.getConfStringList("commands.bungee.staff.slreload.aliases");
    public static String comBReloadPerm = StreamLine.config.getConfString("commands.bungee.staff.slreload.permission");
    // Parties.
    public static boolean comBParties = StreamLine.config.getConfBoolean("commands.bungee.staff.parties.enabled");
    public static String comBPartiesBase = StreamLine.config.getConfString("commands.bungee.staff.parties.base");
    public static List<String> comBPartiesAliases = StreamLine.config.getConfStringList("commands.bungee.staff.parties.aliases");
    public static String comBPartiesPerm = StreamLine.config.getConfString("commands.bungee.staff.parties.permission");
    // Guilds.
    public static boolean comBGuilds = StreamLine.config.getConfBoolean("commands.bungee.staff.guilds.enabled");
    public static String comBGuildsBase = StreamLine.config.getConfString("commands.bungee.staff.guilds.base");
    public static List<String> comBGuildsAliases = StreamLine.config.getConfStringList("commands.bungee.staff.guilds.aliases");
    public static String comBGuildsPerm = StreamLine.config.getConfString("commands.bungee.staff.guilds.permission");
    // GetStats.
    public static boolean comBGetStats = StreamLine.config.getConfBoolean("commands.bungee.staff.getstats.enabled");
    public static String comBGetStatsBase = StreamLine.config.getConfString("commands.bungee.staff.getstats.base");
    public static List<String> comBGetStatsAliases = StreamLine.config.getConfStringList("commands.bungee.staff.getstats.aliases");
    public static String comBGetStatsPerm = StreamLine.config.getConfString("commands.bungee.staff.getstats.permission");
    // BSudo.
    public static boolean comBSudo = StreamLine.config.getConfBoolean("commands.bungee.staff.bsudo.enabled");
    public static String comBSudoBase = StreamLine.config.getConfString("commands.bungee.staff.bsudo.base");
    public static List<String> comBSudoAliases = StreamLine.config.getConfStringList("commands.bungee.staff.bsudo.aliases");
    public static String comBSudoPerm = StreamLine.config.getConfString("commands.bungee.staff.bsudo.permission");
    // SSPY.
    public static boolean comBSSPY = StreamLine.config.getConfBoolean("commands.bungee.staff.sspy.enabled");
    public static String comBSSPYBase = StreamLine.config.getConfString("commands.bungee.staff.sspy.base");
    public static List<String> comBSSPYAliases = StreamLine.config.getConfStringList("commands.bungee.staff.sspy.aliases");
    public static String comBSSPYPerm = StreamLine.config.getConfString("commands.bungee.staff.sspy.permission");
    // GSPY.
    public static boolean comBGSPY = StreamLine.config.getConfBoolean("commands.bungee.staff.gspy.enabled");
    public static String comBGSPYBase = StreamLine.config.getConfString("commands.bungee.staff.gspy.base");
    public static List<String> comBGSPYAliases = StreamLine.config.getConfStringList("commands.bungee.staff.gspy.aliases");
    public static String comBGSPYPerm = StreamLine.config.getConfString("commands.bungee.staff.gspy.permission");
    // PSPY.
    public static boolean comBPSPY = StreamLine.config.getConfBoolean("commands.bungee.staff.pspy.enabled");
    public static String comBPSPYBase = StreamLine.config.getConfString("commands.bungee.staff.pspy.base");
    public static List<String> comBPSPYAliases = StreamLine.config.getConfStringList("commands.bungee.staff.pspy.aliases");
    public static String comBPSPYPerm = StreamLine.config.getConfString("commands.bungee.staff.pspy.permission");
    // SCView.
    public static boolean comBSCView = StreamLine.config.getConfBoolean("commands.bungee.staff.scview.enabled");
    public static String comBSCViewBase = StreamLine.config.getConfString("commands.bungee.staff.scview.base");
    public static List<String> comBSCViewAliases = StreamLine.config.getConfStringList("commands.bungee.staff.scview.aliases");
    public static String comBSCViewPerm = StreamLine.config.getConfString("commands.bungee.staff.scview.permission");
    // BTag.
    public static boolean comBBTag = StreamLine.config.getConfBoolean("commands.bungee.staff.btag.enabled");
    public static String comBBTagBase = StreamLine.config.getConfString("commands.bungee.staff.btag.base");
    public static List<String> comBBTagAliases = StreamLine.config.getConfStringList("commands.bungee.staff.btag.aliases");
    public static String comBBTagPerm = StreamLine.config.getConfString("commands.bungee.staff.btag.permission");
    public static String comBBTagOPerm = StreamLine.config.getConfString("commands.bungee.staff.btag.other-perm");
    public static String comBBTagChPerm = StreamLine.config.getConfString("commands.bungee.staff.btag.change-perm");
    // Event Reload.
    public static boolean comBEReload = StreamLine.config.getConfBoolean("commands.bungee.staff.evreload.enabled");
    public static String comBEReloadBase = StreamLine.config.getConfString("commands.bungee.staff.evreload.base");
    public static List<String> comBEReloadAliases = StreamLine.config.getConfStringList("commands.bungee.staff.evreload.aliases");
    public static String comBEReloadPerm = StreamLine.config.getConfString("commands.bungee.staff.evreload.permission");
    // Network Points.
    public static boolean comBPoints = StreamLine.config.getConfBoolean("commands.bungee.staff.points.enabled");
    public static String comBPointsBase = StreamLine.config.getConfString("commands.bungee.staff.points.base");
    public static List<String> comBPointsAliases = StreamLine.config.getConfStringList("commands.bungee.staff.points.aliases");
    public static String comBPointsPerm = StreamLine.config.getConfString("commands.bungee.staff.points.permission");
    public static String comBPointsOPerm = StreamLine.config.getConfString("commands.bungee.staff.points.other-perm");
    public static String comBPointsChPerm = StreamLine.config.getConfString("commands.bungee.staff.points.change-perm");
    // Server Ping.
    public static boolean comBSPing = StreamLine.config.getConfBoolean("commands.bungee.staff.serverping.enabled");
    public static String comBSPingBase = StreamLine.config.getConfString("commands.bungee.staff.serverping.base");
    public static List<String> comBSPingAliases = StreamLine.config.getConfStringList("commands.bungee.staff.serverping.aliases");
    public static String comBSPingPerm = StreamLine.config.getConfString("commands.bungee.staff.serverping.permission");
    // Mute.
    public static boolean comBMute = StreamLine.config.getConfBoolean("commands.bungee.staff.mute.enabled");
    public static String comBMuteBase = StreamLine.config.getConfString("commands.bungee.staff.mute.base");
    public static List<String> comBMuteAliases = StreamLine.config.getConfStringList("commands.bungee.staff.mute.aliases");
    public static String comBMutePerm = StreamLine.config.getConfString("commands.bungee.staff.mute.permission");
    // Kick.
    public static boolean comBKick = StreamLine.config.getConfBoolean("commands.bungee.staff.kick.enabled");
    public static String comBKickBase = StreamLine.config.getConfString("commands.bungee.staff.kick.base");
    public static List<String> comBKickAliases = StreamLine.config.getConfStringList("commands.bungee.staff.kick.aliases");
    public static String comBKickPerm = StreamLine.config.getConfString("commands.bungee.staff.kick.permission");
    // Ban.
    public static boolean comBBan = StreamLine.config.getConfBoolean("commands.bungee.staff.ban.enabled");
    public static String comBBanBase = StreamLine.config.getConfString("commands.bungee.staff.ban.base");
    public static List<String> comBBanAliases = StreamLine.config.getConfStringList("commands.bungee.staff.ban.aliases");
    public static String comBBanPerm = StreamLine.config.getConfString("commands.bungee.staff.ban.permission");
    // Ban.
    public static boolean comBIPBan = StreamLine.config.getConfBoolean("commands.bungee.staff.ipban.enabled");
    public static String comBIPBanBase = StreamLine.config.getConfString("commands.bungee.staff.ipban.base");
    public static List<String> comBIPBanAliases = StreamLine.config.getConfStringList("commands.bungee.staff.ipban.aliases");
    public static String comBIPBanPerm = StreamLine.config.getConfString("commands.bungee.staff.ipban.permission");
    // Info.
    public static boolean comBInfo = StreamLine.config.getConfBoolean("commands.bungee.staff.info.enabled");
    public static String comBInfoBase = StreamLine.config.getConfString("commands.bungee.staff.info.base");
    public static List<String> comBInfoAliases = StreamLine.config.getConfStringList("commands.bungee.staff.info.aliases");
    public static String comBInfoPerm = StreamLine.config.getConfString("commands.bungee.staff.info.permission");
    // End.
    public static boolean comBEnd = StreamLine.config.getConfBoolean("commands.bungee.staff.end.enabled");
    public static String comBEndBase = StreamLine.config.getConfString("commands.bungee.staff.end.base");
    public static List<String> comBEndAliases = StreamLine.config.getConfStringList("commands.bungee.staff.end.aliases");
    public static String comBEndPerm = StreamLine.config.getConfString("commands.bungee.staff.end.permission");
    // // Configs.
    // Settings.
    public static boolean comBSettings = StreamLine.config.getConfBoolean("commands.bungee.configs.settings.enabled");
    public static String comBSettingsBase = StreamLine.config.getConfString("commands.bungee.configs.settings.base");
    public static List<String> comBSettingsAliases = StreamLine.config.getConfStringList("commands.bungee.configs.settings.aliases");
    public static String comBSettingsPerm = StreamLine.config.getConfString("commands.bungee.configs.settings.permission");
    // ... Messaging.
    // Ignore.
    public static boolean comBIgnore = StreamLine.config.getConfBoolean("commands.bungee.messaging.ignore.enabled");
    public static String comBIgnoreBase = StreamLine.config.getConfString("commands.bungee.messaging.ignore.base");
    public static List<String> comBIgnoreAliases = StreamLine.config.getConfStringList("commands.bungee.messaging.ignore.aliases");
    public static String comBIgnorePerm = StreamLine.config.getConfString("commands.bungee.messaging.ignore.permission");
    // Message.
    public static boolean comBMessage = StreamLine.config.getConfBoolean("commands.bungee.messaging.message.enabled");
    public static String comBMessageBase = StreamLine.config.getConfString("commands.bungee.messaging.message.base");
    public static List<String> comBMessageAliases = StreamLine.config.getConfStringList("commands.bungee.messaging.message.aliases");
    public static String comBMessagePerm = StreamLine.config.getConfString("commands.bungee.messaging.message.permission");
    // Reply.
    public static boolean comBReply = StreamLine.config.getConfBoolean("commands.bungee.messaging.reply.enabled");
    public static String comBReplyBase = StreamLine.config.getConfString("commands.bungee.messaging.reply.base");
    public static List<String> comBReplyAliases = StreamLine.config.getConfStringList("commands.bungee.messaging.reply.aliases");
    public static String comBReplyPerm = StreamLine.config.getConfString("commands.bungee.messaging.reply.permission");
    // Friend.
    public static boolean comBFriend = StreamLine.config.getConfBoolean("commands.bungee.messaging.friend.enabled");
    public static String comBFriendBase = StreamLine.config.getConfString("commands.bungee.messaging.friend.base");
    public static List<String> comBFriendAliases = StreamLine.config.getConfStringList("commands.bungee.messaging.friend.aliases");
    public static String comBFriendPerm = StreamLine.config.getConfString("commands.bungee.messaging.friend.permission");
    // // Debug.
    // Delete Stat.
    public static boolean comBDeleteStat = StreamLine.config.getConfBoolean("commands.bungee.debug.delete-stat.enabled");
    public static String comBDeleteStatBase = StreamLine.config.getConfString("commands.bungee.debug.delete-stat.base");
    public static List<String> comBDeleteStatAliases = StreamLine.config.getConfStringList("commands.bungee.debug.delete-stat.aliases");
    public static String comBDeleteStatPerm = StreamLine.config.getConfString("commands.bungee.debug.delete-stat.permission");
    // ... ... Modules.
    public static String staffPerm = StreamLine.config.getConfString("modules.staff-permission");
    // ... Discord.
    // Basics.
    public static boolean moduleDMainConsole = StreamLine.config.getConfBoolean("modules.discord.main-console");
    public static boolean moduleUseMCAvatar = StreamLine.config.getConfBoolean("modules.discord.use-mc-avatar");
    public static boolean joinsLeavesIcon = StreamLine.config.getConfBoolean("modules.discord.joins-leaves.use-bot-icon");
    public static boolean joinsLeavesAsConsole = StreamLine.config.getConfBoolean("modules.discord.joins-leaves.send-as-console");
    // Reports.
    public static boolean moduleReportsDConfirmation = StreamLine.config.getConfBoolean("modules.discord.reports.send-confirmation");
    public static boolean moduleReportToChannel = StreamLine.config.getConfBoolean("modules.discord.reports.report-to-channel");
    public static boolean moduleReportsDToMinecraft = StreamLine.config.getConfBoolean("modules.discord.reports.discord-to-minecraft");
    public static boolean moduleReportChannelPingsRole = StreamLine.config.getConfBoolean("modules.discord.report-channel-pings-a-role");
    // StaffChat.
    public static boolean moduleStaffChatToMinecraft = StreamLine.config.getConfBoolean("modules.discord.staffchat-to-minecraft");
    public static boolean moduleSCOnlyStaffRole = StreamLine.config.getConfBoolean("modules.discord.staffchat-to-minecraft-only-staff-role");
    public static String moduleStaffChatServer = StreamLine.config.getConfString("modules.discord.staffchat-server");
    // Startup / Shutdowns.
    public static boolean moduleStartups = StreamLine.config.getConfBoolean("modules.discord.startup-message");
    public static boolean moduleShutdowns = StreamLine.config.getConfBoolean("modules.discord.shutdown-message");
    // Say if...
    public static String moduleSayNotACommand = StreamLine.config.getConfString("modules.discord.say-if-not-a-command");
    public static String moduleSayCommandDisabled = StreamLine.config.getConfString("modules.discord.say-if-command-disabled");
    // Player logins / logouts.
    public static String moduleDPlayerJoins = StreamLine.config.getConfString("modules.discord.player-joins");
    public static String moduleDPlayerLeaves = StreamLine.config.getConfString("modules.discord.player-leaves");
    // ... Bungee.
    // Reports.
    public static boolean moduleReportsBConfirmation = StreamLine.config.getConfBoolean("modules.bungee.reports.send-confirmation");
    public static boolean moduleReportsMToDiscord = StreamLine.config.getConfBoolean("modules.bungee.reports.minecraft-to-discord");
    public static boolean moduleReportsSendChat = StreamLine.config.getConfBoolean("modules.bungee.reports.send-in-chat");
    // StaffChat.
    public static boolean moduleStaffChat = StreamLine.config.getConfBoolean("modules.bungee.staffchat.enabled");
    public static boolean moduleStaffChatDoPrefix = StreamLine.config.getConfBoolean("modules.bungee.staffchat.enable-prefix");
    public static String moduleStaffChatPrefix = StreamLine.config.getConfString("modules.bungee.staffchat.prefix");
    public static boolean moduleStaffChatMToDiscord = StreamLine.config.getConfBoolean("modules.bungee.staffchat.minecraft-to-discord");
    // Player logins / logouts.
//    public static String moduleBPlayerJoins = StreamLine.config.getConfString("modules.bungee.player-joins");
//    public static String moduleBPlayerJoinsPerm = StreamLine.config.getConfString("modules.bungee.joins-permission");
//    public static String moduleBPlayerLeaves = StreamLine.config.getConfString("modules.bungee.player-leaves");
//    public static String moduleBPlayerLeavesPerm = StreamLine.config.getConfString("modules.bungee.leaves-permission");
    public static String moduleBPlayerJoins = StreamLine.config.getConfString("modules.bungee.player-joins-order");
    public static String moduleBPlayerJoinsPerm = StreamLine.config.getConfString("modules.bungee.joins-permission");
    public static String moduleBPlayerLeaves = StreamLine.config.getConfString("modules.bungee.player-leaves-order");
    public static String moduleBPlayerLeavesPerm = StreamLine.config.getConfString("modules.bungee.leaves-permission");
    // ... Parties.
    public static boolean partyToDiscord = StreamLine.config.getConfBoolean("modules.bungee.parties.to-discord");
    public static int partyMax = StreamLine.config.getConfInteger("modules.bungee.parties.max-size");
    public static String partyMaxPerm = StreamLine.config.getConfString("modules.bungee.parties.base-permission");
    public static boolean partyConsoleChats = StreamLine.config.getConfBoolean("modules.bungee.parties.console.chat");
    public static boolean partyConsoleCreates = StreamLine.config.getConfBoolean("modules.bungee.parties.console.creates");
    public static boolean partyConsoleDisbands = StreamLine.config.getConfBoolean("modules.bungee.parties.console.disbands");
    public static boolean partyConsoleOpens = StreamLine.config.getConfBoolean("modules.bungee.parties.console.opens");
    public static boolean partyConsoleCloses = StreamLine.config.getConfBoolean("modules.bungee.parties.console.closes");
    public static boolean partyConsoleJoins = StreamLine.config.getConfBoolean("modules.bungee.parties.console.joins");
    public static boolean partyConsoleLeaves = StreamLine.config.getConfBoolean("modules.bungee.parties.console.leaves");
    public static boolean partyConsoleAccepts = StreamLine.config.getConfBoolean("modules.bungee.parties.console.accepts");
    public static boolean partyConsoleDenies = StreamLine.config.getConfBoolean("modules.bungee.parties.console.denies");
    public static boolean partyConsolePromotes = StreamLine.config.getConfBoolean("modules.bungee.parties.console.promotes");
    public static boolean partyConsoleDemotes = StreamLine.config.getConfBoolean("modules.bungee.parties.console.demotes");
    public static boolean partyConsoleInvites = StreamLine.config.getConfBoolean("modules.bungee.parties.console.invites");
    public static boolean partyConsoleKicks = StreamLine.config.getConfBoolean("modules.bungee.parties.console.kicks");
    public static boolean partyConsoleMutes = StreamLine.config.getConfBoolean("modules.bungee.parties.console.mutes");
    public static boolean partyConsoleWarps = StreamLine.config.getConfBoolean("modules.bungee.parties.console.warps");
    public static String partyView = StreamLine.config.getConfString("modules.bungee.parties.view-permission");
    public static boolean partySendJoins = StreamLine.config.getConfBoolean("modules.bungee.parties.send.join");
    public static boolean partySendLeaves = StreamLine.config.getConfBoolean("modules.bungee.parties.send.leave");
    // ... Guilds.
    public static boolean guildToDiscord = StreamLine.config.getConfBoolean("modules.bungee.guilds.to-discord");
    public static int guildMax = StreamLine.config.getConfInteger("modules.bungee.guilds.max-size");
    public static boolean guildConsoleChats = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.chats");
    public static boolean guildConsoleCreates = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.creates");
    public static boolean guildConsoleDisbands = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.disbands");
    public static boolean guildConsoleOpens = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.opens");
    public static boolean guildConsoleCloses = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.closes");
    public static boolean guildConsoleJoins = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.joins");
    public static boolean guildConsoleLeaves = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.leaves");
    public static boolean guildConsoleAccepts = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.accepts");
    public static boolean guildConsoleDenies = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.denies");
    public static boolean guildConsolePromotes = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.promotes");
    public static boolean guildConsoleDemotes = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.demotes");
    public static boolean guildConsoleInvites = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.invites");
    public static boolean guildConsoleKicks = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.kicks");
    public static boolean guildConsoleMutes = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.mutes");
    public static boolean guildConsoleWarps = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.warps");
    public static boolean guildConsoleRenames = StreamLine.config.getConfBoolean("modules.bungee.guilds.console.renames");
    public static int xpPerGiveG = StreamLine.config.getConfInteger("modules.bungee.guilds.xp.amount-per");
    public static int timePerGiveG = StreamLine.config.getConfInteger("modules.bungee.guilds.xp.time-per");
    public static String guildView = StreamLine.config.getConfString("modules.bungee.guilds.view-permission");
    public static int guildMaxLength = StreamLine.config.getConfInteger("modules.bungee.guilds.name.max-length");
    public static boolean guildIncludeColors = StreamLine.config.getConfBoolean("modules.bungee.guilds.name.max-includes-colors");
    public static boolean guildSendJoins = StreamLine.config.getConfBoolean("modules.bungee.guilds.send.join");
    public static boolean guildSendLeaves = StreamLine.config.getConfBoolean("modules.bungee.guilds.send.leave");
    // ... Sudo.
    public static String noSudoPerm = StreamLine.config.getConfString("modules.bungee.sudo.no-sudo-permission");
    // ... Stats.
    public static boolean statsTell = StreamLine.config.getConfBoolean("modules.bungee.stats.tell-when-create");
    public static int xpPerGiveP = StreamLine.config.getConfInteger("modules.bungee.stats.xp.amount-per");
    public static int timePerGiveP = StreamLine.config.getConfInteger("modules.bungee.stats.xp.time-per");
    public static int cachedPClear = StreamLine.config.getConfInteger("modules.bungee.stats.cache-clear");
    public static boolean updateDisplayNames = StreamLine.config.getConfBoolean("modules.bungee.stats.update-display-names");
    // ... Redirect.
    public static boolean redirectEnabled = StreamLine.config.getConfBoolean("modules.bungee.redirect.enabled");
    public static String redirectPre = StreamLine.config.getConfString("modules.bungee.redirect.permission-prefix");
    public static String redirectMain = StreamLine.config.getConfString("modules.bungee.redirect.main");
    // Version Block.
    public static boolean vbEnabled = StreamLine.config.getConfBoolean("modules.bungee.redirect.version-block.enabled");
    public static String vbOverridePerm = StreamLine.config.getConfString("modules.bungee.redirect.version-block.override-permission");
    public static String vbServerFile = StreamLine.config.getConfString("modules.bungee.redirect.version-block.server-permission-file");
    // Lobbies.
    public static boolean lobbies = StreamLine.config.getConfBoolean("modules.bungee.redirect.lobbies.enabled");
    public static String lobbiesFile = StreamLine.config.getConfString("modules.bungee.redirect.lobbies.file");
    public static int lobbyTimeOut = StreamLine.config.getConfInteger("modules.bungee.redirect.lobbies.time-out");
    // Points.
    public static int pointsDefault = StreamLine.config.getConfInteger("modules.bungee.points.default");
    // Tags.
    public static List<String> tagsDefaults = StreamLine.config.getConfStringList("modules.bungee.tags.defaults");
    // Events.
    public static boolean events = StreamLine.config.getConfBoolean("modules.bungee.events.enabled");
    public static String eventsFolder = StreamLine.config.getConfString("modules.bungee.events.folder");
    public static boolean eventsWhenEmpty = StreamLine.config.getConfBoolean("modules.bungee.events.add-default-when-empty");
    // Errors.
    public static boolean errSendToConsole = StreamLine.config.getConfBoolean("modules.bungee.user-errors.send-to-console");
    // ... Punishments.
    // Mutes.
    public static boolean punMutes = StreamLine.config.getConfBoolean("modules.bungee.punishments.mutes.enabled");
    public static boolean punMutesHard = StreamLine.config.getConfBoolean("modules.bungee.punishments.mutes.hard-mutes");
    public static boolean punMutesReplaceable = StreamLine.config.getConfBoolean("modules.bungee.punishments.mutes.replaceable");
    public static boolean punMutesDiscord = StreamLine.config.getConfBoolean("modules.bungee.punishments.mutes.discord");
    public static String punMutesBypass = StreamLine.config.getConfString("modules.bungee.punishments.mutes.by-pass");
    // Kicks
    public static boolean punKicksDiscord = StreamLine.config.getConfBoolean("modules.bungee.punishments.kicks.discord");
    public static String punKicksBypass = StreamLine.config.getConfString("modules.bungee.punishments.kicks.by-pass");
    // Bans.
    public static boolean punBans = StreamLine.config.getConfBoolean("modules.bungee.punishments.bans.enabled");
    public static boolean punBansReplaceable = StreamLine.config.getConfBoolean("modules.bungee.punishments.bans.replaceable");
    public static boolean punBansDiscord = StreamLine.config.getConfBoolean("modules.bungee.punishments.bans.discord");
    public static String punBansBypass = StreamLine.config.getConfString("modules.bungee.punishments.bans.by-pass");
    // IPBans.
    public static boolean punIPBans = StreamLine.config.getConfBoolean("modules.bungee.punishments.ipbans.enabled");
    public static boolean punIPBansReplaceable = StreamLine.config.getConfBoolean("modules.bungee.punishments.ipbans.replaceable");
    public static boolean punIPBansDiscord = StreamLine.config.getConfBoolean("modules.bungee.punishments.ipbans.discord");
    public static String punIPBansBypass = StreamLine.config.getConfString("modules.bungee.punishments.ipbans.by-pass");
    // Messaging.
    public static String messViewPerm = StreamLine.config.getConfString("modules.bungee.messaging.view-permission");
    public static String messReplyTo = StreamLine.config.getConfString("modules.bungee.messaging.reply-to");
    // Server ConfigHandler.
    public static boolean sc = StreamLine.config.getConfBoolean("modules.bungee.server-config.enabled");
    public static boolean scMakeDefault = StreamLine.config.getConfBoolean("modules.bungee.server-config.make-if-not-exist");
    public static boolean scMOTD = StreamLine.config.getConfBoolean("modules.bungee.server-config.motd");
    public static boolean scVersion = StreamLine.config.getConfBoolean("modules.bungee.server-config.version");
    public static boolean scSample = StreamLine.config.getConfBoolean("modules.bungee.server-config.sample");
    public static boolean scMaxPlayers = StreamLine.config.getConfBoolean("modules.bungee.server-config.max-players");
    public static boolean scOnlinePlayers = StreamLine.config.getConfBoolean("modules.bungee.server-config.online-players");
    // Console.
    public static String consoleName = StreamLine.config.getConfString("modules.bungee.console.name");
    public static String consoleDisplayName = StreamLine.config.getConfString("modules.bungee.console.display-name");
    public static List<String> consoleDefaultTags = StreamLine.config.getConfStringList("modules.bungee.console.default-tags");
    public static int consoleDefaultPoints = StreamLine.config.getConfInteger("modules.bungee.console.default-points");
    public static String consoleServer = StreamLine.config.getConfString("modules.bungee.console.server");
    // On Close.
    public static boolean onCloseSettings = StreamLine.config.getConfBoolean("modules.bungee.on-close.save-settings");
    public static boolean onCloseMain = StreamLine.config.getConfBoolean("modules.bungee.on-close.save-main");
    public static boolean onCloseSafeKick = StreamLine.config.getConfBoolean("modules.bungee.on-close.safe-kick");
    public static boolean onCloseKickMessage = StreamLine.config.getConfBoolean("modules.bungee.on-close.kick-message");
    public static boolean onCloseHackEnd = StreamLine.config.getConfBoolean("modules.bungee.on-close.hack-end-command");
    // Spies.
    public static List<String> viewSelfAliases = StreamLine.config.getConfStringList("modules.bungee.spies.view-self-aliases");
}
