package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConfigUtils {
    private static final Configuration config = Config.getConf();
    // Config //
//    public static final String s = config.getString("");
    // Important.
    public static final String version = config.getString("version");
    // Debug.
    public static final boolean debug = config.getBoolean("debug");
    // ... Basics.
    // Links.
    public static final String linkPre = config.getString("link-prefix");
//    public static final String linkSuff = config.getString("link.suffix");
    // Bot Stuff.
    public static final String botPrefix = config.getString("bot.prefix");
    public static final String botToken = config.getString("bot.token");
    public static final String botStatusMessage = config.getString("bot.server-ip");
    // ... Discord.
    // Text Channels.
    public static final String textChannelReports = config.getString("discord.text-channels.reports");
    public static final String textChannelStaffChat = config.getString("discord.text-channels.staffchat");
    public static final String textChannelOfflineOnline = config.getString("discord.text-channels.offline-online");
    public static final String textChannelBJoins = config.getString("discord.text-channels.bungee-joins");
    public static final String textChannelBLeaves = config.getString("discord.text-channels.bungee-leaves");
    public static final String textChannelBConsole = config.getString("discord.text-channels.console");
    public static final String textChannelGuilds = config.getString("discord.text-channels.guilds");
    public static final String textChannelParties = config.getString("discord.text-channels.parties");
    // Roles.
    public static final String roleReports = config.getString("discord.roles.reports");
    public static final String roleStaff = config.getString("discord.roles.staff");
    // ... ... ... Commands.
    // ... ... Discord Stuff.
    // Commands.
    public static final boolean comDCommands = config.getBoolean("commands.discord.help.enabled");
    public static final List<String> comDCommandsAliases = config.getStringList("commands.discord.help.aliases");
    public static final String comDCommandsPerm = config.getString("commands.discord.help.permission");
    // Online.
    public static final boolean comDOnline = config.getBoolean("commands.discord.online.enabled");
    public static final List<String> comDOnlineAliases = config.getStringList("commands.discord.online.aliases");
    public static final String comDOnlinePerm = config.getString("commands.discord.online.permission");
    // Report.
    public static final boolean comDReport = config.getBoolean("commands.discord.report.enabled");
    public static final List<String> comDReportAliases = config.getStringList("commands.discord.report.aliases");
    public static final String comDReportPerm = config.getString("commands.discord.report.permission");
    // StaffChat.
    public static final boolean comDStaffChat = config.getBoolean("commands.discord.staffchat.enabled");
    public static final List<String> comDStaffChatAliases = config.getStringList("commands.discord.staffchat.aliases");
    public static final String comDStaffChatPerm = config.getString("commands.discord.staffchat.permission");
    // StaffOnline.
    public static final boolean comDStaffOnline = config.getBoolean("commands.discord.staffonline.enabled");
    public static final List<String> comDStaffOnlineAliases = config.getStringList("commands.discord.staffonline.aliases");
    public static final String comDStaffOnlinePerm = config.getString("commands.discord.staffonline.permission");
    // ... ... Bungee Stuff.
    // Ping.
    public static final boolean comBPing = config.getBoolean("commands.bungee.ping.enabled");
    public static final String comBPingBase = config.getString("commands.bungee.ping.base");
    public static final List<String> comBPingAliases = config.getStringList("commands.bungee.ping.aliases");
    public static final String comBPingPerm = config.getString("commands.bungee.ping.permission");
    // Plugins.
    public static final boolean comBPlugins = config.getBoolean("commands.bungee.plugins.enabled");
    public static final String comBPluginsBase = config.getString("commands.bungee.plugins.base");
    public static final List<String> comBPluginsAliases = config.getStringList("commands.bungee.plugins.aliases");
    public static final String comBPluginsPerm = config.getString("commands.bungee.plugins.permission");
    // Stream.
    public static final boolean comBStream = config.getBoolean("commands.bungee.stream.enabled");
    public static final String comBStreamBase = config.getString("commands.bungee.stream.base");
    public static final List<String> comBStreamAliases = config.getStringList("commands.bungee.stream.aliases");
    public static final String comBStreamPerm = config.getString("commands.bungee.stream.permission");
    // Report.
    public static final boolean comBReport = config.getBoolean("commands.bungee.report.enabled");
    public static final String comBReportBase = config.getString("commands.bungee.report.base");
    public static final List<String> comBReportAliases = config.getStringList("commands.bungee.report.aliases");
    public static final String comBReportPerm = config.getString("commands.bungee.report.permission");
    // StatsCommand
    public static final boolean comBStats = config.getBoolean("commands.bungee.stats.enabled");
    public static final String comBStatsBase = config.getString("commands.bungee.stats.base");
    public static final List<String> comBStatsAliases = config.getStringList("commands.bungee.stats.aliases");
    public static final String comBStatsPerm = config.getString("commands.bungee.stats.permission");
    public static final boolean comBStatsOthers = config.getBoolean("commands.bungee.stats.view-others.enabled");
    public static final String comBStatsPermOthers = config.getString("commands.bungee.stats.view-others.permission");
    // ... Party.
    //
    public static final boolean comBParty = config.getBoolean("commands.bungee.party.enabled");
    public static final String comBPartyBase = config.getString("commands.bungee.party.base");
    public static final boolean comBParQuick = config.getBoolean("commands.bungee.party.quick-chat");
    public static final List<String> comBParMainAliases = config.getStringList("commands.bungee.party.aliases.main");
    public static final String comBParPerm = config.getString("commands.bungee.party.permission");
    // Join.
    public static final List<String> comBParJoinAliases = config.getStringList("commands.bungee.party.aliases.join");
    // Leave.
    public static final List<String> comBParLeaveAliases = config.getStringList("commands.bungee.party.aliases.leave");
    // Create.
    public static final List<String> comBParCreateAliases = config.getStringList("commands.bungee.party.aliases.create");
    // Promote.
    public static final List<String> comBParPromoteAliases = config.getStringList("commands.bungee.party.aliases.promote");
    // Demote.
    public static final List<String> comBParDemoteAliases = config.getStringList("commands.bungee.party.aliases.demote");
    // Chat.
    public static final List<String> comBParChatAliases = config.getStringList("commands.bungee.party.aliases.chat");
    // List.
    public static final List<String> comBParListAliases = config.getStringList("commands.bungee.party.aliases.list");
    // Open.
    public static final List<String> comBParOpenAliases = config.getStringList("commands.bungee.party.aliases.open");
    // Close.
    public static final List<String> comBParCloseAliases = config.getStringList("commands.bungee.party.aliases.close");
    // Disband.
    public static final List<String> comBParDisbandAliases = config.getStringList("commands.bungee.party.aliases.disband");
    // Accept.
    public static final List<String> comBParAcceptAliases = config.getStringList("commands.bungee.party.aliases.accept");
    // Deny.
    public static final List<String> comBParDenyAliases = config.getStringList("commands.bungee.party.aliases.deny");
    // Invite.
    public static final List<String> comBParInvAliases = config.getStringList("commands.bungee.party.aliases.invite");
    // Kick.
    public static final List<String> comBParKickAliases = config.getStringList("commands.bungee.party.aliases.kick");
    // Mute.
    public static final List<String> comBParMuteAliases = config.getStringList("commands.bungee.party.aliases.mute");
    // Warp.
    public static final List<String> comBParWarpAliases = config.getStringList("commands.bungee.party.aliases.warp");
    // ... Guild.
    //
    public static final boolean comBGuild = config.getBoolean("commands.bungee.guild.enabled");
    public static final String comBGuildBase = config.getString("commands.bungee.guild.base");
    public static final boolean comBGuildQuick = config.getBoolean("commands.bungee.guild.quick-chat");
    public static final String comBGuildPerm = config.getString("commands.bungee.guild.permission");
    public static final List<String> comBGuildMainAliases = config.getStringList("commands.bungee.guild.aliases.main");
    // Join.
    public static final List<String> comBGuildJoinAliases = config.getStringList("commands.bungee.guild.aliases.join");
    // Leave.
    public static final List<String> comBGuildLeaveAliases = config.getStringList("commands.bungee.guild.aliases.leave");
    // Create.
    public static final List<String> comBGuildCreateAliases = config.getStringList("commands.bungee.guild.aliases.create");
    // Promote.
    public static final List<String> comBGuildPromoteAliases = config.getStringList("commands.bungee.guild.aliases.promote");
    // Demote.
    public static final List<String> comBGuildDemoteAliases = config.getStringList("commands.bungee.guild.aliases.demote");
    // Chat.
    public static final List<String> comBGuildChatAliases = config.getStringList("commands.bungee.guild.aliases.chat");
    // List.
    public static final List<String> comBGuildListAliases = config.getStringList("commands.bungee.guild.aliases.list");
    // Open.
    public static final List<String> comBGuildOpenAliases = config.getStringList("commands.bungee.guild.aliases.open");
    // Close.
    public static final List<String> comBGuildCloseAliases = config.getStringList("commands.bungee.guild.aliases.close");
    // Disband.
    public static final List<String> comBGuildDisbandAliases = config.getStringList("commands.bungee.guild.aliases.disband");
    // Accept.
    public static final List<String> comBGuildAcceptAliases = config.getStringList("commands.bungee.guild.aliases.accept");
    // Deny.
    public static final List<String> comBGuildDenyAliases = config.getStringList("commands.bungee.guild.aliases.deny");
    // Invite.
    public static final List<String> comBGuildInvAliases = config.getStringList("commands.bungee.guild.aliases.invite");
    // Kick.
    public static final List<String> comBGuildKickAliases = config.getStringList("commands.bungee.guild.aliases.kick");
    // Mute.
    public static final List<String> comBGuildMuteAliases = config.getStringList("commands.bungee.guild.aliases.mute");
    // Warp.
    public static final List<String> comBGuildWarpAliases = config.getStringList("commands.bungee.guild.aliases.warp");
    // Info.
    public static final List<String> comBGuildInfoAliases = config.getStringList("commands.bungee.guild.aliases.info");
    // Rename.
    public static final List<String> comBGuildRenameAliases = config.getStringList("commands.bungee.guild.aliases.rename");
    // ... Servers.
    // Lobby.
    public static final boolean comBLobby = config.getBoolean("commands.bungee.servers.lobby.enabled");
    public static final String comBLobbyBase = config.getString("commands.bungee.staff.lobby.base");
    public static final List<String> comBLobbyAliases = config.getStringList("commands.bungee.servers.lobby.aliases");
    public static final String comBLobbyEnd = config.getString("commands.bungee.servers.lobby.points-to");
    public static final String comBLobbyPerm = config.getString("commands.bungee.servers.lobby.permission");
    // Fabric Fix.
    public static final boolean comBFabric = config.getBoolean("commands.bungee.servers.fabric-fix.enabled");
    public static final String comBFabricEnd = config.getString("commands.bungee.servers.fabric-fix.points-to");
    public static final String comBFabricPerm = config.getString("commands.bungee.servers.fabric-fix.permission");
    // ... Staff.
    // GlobalOnline.
    public static final boolean comBGlobalOnline = config.getBoolean("commands.bungee.staff.globalonline.enabled");
    public static final String comBGlobalOnlineBase = config.getString("commands.bungee.staff.globalonline.base");
    public static final List<String> comBGlobalOnlineAliases = config.getStringList("commands.bungee.staff.globalonline.aliases");
    public static final String comBGlobalOnlinePerm = config.getString("commands.bungee.staff.globalonline.permission");
    // StaffChat.
    public static final boolean comBStaffChat = config.getBoolean("commands.bungee.staff.staffchat.enabled");
    public static final String comBStaffChatBase = config.getString("commands.bungee.staff.staffchat.base");
    public static final List<String> comBStaffChatAliases = config.getStringList("commands.bungee.staff.staffchat.aliases");
    public static final String comBStaffChatPerm = config.getString("commands.bungee.staff.staffchat.permission");
    // StaffOnline.
    public static final boolean comBStaffOnline = config.getBoolean("commands.bungee.staff.staffonline.enabled");
    public static final String comBStaffOnlineBase = config.getString("commands.bungee.staff.staffonline.base");
    public static final List<String> comBStaffOnlineAliases = config.getStringList("commands.bungee.staff.staffonline.aliases");
    public static final String comBStaffOnlinePerm = config.getString("commands.bungee.staff.staffonline.permission");
    // Reload.
    public static final String comBReloadBase = config.getString("commands.bungee.staff.slreload.base");
    public static final List<String> comBReloadAliases = config.getStringList("commands.bungee.staff.slreload.aliases");
    public static final String comBReloadPerm = config.getString("commands.bungee.staff.slreload.permission");
    // Parties.
    public static final boolean comBParties = config.getBoolean("commands.bungee.staff.parties.enabled");
    public static final String comBPartiesBase = config.getString("commands.bungee.staff.parties.base");
    public static final List<String> comBPartiesAliases = config.getStringList("commands.bungee.staff.parties.aliases");
    public static final String comBPartiesPerm = config.getString("commands.bungee.staff.parties.permission");
    // Guilds.
    public static final boolean comBGuilds = config.getBoolean("commands.bungee.staff.guilds.enabled");
    public static final String comBGuildsBase = config.getString("commands.bungee.staff.guilds.base");
    public static final List<String> comBGuildsAliases = config.getStringList("commands.bungee.staff.guilds.aliases");
    public static final String comBGuildsPerm = config.getString("commands.bungee.staff.guilds.permission");
    // GetStats.
    public static final boolean comBGetStats = config.getBoolean("commands.bungee.staff.getstats.enabled");
    public static final String comBGetStatsBase = config.getString("commands.bungee.staff.getstats.base");
    public static final List<String> comBGetStatsAliases = config.getStringList("commands.bungee.staff.getstats.aliases");
    public static final String comBGetStatsPerm = config.getString("commands.bungee.staff.getstats.permission");
    // BSudo.
    public static final boolean comBSudo = config.getBoolean("commands.bungee.staff.bsudo.enabled");
    public static final String comBSudoBase = config.getString("commands.bungee.staff.bsudo.base");
    public static final List<String> comBSudoAliases = config.getStringList("commands.bungee.staff.bsudo.aliases");
    public static final String comBSudoPerm = config.getString("commands.bungee.staff.bsudo.permission");
    // SSPY.
    public static final boolean comBSSPY = config.getBoolean("commands.bungee.staff.sspy.enabled");
    public static final String comBSSPYBase = config.getString("commands.bungee.staff.sspy.base");
    public static final List<String> comBSSPYAliases = config.getStringList("commands.bungee.staff.sspy.aliases");
    public static final String comBSSPYPerm = config.getString("commands.bungee.staff.sspy.permission");
    // GSPY.
    public static final boolean comBGSPY = config.getBoolean("commands.bungee.staff.gspy.enabled");
    public static final String comBGSPYBase = config.getString("commands.bungee.staff.gspy.base");
    public static final List<String> comBGSPYAliases = config.getStringList("commands.bungee.staff.gspy.aliases");
    public static final String comBGSPYPerm = config.getString("commands.bungee.staff.gspy.permission");
    // PSPY.
    public static final boolean comBPSPY = config.getBoolean("commands.bungee.staff.pspy.enabled");
    public static final String comBPSPYBase = config.getString("commands.bungee.staff.pspy.base");
    public static final List<String> comBPSPYAliases = config.getStringList("commands.bungee.staff.pspy.aliases");
    public static final String comBPSPYPerm = config.getString("commands.bungee.staff.pspy.permission");
    // BTag.
    public static final boolean comBBTag = config.getBoolean("commands.bungee.staff.btag.enabled");
    public static final String comBBTagBase = config.getString("commands.bungee.staff.btag.base");
    public static final List<String> comBBTagAliases = config.getStringList("commands.bungee.staff.btag.aliases");
    public static final String comBBTagPerm = config.getString("commands.bungee.staff.btag.permission");
    public static final String comBBTagOPerm = config.getString("commands.bungee.staff.btag.other-perm");
    public static final String comBBTagChPerm = config.getString("commands.bungee.staff.btag.change-perm");
    // Event Reload.
    public static final boolean comBEReload = config.getBoolean("commands.bungee.staff.evreload.enabled");
    public static final String comBEReloadBase = config.getString("commands.bungee.staff.evreload.base");
    public static final List<String> comBEReloadAliases = config.getStringList("commands.bungee.staff.evreload.aliases");
    public static final String comBEReloadPerm = config.getString("commands.bungee.staff.evreload.permission");
    // Network Points.
    public static final boolean comBPoints = config.getBoolean("commands.bungee.staff.points.enabled");
    public static final String comBPointsBase = config.getString("commands.bungee.staff.points.base");
    public static final List<String> comBPointsAliases = config.getStringList("commands.bungee.staff.points.aliases");
    public static final String comBPointsPerm = config.getString("commands.bungee.staff.points.permission");
    public static final String comBPointsOPerm = config.getString("commands.bungee.staff.points.other-perm");
    public static final String comBPointsChPerm = config.getString("commands.bungee.staff.points.change-perm");
    // Server Ping.
    public static final boolean comBSPing = config.getBoolean("commands.bungee.staff.serverping.enabled");
    public static final String comBSPingBase = config.getString("commands.bungee.staff.serverping.base");
    public static final List<String> comBSPingAliases = config.getStringList("commands.bungee.staff.serverping.aliases");
    public static final String comBSPingPerm = config.getString("commands.bungee.staff.serverping.permission");
    // Mute.
    public static final boolean comBMute = config.getBoolean("commands.bungee.staff.mute.enabled");
    public static final String comBMuteBase = config.getString("commands.bungee.staff.mute.base");
    public static final List<String> comBMuteAliases = config.getStringList("commands.bungee.staff.mute.aliases");
    public static final String comBMutePerm = config.getString("commands.bungee.staff.mute.permission");
    // Kick.
    public static final boolean comBKick = config.getBoolean("commands.bungee.staff.kick.enabled");
    public static final String comBKickBase = config.getString("commands.bungee.staff.kick.base");
    public static final List<String> comBKickAliases = config.getStringList("commands.bungee.staff.kick.aliases");
    public static final String comBKickPerm = config.getString("commands.bungee.staff.kick.permission");
    // Ban.
    public static final boolean comBBan = config.getBoolean("commands.bungee.staff.ban.enabled");
    public static final String comBBanBase = config.getString("commands.bungee.staff.ban.base");
    public static final List<String> comBBanAliases = config.getStringList("commands.bungee.staff.ban.aliases");
    public static final String comBBanPerm = config.getString("commands.bungee.staff.ban.permission");
    // Settings.
    public static final boolean comBSettings = config.getBoolean("commands.bungee.staff.settings.enabled");
    public static final String comBSettingsBase = config.getString("commands.bungee.staff.settings.base");
    public static final List<String> comBSettingsAliases = config.getStringList("commands.bungee.staff.settings.aliases");
    public static final String comBSettingsPerm = config.getString("commands.bungee.staff.settings.permission");
    // ... Messaging.
    // Ignore.
    public static final boolean comBIgnore = config.getBoolean("commands.bungee.messaging.ignore.enabled");
    public static final String comBIgnoreBase = config.getString("commands.bungee.messaging.ignore.base");
    public static final List<String> comBIgnoreAliases = config.getStringList("commands.bungee.messaging.ignore.aliases");
    public static final String comBIgnorePerm = config.getString("commands.bungee.messaging.ignore.permission");
    // Message.
    public static final boolean comBMessage = config.getBoolean("commands.bungee.messaging.message.enabled");
    public static final String comBMessageBase = config.getString("commands.bungee.messaging.message.base");
    public static final List<String> comBMessageAliases = config.getStringList("commands.bungee.messaging.message.aliases");
    public static final String comBMessagePerm = config.getString("commands.bungee.messaging.message.permission");
    // Reply.
    public static final boolean comBReply = config.getBoolean("commands.bungee.messaging.reply.enabled");
    public static final String comBReplyBase = config.getString("commands.bungee.messaging.reply.base");
    public static final List<String> comBReplyAliases = config.getStringList("commands.bungee.messaging.reply.aliases");
    public static final String comBReplyPerm = config.getString("commands.bungee.messaging.reply.permission");
    // Friend.
    public static final boolean comBFriend = config.getBoolean("commands.bungee.messaging.friend.enabled");
    public static final String comBFriendBase = config.getString("commands.bungee.messaging.friend.base");
    public static final List<String> comBFriendAliases = config.getStringList("commands.bungee.messaging.friend.aliases");
    public static final String comBFriendPerm = config.getString("commands.bungee.messaging.friend.permission");
    // ... ... Modules.
    public static final String staffPerm = config.getString("modules.staff-permission");
    // ... Discord.
    // Basics.
    public static final boolean moduleDMainConsole = config.getBoolean("modules.discord.main-console");
    public static final boolean moduleUseMCAvatar = config.getBoolean("modules.discord.use-mc-avatar");
    public static final boolean joinsLeavesIcon = config.getBoolean("modules.discord.joins-leaves.use-bot-icon");
    public static final boolean joinsLeavesAsConsole = config.getBoolean("modules.discord.joins-leaves.send-as-console");
    // Reports.
    public static final boolean moduleReportsDConfirmation = config.getBoolean("modules.discord.reports.send-confirmation");
    public static final boolean moduleReportToChannel = config.getBoolean("modules.discord.reports.report-to-channel");
    public static final boolean moduleReportsDToMinecraft = config.getBoolean("modules.discord.reports.discord-to-minecraft");
    public static final boolean moduleReportChannelPingsRole = config.getBoolean("modules.discord.report-channel-pings-a-role");
    // StaffChat.
    public static final boolean moduleStaffChatToMinecraft = config.getBoolean("modules.discord.staffchat-to-minecraft");
    public static final boolean moduleSCOnlyStaffRole = config.getBoolean("modules.discord.staffchat-to-minecraft-only-staff-role");
    // Startup / Shutdowns.
    public static final boolean moduleStartups = config.getBoolean("modules.discord.startup-messages");
    public static final boolean moduleShutdowns = config.getBoolean("modules.discord.shutdown-messages");
    // Say if...
    public static final String moduleSayNotACommand = config.getString("modules.discord.say-if-not-a-command");
    public static final String moduleSayCommandDisabled = config.getString("modules.discord.say-if-command-disabled");
    // Player logins / logouts.
    public static final String moduleDPlayerJoins = config.getString("modules.discord.player-joins");
    public static final String moduleDPlayerLeaves = config.getString("modules.discord.player-leaves");
    // ... Bungee.
    // Reports.
    public static final boolean moduleReportsBConfirmation = config.getBoolean("modules.bungee.reports.send-confirmation");
    public static final boolean moduleReportsMToDiscord = config.getBoolean("modules.bungee.reports.minecraft-to-discord");
    public static final boolean moduleReportsSendChat = config.getBoolean("modules.bungee.reports.send-in-chat");
    // StaffChat.
    public static final boolean moduleStaffChat = config.getBoolean("modules.bungee.staffchat.enabled");
    public static final boolean moduleStaffChatDoPrefix = config.getBoolean("modules.bungee.staffchat.enable-prefix");
    public static final String moduleStaffChatPrefix = config.getString("modules.bungee.staffchat.prefix");
    public static final boolean moduleStaffChatMToDiscord = config.getBoolean("modules.bungee.staffchat.minecraft-to-discord");
    // Player logins / logouts.
//    public static final String moduleBPlayerJoins = config.getString("modules.bungee.player-joins");
//    public static final String moduleBPlayerJoinsPerm = config.getString("modules.bungee.joins-permission");
//    public static final String moduleBPlayerLeaves = config.getString("modules.bungee.player-leaves");
//    public static final String moduleBPlayerLeavesPerm = config.getString("modules.bungee.leaves-permission");
    public static final String moduleBPlayerJoins = config.getString("modules.bungee.player-joins-order");
    public static final String moduleBPlayerJoinsPerm = config.getString("modules.bungee.joins-permission");
    public static final String moduleBPlayerLeaves = config.getString("modules.bungee.player-leaves-order");
    public static final String moduleBPlayerLeavesPerm = config.getString("modules.bungee.leaves-permission");
    // ... Parties.
    public static final boolean partyToDiscord = config.getBoolean("modules.bungee.parties.to-discord");
    public static final int partyMax = config.getInt("modules.bungee.parties.max-size");
    public static final String partyMaxPerm = config.getString("modules.bungee.parties.base-permission");
    public static final boolean partyConsoleChats = config.getBoolean("modules.bungee.parties.console.chat");
    public static final boolean partyConsoleCreates = config.getBoolean("modules.bungee.parties.console.creates");
    public static final boolean partyConsoleDisbands = config.getBoolean("modules.bungee.parties.console.disbands");
    public static final boolean partyConsoleOpens = config.getBoolean("modules.bungee.parties.console.opens");
    public static final boolean partyConsoleCloses = config.getBoolean("modules.bungee.parties.console.closes");
    public static final boolean partyConsoleJoins = config.getBoolean("modules.bungee.parties.console.joins");
    public static final boolean partyConsoleLeaves = config.getBoolean("modules.bungee.parties.console.leaves");
    public static final boolean partyConsoleAccepts = config.getBoolean("modules.bungee.parties.console.accepts");
    public static final boolean partyConsoleDenies = config.getBoolean("modules.bungee.parties.console.denies");
    public static final boolean partyConsolePromotes = config.getBoolean("modules.bungee.parties.console.promotes");
    public static final boolean partyConsoleDemotes = config.getBoolean("modules.bungee.parties.console.demotes");
    public static final boolean partyConsoleInvites = config.getBoolean("modules.bungee.parties.console.invites");
    public static final boolean partyConsoleKicks = config.getBoolean("modules.bungee.parties.console.kicks");
    public static final boolean partyConsoleMutes = config.getBoolean("modules.bungee.parties.console.mutes");
    public static final boolean partyConsoleWarps = config.getBoolean("modules.bungee.parties.console.warps");
    public static final String partyView = config.getString("modules.bungee.parties.view-permission");
    public static final boolean partySendJoins = config.getBoolean("modules.bungee.parties.send.join");
    public static final boolean partySendLeaves = config.getBoolean("modules.bungee.parties.send.leave");
    // ... Guilds.
    public static final boolean guildToDiscord = config.getBoolean("modules.bungee.guilds.to-discord");
    public static final int guildMax = config.getInt("modules.bungee.guilds.max-size");
    public static final boolean guildConsoleChats = config.getBoolean("modules.bungee.guilds.console.chats");
    public static final boolean guildConsoleCreates = config.getBoolean("modules.bungee.guilds.console.creates");
    public static final boolean guildConsoleDisbands = config.getBoolean("modules.bungee.guilds.console.disbands");
    public static final boolean guildConsoleOpens = config.getBoolean("modules.bungee.guilds.console.opens");
    public static final boolean guildConsoleCloses = config.getBoolean("modules.bungee.guilds.console.closes");
    public static final boolean guildConsoleJoins = config.getBoolean("modules.bungee.guilds.console.joins");
    public static final boolean guildConsoleLeaves = config.getBoolean("modules.bungee.guilds.console.leaves");
    public static final boolean guildConsoleAccepts = config.getBoolean("modules.bungee.guilds.console.accepts");
    public static final boolean guildConsoleDenies = config.getBoolean("modules.bungee.guilds.console.denies");
    public static final boolean guildConsolePromotes = config.getBoolean("modules.bungee.guilds.console.promotes");
    public static final boolean guildConsoleDemotes = config.getBoolean("modules.bungee.guilds.console.demotes");
    public static final boolean guildConsoleInvites = config.getBoolean("modules.bungee.guilds.console.invites");
    public static final boolean guildConsoleKicks = config.getBoolean("modules.bungee.guilds.console.kicks");
    public static final boolean guildConsoleMutes = config.getBoolean("modules.bungee.guilds.console.mutes");
    public static final boolean guildConsoleWarps = config.getBoolean("modules.bungee.guilds.console.warps");
    public static final boolean guildConsoleRenames = config.getBoolean("modules.bungee.guilds.console.renames");
    public static final int xpPerGiveG = config.getInt("modules.bungee.guilds.totalXP.amount-per");
    public static final int timePerGiveG = config.getInt("modules.bungee.guilds.totalXP.time-per");
    public static final String guildView = config.getString("modules.bungee.guilds.view-permission");
    public static final int guildMaxLength = config.getInt("modules.bungee.guilds.name.max-length");
    public static final boolean guildIncludeColors = config.getBoolean("modules.bungee.guilds.name.max-includes-colors");
    public static final boolean guildSendJoins = config.getBoolean("modules.bungee.guilds.send.join");
    public static final boolean guildSendLeaves = config.getBoolean("modules.bungee.guilds.send.leave");
    // ... Sudo.
    public static final String noSudoPerm = config.getString("modules.bungee.sudo.no-sudo-permission");
    // ... Stats.
    public static final boolean statsTell = config.getBoolean("modules.bungee.stats.tell-when-create");
    public static final int xpPerGiveP = config.getInt("modules.bungee.stats.totalXP.amount-per");
    public static final int timePerGiveP = config.getInt("modules.bungee.stats.totalXP.time-per");
    public static final int cachedPClear = config.getInt("modules.bungee.stats.cache-clear");
    public static final boolean updateDisplayNames = config.getBoolean("modules.bungee.stats.update-display-names");
    // ... Redirect.
    public static final boolean redirectEnabled = config.getBoolean("modules.bungee.redirect.enabled");
    public static final String redirectPre = config.getString("modules.bungee.redirect.permission-prefix");
    public static final String redirectMain = config.getString("modules.bungee.redirect.main");
    // Version Block.
    public static final boolean vbEnabled = config.getBoolean("modules.bungee.redirect.version-block.enabled");
    public static final String vbOverridePerm = config.getString("modules.bungee.redirect.version-block.override-permission");
    public static final String vbServerFile = config.getString("modules.bungee.redirect.version-block.server-permission-file");
    // Lobbies.
    public static final boolean lobbies = config.getBoolean("modules.bungee.redirect.lobbies.enabled");
    public static final String lobbiesFile = config.getString("modules.bungee.redirect.lobbies.file");
    public static final int lobbyTimeOut = config.getInt("modules.bungee.redirect.lobbies.time-out");
    // Points.
    public static final int pointsDefault = config.getInt("modules.bungee.points.default");
    // Tags.
    public static final List<String> tagsDefaults = config.getStringList("modules.bungee.tags.defaults");
    // Events.
    public static final boolean events = config.getBoolean("modules.bungee.events.enabled");
    public static final String eventsFolder = config.getString("modules.bungee.events.folder");
    public static final boolean eventsWhenEmpty = config.getBoolean("modules.bungee.events.add-default-when-empty");
    // Errors.
    public static final boolean errSendToConsole = config.getBoolean("modules.bungee.user-errors.send-to-console");
    // ... Punishments.
    // Mutes.
    public static final boolean punMutes = config.getBoolean("modules.bungee.punishments.mutes.enabled");
    public static final boolean punMutesHard = config.getBoolean("modules.bungee.punishments.mutes.hard-mutes");
    public static final boolean punMutesReplaceable = config.getBoolean("modules.bungee.punishments.mutes.replaceable");
    // Bans.
    public static final boolean punBans = config.getBoolean("modules.bungee.punishments.bans.enabled");
    public static final boolean punBansReplaceable = config.getBoolean("modules.bungee.punishments.bans.replaceable");
    // Messaging.
    public static final String messViewPerm = config.getString("modules.bungee.messaging.view-permission");
    public static final String messReplyTo = config.getString("modules.bungee.messaging.reply-to");
    // ... Server Config.
    public static final boolean sc = config.getBoolean("modules.bungee.server-config.enabled");
    public static final boolean scMakeDefault = config.getBoolean("modules.bungee.server-config.make-if-not-exist");
    public static final boolean scMOTD = config.getBoolean("modules.bungee.server-config.motd");
    public static final boolean scVersion = config.getBoolean("modules.bungee.server-config.version");
    public static final boolean scSample = config.getBoolean("modules.bungee.server-config.sample");
}
