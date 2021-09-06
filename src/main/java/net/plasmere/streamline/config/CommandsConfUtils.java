package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;

import java.util.List;

public class CommandsConfUtils {
    // ... ... ... Commands.
    // ... ... Discord Stuff.
    // Commands.
    public static boolean comDCommands = StreamLine.config.getCommandBoolean("commands.discord.help.enabled");
    public static List<String> comDCommandsAliases = StreamLine.config.getCommandStringList("commands.discord.help.aliases");
    public static String comDCommandsPerm = StreamLine.config.getCommandString("commands.discord.help.permission");
    // Online.
    public static boolean comDOnline = StreamLine.config.getCommandBoolean("commands.discord.online.enabled");
    public static List<String> comDOnlineAliases = StreamLine.config.getCommandStringList("commands.discord.online.aliases");
    public static String comDOnlinePerm = StreamLine.config.getCommandString("commands.discord.online.permission");
    // Report.
    public static boolean comDReport = StreamLine.config.getCommandBoolean("commands.discord.report.enabled");
    public static List<String> comDReportAliases = StreamLine.config.getCommandStringList("commands.discord.report.aliases");
    public static String comDReportPerm = StreamLine.config.getCommandString("commands.discord.report.permission");
    // StaffChat.
    public static boolean comDStaffChat = StreamLine.config.getCommandBoolean("commands.discord.staffchat.enabled");
    public static List<String> comDStaffChatAliases = StreamLine.config.getCommandStringList("commands.discord.staffchat.aliases");
    public static String comDStaffChatPerm = StreamLine.config.getCommandString("commands.discord.staffchat.permission");
    // StaffOnline.
    public static boolean comDStaffOnline = StreamLine.config.getCommandBoolean("commands.discord.staffonline.enabled");
    public static List<String> comDStaffOnlineAliases = StreamLine.config.getCommandStringList("commands.discord.staffonline.aliases");
    public static String comDStaffOnlinePerm = StreamLine.config.getCommandString("commands.discord.staffonline.permission");
    // ... ... Bungee Stuff.
    // Ping.
    public static boolean comBPing = StreamLine.config.getCommandBoolean("commands.bungee.ping.enabled");
    public static String comBPingBase = StreamLine.config.getCommandString("commands.bungee.ping.base");
    public static List<String> comBPingAliases = StreamLine.config.getCommandStringList("commands.bungee.ping.aliases");
    public static String comBPingPerm = StreamLine.config.getCommandString("commands.bungee.ping.permission");
    // Plugins.
    public static boolean comBPlugins = StreamLine.config.getCommandBoolean("commands.bungee.plugins.enabled");
    public static String comBPluginsBase = StreamLine.config.getCommandString("commands.bungee.plugins.base");
    public static List<String> comBPluginsAliases = StreamLine.config.getCommandStringList("commands.bungee.plugins.aliases");
    public static String comBPluginsPerm = StreamLine.config.getCommandString("commands.bungee.plugins.permission");
    // Stream.
    public static boolean comBStream = StreamLine.config.getCommandBoolean("commands.bungee.stream.enabled");
    public static String comBStreamBase = StreamLine.config.getCommandString("commands.bungee.stream.base");
    public static List<String> comBStreamAliases = StreamLine.config.getCommandStringList("commands.bungee.stream.aliases");
    public static String comBStreamPerm = StreamLine.config.getCommandString("commands.bungee.stream.permission");
    // Report.
    public static boolean comBReport = StreamLine.config.getCommandBoolean("commands.bungee.report.enabled");
    public static String comBReportBase = StreamLine.config.getCommandString("commands.bungee.report.base");
    public static List<String> comBReportAliases = StreamLine.config.getCommandStringList("commands.bungee.report.aliases");
    public static String comBReportPerm = StreamLine.config.getCommandString("commands.bungee.report.permission");
    // StatsCommand
    public static boolean comBStats = StreamLine.config.getCommandBoolean("commands.bungee.stats.enabled");
    public static String comBStatsBase = StreamLine.config.getCommandString("commands.bungee.stats.base");
    public static List<String> comBStatsAliases = StreamLine.config.getCommandStringList("commands.bungee.stats.aliases");
    public static String comBStatsPerm = StreamLine.config.getCommandString("commands.bungee.stats.permission");
    public static boolean comBStatsOthers = StreamLine.config.getCommandBoolean("commands.bungee.stats.view-others.enabled");
    public static String comBStatsPermOthers = StreamLine.config.getCommandString("commands.bungee.stats.view-others.permission");
    // ... Party.
    //
    public static boolean comBParty = StreamLine.config.getCommandBoolean("commands.bungee.party.enabled");
    public static String comBPartyBase = StreamLine.config.getCommandString("commands.bungee.party.base");
    public static boolean comBParQuick = StreamLine.config.getCommandBoolean("commands.bungee.party.quick-chat");
    public static List<String> comBParMainAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.main");
    public static String comBParPerm = StreamLine.config.getCommandString("commands.bungee.party.permission");
    // Join.
    public static List<String> comBParJoinAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.join");
    // Leave.
    public static List<String> comBParLeaveAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.leave");
    // Create.
    public static List<String> comBParCreateAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.create");
    // Promote.
    public static List<String> comBParPromoteAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.promote");
    // Demote.
    public static List<String> comBParDemoteAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.demote");
    // Chat.
    public static List<String> comBParChatAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.chat");
    // List.
    public static List<String> comBParListAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.list");
    // Open.
    public static List<String> comBParOpenAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.open");
    // Close.
    public static List<String> comBParCloseAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.close");
    // Disband.
    public static List<String> comBParDisbandAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.disband");
    // Accept.
    public static List<String> comBParAcceptAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.accept");
    // Deny.
    public static List<String> comBParDenyAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.deny");
    // Invite.
    public static List<String> comBParInvAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.invite");
    // Kick.
    public static List<String> comBParKickAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.kick");
    // Mute.
    public static List<String> comBParMuteAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.mute");
    // Warp.
    public static List<String> comBParWarpAliases = StreamLine.config.getCommandStringList("commands.bungee.party.aliases.warp");
    // ... Guild.
    //
    public static boolean comBGuild = StreamLine.config.getCommandBoolean("commands.bungee.guild.enabled");
    public static String comBGuildBase = StreamLine.config.getCommandString("commands.bungee.guild.base");
    public static boolean comBGuildQuick = StreamLine.config.getCommandBoolean("commands.bungee.guild.quick-chat");
    public static String comBGuildPerm = StreamLine.config.getCommandString("commands.bungee.guild.permission");
    public static List<String> comBGuildMainAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.main");
    // Join.
    public static List<String> comBGuildJoinAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.join");
    // Leave.
    public static List<String> comBGuildLeaveAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.leave");
    // Create.
    public static List<String> comBGuildCreateAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.create");
    // Promote.
    public static List<String> comBGuildPromoteAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.promote");
    // Demote.
    public static List<String> comBGuildDemoteAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.demote");
    // Chat.
    public static List<String> comBGuildChatAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.chat");
    // List.
    public static List<String> comBGuildListAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.list");
    // Open.
    public static List<String> comBGuildOpenAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.open");
    // Close.
    public static List<String> comBGuildCloseAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.close");
    // Disband.
    public static List<String> comBGuildDisbandAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.disband");
    // Accept.
    public static List<String> comBGuildAcceptAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.accept");
    // Deny.
    public static List<String> comBGuildDenyAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.deny");
    // Invite.
    public static List<String> comBGuildInvAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.invite");
    // Kick.
    public static List<String> comBGuildKickAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.kick");
    // Mute.
    public static List<String> comBGuildMuteAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.mute");
    // Warp.
    public static List<String> comBGuildWarpAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.warp");
    // Info.
    public static List<String> comBGuildInfoAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.info");
    // Rename.
    public static List<String> comBGuildRenameAliases = StreamLine.config.getCommandStringList("commands.bungee.guild.aliases.rename");
    // ... Servers.
    // Lobby.
    public static boolean comBLobby = StreamLine.config.getCommandBoolean("commands.bungee.servers.lobby.enabled");
    public static String comBLobbyBase = StreamLine.config.getCommandString("commands.bungee.staff.lobby.base");
    public static List<String> comBLobbyAliases = StreamLine.config.getCommandStringList("commands.bungee.servers.lobby.aliases");
    public static String comBLobbyEnd = StreamLine.config.getCommandString("commands.bungee.servers.lobby.points-to");
    public static String comBLobbyPerm = StreamLine.config.getCommandString("commands.bungee.servers.lobby.permission");
    // Fabric Fix.
    public static boolean comBFabric = StreamLine.config.getCommandBoolean("commands.bungee.servers.fabric-fix.enabled");
    public static String comBFabricEnd = StreamLine.config.getCommandString("commands.bungee.servers.fabric-fix.points-to");
    public static String comBFabricPerm = StreamLine.config.getCommandString("commands.bungee.servers.fabric-fix.permission");
    // ... Staff.
    // GlobalOnline.
    public static boolean comBGlobalOnline = StreamLine.config.getCommandBoolean("commands.bungee.staff.globalonline.enabled");
    public static String comBGlobalOnlineBase = StreamLine.config.getCommandString("commands.bungee.staff.globalonline.base");
    public static List<String> comBGlobalOnlineAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.globalonline.aliases");
    public static String comBGlobalOnlinePerm = StreamLine.config.getCommandString("commands.bungee.staff.globalonline.permission");
    // StaffChat.
    public static boolean comBStaffChat = StreamLine.config.getCommandBoolean("commands.bungee.staff.staffchat.enabled");
    public static String comBStaffChatBase = StreamLine.config.getCommandString("commands.bungee.staff.staffchat.base");
    public static List<String> comBStaffChatAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.staffchat.aliases");
    public static String comBStaffChatPerm = StreamLine.config.getCommandString("commands.bungee.staff.staffchat.permission");
    // StaffOnline.
    public static boolean comBStaffOnline = StreamLine.config.getCommandBoolean("commands.bungee.staff.staffonline.enabled");
    public static String comBStaffOnlineBase = StreamLine.config.getCommandString("commands.bungee.staff.staffonline.base");
    public static List<String> comBStaffOnlineAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.staffonline.aliases");
    public static String comBStaffOnlinePerm = StreamLine.config.getCommandString("commands.bungee.staff.staffonline.permission");
    // Reload.
    public static String comBReloadBase = StreamLine.config.getCommandString("commands.bungee.staff.slreload.base");
    public static List<String> comBReloadAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.slreload.aliases");
    public static String comBReloadPerm = StreamLine.config.getCommandString("commands.bungee.staff.slreload.permission");
    // Parties.
    public static boolean comBParties = StreamLine.config.getCommandBoolean("commands.bungee.staff.parties.enabled");
    public static String comBPartiesBase = StreamLine.config.getCommandString("commands.bungee.staff.parties.base");
    public static List<String> comBPartiesAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.parties.aliases");
    public static String comBPartiesPerm = StreamLine.config.getCommandString("commands.bungee.staff.parties.permission");
    // Guilds.
    public static boolean comBGuilds = StreamLine.config.getCommandBoolean("commands.bungee.staff.guilds.enabled");
    public static String comBGuildsBase = StreamLine.config.getCommandString("commands.bungee.staff.guilds.base");
    public static List<String> comBGuildsAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.guilds.aliases");
    public static String comBGuildsPerm = StreamLine.config.getCommandString("commands.bungee.staff.guilds.permission");
    // GetStats.
    public static boolean comBGetStats = StreamLine.config.getCommandBoolean("commands.bungee.staff.getstats.enabled");
    public static String comBGetStatsBase = StreamLine.config.getCommandString("commands.bungee.staff.getstats.base");
    public static List<String> comBGetStatsAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.getstats.aliases");
    public static String comBGetStatsPerm = StreamLine.config.getCommandString("commands.bungee.staff.getstats.permission");
    // BSudo.
    public static boolean comBSudo = StreamLine.config.getCommandBoolean("commands.bungee.staff.bsudo.enabled");
    public static String comBSudoBase = StreamLine.config.getCommandString("commands.bungee.staff.bsudo.base");
    public static List<String> comBSudoAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.bsudo.aliases");
    public static String comBSudoPerm = StreamLine.config.getCommandString("commands.bungee.staff.bsudo.permission");
    // SSPY.
    public static boolean comBSSPY = StreamLine.config.getCommandBoolean("commands.bungee.staff.sspy.enabled");
    public static String comBSSPYBase = StreamLine.config.getCommandString("commands.bungee.staff.sspy.base");
    public static List<String> comBSSPYAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.sspy.aliases");
    public static String comBSSPYPerm = StreamLine.config.getCommandString("commands.bungee.staff.sspy.permission");
    // GSPY.
    public static boolean comBGSPY = StreamLine.config.getCommandBoolean("commands.bungee.staff.gspy.enabled");
    public static String comBGSPYBase = StreamLine.config.getCommandString("commands.bungee.staff.gspy.base");
    public static List<String> comBGSPYAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.gspy.aliases");
    public static String comBGSPYPerm = StreamLine.config.getCommandString("commands.bungee.staff.gspy.permission");
    // PSPY.
    public static boolean comBPSPY = StreamLine.config.getCommandBoolean("commands.bungee.staff.pspy.enabled");
    public static String comBPSPYBase = StreamLine.config.getCommandString("commands.bungee.staff.pspy.base");
    public static List<String> comBPSPYAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.pspy.aliases");
    public static String comBPSPYPerm = StreamLine.config.getCommandString("commands.bungee.staff.pspy.permission");
    // SCView.
    public static boolean comBSCView = StreamLine.config.getCommandBoolean("commands.bungee.staff.scview.enabled");
    public static String comBSCViewBase = StreamLine.config.getCommandString("commands.bungee.staff.scview.base");
    public static List<String> comBSCViewAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.scview.aliases");
    public static String comBSCViewPerm = StreamLine.config.getCommandString("commands.bungee.staff.scview.permission");
    // BTag.
    public static boolean comBBTag = StreamLine.config.getCommandBoolean("commands.bungee.staff.btag.enabled");
    public static String comBBTagBase = StreamLine.config.getCommandString("commands.bungee.staff.btag.base");
    public static List<String> comBBTagAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.btag.aliases");
    public static String comBBTagPerm = StreamLine.config.getCommandString("commands.bungee.staff.btag.permission");
    public static String comBBTagOPerm = StreamLine.config.getCommandString("commands.bungee.staff.btag.other-perm");
    public static String comBBTagChPerm = StreamLine.config.getCommandString("commands.bungee.staff.btag.change-perm");
    // Event Reload.
    public static boolean comBEReload = StreamLine.config.getCommandBoolean("commands.bungee.staff.evreload.enabled");
    public static String comBEReloadBase = StreamLine.config.getCommandString("commands.bungee.staff.evreload.base");
    public static List<String> comBEReloadAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.evreload.aliases");
    public static String comBEReloadPerm = StreamLine.config.getCommandString("commands.bungee.staff.evreload.permission");
    // Network Points.
    public static boolean comBPoints = StreamLine.config.getCommandBoolean("commands.bungee.staff.points.enabled");
    public static String comBPointsBase = StreamLine.config.getCommandString("commands.bungee.staff.points.base");
    public static List<String> comBPointsAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.points.aliases");
    public static String comBPointsPerm = StreamLine.config.getCommandString("commands.bungee.staff.points.permission");
    public static String comBPointsOPerm = StreamLine.config.getCommandString("commands.bungee.staff.points.other-perm");
    public static String comBPointsChPerm = StreamLine.config.getCommandString("commands.bungee.staff.points.change-perm");
    // Server Ping.
    public static boolean comBSPing = StreamLine.config.getCommandBoolean("commands.bungee.staff.serverping.enabled");
    public static String comBSPingBase = StreamLine.config.getCommandString("commands.bungee.staff.serverping.base");
    public static List<String> comBSPingAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.serverping.aliases");
    public static String comBSPingPerm = StreamLine.config.getCommandString("commands.bungee.staff.serverping.permission");
    // Mute.
    public static boolean comBMute = StreamLine.config.getCommandBoolean("commands.bungee.staff.mute.enabled");
    public static String comBMuteBase = StreamLine.config.getCommandString("commands.bungee.staff.mute.base");
    public static List<String> comBMuteAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.mute.aliases");
    public static String comBMutePerm = StreamLine.config.getCommandString("commands.bungee.staff.mute.permission");
    // Kick.
    public static boolean comBKick = StreamLine.config.getCommandBoolean("commands.bungee.staff.kick.enabled");
    public static String comBKickBase = StreamLine.config.getCommandString("commands.bungee.staff.kick.base");
    public static List<String> comBKickAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.kick.aliases");
    public static String comBKickPerm = StreamLine.config.getCommandString("commands.bungee.staff.kick.permission");
    // Ban.
    public static boolean comBBan = StreamLine.config.getCommandBoolean("commands.bungee.staff.ban.enabled");
    public static String comBBanBase = StreamLine.config.getCommandString("commands.bungee.staff.ban.base");
    public static List<String> comBBanAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.ban.aliases");
    public static String comBBanPerm = StreamLine.config.getCommandString("commands.bungee.staff.ban.permission");
    // Ban.
    public static boolean comBIPBan = StreamLine.config.getCommandBoolean("commands.bungee.staff.ipban.enabled");
    public static String comBIPBanBase = StreamLine.config.getCommandString("commands.bungee.staff.ipban.base");
    public static List<String> comBIPBanAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.ipban.aliases");
    public static String comBIPBanPerm = StreamLine.config.getCommandString("commands.bungee.staff.ipban.permission");
    // Info.
    public static boolean comBInfo = StreamLine.config.getCommandBoolean("commands.bungee.staff.info.enabled");
    public static String comBInfoBase = StreamLine.config.getCommandString("commands.bungee.staff.info.base");
    public static List<String> comBInfoAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.info.aliases");
    public static String comBInfoPerm = StreamLine.config.getCommandString("commands.bungee.staff.info.permission");
    // End.
    public static boolean comBEnd = StreamLine.config.getCommandBoolean("commands.bungee.staff.end.enabled");
    public static String comBEndBase = StreamLine.config.getCommandString("commands.bungee.staff.end.base");
    public static List<String> comBEndAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.end.aliases");
    public static String comBEndPerm = StreamLine.config.getCommandString("commands.bungee.staff.end.permission");
    // B-Teleport.
    public static boolean comBTeleport = StreamLine.config.getCommandBoolean("commands.bungee.staff.bteleport.enabled");
    public static String comBTeleportBase = StreamLine.config.getCommandString("commands.bungee.staff.bteleport.base");
    public static List<String> comBTeleportAliases = StreamLine.config.getCommandStringList("commands.bungee.staff.bteleport.aliases");
    public static String comBTeleportPerm = StreamLine.config.getCommandString("commands.bungee.staff.bteleport.permission");
    // // Configs.
    // Settings.
    public static boolean comBSettings = StreamLine.config.getCommandBoolean("commands.bungee.configs.settings.enabled");
    public static String comBSettingsBase = StreamLine.config.getCommandString("commands.bungee.configs.settings.base");
    public static List<String> comBSettingsAliases = StreamLine.config.getCommandStringList("commands.bungee.configs.settings.aliases");
    public static String comBSettingsPerm = StreamLine.config.getCommandString("commands.bungee.configs.settings.permission");
    // Language.
    public static boolean comBLang = StreamLine.config.getCommandBoolean("commands.bungee.configs.language.enabled");
    public static String comBLangBase = StreamLine.config.getCommandString("commands.bungee.configs.language.base");
    public static List<String> comBLangAliases = StreamLine.config.getCommandStringList("commands.bungee.configs.language.aliases");
    public static String comBLangPerm = StreamLine.config.getCommandString("commands.bungee.configs.language.permission");
    // ... Messaging.
    // Ignore.
    public static boolean comBIgnore = StreamLine.config.getCommandBoolean("commands.bungee.messaging.ignore.enabled");
    public static String comBIgnoreBase = StreamLine.config.getCommandString("commands.bungee.messaging.ignore.base");
    public static List<String> comBIgnoreAliases = StreamLine.config.getCommandStringList("commands.bungee.messaging.ignore.aliases");
    public static String comBIgnorePerm = StreamLine.config.getCommandString("commands.bungee.messaging.ignore.permission");
    // Message.
    public static boolean comBMessage = StreamLine.config.getCommandBoolean("commands.bungee.messaging.message.enabled");
    public static String comBMessageBase = StreamLine.config.getCommandString("commands.bungee.messaging.message.base");
    public static List<String> comBMessageAliases = StreamLine.config.getCommandStringList("commands.bungee.messaging.message.aliases");
    public static String comBMessagePerm = StreamLine.config.getCommandString("commands.bungee.messaging.message.permission");
    // Reply.
    public static boolean comBReply = StreamLine.config.getCommandBoolean("commands.bungee.messaging.reply.enabled");
    public static String comBReplyBase = StreamLine.config.getCommandString("commands.bungee.messaging.reply.base");
    public static List<String> comBReplyAliases = StreamLine.config.getCommandStringList("commands.bungee.messaging.reply.aliases");
    public static String comBReplyPerm = StreamLine.config.getCommandString("commands.bungee.messaging.reply.permission");
    // Friend.
    public static boolean comBFriend = StreamLine.config.getCommandBoolean("commands.bungee.messaging.friend.enabled");
    public static String comBFriendBase = StreamLine.config.getCommandString("commands.bungee.messaging.friend.base");
    public static List<String> comBFriendAliases = StreamLine.config.getCommandStringList("commands.bungee.messaging.friend.aliases");
    public static String comBFriendPerm = StreamLine.config.getCommandString("commands.bungee.messaging.friend.permission");
    // // Debug.
    // Delete Stat.
    public static boolean comBDeleteStat = StreamLine.config.getCommandBoolean("commands.bungee.debug.delete-stat.enabled");
    public static String comBDeleteStatBase = StreamLine.config.getCommandString("commands.bungee.debug.delete-stat.base");
    public static List<String> comBDeleteStatAliases = StreamLine.config.getCommandStringList("commands.bungee.debug.delete-stat.aliases");
    public static String comBDeleteStatPerm = StreamLine.config.getCommandString("commands.bungee.debug.delete-stat.permission");
}
