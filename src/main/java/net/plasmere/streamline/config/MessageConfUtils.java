package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.md_5.bungee.config.Configuration;

public class MessageConfUtils {
    // Messages:
//    public static String s = StreamLine.getConfig().getMessString("");
    // Basics.
    public static String version = StreamLine.getConfig().getMessString("version");
    public static String prefix = StreamLine.getConfig().getMessString("message-prefix");
    public static String noPerm = StreamLine.getConfig().getMessString("no-permission");
    public static String reload = StreamLine.getConfig().getMessString("reload-message");
    public static String onlyPlayers = StreamLine.getConfig().getMessString("only-players");
    public static String noPlayer = StreamLine.getConfig().getMessString("no-player");
    public static String discordErrTitle = StreamLine.getConfig().getMessString("discord-err-title");
    // ... Command Error.
    // Discord.
    public static String discordCommandErrorUnd = StreamLine.getConfig().getMessString("command-error.discord.undefined");
    // Bungee.
    public static String bungeeCommandErrorUnd = StreamLine.getConfig().getMessString("command-error.bungee.undefined");
    public static String bungeeCommandErrorInt = StreamLine.getConfig().getMessString("command-error.bungee.needs-int");
    public static String bungeeCommandErrorSTime = StreamLine.getConfig().getMessString("command-error.bungee.needs-stringed-time");
    // Command Disabled.
    public static String discordCommandDisabled = StreamLine.getConfig().getMessString("command-disabled.discord");
    public static String bungeeCommandDisabled = StreamLine.getConfig().getMessString("command-disabled.bungee");
    // Module Disabled.
    public static String discordModuleDisabled = StreamLine.getConfig().getMessString("module-disabled.discord");
    public static String bungeeModuleDisabled = StreamLine.getConfig().getMessString("module-disabled.bungee");
    // Not command / improper usage.
    public static String discordNotACommand = StreamLine.getConfig().getMessString("not-a-command.discord");
    public static String bungeeImproperUsage = StreamLine.getConfig().getMessString("improper-usage.bungee");
    // Command needs args.
    public static String discordNeedsMore = StreamLine.getConfig().getMessString("command-needs-args.more.discord");
    public static String bungeeNeedsMore = StreamLine.getConfig().getMessString("command-needs-args.more.bungee");
    public static String discordNeedsLess = StreamLine.getConfig().getMessString("command-needs-args.less.discord");
    public static String bungeeNeedsLess = StreamLine.getConfig().getMessString("command-needs-args.less.bungee");
    // Players.
    public static String offlineB = StreamLine.getConfig().getMessString("players.bungee.offline");
    public static String onlineB = StreamLine.getConfig().getMessString("players.bungee.online");
    public static String nullB = StreamLine.getConfig().getMessString("players.bungee.null");
    public static String offlineD = StreamLine.getConfig().getMessString("players.discord.offline");
    public static String onlineD = StreamLine.getConfig().getMessString("players.discord.online");
    public static String nullD = StreamLine.getConfig().getMessString("players.discord.null");
    // Redirect.
    public static String vbBlocked = StreamLine.getConfig().getMessString("redirect.by-version.blocked");
    // Kicks.
    public static String kicksStopping = StreamLine.getConfig().getMessString("kicks.stopping");
    // ... Punishments.
    // Mutes.
    public static String punMutedTemp = StreamLine.getConfig().getMessString("punishments.muted.temp");
    public static String punMutedPerm = StreamLine.getConfig().getMessString("punishments.muted.perm");
    // Bans.
    public static String punBannedTemp = StreamLine.getConfig().getMessString("punishments.banned.temp");
    public static String punBannedPerm = StreamLine.getConfig().getMessString("punishments.banned.perm");
    // IPBans.
    public static String punIPBannedTemp = StreamLine.getConfig().getMessString("punishments.ip-banned.temp");
    public static String punIPBannedPerm = StreamLine.getConfig().getMessString("punishments.ip-banned.perm");
    // Reports.
    public static String reportEmbedTitle = StreamLine.getConfig().getMessString("report-message.embed-title");
    public static String dToDReportMessage = StreamLine.getConfig().getMessString("report-message.from-discord.discord");
    public static String dToBReportMessage = StreamLine.getConfig().getMessString("report-message.from-discord.bungee");
    public static String dConfirmReportMessage = StreamLine.getConfig().getMessString("report-message.from-discord.confirmation");
    public static String bToDReportMessage = StreamLine.getConfig().getMessString("report-message.from-bungee.discord");
    public static String bToBReportMessage = StreamLine.getConfig().getMessString("report-message.from-bungee.bungee");
    public static String bConfirmReportMessage = StreamLine.getConfig().getMessString("report-message.from-bungee.confirmation");
    // Start.
    public static String startTitle = StreamLine.getConfig().getMessString("start.embed-title");
    public static String startMessage = StreamLine.getConfig().getMessString("start.message");
    // Stop.
    public static String shutdownTitle = StreamLine.getConfig().getMessString("shutdown.embed-title");
    public static String shutdownMessage = StreamLine.getConfig().getMessString("shutdown.message");
    // StaffChat.
    public static String staffChatEmbedTitle = StreamLine.getConfig().getMessString("staffchat.message.embed-title");
    public static String discordStaffChatMessage = StreamLine.getConfig().getMessString("staffchat.message.discord");
    public static String bungeeStaffChatMessage = StreamLine.getConfig().getMessString("staffchat.message.bungee");
    public static String discordStaffChatFrom = StreamLine.getConfig().getMessString("staffchat.message.from.discord");
    public static String bungeeStaffChatFrom = StreamLine.getConfig().getMessString("staffchat.message.from.bungee");
    public static String staffChatJustPrefix = StreamLine.getConfig().getMessString("staffchat.just-prefix");
    public static String staffChatToggle = StreamLine.getConfig().getMessString("staffchat.message.toggle");
    public static String staffChatOn = StreamLine.getConfig().getMessString("staffchat.toggle.on");
    public static String staffChatOff = StreamLine.getConfig().getMessString("staffchat.toggle.off");
    // Online.
    public static String onlineMessageNoPlayers = StreamLine.getConfig().getMessString("online.message.no-players");
    public static String onlineMessageNoGroups = StreamLine.getConfig().getMessString("online.message.no-groups");
    public static String onlineMessageEmbedTitle = StreamLine.getConfig().getMessString("online.message.embed-title");
    public static String onlineMessageDiscord = StreamLine.getConfig().getMessString("online.message.discord");
    public static String onlineMessageBMain = StreamLine.getConfig().getMessString("online.message.bungee.main");
    public static String onlineMessageBServers = StreamLine.getConfig().getMessString("online.message.bungee.servers");
    public static String onlineMessageBPlayersMain = StreamLine.getConfig().getMessString("online.message.bungee.players.main");
    public static String onlineMessageBPlayersBulkNotLast = StreamLine.getConfig().getMessString("online.message.bungee.players.playerbulk.if-not-last");
    public static String onlineMessageBPlayersBulkLast = StreamLine.getConfig().getMessString("online.message.bungee.players.playerbulk.if-last");
    // ... Join Leaves.
    // Discord.
    public static String discordOnline = StreamLine.getConfig().getMessString("join-leave.discord.online.text");
    public static String discordOnlineEmbed = StreamLine.getConfig().getMessString("join-leave.discord.online.embed");
    public static String discordOffline = StreamLine.getConfig().getMessString("join-leave.discord.offline.text");
    public static String discordOfflineEmbed = StreamLine.getConfig().getMessString("join-leave.discord.offline.embed");
    // Bungee.
    public static String bungeeOnline = StreamLine.getConfig().getMessString("join-leave.bungee.online");
    public static String bungeeOffline = StreamLine.getConfig().getMessString("join-leave.bungee.offline");
    // ... StaffOnline.
    // Discord.
    public static String sOnlineMessageEmbedTitle = StreamLine.getConfig().getMessString("staffonline.message.embed-title");
    public static String sOnlineDiscordMain = StreamLine.getConfig().getMessString("staffonline.message.discord.main");
    public static String sOnlineDiscordBulkNotLast = StreamLine.getConfig().getMessString("staffonline.message.discord.staffbulk.if-not-last");
    public static String sOnlineDiscordBulkLast = StreamLine.getConfig().getMessString("staffonline.message.discord.staffbulk.if-last");
    // Bungee.
    public static String sOnlineBungeeMain = StreamLine.getConfig().getMessString("staffonline.message.bungee.main");
    public static String sOnlineBungeeBulkNotLast = StreamLine.getConfig().getMessString("staffonline.message.bungee.staffbulk.if-not-last");
    public static String sOnlineBungeeBulkLast = StreamLine.getConfig().getMessString("staffonline.message.bungee.staffbulk.if-last");
    // Stream.
    public static String streamNeedLink = StreamLine.getConfig().getMessString("stream.need-link");
    public static String streamNotLink = StreamLine.getConfig().getMessString("stream.not-link");
    public static String streamMessage = StreamLine.getConfig().getMessString("stream.message");
    public static String streamHoverPrefix = StreamLine.getConfig().getMessString("stream.hover-prefix");
    // Party.
    public static String partyConnect = StreamLine.getConfig().getMessString("party.connect");
    public static String partyDisconnect = StreamLine.getConfig().getMessString("party.disconnect");
    // Guild.
    public static String guildConnect = StreamLine.getConfig().getMessString("guild.connect");
    public static String guildDisconnect = StreamLine.getConfig().getMessString("guild.disconnect");
    // Parties.
    public static String partiesNone = StreamLine.getConfig().getMessString("parties.no-parties");
    public static String partiesMessage = StreamLine.getConfig().getMessString("parties.parties");
    public static String partiesModsNLast = StreamLine.getConfig().getMessString("parties.mods.not-last");
    public static String partiesModsLast = StreamLine.getConfig().getMessString("parties.mods.last");
    public static String partiesMemsNLast = StreamLine.getConfig().getMessString("parties.members.not-last");
    public static String partiesMemsLast = StreamLine.getConfig().getMessString("parties.members.last");
    public static String partiesTMemsNLast = StreamLine.getConfig().getMessString("parties.totalmembers.not-last");
    public static String partiesTMemsLast = StreamLine.getConfig().getMessString("parties.totalmembers.last");
    public static String partiesInvsNLast = StreamLine.getConfig().getMessString("parties.invites.not-last");
    public static String partiesInvsLast = StreamLine.getConfig().getMessString("parties.invites.last");
    public static String partiesIsPublicTrue = StreamLine.getConfig().getMessString("parties.ispublic.true");
    public static String partiesIsPublicFalse = StreamLine.getConfig().getMessString("parties.ispublic.false");
    public static String partiesIsMutedTrue = StreamLine.getConfig().getMessString("parties.ismuted.true");
    public static String partiesIsMutedFalse = StreamLine.getConfig().getMessString("parties.ismuted.false");
    // Guilds.
    public static String guildsNone = StreamLine.getConfig().getMessString("guilds.no-guilds");
    public static String guildsMessage = StreamLine.getConfig().getMessString("guilds.guilds");
    public static String guildsModsNLast = StreamLine.getConfig().getMessString("guilds.mods.not-last");
    public static String guildsModsLast = StreamLine.getConfig().getMessString("guilds.mods.last");
    public static String guildsMemsNLast = StreamLine.getConfig().getMessString("guilds.members.not-last");
    public static String guildsMemsLast = StreamLine.getConfig().getMessString("guilds.members.last");
    public static String guildsTMemsNLast = StreamLine.getConfig().getMessString("guilds.totalmembers.not-last");
    public static String guildsTMemsLast = StreamLine.getConfig().getMessString("guilds.totalmembers.last");
    public static String guildsInvsNLast = StreamLine.getConfig().getMessString("guilds.invites.not-last");
    public static String guildsInvsLast = StreamLine.getConfig().getMessString("guilds.invites.last");
    public static String guildsIsPublicTrue = StreamLine.getConfig().getMessString("guilds.ispublic.true");
    public static String guildsIsPublicFalse = StreamLine.getConfig().getMessString("guilds.ispublic.false");
    public static String guildsIsMutedTrue = StreamLine.getConfig().getMessString("guilds.ismuted.true");
    public static String guildsIsMutedFalse = StreamLine.getConfig().getMessString("guilds.ismuted.false");
    // Sudo.
    public static String sudoWorked = StreamLine.getConfig().getMessString("sudo.worked");
    public static String sudoNoWork = StreamLine.getConfig().getMessString("sudo.no-work");
    public static String sudoNoSudo = StreamLine.getConfig().getMessString("sudo.no-sudo");
    // SSPY.
    public static String sspyToggle = StreamLine.getConfig().getMessString("sspy.message");
    public static String sspyOn = StreamLine.getConfig().getMessString("sspy.toggle.true");
    public static String sspyOff = StreamLine.getConfig().getMessString("sspy.toggle.false");
    // GSPY.
    public static String gspyToggle = StreamLine.getConfig().getMessString("gspy.message");
    public static String gspyOn = StreamLine.getConfig().getMessString("gspy.toggle.true");
    public static String gspyOff = StreamLine.getConfig().getMessString("gspy.toggle.false");
    // PSPY.
    public static String pspyToggle = StreamLine.getConfig().getMessString("pspy.message");
    public static String pspyOn = StreamLine.getConfig().getMessString("pspy.toggle.true");
    public static String pspyOff = StreamLine.getConfig().getMessString("pspy.toggle.false");
    // PSPY.
    public static String scViewToggle = StreamLine.getConfig().getMessString("sc-view.message");
    public static String scViewOn = StreamLine.getConfig().getMessString("sc-view.toggle.true");
    public static String scViewOff = StreamLine.getConfig().getMessString("sc-view.toggle.false");
    // EVReload.
    public static String evReload = StreamLine.getConfig().getMessString("evreload.message");
    // Points.
    public static String pointsViewS = StreamLine.getConfig().getMessString("points.view.self");
    public static String pointsViewO = StreamLine.getConfig().getMessString("points.view.other");
    public static String pointsAddS = StreamLine.getConfig().getMessString("points.add.self");
    public static String pointsAddO = StreamLine.getConfig().getMessString("points.add.other");
    public static String pointsRemoveS = StreamLine.getConfig().getMessString("points.remove.self");
    public static String pointsRemoveO = StreamLine.getConfig().getMessString("points.remove.other");
    public static String pointsSetS = StreamLine.getConfig().getMessString("points.set.self");
    public static String pointsSetO = StreamLine.getConfig().getMessString("points.set.other");
    // Ignore.
    public static String ignoreAddSelf = StreamLine.getConfig().getMessString("ignore.add.self");
    public static String ignoreAddIgnored = StreamLine.getConfig().getMessString("ignore.add.ignored");
    public static String ignoreAddAlready = StreamLine.getConfig().getMessString("ignore.add.alreadyMade");
    public static String ignoreAddNSelf = StreamLine.getConfig().getMessString("ignore.add.not-self");
    public static String ignoreRemSelf = StreamLine.getConfig().getMessString("ignore.remove.self");
    public static String ignoreRemIgnored = StreamLine.getConfig().getMessString("ignore.remove.ignored");
    public static String ignoreRemAlready = StreamLine.getConfig().getMessString("ignore.remove.alreadyMade");
    public static String ignoreRemNSelf = StreamLine.getConfig().getMessString("ignore.remove.not-self");
    public static String ignoreListMain = StreamLine.getConfig().getMessString("ignore.list.main");
    public static String ignoreListNLast = StreamLine.getConfig().getMessString("ignore.list.ignores.not-last");
    public static String ignoreListLast = StreamLine.getConfig().getMessString("ignore.list.ignores.last");
    // Message.
    public static String messageSender = StreamLine.getConfig().getMessString("message.sender");
    public static String messageTo = StreamLine.getConfig().getMessString("message.to");
    public static String messageIgnored = StreamLine.getConfig().getMessString("message.ignored");
    public static String messageSSPY = StreamLine.getConfig().getMessString("message.sspy");
    // Reply.
    public static String replySender = StreamLine.getConfig().getMessString("message.sender");
    public static String replyTo = StreamLine.getConfig().getMessString("message.to");
    public static String replyIgnored = StreamLine.getConfig().getMessString("message.ignored");
    public static String replySSPY = StreamLine.getConfig().getMessString("message.sspy");
    // Mute.
    public static String muteEmbed = StreamLine.getConfig().getMessString("mute.discord-embed-title");
    public static String muteCannot = StreamLine.getConfig().getMessString("mute.cannot");
    public static String muteMTempSender = StreamLine.getConfig().getMessString("mute.mute.temp.sender");
    public static String muteMTempMuted = StreamLine.getConfig().getMessString("mute.mute.temp.muted");
    public static String muteMTempAlready = StreamLine.getConfig().getMessString("mute.mute.temp.alreadyMade");
    public static String muteMTempStaff = StreamLine.getConfig().getMessString("mute.mute.temp.staff");
    public static String muteMTempDiscord = StreamLine.getConfig().getMessString("mute.mute.temp.discord");
    public static String muteMPermSender = StreamLine.getConfig().getMessString("mute.mute.perm.sender");
    public static String muteMPermMuted = StreamLine.getConfig().getMessString("mute.mute.perm.muted");
    public static String muteMPermAlready = StreamLine.getConfig().getMessString("mute.mute.perm.alreadyMade");
    public static String muteMPermStaff = StreamLine.getConfig().getMessString("mute.mute.perm.staff");
    public static String muteMPermDiscord = StreamLine.getConfig().getMessString("mute.mute.perm.discord");
    public static String muteUnSender = StreamLine.getConfig().getMessString("mute.unmute.sender");
    public static String muteUnMuted = StreamLine.getConfig().getMessString("mute.unmute.muted");
    public static String muteUnAlready = StreamLine.getConfig().getMessString("mute.unmute.alreadyMade");
    public static String muteUnStaff = StreamLine.getConfig().getMessString("mute.unmute.staff");
    public static String muteUnDiscord = StreamLine.getConfig().getMessString("mute.unmute.discord");
    public static String muteCheckMain = StreamLine.getConfig().getMessString("mute.check.main");
    public static String muteCheckMuted = StreamLine.getConfig().getMessString("mute.check.muted");
    public static String muteCheckUnMuted = StreamLine.getConfig().getMessString("mute.check.unmuted");
    public static String muteCheckNoDate = StreamLine.getConfig().getMessString("mute.check.no-date");
    // Kick.
    public static String kickEmbed = StreamLine.getConfig().getMessString("kick.discord-embed-title");
    public static String kickCannot = StreamLine.getConfig().getMessString("kick.cannot");
    public static String kickSender = StreamLine.getConfig().getMessString("kick.sender");
    public static String kickKicked = StreamLine.getConfig().getMessString("kick.kicked");
    public static String kickStaff = StreamLine.getConfig().getMessString("kick.staff");
    public static String kickDiscord = StreamLine.getConfig().getMessString("kick.discord");
    // Ban.
    public static String banEmbed = StreamLine.getConfig().getMessString("ban.discord-embed-title");
    public static String banCannot = StreamLine.getConfig().getMessString("ban.cannot");
    public static String banBTempSender = StreamLine.getConfig().getMessString("ban.ban.temp.sender");
    public static String banBTempAlready = StreamLine.getConfig().getMessString("ban.ban.temp.alreadyMade");
    public static String banBTempStaff = StreamLine.getConfig().getMessString("ban.ban.temp.staff");
    public static String banBTempDiscord = StreamLine.getConfig().getMessString("ban.ban.temp.discord");
    public static String banBPermSender = StreamLine.getConfig().getMessString("ban.ban.perm.sender");
    public static String banBPermAlready = StreamLine.getConfig().getMessString("ban.ban.perm.alreadyMade");
    public static String banBPermStaff = StreamLine.getConfig().getMessString("ban.ban.perm.staff");
    public static String banBPermDiscord = StreamLine.getConfig().getMessString("ban.ban.perm.discord");
    public static String banUnSender = StreamLine.getConfig().getMessString("ban.unban.sender");
    public static String banUnAlready = StreamLine.getConfig().getMessString("ban.unban.alreadyMade");
    public static String banUnStaff = StreamLine.getConfig().getMessString("ban.unban.staff");
    public static String banUnDiscord = StreamLine.getConfig().getMessString("ban.unban.discord");
    public static String banCheckMain = StreamLine.getConfig().getMessString("ban.check.main");
    public static String banCheckBanned = StreamLine.getConfig().getMessString("ban.check.banned");
    public static String banCheckUnBanned = StreamLine.getConfig().getMessString("ban.check.unbanned");
    public static String banCheckNoDate = StreamLine.getConfig().getMessString("ban.check.no-date");
    // IPBan.
    public static String ipBanEmbed = StreamLine.getConfig().getMessString("ipban.discord-embed-title");
    public static String ipBanCannot = StreamLine.getConfig().getMessString("ipban.cannot");
    public static String ipBanBTempSender = StreamLine.getConfig().getMessString("ipban.ban.temp.sender");
    public static String ipBanBTempAlready = StreamLine.getConfig().getMessString("ipban.ban.temp.alreadyMade");
    public static String ipBanBTempStaff = StreamLine.getConfig().getMessString("ipban.ban.temp.staff");
    public static String ipBanBTempDiscord = StreamLine.getConfig().getMessString("ipban.ban.temp.discord");
    public static String ipBanBPermSender = StreamLine.getConfig().getMessString("ipban.ban.perm.sender");
    public static String ipBanBPermAlready = StreamLine.getConfig().getMessString("ipban.ban.perm.alreadyMade");
    public static String ipBanBPermStaff = StreamLine.getConfig().getMessString("ipban.ban.perm.staff");
    public static String ipBanBPermDiscord = StreamLine.getConfig().getMessString("ipban.ban.perm.discord");
    public static String ipBanUnSender = StreamLine.getConfig().getMessString("ipban.unban.sender");
    public static String ipBanUnAlready = StreamLine.getConfig().getMessString("ipban.unban.alreadyMade");
    public static String ipBanUnStaff = StreamLine.getConfig().getMessString("ipban.unban.staff");
    public static String ipBanUnDiscord = StreamLine.getConfig().getMessString("ipban.unban.discord");
    public static String ipBanCheckMain = StreamLine.getConfig().getMessString("ipban.check.main");
    public static String ipBanCheckBanned = StreamLine.getConfig().getMessString("ipban.check.banned");
    public static String ipBanCheckUnBanned = StreamLine.getConfig().getMessString("ipban.check.unbanned");
    public static String ipBanCheckNoDate = StreamLine.getConfig().getMessString("ipban.check.no-date");
    // Ignore.
    public static String friendReqSelf = StreamLine.getConfig().getMessString("friend.request.self");
    public static String friendReqOther = StreamLine.getConfig().getMessString("friend.request.other");
    public static String friendReqAlready = StreamLine.getConfig().getMessString("friend.request.alreadyMade");
    public static String friendReqNSelf = StreamLine.getConfig().getMessString("friend.request.not-self");
    public static String friendReqIgnored = StreamLine.getConfig().getMessString("friend.request.ignored");
    public static String friendAcceptSelf = StreamLine.getConfig().getMessString("friend.accept.self");
    public static String friendAcceptOther = StreamLine.getConfig().getMessString("friend.accept.other");
    public static String friendAcceptNone = StreamLine.getConfig().getMessString("friend.accept.none");
    public static String friendDenySelf = StreamLine.getConfig().getMessString("friend.deny.self");
    public static String friendDenyOther = StreamLine.getConfig().getMessString("friend.deny.other");
    public static String friendDenyNone = StreamLine.getConfig().getMessString("friend.deny.none");
    public static String friendRemSelf = StreamLine.getConfig().getMessString("friend.remove.self");
    public static String friendRemOther = StreamLine.getConfig().getMessString("friend.remove.other");
    public static String friendRemAlready = StreamLine.getConfig().getMessString("friend.remove.alreadyMade");
    public static String friendRemNSelf = StreamLine.getConfig().getMessString("friend.remove.not-self");
    public static String friendListMain = StreamLine.getConfig().getMessString("friend.list.main");
    public static String friendListFNLast = StreamLine.getConfig().getMessString("friend.list.friends.not-last");
    public static String friendListFLast = StreamLine.getConfig().getMessString("friend.list.friends.last");
    public static String friendListPTNLast = StreamLine.getConfig().getMessString("friend.list.pending-to.not-last");
    public static String friendListPTLast = StreamLine.getConfig().getMessString("friend.list.pending-to.last");
    public static String friendListPFNLast = StreamLine.getConfig().getMessString("friend.list.pending-from.not-last");
    public static String friendListPFLast = StreamLine.getConfig().getMessString("friend.list.pending-from.last");
    public static String friendConnect = StreamLine.getConfig().getMessString("friend.connect");
    public static String friendDisconnect = StreamLine.getConfig().getMessString("friend.disconnect");
    // GetStats.
    public static String getStatsNone = StreamLine.getConfig().getMessString("getstats.no-stats");
    public static String getStatsMessage = StreamLine.getConfig().getMessString("getstats.message.main");
    public static String getStatsNLast = StreamLine.getConfig().getMessString("getstats.message.not-last");
    public static String getStatsLast = StreamLine.getConfig().getMessString("getstats.message.last");
    // // Settings.
    // Set.
    public static String settingsSetMOTD = StreamLine.getConfig().getMessString("settings.set.motd");
    public static String settingsSetMOTDTime = StreamLine.getConfig().getMessString("settings.set.motd-time");
    public static String settingsSetVersion = StreamLine.getConfig().getMessString("settings.set.version");
    public static String settingsSetSample = StreamLine.getConfig().getMessString("settings.set.sample");
    public static String settingsSetMaxP = StreamLine.getConfig().getMessString("settings.set.max-players");
    public static String settingsSetOnlineP = StreamLine.getConfig().getMessString("settings.set.online-players");
    // Get.
    public static String settingsGetMOTD = StreamLine.getConfig().getMessString("settings.get.motd");
    public static String settingsGetMOTDTime = StreamLine.getConfig().getMessString("settings.get.motd-time");
    public static String settingsGetVersion = StreamLine.getConfig().getMessString("settings.get.version");
    public static String settingsGetSample = StreamLine.getConfig().getMessString("settings.get.sample");
    public static String settingsGetMaxP = StreamLine.getConfig().getMessString("settings.get.max-players");
    public static String settingsGetOnlineP = StreamLine.getConfig().getMessString("settings.get.online-players");
    // // Info.
    public static String info = StreamLine.getConfig().getMessString("info");
}