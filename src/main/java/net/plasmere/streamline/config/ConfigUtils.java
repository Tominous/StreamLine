package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConfigUtils {
    // Config //
//    public static final String s = StreamLine.getConfig().getConfString("");
    // Important.
    public static final String version = StreamLine.getConfig().getConfString("version");
    // Debug.
    public static final boolean debug = StreamLine.getConfig().getConfBoolean("debug");
    // ... Basics.
    // Links.
    public static final String linkPre = StreamLine.getConfig().getConfString("link-prefix");
//    public static final String linkSuff = StreamLine.getConfig().getConfString("link.suffix");
    // Bot Stuff.
    public static final String botPrefix = StreamLine.getConfig().getConfString("bot.prefix");
    public static final String botToken = StreamLine.getConfig().getConfString("bot.token");
    public static final String botStatusMessage = StreamLine.getConfig().getConfString("bot.server-ip");
    // ... Discord.
    // Text Channels.
    public static final String textChannelReports = StreamLine.getConfig().getConfString("discord.text-channels.reports");
    public static final String textChannelStaffChat = StreamLine.getConfig().getConfString("discord.text-channels.staffchat");
    public static final String textChannelOfflineOnline = StreamLine.getConfig().getConfString("discord.text-channels.offline-online");
    public static final String textChannelBJoins = StreamLine.getConfig().getConfString("discord.text-channels.bungee-joins");
    public static final String textChannelBLeaves = StreamLine.getConfig().getConfString("discord.text-channels.bungee-leaves");
    public static final String textChannelBConsole = StreamLine.getConfig().getConfString("discord.text-channels.console");
    public static final String textChannelGuilds = StreamLine.getConfig().getConfString("discord.text-channels.guilds");
    public static final String textChannelParties = StreamLine.getConfig().getConfString("discord.text-channels.parties");
    // Roles.
    public static final String roleReports = StreamLine.getConfig().getConfString("discord.roles.reports");
    public static final String roleStaff = StreamLine.getConfig().getConfString("discord.roles.staff");
    // ... ... ... Commands.
    // ... ... Discord Stuff.
    // Commands.
    public static final boolean comDCommands = StreamLine.getConfig().getConfBoolean("commands.discord.help.enabled");
    public static final List<String> comDCommandsAliases = StreamLine.getConfig().getConfStringList("commands.discord.help.aliases");
    public static final String comDCommandsPerm = StreamLine.getConfig().getConfString("commands.discord.help.permission");
    // Online.
    public static final boolean comDOnline = StreamLine.getConfig().getConfBoolean("commands.discord.online.enabled");
    public static final List<String> comDOnlineAliases = StreamLine.getConfig().getConfStringList("commands.discord.online.aliases");
    public static final String comDOnlinePerm = StreamLine.getConfig().getConfString("commands.discord.online.permission");
    // Report.
    public static final boolean comDReport = StreamLine.getConfig().getConfBoolean("commands.discord.report.enabled");
    public static final List<String> comDReportAliases = StreamLine.getConfig().getConfStringList("commands.discord.report.aliases");
    public static final String comDReportPerm = StreamLine.getConfig().getConfString("commands.discord.report.permission");
    // StaffChat.
    public static final boolean comDStaffChat = StreamLine.getConfig().getConfBoolean("commands.discord.staffchat.enabled");
    public static final List<String> comDStaffChatAliases = StreamLine.getConfig().getConfStringList("commands.discord.staffchat.aliases");
    public static final String comDStaffChatPerm = StreamLine.getConfig().getConfString("commands.discord.staffchat.permission");
    // StaffOnline.
    public static final boolean comDStaffOnline = StreamLine.getConfig().getConfBoolean("commands.discord.staffonline.enabled");
    public static final List<String> comDStaffOnlineAliases = StreamLine.getConfig().getConfStringList("commands.discord.staffonline.aliases");
    public static final String comDStaffOnlinePerm = StreamLine.getConfig().getConfString("commands.discord.staffonline.permission");
    // ... ... Bungee Stuff.
    // Ping.
    public static final boolean comBPing = StreamLine.getConfig().getConfBoolean("commands.bungee.ping.enabled");
    public static final String comBPingBase = StreamLine.getConfig().getConfString("commands.bungee.ping.base");
    public static final List<String> comBPingAliases = StreamLine.getConfig().getConfStringList("commands.bungee.ping.aliases");
    public static final String comBPingPerm = StreamLine.getConfig().getConfString("commands.bungee.ping.permission");
    // Plugins.
    public static final boolean comBPlugins = StreamLine.getConfig().getConfBoolean("commands.bungee.plugins.enabled");
    public static final String comBPluginsBase = StreamLine.getConfig().getConfString("commands.bungee.plugins.base");
    public static final List<String> comBPluginsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.plugins.aliases");
    public static final String comBPluginsPerm = StreamLine.getConfig().getConfString("commands.bungee.plugins.permission");
    // Stream.
    public static final boolean comBStream = StreamLine.getConfig().getConfBoolean("commands.bungee.stream.enabled");
    public static final String comBStreamBase = StreamLine.getConfig().getConfString("commands.bungee.stream.base");
    public static final List<String> comBStreamAliases = StreamLine.getConfig().getConfStringList("commands.bungee.stream.aliases");
    public static final String comBStreamPerm = StreamLine.getConfig().getConfString("commands.bungee.stream.permission");
    // Report.
    public static final boolean comBReport = StreamLine.getConfig().getConfBoolean("commands.bungee.report.enabled");
    public static final String comBReportBase = StreamLine.getConfig().getConfString("commands.bungee.report.base");
    public static final List<String> comBReportAliases = StreamLine.getConfig().getConfStringList("commands.bungee.report.aliases");
    public static final String comBReportPerm = StreamLine.getConfig().getConfString("commands.bungee.report.permission");
    // StatsCommand
    public static final boolean comBStats = StreamLine.getConfig().getConfBoolean("commands.bungee.stats.enabled");
    public static final String comBStatsBase = StreamLine.getConfig().getConfString("commands.bungee.stats.base");
    public static final List<String> comBStatsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.stats.aliases");
    public static final String comBStatsPerm = StreamLine.getConfig().getConfString("commands.bungee.stats.permission");
    public static final boolean comBStatsOthers = StreamLine.getConfig().getConfBoolean("commands.bungee.stats.view-others.enabled");
    public static final String comBStatsPermOthers = StreamLine.getConfig().getConfString("commands.bungee.stats.view-others.permission");
    // ... Party.
    //
    public static final boolean comBParty = StreamLine.getConfig().getConfBoolean("commands.bungee.party.enabled");
    public static final String comBPartyBase = StreamLine.getConfig().getConfString("commands.bungee.party.base");
    public static final boolean comBParQuick = StreamLine.getConfig().getConfBoolean("commands.bungee.party.quick-chat");
    public static final List<String> comBParMainAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.main");
    public static final String comBParPerm = StreamLine.getConfig().getConfString("commands.bungee.party.permission");
    // Join.
    public static final List<String> comBParJoinAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.join");
    // Leave.
    public static final List<String> comBParLeaveAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.leave");
    // Create.
    public static final List<String> comBParCreateAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.create");
    // Promote.
    public static final List<String> comBParPromoteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.promote");
    // Demote.
    public static final List<String> comBParDemoteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.demote");
    // Chat.
    public static final List<String> comBParChatAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.chat");
    // List.
    public static final List<String> comBParListAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.list");
    // Open.
    public static final List<String> comBParOpenAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.open");
    // Close.
    public static final List<String> comBParCloseAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.close");
    // Disband.
    public static final List<String> comBParDisbandAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.disband");
    // Accept.
    public static final List<String> comBParAcceptAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.accept");
    // Deny.
    public static final List<String> comBParDenyAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.deny");
    // Invite.
    public static final List<String> comBParInvAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.invite");
    // Kick.
    public static final List<String> comBParKickAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.kick");
    // Mute.
    public static final List<String> comBParMuteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.mute");
    // Warp.
    public static final List<String> comBParWarpAliases = StreamLine.getConfig().getConfStringList("commands.bungee.party.aliases.warp");
    // ... Guild.
    //
    public static final boolean comBGuild = StreamLine.getConfig().getConfBoolean("commands.bungee.guild.enabled");
    public static final String comBGuildBase = StreamLine.getConfig().getConfString("commands.bungee.guild.base");
    public static final boolean comBGuildQuick = StreamLine.getConfig().getConfBoolean("commands.bungee.guild.quick-chat");
    public static final String comBGuildPerm = StreamLine.getConfig().getConfString("commands.bungee.guild.permission");
    public static final List<String> comBGuildMainAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.main");
    // Join.
    public static final List<String> comBGuildJoinAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.join");
    // Leave.
    public static final List<String> comBGuildLeaveAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.leave");
    // Create.
    public static final List<String> comBGuildCreateAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.create");
    // Promote.
    public static final List<String> comBGuildPromoteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.promote");
    // Demote.
    public static final List<String> comBGuildDemoteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.demote");
    // Chat.
    public static final List<String> comBGuildChatAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.chat");
    // List.
    public static final List<String> comBGuildListAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.list");
    // Open.
    public static final List<String> comBGuildOpenAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.open");
    // Close.
    public static final List<String> comBGuildCloseAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.close");
    // Disband.
    public static final List<String> comBGuildDisbandAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.disband");
    // Accept.
    public static final List<String> comBGuildAcceptAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.accept");
    // Deny.
    public static final List<String> comBGuildDenyAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.deny");
    // Invite.
    public static final List<String> comBGuildInvAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.invite");
    // Kick.
    public static final List<String> comBGuildKickAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.kick");
    // Mute.
    public static final List<String> comBGuildMuteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.mute");
    // Warp.
    public static final List<String> comBGuildWarpAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.warp");
    // Info.
    public static final List<String> comBGuildInfoAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.info");
    // Rename.
    public static final List<String> comBGuildRenameAliases = StreamLine.getConfig().getConfStringList("commands.bungee.guild.aliases.rename");
    // ... Servers.
    // Lobby.
    public static final boolean comBLobby = StreamLine.getConfig().getConfBoolean("commands.bungee.servers.lobby.enabled");
    public static final String comBLobbyBase = StreamLine.getConfig().getConfString("commands.bungee.staff.lobby.base");
    public static final List<String> comBLobbyAliases = StreamLine.getConfig().getConfStringList("commands.bungee.servers.lobby.aliases");
    public static final String comBLobbyEnd = StreamLine.getConfig().getConfString("commands.bungee.servers.lobby.points-to");
    public static final String comBLobbyPerm = StreamLine.getConfig().getConfString("commands.bungee.servers.lobby.permission");
    // Fabric Fix.
    public static final boolean comBFabric = StreamLine.getConfig().getConfBoolean("commands.bungee.servers.fabric-fix.enabled");
    public static final String comBFabricEnd = StreamLine.getConfig().getConfString("commands.bungee.servers.fabric-fix.points-to");
    public static final String comBFabricPerm = StreamLine.getConfig().getConfString("commands.bungee.servers.fabric-fix.permission");
    // ... Staff.
    // GlobalOnline.
    public static final boolean comBGlobalOnline = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.globalonline.enabled");
    public static final String comBGlobalOnlineBase = StreamLine.getConfig().getConfString("commands.bungee.staff.globalonline.base");
    public static final List<String> comBGlobalOnlineAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.globalonline.aliases");
    public static final String comBGlobalOnlinePerm = StreamLine.getConfig().getConfString("commands.bungee.staff.globalonline.permission");
    // StaffChat.
    public static final boolean comBStaffChat = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.staffchat.enabled");
    public static final String comBStaffChatBase = StreamLine.getConfig().getConfString("commands.bungee.staff.staffchat.base");
    public static final List<String> comBStaffChatAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.staffchat.aliases");
    public static final String comBStaffChatPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.staffchat.permission");
    // StaffOnline.
    public static final boolean comBStaffOnline = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.staffonline.enabled");
    public static final String comBStaffOnlineBase = StreamLine.getConfig().getConfString("commands.bungee.staff.staffonline.base");
    public static final List<String> comBStaffOnlineAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.staffonline.aliases");
    public static final String comBStaffOnlinePerm = StreamLine.getConfig().getConfString("commands.bungee.staff.staffonline.permission");
    // Reload.
    public static final String comBReloadBase = StreamLine.getConfig().getConfString("commands.bungee.staff.slreload.base");
    public static final List<String> comBReloadAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.slreload.aliases");
    public static final String comBReloadPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.slreload.permission");
    // Parties.
    public static final boolean comBParties = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.parties.enabled");
    public static final String comBPartiesBase = StreamLine.getConfig().getConfString("commands.bungee.staff.parties.base");
    public static final List<String> comBPartiesAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.parties.aliases");
    public static final String comBPartiesPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.parties.permission");
    // Guilds.
    public static final boolean comBGuilds = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.guilds.enabled");
    public static final String comBGuildsBase = StreamLine.getConfig().getConfString("commands.bungee.staff.guilds.base");
    public static final List<String> comBGuildsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.guilds.aliases");
    public static final String comBGuildsPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.guilds.permission");
    // GetStats.
    public static final boolean comBGetStats = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.getstats.enabled");
    public static final String comBGetStatsBase = StreamLine.getConfig().getConfString("commands.bungee.staff.getstats.base");
    public static final List<String> comBGetStatsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.getstats.aliases");
    public static final String comBGetStatsPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.getstats.permission");
    // BSudo.
    public static final boolean comBSudo = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.bsudo.enabled");
    public static final String comBSudoBase = StreamLine.getConfig().getConfString("commands.bungee.staff.bsudo.base");
    public static final List<String> comBSudoAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.bsudo.aliases");
    public static final String comBSudoPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.bsudo.permission");
    // SSPY.
    public static final boolean comBSSPY = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.sspy.enabled");
    public static final String comBSSPYBase = StreamLine.getConfig().getConfString("commands.bungee.staff.sspy.base");
    public static final List<String> comBSSPYAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.sspy.aliases");
    public static final String comBSSPYPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.sspy.permission");
    // GSPY.
    public static final boolean comBGSPY = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.gspy.enabled");
    public static final String comBGSPYBase = StreamLine.getConfig().getConfString("commands.bungee.staff.gspy.base");
    public static final List<String> comBGSPYAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.gspy.aliases");
    public static final String comBGSPYPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.gspy.permission");
    // PSPY.
    public static final boolean comBPSPY = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.pspy.enabled");
    public static final String comBPSPYBase = StreamLine.getConfig().getConfString("commands.bungee.staff.pspy.base");
    public static final List<String> comBPSPYAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.pspy.aliases");
    public static final String comBPSPYPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.pspy.permission");
    // BTag.
    public static final boolean comBBTag = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.btag.enabled");
    public static final String comBBTagBase = StreamLine.getConfig().getConfString("commands.bungee.staff.btag.base");
    public static final List<String> comBBTagAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.btag.aliases");
    public static final String comBBTagPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.btag.permission");
    public static final String comBBTagOPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.btag.other-perm");
    public static final String comBBTagChPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.btag.change-perm");
    // Event Reload.
    public static final boolean comBEReload = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.evreload.enabled");
    public static final String comBEReloadBase = StreamLine.getConfig().getConfString("commands.bungee.staff.evreload.base");
    public static final List<String> comBEReloadAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.evreload.aliases");
    public static final String comBEReloadPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.evreload.permission");
    // Network Points.
    public static final boolean comBPoints = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.points.enabled");
    public static final String comBPointsBase = StreamLine.getConfig().getConfString("commands.bungee.staff.points.base");
    public static final List<String> comBPointsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.points.aliases");
    public static final String comBPointsPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.points.permission");
    public static final String comBPointsOPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.points.other-perm");
    public static final String comBPointsChPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.points.change-perm");
    // Server Ping.
    public static final boolean comBSPing = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.serverping.enabled");
    public static final String comBSPingBase = StreamLine.getConfig().getConfString("commands.bungee.staff.serverping.base");
    public static final List<String> comBSPingAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.serverping.aliases");
    public static final String comBSPingPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.serverping.permission");
    // Mute.
    public static final boolean comBMute = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.mute.enabled");
    public static final String comBMuteBase = StreamLine.getConfig().getConfString("commands.bungee.staff.mute.base");
    public static final List<String> comBMuteAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.mute.aliases");
    public static final String comBMutePerm = StreamLine.getConfig().getConfString("commands.bungee.staff.mute.permission");
    // Kick.
    public static final boolean comBKick = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.kick.enabled");
    public static final String comBKickBase = StreamLine.getConfig().getConfString("commands.bungee.staff.kick.base");
    public static final List<String> comBKickAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.kick.aliases");
    public static final String comBKickPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.kick.permission");
    // Ban.
    public static final boolean comBBan = StreamLine.getConfig().getConfBoolean("commands.bungee.staff.ban.enabled");
    public static final String comBBanBase = StreamLine.getConfig().getConfString("commands.bungee.staff.ban.base");
    public static final List<String> comBBanAliases = StreamLine.getConfig().getConfStringList("commands.bungee.staff.ban.aliases");
    public static final String comBBanPerm = StreamLine.getConfig().getConfString("commands.bungee.staff.ban.permission");
    // Settings.
    public static final boolean comBSettings = StreamLine.getConfig().getConfBoolean("commands.bungee.configs.settings.enabled");
    public static final String comBSettingsBase = StreamLine.getConfig().getConfString("commands.bungee.configs.settings.base");
    public static final List<String> comBSettingsAliases = StreamLine.getConfig().getConfStringList("commands.bungee.configs.settings.aliases");
    public static final String comBSettingsPerm = StreamLine.getConfig().getConfString("commands.bungee.configs.settings.permission");
    // ... Messaging.
    // Ignore.
    public static final boolean comBIgnore = StreamLine.getConfig().getConfBoolean("commands.bungee.messaging.ignore.enabled");
    public static final String comBIgnoreBase = StreamLine.getConfig().getConfString("commands.bungee.messaging.ignore.base");
    public static final List<String> comBIgnoreAliases = StreamLine.getConfig().getConfStringList("commands.bungee.messaging.ignore.aliases");
    public static final String comBIgnorePerm = StreamLine.getConfig().getConfString("commands.bungee.messaging.ignore.permission");
    // Message.
    public static final boolean comBMessage = StreamLine.getConfig().getConfBoolean("commands.bungee.messaging.message.enabled");
    public static final String comBMessageBase = StreamLine.getConfig().getConfString("commands.bungee.messaging.message.base");
    public static final List<String> comBMessageAliases = StreamLine.getConfig().getConfStringList("commands.bungee.messaging.message.aliases");
    public static final String comBMessagePerm = StreamLine.getConfig().getConfString("commands.bungee.messaging.message.permission");
    // Reply.
    public static final boolean comBReply = StreamLine.getConfig().getConfBoolean("commands.bungee.messaging.reply.enabled");
    public static final String comBReplyBase = StreamLine.getConfig().getConfString("commands.bungee.messaging.reply.base");
    public static final List<String> comBReplyAliases = StreamLine.getConfig().getConfStringList("commands.bungee.messaging.reply.aliases");
    public static final String comBReplyPerm = StreamLine.getConfig().getConfString("commands.bungee.messaging.reply.permission");
    // Friend.
    public static final boolean comBFriend = StreamLine.getConfig().getConfBoolean("commands.bungee.messaging.friend.enabled");
    public static final String comBFriendBase = StreamLine.getConfig().getConfString("commands.bungee.messaging.friend.base");
    public static final List<String> comBFriendAliases = StreamLine.getConfig().getConfStringList("commands.bungee.messaging.friend.aliases");
    public static final String comBFriendPerm = StreamLine.getConfig().getConfString("commands.bungee.messaging.friend.permission");
    // ... ... Modules.
    public static final String staffPerm = StreamLine.getConfig().getConfString("modules.staff-permission");
    // ... Discord.
    // Basics.
    public static final boolean moduleDMainConsole = StreamLine.getConfig().getConfBoolean("modules.discord.main-console");
    public static final boolean moduleUseMCAvatar = StreamLine.getConfig().getConfBoolean("modules.discord.use-mc-avatar");
    public static final boolean joinsLeavesIcon = StreamLine.getConfig().getConfBoolean("modules.discord.joins-leaves.use-bot-icon");
    public static final boolean joinsLeavesAsConsole = StreamLine.getConfig().getConfBoolean("modules.discord.joins-leaves.send-as-console");
    // Reports.
    public static final boolean moduleReportsDConfirmation = StreamLine.getConfig().getConfBoolean("modules.discord.reports.send-confirmation");
    public static final boolean moduleReportToChannel = StreamLine.getConfig().getConfBoolean("modules.discord.reports.report-to-channel");
    public static final boolean moduleReportsDToMinecraft = StreamLine.getConfig().getConfBoolean("modules.discord.reports.discord-to-minecraft");
    public static final boolean moduleReportChannelPingsRole = StreamLine.getConfig().getConfBoolean("modules.discord.report-channel-pings-a-role");
    // StaffChat.
    public static final boolean moduleStaffChatToMinecraft = StreamLine.getConfig().getConfBoolean("modules.discord.staffchat-to-minecraft");
    public static final boolean moduleSCOnlyStaffRole = StreamLine.getConfig().getConfBoolean("modules.discord.staffchat-to-minecraft-only-staff-role");
    // Startup / Shutdowns.
    public static final boolean moduleStartups = StreamLine.getConfig().getConfBoolean("modules.discord.startup-messages");
    public static final boolean moduleShutdowns = StreamLine.getConfig().getConfBoolean("modules.discord.shutdown-messages");
    // Say if...
    public static final String moduleSayNotACommand = StreamLine.getConfig().getConfString("modules.discord.say-if-not-a-command");
    public static final String moduleSayCommandDisabled = StreamLine.getConfig().getConfString("modules.discord.say-if-command-disabled");
    // Player logins / logouts.
    public static final String moduleDPlayerJoins = StreamLine.getConfig().getConfString("modules.discord.player-joins");
    public static final String moduleDPlayerLeaves = StreamLine.getConfig().getConfString("modules.discord.player-leaves");
    // ... Bungee.
    // Reports.
    public static final boolean moduleReportsBConfirmation = StreamLine.getConfig().getConfBoolean("modules.bungee.reports.send-confirmation");
    public static final boolean moduleReportsMToDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.reports.minecraft-to-discord");
    public static final boolean moduleReportsSendChat = StreamLine.getConfig().getConfBoolean("modules.bungee.reports.send-in-chat");
    // StaffChat.
    public static final boolean moduleStaffChat = StreamLine.getConfig().getConfBoolean("modules.bungee.staffchat.enabled");
    public static final boolean moduleStaffChatDoPrefix = StreamLine.getConfig().getConfBoolean("modules.bungee.staffchat.enable-prefix");
    public static final String moduleStaffChatPrefix = StreamLine.getConfig().getConfString("modules.bungee.staffchat.prefix");
    public static final boolean moduleStaffChatMToDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.staffchat.minecraft-to-discord");
    // Player logins / logouts.
//    public static final String moduleBPlayerJoins = StreamLine.getConfig().getConfString("modules.bungee.player-joins");
//    public static final String moduleBPlayerJoinsPerm = StreamLine.getConfig().getConfString("modules.bungee.joins-permission");
//    public static final String moduleBPlayerLeaves = StreamLine.getConfig().getConfString("modules.bungee.player-leaves");
//    public static final String moduleBPlayerLeavesPerm = StreamLine.getConfig().getConfString("modules.bungee.leaves-permission");
    public static final String moduleBPlayerJoins = StreamLine.getConfig().getConfString("modules.bungee.player-joins-order");
    public static final String moduleBPlayerJoinsPerm = StreamLine.getConfig().getConfString("modules.bungee.joins-permission");
    public static final String moduleBPlayerLeaves = StreamLine.getConfig().getConfString("modules.bungee.player-leaves-order");
    public static final String moduleBPlayerLeavesPerm = StreamLine.getConfig().getConfString("modules.bungee.leaves-permission");
    // ... Parties.
    public static final boolean partyToDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.to-discord");
    public static final int partyMax = StreamLine.getConfig().getConfInteger("modules.bungee.parties.max-size");
    public static final String partyMaxPerm = StreamLine.getConfig().getConfString("modules.bungee.parties.base-permission");
    public static final boolean partyConsoleChats = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.chat");
    public static final boolean partyConsoleCreates = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.creates");
    public static final boolean partyConsoleDisbands = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.disbands");
    public static final boolean partyConsoleOpens = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.opens");
    public static final boolean partyConsoleCloses = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.closes");
    public static final boolean partyConsoleJoins = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.joins");
    public static final boolean partyConsoleLeaves = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.leaves");
    public static final boolean partyConsoleAccepts = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.accepts");
    public static final boolean partyConsoleDenies = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.denies");
    public static final boolean partyConsolePromotes = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.promotes");
    public static final boolean partyConsoleDemotes = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.demotes");
    public static final boolean partyConsoleInvites = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.invites");
    public static final boolean partyConsoleKicks = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.kicks");
    public static final boolean partyConsoleMutes = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.mutes");
    public static final boolean partyConsoleWarps = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.console.warps");
    public static final String partyView = StreamLine.getConfig().getConfString("modules.bungee.parties.view-permission");
    public static final boolean partySendJoins = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.send.join");
    public static final boolean partySendLeaves = StreamLine.getConfig().getConfBoolean("modules.bungee.parties.send.leave");
    // ... Guilds.
    public static final boolean guildToDiscord = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.to-discord");
    public static final int guildMax = StreamLine.getConfig().getConfInteger("modules.bungee.guilds.max-size");
    public static final boolean guildConsoleChats = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.chats");
    public static final boolean guildConsoleCreates = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.creates");
    public static final boolean guildConsoleDisbands = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.disbands");
    public static final boolean guildConsoleOpens = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.opens");
    public static final boolean guildConsoleCloses = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.closes");
    public static final boolean guildConsoleJoins = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.joins");
    public static final boolean guildConsoleLeaves = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.leaves");
    public static final boolean guildConsoleAccepts = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.accepts");
    public static final boolean guildConsoleDenies = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.denies");
    public static final boolean guildConsolePromotes = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.promotes");
    public static final boolean guildConsoleDemotes = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.demotes");
    public static final boolean guildConsoleInvites = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.invites");
    public static final boolean guildConsoleKicks = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.kicks");
    public static final boolean guildConsoleMutes = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.mutes");
    public static final boolean guildConsoleWarps = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.warps");
    public static final boolean guildConsoleRenames = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.console.renames");
    public static final int xpPerGiveG = StreamLine.getConfig().getConfInteger("modules.bungee.guilds.totalXP.amount-per");
    public static final int timePerGiveG = StreamLine.getConfig().getConfInteger("modules.bungee.guilds.totalXP.time-per");
    public static final String guildView = StreamLine.getConfig().getConfString("modules.bungee.guilds.view-permission");
    public static final int guildMaxLength = StreamLine.getConfig().getConfInteger("modules.bungee.guilds.name.max-length");
    public static final boolean guildIncludeColors = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.name.max-includes-colors");
    public static final boolean guildSendJoins = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.send.join");
    public static final boolean guildSendLeaves = StreamLine.getConfig().getConfBoolean("modules.bungee.guilds.send.leave");
    // ... Sudo.
    public static final String noSudoPerm = StreamLine.getConfig().getConfString("modules.bungee.sudo.no-sudo-permission");
    // ... Stats.
    public static final boolean statsTell = StreamLine.getConfig().getConfBoolean("modules.bungee.stats.tell-when-create");
    public static final int xpPerGiveP = StreamLine.getConfig().getConfInteger("modules.bungee.stats.totalXP.amount-per");
    public static final int timePerGiveP = StreamLine.getConfig().getConfInteger("modules.bungee.stats.totalXP.time-per");
    public static final int cachedPClear = StreamLine.getConfig().getConfInteger("modules.bungee.stats.cache-clear");
    public static final boolean updateDisplayNames = StreamLine.getConfig().getConfBoolean("modules.bungee.stats.update-display-names");
    // ... Redirect.
    public static final boolean redirectEnabled = StreamLine.getConfig().getConfBoolean("modules.bungee.redirect.enabled");
    public static final String redirectPre = StreamLine.getConfig().getConfString("modules.bungee.redirect.permission-prefix");
    public static final String redirectMain = StreamLine.getConfig().getConfString("modules.bungee.redirect.main");
    // Version Block.
    public static final boolean vbEnabled = StreamLine.getConfig().getConfBoolean("modules.bungee.redirect.version-block.enabled");
    public static final String vbOverridePerm = StreamLine.getConfig().getConfString("modules.bungee.redirect.version-block.override-permission");
    public static final String vbServerFile = StreamLine.getConfig().getConfString("modules.bungee.redirect.version-block.server-permission-file");
    // Lobbies.
    public static final boolean lobbies = StreamLine.getConfig().getConfBoolean("modules.bungee.redirect.lobbies.enabled");
    public static final String lobbiesFile = StreamLine.getConfig().getConfString("modules.bungee.redirect.lobbies.file");
    public static final int lobbyTimeOut = StreamLine.getConfig().getConfInteger("modules.bungee.redirect.lobbies.time-out");
    // Points.
    public static final int pointsDefault = StreamLine.getConfig().getConfInteger("modules.bungee.points.default");
    // Tags.
    public static final List<String> tagsDefaults = StreamLine.getConfig().getConfStringList("modules.bungee.tags.defaults");
    // Events.
    public static final boolean events = StreamLine.getConfig().getConfBoolean("modules.bungee.events.enabled");
    public static final String eventsFolder = StreamLine.getConfig().getConfString("modules.bungee.events.folder");
    public static final boolean eventsWhenEmpty = StreamLine.getConfig().getConfBoolean("modules.bungee.events.add-default-when-empty");
    // Errors.
    public static final boolean errSendToConsole = StreamLine.getConfig().getConfBoolean("modules.bungee.user-errors.send-to-console");
    // ... Punishments.
    // Mutes.
    public static final boolean punMutes = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.mutes.enabled");
    public static final boolean punMutesHard = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.mutes.hard-mutes");
    public static final boolean punMutesReplaceable = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.mutes.replaceable");
    // Bans.
    public static final boolean punBans = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.bans.enabled");
    public static final boolean punBansReplaceable = StreamLine.getConfig().getConfBoolean("modules.bungee.punishments.bans.replaceable");
    // Messaging.
    public static final String messViewPerm = StreamLine.getConfig().getConfString("modules.bungee.messaging.view-permission");
    public static final String messReplyTo = StreamLine.getConfig().getConfString("modules.bungee.messaging.reply-to");
    // ... Server Config.
    public static final boolean sc = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.enabled");
    public static final boolean scMakeDefault = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.make-if-not-exist");
    public static final boolean scMOTD = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.motd");
    public static final boolean scVersion = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.version");
    public static final boolean scSample = StreamLine.getConfig().getConfBoolean("modules.bungee.server-config.sample");
}
