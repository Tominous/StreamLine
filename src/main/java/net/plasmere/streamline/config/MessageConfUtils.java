package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;

public class MessageConfUtils {
    // Messages:
//    public static String s = StreamLine.config.getMessString("");
    // Basics.
    public static String version = StreamLine.config.getMessString("version");
    public static String prefix = StreamLine.config.getMessString("message-prefix");
    public static String noPerm = StreamLine.config.getMessString("no-permission");
    public static String reload = StreamLine.config.getMessString("reload-message");
    public static String onlyPlayers = StreamLine.config.getMessString("only-players");
    public static String noPlayer = StreamLine.config.getMessString("no-player");
    public static String discordErrTitle = StreamLine.config.getMessString("discord-err-title");
    // ... Command Error.
    // Discord.
    public static String discordCommandErrorUnd = StreamLine.config.getMessString("command-error.discord.undefined");
    // Bungee.
    public static String bungeeCommandErrorUnd = StreamLine.config.getMessString("command-error.bungee.undefined");
    public static String bungeeCommandErrorInt = StreamLine.config.getMessString("command-error.bungee.needs-int");
    public static String bungeeCommandErrorSTime = StreamLine.config.getMessString("command-error.bungee.needs-stringed-time");
    // Command Disabled.
    public static String discordCommandDisabled = StreamLine.config.getMessString("command-disabled.discord");
    public static String bungeeCommandDisabled = StreamLine.config.getMessString("command-disabled.bungee");
    // Module Disabled.
    public static String discordModuleDisabled = StreamLine.config.getMessString("module-disabled.discord");
    public static String bungeeModuleDisabled = StreamLine.config.getMessString("module-disabled.bungee");
    // Not command / improper usage.
    public static String discordNotACommand = StreamLine.config.getMessString("not-a-command.discord");
    public static String bungeeImproperUsage = StreamLine.config.getMessString("improper-usage.bungee");
    // Command needs args.
    public static String discordNeedsMore = StreamLine.config.getMessString("command-needs-args.more.discord");
    public static String bungeeNeedsMore = StreamLine.config.getMessString("command-needs-args.more.bungee");
    public static String discordNeedsLess = StreamLine.config.getMessString("command-needs-args.less.discord");
    public static String bungeeNeedsLess = StreamLine.config.getMessString("command-needs-args.less.bungee");
    // Players.
    public static String offlineB = StreamLine.config.getMessString("players.bungee.offline");
    public static String onlineB = StreamLine.config.getMessString("players.bungee.online");
    public static String nullB = StreamLine.config.getMessString("players.bungee.null");
    public static String offlineD = StreamLine.config.getMessString("players.discord.offline");
    public static String onlineD = StreamLine.config.getMessString("players.discord.online");
    public static String nullD = StreamLine.config.getMessString("players.discord.null");
    // Redirect.
    public static String vbBlocked = StreamLine.config.getMessString("redirect.by-version.blocked");
    // Kicks.
    public static String kicksStopping = StreamLine.config.getMessString("kicks.stopping");
    // ... Punishments.
    // Mutes.
    public static String punMutedTemp = StreamLine.config.getMessString("punishments.muted.temp");
    public static String punMutedPerm = StreamLine.config.getMessString("punishments.muted.perm");
    // Bans.
    public static String punBannedTemp = StreamLine.config.getMessString("punishments.banned.temp");
    public static String punBannedPerm = StreamLine.config.getMessString("punishments.banned.perm");
    // IPBans.
    public static String punIPBannedTemp = StreamLine.config.getMessString("punishments.ip-banned.temp");
    public static String punIPBannedPerm = StreamLine.config.getMessString("punishments.ip-banned.perm");
    // Reports.
    public static String reportEmbedTitle = StreamLine.config.getMessString("report-message.embed-title");
    public static String dToDReportMessage = StreamLine.config.getMessString("report-message.from-discord.discord");
    public static String dToBReportMessage = StreamLine.config.getMessString("report-message.from-discord.bungee");
    public static String dConfirmReportMessage = StreamLine.config.getMessString("report-message.from-discord.confirmation");
    public static String bToDReportMessage = StreamLine.config.getMessString("report-message.from-bungee.discord");
    public static String bToBReportMessage = StreamLine.config.getMessString("report-message.from-bungee.bungee");
    public static String bConfirmReportMessage = StreamLine.config.getMessString("report-message.from-bungee.confirmation");
    // Start.
    public static String startTitle = StreamLine.config.getMessString("start.embed-title");
    public static String startMessage = StreamLine.config.getMessString("start.message");
    // Stop.
    public static String shutdownTitle = StreamLine.config.getMessString("shutdown.embed-title");
    public static String shutdownMessage = StreamLine.config.getMessString("shutdown.message");
    // StaffChat.
    public static String staffChatEmbedTitle = StreamLine.config.getMessString("staffchat.message.embed-title");
    public static String discordStaffChatMessage = StreamLine.config.getMessString("staffchat.message.discord");
    public static String bungeeStaffChatMessage = StreamLine.config.getMessString("staffchat.message.bungee");
    public static String discordStaffChatFrom = StreamLine.config.getMessString("staffchat.message.from.discord");
    public static String bungeeStaffChatFrom = StreamLine.config.getMessString("staffchat.message.from.bungee");
    public static String staffChatJustPrefix = StreamLine.config.getMessString("staffchat.just-prefix");
    public static String staffChatToggle = StreamLine.config.getMessString("staffchat.message.toggle");
    public static String staffChatOn = StreamLine.config.getMessString("staffchat.toggle.true");
    public static String staffChatOff = StreamLine.config.getMessString("staffchat.toggle.false");
    // Online.
    public static String onlineMessageNoPlayers = StreamLine.config.getMessString("online.message.no-players");
    public static String onlineMessageNoGroups = StreamLine.config.getMessString("online.message.no-groups");
    public static String onlineMessageEmbedTitle = StreamLine.config.getMessString("online.message.embed-title");
    public static String onlineMessageDiscord = StreamLine.config.getMessString("online.message.discord");
    public static String onlineMessageBMain = StreamLine.config.getMessString("online.message.bungee.main");
    public static String onlineMessageBServers = StreamLine.config.getMessString("online.message.bungee.servers");
    public static String onlineMessageBPlayersMain = StreamLine.config.getMessString("online.message.bungee.players.main");
    public static String onlineMessageBPlayersBulkNotLast = StreamLine.config.getMessString("online.message.bungee.players.playerbulk.if-not-last");
    public static String onlineMessageBPlayersBulkLast = StreamLine.config.getMessString("online.message.bungee.players.playerbulk.if-last");
    // ... Join Leaves.
    // Discord.
    public static String discordOnline = StreamLine.config.getMessString("join-leave.discord.online.text");
    public static String discordOnlineEmbed = StreamLine.config.getMessString("join-leave.discord.online.embed");
    public static String discordOffline = StreamLine.config.getMessString("join-leave.discord.offline.text");
    public static String discordOfflineEmbed = StreamLine.config.getMessString("join-leave.discord.offline.embed");
    // Bungee.
    public static String bungeeOnline = StreamLine.config.getMessString("join-leave.bungee.online");
    public static String bungeeOffline = StreamLine.config.getMessString("join-leave.bungee.offline");
    // ... StaffOnline.
    // Discord.
    public static String sOnlineMessageEmbedTitle = StreamLine.config.getMessString("staffonline.message.embed-title");
    public static String sOnlineDiscordMain = StreamLine.config.getMessString("staffonline.message.discord.main");
    public static String sOnlineDiscordBulkNotLast = StreamLine.config.getMessString("staffonline.message.discord.staffbulk.if-not-last");
    public static String sOnlineDiscordBulkLast = StreamLine.config.getMessString("staffonline.message.discord.staffbulk.if-last");
    // Bungee.
    public static String sOnlineBungeeMain = StreamLine.config.getMessString("staffonline.message.bungee.main");
    public static String sOnlineBungeeBulkNotLast = StreamLine.config.getMessString("staffonline.message.bungee.staffbulk.if-not-last");
    public static String sOnlineBungeeBulkLast = StreamLine.config.getMessString("staffonline.message.bungee.staffbulk.if-last");
    // Stream.
    public static String streamNeedLink = StreamLine.config.getMessString("stream.need-link");
    public static String streamNotLink = StreamLine.config.getMessString("stream.not-link");
    public static String streamMessage = StreamLine.config.getMessString("stream.message");
    public static String streamHoverPrefix = StreamLine.config.getMessString("stream.hover-prefix");
    // Party.
    public static String partyConnect = StreamLine.config.getMessString("party.connect");
    public static String partyDisconnect = StreamLine.config.getMessString("party.disconnect");
    // Guild.
    public static String guildConnect = StreamLine.config.getMessString("guild.connect");
    public static String guildDisconnect = StreamLine.config.getMessString("guild.disconnect");
    // Parties.
    public static String partiesNone = StreamLine.config.getMessString("parties.no-parties");
    public static String partiesMessage = StreamLine.config.getMessString("parties.parties");
    public static String partiesModsNLast = StreamLine.config.getMessString("parties.mods.not-last");
    public static String partiesModsLast = StreamLine.config.getMessString("parties.mods.last");
    public static String partiesMemsNLast = StreamLine.config.getMessString("parties.members.not-last");
    public static String partiesMemsLast = StreamLine.config.getMessString("parties.members.last");
    public static String partiesTMemsNLast = StreamLine.config.getMessString("parties.totalmembers.not-last");
    public static String partiesTMemsLast = StreamLine.config.getMessString("parties.totalmembers.last");
    public static String partiesInvsNLast = StreamLine.config.getMessString("parties.invites.not-last");
    public static String partiesInvsLast = StreamLine.config.getMessString("parties.invites.last");
    public static String partiesIsPublicTrue = StreamLine.config.getMessString("parties.ispublic.true");
    public static String partiesIsPublicFalse = StreamLine.config.getMessString("parties.ispublic.false");
    public static String partiesIsMutedTrue = StreamLine.config.getMessString("parties.ismuted.true");
    public static String partiesIsMutedFalse = StreamLine.config.getMessString("parties.ismuted.false");
    // Guilds.
    public static String guildsNone = StreamLine.config.getMessString("guilds.no-guilds");
    public static String guildsMessage = StreamLine.config.getMessString("guilds.guilds");
    public static String guildsModsNLast = StreamLine.config.getMessString("guilds.mods.not-last");
    public static String guildsModsLast = StreamLine.config.getMessString("guilds.mods.last");
    public static String guildsMemsNLast = StreamLine.config.getMessString("guilds.members.not-last");
    public static String guildsMemsLast = StreamLine.config.getMessString("guilds.members.last");
    public static String guildsTMemsNLast = StreamLine.config.getMessString("guilds.totalmembers.not-last");
    public static String guildsTMemsLast = StreamLine.config.getMessString("guilds.totalmembers.last");
    public static String guildsInvsNLast = StreamLine.config.getMessString("guilds.invites.not-last");
    public static String guildsInvsLast = StreamLine.config.getMessString("guilds.invites.last");
    public static String guildsIsPublicTrue = StreamLine.config.getMessString("guilds.ispublic.true");
    public static String guildsIsPublicFalse = StreamLine.config.getMessString("guilds.ispublic.false");
    public static String guildsIsMutedTrue = StreamLine.config.getMessString("guilds.ismuted.true");
    public static String guildsIsMutedFalse = StreamLine.config.getMessString("guilds.ismuted.false");
    // Sudo.
    public static String sudoWorked = StreamLine.config.getMessString("sudo.worked");
    public static String sudoNoWork = StreamLine.config.getMessString("sudo.no-work");
    public static String sudoNoSudo = StreamLine.config.getMessString("sudo.no-sudo");
    // SSPY.
    public static String sspyToggle = StreamLine.config.getMessString("sspy.message");
    public static String sspyOn = StreamLine.config.getMessString("sspy.toggle.true");
    public static String sspyOff = StreamLine.config.getMessString("sspy.toggle.false");
    // GSPY.
    public static String gspyToggle = StreamLine.config.getMessString("gspy.message");
    public static String gspyOn = StreamLine.config.getMessString("gspy.toggle.true");
    public static String gspyOff = StreamLine.config.getMessString("gspy.toggle.false");
    // PSPY.
    public static String pspyToggle = StreamLine.config.getMessString("pspy.message");
    public static String pspyOn = StreamLine.config.getMessString("pspy.toggle.true");
    public static String pspyOff = StreamLine.config.getMessString("pspy.toggle.false");
    // PSPY.
    public static String scViewToggle = StreamLine.config.getMessString("sc-view.message");
    public static String scViewOn = StreamLine.config.getMessString("sc-view.toggle.true");
    public static String scViewOff = StreamLine.config.getMessString("sc-view.toggle.false");
    // EVReload.
    public static String evReload = StreamLine.config.getMessString("evreload.message");
    // Points.
    public static String pointsViewS = StreamLine.config.getMessString("points.view.self");
    public static String pointsViewO = StreamLine.config.getMessString("points.view.other");
    public static String pointsAddS = StreamLine.config.getMessString("points.add.self");
    public static String pointsAddO = StreamLine.config.getMessString("points.add.other");
    public static String pointsRemoveS = StreamLine.config.getMessString("points.remove.self");
    public static String pointsRemoveO = StreamLine.config.getMessString("points.remove.other");
    public static String pointsSetS = StreamLine.config.getMessString("points.set.self");
    public static String pointsSetO = StreamLine.config.getMessString("points.set.other");
    // Ignore.
    public static String ignoreAddSelf = StreamLine.config.getMessString("ignore.add.self");
    public static String ignoreAddIgnored = StreamLine.config.getMessString("ignore.add.ignored");
    public static String ignoreAddAlready = StreamLine.config.getMessString("ignore.add.already");
    public static String ignoreAddNSelf = StreamLine.config.getMessString("ignore.add.not-self");
    public static String ignoreRemSelf = StreamLine.config.getMessString("ignore.remove.self");
    public static String ignoreRemIgnored = StreamLine.config.getMessString("ignore.remove.ignored");
    public static String ignoreRemAlready = StreamLine.config.getMessString("ignore.remove.already");
    public static String ignoreRemNSelf = StreamLine.config.getMessString("ignore.remove.not-self");
    public static String ignoreListMain = StreamLine.config.getMessString("ignore.list.main");
    public static String ignoreListNLast = StreamLine.config.getMessString("ignore.list.ignores.not-last");
    public static String ignoreListLast = StreamLine.config.getMessString("ignore.list.ignores.last");
    // Message.
    public static String messageSender = StreamLine.config.getMessString("message.sender");
    public static String messageTo = StreamLine.config.getMessString("message.to");
    public static String messageIgnored = StreamLine.config.getMessString("message.ignored");
    public static String messageSSPY = StreamLine.config.getMessString("message.sspy");
    // Reply.
    public static String replySender = StreamLine.config.getMessString("message.sender");
    public static String replyTo = StreamLine.config.getMessString("message.to");
    public static String replyIgnored = StreamLine.config.getMessString("message.ignored");
    public static String replySSPY = StreamLine.config.getMessString("message.sspy");
    // Mute.
    public static String muteEmbed = StreamLine.config.getMessString("mute.discord-embed-title");
    public static String muteCannot = StreamLine.config.getMessString("mute.cannot");
    public static String muteMTempSender = StreamLine.config.getMessString("mute.mute.temp.sender");
    public static String muteMTempMuted = StreamLine.config.getMessString("mute.mute.temp.muted");
    public static String muteMTempAlready = StreamLine.config.getMessString("mute.mute.temp.already");
    public static String muteMTempStaff = StreamLine.config.getMessString("mute.mute.temp.staff");
    public static String muteMTempDiscord = StreamLine.config.getMessString("mute.mute.temp.discord");
    public static String muteMPermSender = StreamLine.config.getMessString("mute.mute.perm.sender");
    public static String muteMPermMuted = StreamLine.config.getMessString("mute.mute.perm.muted");
    public static String muteMPermAlready = StreamLine.config.getMessString("mute.mute.perm.already");
    public static String muteMPermStaff = StreamLine.config.getMessString("mute.mute.perm.staff");
    public static String muteMPermDiscord = StreamLine.config.getMessString("mute.mute.perm.discord");
    public static String muteUnSender = StreamLine.config.getMessString("mute.unmute.sender");
    public static String muteUnMuted = StreamLine.config.getMessString("mute.unmute.muted");
    public static String muteUnAlready = StreamLine.config.getMessString("mute.unmute.already");
    public static String muteUnStaff = StreamLine.config.getMessString("mute.unmute.staff");
    public static String muteUnDiscord = StreamLine.config.getMessString("mute.unmute.discord");
    public static String muteCheckMain = StreamLine.config.getMessString("mute.check.main");
    public static String muteCheckMuted = StreamLine.config.getMessString("mute.check.muted");
    public static String muteCheckUnMuted = StreamLine.config.getMessString("mute.check.unmuted");
    public static String muteCheckNoDate = StreamLine.config.getMessString("mute.check.no-date");
    // Kick.
    public static String kickEmbed = StreamLine.config.getMessString("kick.discord-embed-title");
    public static String kickCannot = StreamLine.config.getMessString("kick.cannot");
    public static String kickSender = StreamLine.config.getMessString("kick.sender");
    public static String kickKicked = StreamLine.config.getMessString("kick.kicked");
    public static String kickStaff = StreamLine.config.getMessString("kick.staff");
    public static String kickDiscord = StreamLine.config.getMessString("kick.discord");
    // Ban.
    public static String banEmbed = StreamLine.config.getMessString("ban.discord-embed-title");
    public static String banCannot = StreamLine.config.getMessString("ban.cannot");
    public static String banBTempSender = StreamLine.config.getMessString("ban.ban.temp.sender");
    public static String banBTempAlready = StreamLine.config.getMessString("ban.ban.temp.already");
    public static String banBTempStaff = StreamLine.config.getMessString("ban.ban.temp.staff");
    public static String banBTempDiscord = StreamLine.config.getMessString("ban.ban.temp.discord");
    public static String banBPermSender = StreamLine.config.getMessString("ban.ban.perm.sender");
    public static String banBPermAlready = StreamLine.config.getMessString("ban.ban.perm.already");
    public static String banBPermStaff = StreamLine.config.getMessString("ban.ban.perm.staff");
    public static String banBPermDiscord = StreamLine.config.getMessString("ban.ban.perm.discord");
    public static String banUnSender = StreamLine.config.getMessString("ban.unban.sender");
    public static String banUnAlready = StreamLine.config.getMessString("ban.unban.already");
    public static String banUnStaff = StreamLine.config.getMessString("ban.unban.staff");
    public static String banUnDiscord = StreamLine.config.getMessString("ban.unban.discord");
    public static String banCheckMain = StreamLine.config.getMessString("ban.check.main");
    public static String banCheckBanned = StreamLine.config.getMessString("ban.check.banned");
    public static String banCheckUnBanned = StreamLine.config.getMessString("ban.check.unbanned");
    public static String banCheckNoDate = StreamLine.config.getMessString("ban.check.no-date");
    // IPBan.
    public static String ipBanEmbed = StreamLine.config.getMessString("ipban.discord-embed-title");
    public static String ipBanCannot = StreamLine.config.getMessString("ipban.cannot");
    public static String ipBanBTempSender = StreamLine.config.getMessString("ipban.ban.temp.sender");
    public static String ipBanBTempAlready = StreamLine.config.getMessString("ipban.ban.temp.already");
    public static String ipBanBTempStaff = StreamLine.config.getMessString("ipban.ban.temp.staff");
    public static String ipBanBTempDiscord = StreamLine.config.getMessString("ipban.ban.temp.discord");
    public static String ipBanBPermSender = StreamLine.config.getMessString("ipban.ban.perm.sender");
    public static String ipBanBPermAlready = StreamLine.config.getMessString("ipban.ban.perm.already");
    public static String ipBanBPermStaff = StreamLine.config.getMessString("ipban.ban.perm.staff");
    public static String ipBanBPermDiscord = StreamLine.config.getMessString("ipban.ban.perm.discord");
    public static String ipBanUnSender = StreamLine.config.getMessString("ipban.unban.sender");
    public static String ipBanUnAlready = StreamLine.config.getMessString("ipban.unban.already");
    public static String ipBanUnStaff = StreamLine.config.getMessString("ipban.unban.staff");
    public static String ipBanUnDiscord = StreamLine.config.getMessString("ipban.unban.discord");
    public static String ipBanCheckMain = StreamLine.config.getMessString("ipban.check.main");
    public static String ipBanCheckBanned = StreamLine.config.getMessString("ipban.check.banned");
    public static String ipBanCheckUnBanned = StreamLine.config.getMessString("ipban.check.unbanned");
    public static String ipBanCheckNoDate = StreamLine.config.getMessString("ipban.check.no-date");
    // Ignore.
    public static String friendReqSelf = StreamLine.config.getMessString("friend.request.self");
    public static String friendReqOther = StreamLine.config.getMessString("friend.request.other");
    public static String friendReqAlready = StreamLine.config.getMessString("friend.request.already");
    public static String friendReqNSelf = StreamLine.config.getMessString("friend.request.not-self");
    public static String friendReqIgnored = StreamLine.config.getMessString("friend.request.ignored");
    public static String friendAcceptSelf = StreamLine.config.getMessString("friend.accept.self");
    public static String friendAcceptOther = StreamLine.config.getMessString("friend.accept.other");
    public static String friendAcceptNone = StreamLine.config.getMessString("friend.accept.none");
    public static String friendDenySelf = StreamLine.config.getMessString("friend.deny.self");
    public static String friendDenyOther = StreamLine.config.getMessString("friend.deny.other");
    public static String friendDenyNone = StreamLine.config.getMessString("friend.deny.none");
    public static String friendRemSelf = StreamLine.config.getMessString("friend.remove.self");
    public static String friendRemOther = StreamLine.config.getMessString("friend.remove.other");
    public static String friendRemAlready = StreamLine.config.getMessString("friend.remove.already");
    public static String friendRemNSelf = StreamLine.config.getMessString("friend.remove.not-self");
    public static String friendListMain = StreamLine.config.getMessString("friend.list.main");
    public static String friendListFNLast = StreamLine.config.getMessString("friend.list.friends.not-last");
    public static String friendListFLast = StreamLine.config.getMessString("friend.list.friends.last");
    public static String friendListPTNLast = StreamLine.config.getMessString("friend.list.pending-to.not-last");
    public static String friendListPTLast = StreamLine.config.getMessString("friend.list.pending-to.last");
    public static String friendListPFNLast = StreamLine.config.getMessString("friend.list.pending-from.not-last");
    public static String friendListPFLast = StreamLine.config.getMessString("friend.list.pending-from.last");
    public static String friendConnect = StreamLine.config.getMessString("friend.connect");
    public static String friendDisconnect = StreamLine.config.getMessString("friend.disconnect");
    // GetStats.
    public static String getStatsNone = StreamLine.config.getMessString("getstats.no-stats");
    public static String getStatsMessage = StreamLine.config.getMessString("getstats.message.main");
    public static String getStatsNLast = StreamLine.config.getMessString("getstats.message.not-last");
    public static String getStatsLast = StreamLine.config.getMessString("getstats.message.last");
    // // Settings.
    // Set.
    public static String settingsSetMOTD = StreamLine.config.getMessString("settings.set.motd");
    public static String settingsSetMOTDTime = StreamLine.config.getMessString("settings.set.motd-time");
    public static String settingsSetVersion = StreamLine.config.getMessString("settings.set.version");
    public static String settingsSetSample = StreamLine.config.getMessString("settings.set.sample");
    public static String settingsSetMaxP = StreamLine.config.getMessString("settings.set.max-players");
    public static String settingsSetOnlineP = StreamLine.config.getMessString("settings.set.online-players");
    // Get.
    public static String settingsGetMOTD = StreamLine.config.getMessString("settings.get.motd");
    public static String settingsGetMOTDTime = StreamLine.config.getMessString("settings.get.motd-time");
    public static String settingsGetVersion = StreamLine.config.getMessString("settings.get.version");
    public static String settingsGetSample = StreamLine.config.getMessString("settings.get.sample");
    public static String settingsGetMaxP = StreamLine.config.getMessString("settings.get.max-players");
    public static String settingsGetOnlineP = StreamLine.config.getMessString("settings.get.online-players");
    // // Info.
    public static String info = StreamLine.config.getMessString("info");
    // // Graceful End.
    public static String gracefulEndSender = StreamLine.config.getMessString("graceful-end.sender");
    public static String gracefulEndKickMessage = StreamLine.config.getMessString("graceful-end.kick-message");
}