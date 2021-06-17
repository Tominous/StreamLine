package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.md_5.bungee.config.Configuration;

public class MessageConfUtils {
    // Messages:
//    public static final String s = StreamLine.getConfig().getMessString("");
    // Basics.
    public static final String version = StreamLine.getConfig().getMessString("version");
    public static final String prefix = StreamLine.getConfig().getMessString("message-prefix");
    public static final String noPerm = StreamLine.getConfig().getMessString("no-permission");
    public static final String reload = StreamLine.getConfig().getMessString("reload-message");
    public static final String onlyPlayers = StreamLine.getConfig().getMessString("only-players");
    public static final String noPlayer = StreamLine.getConfig().getMessString("no-player");
    public static final String discordErrTitle = StreamLine.getConfig().getMessString("discord-err-title");
    // ... Command Error.
    // Discord.
    public static final String discordCommandErrorUnd = StreamLine.getConfig().getMessString("command-error.discord.undefined");
    // Bungee.
    public static final String bungeeCommandErrorUnd = StreamLine.getConfig().getMessString("command-error.bungee.undefined");
    public static final String bungeeCommandErrorInt = StreamLine.getConfig().getMessString("command-error.bungee.needs-int");
    public static final String bungeeCommandErrorSTime = StreamLine.getConfig().getMessString("command-error.bungee.needs-stringed-time");
    // Command Disabled.
    public static final String discordCommandDisabled = StreamLine.getConfig().getMessString("command-disabled.discord");
    public static final String bungeeCommandDisabled = StreamLine.getConfig().getMessString("command-disabled.bungee");
    // Module Disabled.
    public static final String discordModuleDisabled = StreamLine.getConfig().getMessString("module-disabled.discord");
    public static final String bungeeModuleDisabled = StreamLine.getConfig().getMessString("module-disabled.bungee");
    // Not command / improper usage.
    public static final String discordNotACommand = StreamLine.getConfig().getMessString("not-a-command.discord");
    public static final String bungeeImproperUsage = StreamLine.getConfig().getMessString("improper-usage.bungee");
    // Command needs args.
    public static final String discordNeedsMore = StreamLine.getConfig().getMessString("command-needs-args.more.discord");
    public static final String bungeeNeedsMore = StreamLine.getConfig().getMessString("command-needs-args.more.bungee");
    public static final String discordNeedsLess = StreamLine.getConfig().getMessString("command-needs-args.less.discord");
    public static final String bungeeNeedsLess = StreamLine.getConfig().getMessString("command-needs-args.less.bungee");
    // Players.
    public static final String offlineB = StreamLine.getConfig().getMessString("players.bungee.offline");
    public static final String onlineB = StreamLine.getConfig().getMessString("players.bungee.online");
    public static final String offlineD = StreamLine.getConfig().getMessString("players.discord.offline");
    public static final String onlineD = StreamLine.getConfig().getMessString("players.discord.online");
    // Redirect.
    public static final String vbBlocked = StreamLine.getConfig().getMessString("redirect.by-version.blocked");
    // ... Punishments.
    // Mutes.
    public static final String punMutedTemp = StreamLine.getConfig().getMessString("punishments.muted.temp");
    public static final String punMutedPerm = StreamLine.getConfig().getMessString("punishments.muted.perm");
    // Bans.
    public static final String punBannedTemp = StreamLine.getConfig().getMessString("punishments.banned.temp");
    public static final String punBannedPerm = StreamLine.getConfig().getMessString("punishments.banned.perm");
    // Reports.
    public static final String reportEmbedTitle = StreamLine.getConfig().getMessString("report-message.embed-title");
    public static final String dToDReportMessage = StreamLine.getConfig().getMessString("report-message.from-discord.discord");
    public static final String dToBReportMessage = StreamLine.getConfig().getMessString("report-message.from-discord.bungee");
    public static final String dConfirmReportMessage = StreamLine.getConfig().getMessString("report-message.from-discord.confirmation");
    public static final String bToDReportMessage = StreamLine.getConfig().getMessString("report-message.from-bungee.discord");
    public static final String bToBReportMessage = StreamLine.getConfig().getMessString("report-message.from-bungee.bungee");
    public static final String bConfirmReportMessage = StreamLine.getConfig().getMessString("report-message.from-bungee.confirmation");
    // StaffChat.
    public static final String staffChatEmbedTitle = StreamLine.getConfig().getMessString("staffchat.message.embed-title");
    public static final String discordStaffChatMessage = StreamLine.getConfig().getMessString("staffchat.message.discord");
    public static final String bungeeStaffChatMessage = StreamLine.getConfig().getMessString("staffchat.message.bungee");
    public static final String discordStaffChatFrom = StreamLine.getConfig().getMessString("staffchat.message.from.discord");
    public static final String bungeeStaffChatFrom = StreamLine.getConfig().getMessString("staffchat.message.from.bungee");
    public static final String staffChatJustPrefix = StreamLine.getConfig().getMessString("staffchat.just-prefix");
    public static final String staffChatToggle = StreamLine.getConfig().getMessString("staffchat.toggle.message");
    public static final String staffChatOn = StreamLine.getConfig().getMessString("staffchat.toggle.on");
    public static final String staffChatOff = StreamLine.getConfig().getMessString("staffchat.toggle.off");
    // Online.
    public static final String onlineMessageNoPlayers = StreamLine.getConfig().getMessString("online.message.no-players");
    public static final String onlineMessageNoGroups = StreamLine.getConfig().getMessString("online.message.no-groups");
    public static final String onlineMessageEmbedTitle = StreamLine.getConfig().getMessString("online.message.embed-title");
    public static final String onlineMessageDiscord = StreamLine.getConfig().getMessString("online.message.discord");
    public static final String onlineMessageBMain = StreamLine.getConfig().getMessString("online.message.bungee.main");
    public static final String onlineMessageBServers = StreamLine.getConfig().getMessString("online.message.bungee.servers");
    public static final String onlineMessageBPlayersMain = StreamLine.getConfig().getMessString("online.message.bungee.players.main");
    public static final String onlineMessageBPlayersBulkNotLast = StreamLine.getConfig().getMessString("online.message.bungee.players.playerbulk.if-not-last");
    public static final String onlineMessageBPlayersBulkLast = StreamLine.getConfig().getMessString("online.message.bungee.players.playerbulk.if-last");
    // ... Join Leaves.
    // Discord.
    public static final String discordOnline = StreamLine.getConfig().getMessString("join-leave.discord.online.text");
    public static final String discordOnlineEmbed = StreamLine.getConfig().getMessString("join-leave.discord.online.embed");
    public static final String discordOffline = StreamLine.getConfig().getMessString("join-leave.discord.offline.text");
    public static final String discordOfflineEmbed = StreamLine.getConfig().getMessString("join-leave.discord.offline.embed");
    // Bungee.
    public static final String bungeeOnline = StreamLine.getConfig().getMessString("join-leave.bungee.online");
    public static final String bungeeOffline = StreamLine.getConfig().getMessString("join-leave.bungee.offline");
    // ... StaffOnline.
    // Discord.
    public static final String sOnlineMessageEmbedTitle = StreamLine.getConfig().getMessString("staffonline.message.embed-title");
    public static final String sOnlineDiscordOnline = StreamLine.getConfig().getMessString("staffonline.message.discord.online");
    public static final String sOnlineDiscordOffline = StreamLine.getConfig().getMessString("staffonline.message.discord.offline");
    public static final String sOnlineDiscordMain = StreamLine.getConfig().getMessString("staffonline.message.discord.main");
    public static final String sOnlineDiscordBulkNotLast = StreamLine.getConfig().getMessString("staffonline.message.discord.staffbulk.if-not-last");
    public static final String sOnlineDiscordBulkLast = StreamLine.getConfig().getMessString("staffonline.message.discord.staffbulk.if-last");
    // Bungee.
    public static final String sOnlineBungeeMain = StreamLine.getConfig().getMessString("staffonline.message.bungee.main");
    public static final String sOnlineBungeeBulkNotLast = StreamLine.getConfig().getMessString("staffonline.message.bungee.staffbulk.if-not-last");
    public static final String sOnlineBungeeBulkLast = StreamLine.getConfig().getMessString("staffonline.message.bungee.staffbulk.if-last");
    // Stream.
    public static final String streamNeedLink = StreamLine.getConfig().getMessString("stream.need-link");
    public static final String streamNotLink = StreamLine.getConfig().getMessString("stream.not-link");
    public static final String streamMessage = StreamLine.getConfig().getMessString("stream.message");
    public static final String streamHoverPrefix = StreamLine.getConfig().getMessString("stream.hover-prefix");
    // Party.
    public static final String partyConnect = StreamLine.getConfig().getMessString("party.connect");
    public static final String partyDisconnect = StreamLine.getConfig().getMessString("party.disconnect");
    // Guild.
    public static final String guildConnect = StreamLine.getConfig().getMessString("guild.connect");
    public static final String guildDisconnect = StreamLine.getConfig().getMessString("guild.disconnect");
    // Parties.
    public static final String partiesNone = StreamLine.getConfig().getMessString("parties.no-parties");
    public static final String partiesMessage = StreamLine.getConfig().getMessString("parties.parties");
    public static final String partiesModsNLast = StreamLine.getConfig().getMessString("parties.mods.not-last");
    public static final String partiesModsLast = StreamLine.getConfig().getMessString("parties.mods.last");
    public static final String partiesMemsNLast = StreamLine.getConfig().getMessString("parties.members.not-last");
    public static final String partiesMemsLast = StreamLine.getConfig().getMessString("parties.members.last");
    public static final String partiesTMemsNLast = StreamLine.getConfig().getMessString("parties.totalmembers.not-last");
    public static final String partiesTMemsLast = StreamLine.getConfig().getMessString("parties.totalmembers.last");
    public static final String partiesInvsNLast = StreamLine.getConfig().getMessString("parties.invites.not-last");
    public static final String partiesInvsLast = StreamLine.getConfig().getMessString("parties.invites.last");
    public static final String partiesIsPublicTrue = StreamLine.getConfig().getMessString("parties.ispublic.true");
    public static final String partiesIsPublicFalse = StreamLine.getConfig().getMessString("parties.ispublic.false");
    public static final String partiesIsMutedTrue = StreamLine.getConfig().getMessString("parties.ismuted.true");
    public static final String partiesIsMutedFalse = StreamLine.getConfig().getMessString("parties.ismuted.false");
    // Guilds.
    public static final String guildsNone = StreamLine.getConfig().getMessString("guilds.no-guilds");
    public static final String guildsMessage = StreamLine.getConfig().getMessString("guilds.guilds");
    public static final String guildsModsNLast = StreamLine.getConfig().getMessString("guilds.mods.not-last");
    public static final String guildsModsLast = StreamLine.getConfig().getMessString("guilds.mods.last");
    public static final String guildsMemsNLast = StreamLine.getConfig().getMessString("guilds.members.not-last");
    public static final String guildsMemsLast = StreamLine.getConfig().getMessString("guilds.members.last");
    public static final String guildsTMemsNLast = StreamLine.getConfig().getMessString("guilds.totalmembers.not-last");
    public static final String guildsTMemsLast = StreamLine.getConfig().getMessString("guilds.totalmembers.last");
    public static final String guildsInvsNLast = StreamLine.getConfig().getMessString("guilds.invites.not-last");
    public static final String guildsInvsLast = StreamLine.getConfig().getMessString("guilds.invites.last");
    public static final String guildsIsPublicTrue = StreamLine.getConfig().getMessString("guilds.ispublic.true");
    public static final String guildsIsPublicFalse = StreamLine.getConfig().getMessString("guilds.ispublic.false");
    public static final String guildsIsMutedTrue = StreamLine.getConfig().getMessString("guilds.ismuted.true");
    public static final String guildsIsMutedFalse = StreamLine.getConfig().getMessString("guilds.ismuted.false");
    // Sudo.
    public static final String sudoWorked = StreamLine.getConfig().getMessString("sudo.worked");
    public static final String sudoNoWork = StreamLine.getConfig().getMessString("sudo.no-work");
    public static final String sudoNoSudo = StreamLine.getConfig().getMessString("sudo.no-sudo");
    // SSPY.
    public static final String sspyToggle = StreamLine.getConfig().getMessString("sspy.toggle.message");
    public static final String sspyOn = StreamLine.getConfig().getMessString("sspy.toggle.on");
    public static final String sspyOff = StreamLine.getConfig().getMessString("sspy.toggle.off");
    // GSPY.
    public static final String gspyToggle = StreamLine.getConfig().getMessString("gspy.toggle.message");
    public static final String gspyOn = StreamLine.getConfig().getMessString("gspy.toggle.on");
    public static final String gspyOff = StreamLine.getConfig().getMessString("gspy.toggle.off");
    // PSPY.
    public static final String pspyToggle = StreamLine.getConfig().getMessString("pspy.toggle.message");
    public static final String pspyOn = StreamLine.getConfig().getMessString("pspy.toggle.on");
    public static final String pspyOff = StreamLine.getConfig().getMessString("pspy.toggle.off");
    // EVReload.
    public static final String evReload = StreamLine.getConfig().getMessString("evreload.message");
    // Points.
    public static final String pointsViewS = StreamLine.getConfig().getMessString("points.view.self");
    public static final String pointsViewO = StreamLine.getConfig().getMessString("points.view.other");
    public static final String pointsAddS = StreamLine.getConfig().getMessString("points.add.self");
    public static final String pointsAddO = StreamLine.getConfig().getMessString("points.add.other");
    public static final String pointsRemoveS = StreamLine.getConfig().getMessString("points.remove.self");
    public static final String pointsRemoveO = StreamLine.getConfig().getMessString("points.remove.other");
    public static final String pointsSetS = StreamLine.getConfig().getMessString("points.set.self");
    public static final String pointsSetO = StreamLine.getConfig().getMessString("points.set.other");
    // Ignore.
    public static final String ignoreAddSelf = StreamLine.getConfig().getMessString("ignore.add.self");
    public static final String ignoreAddIgnored = StreamLine.getConfig().getMessString("ignore.add.ignored");
    public static final String ignoreAddAlready = StreamLine.getConfig().getMessString("ignore.add.already");
    public static final String ignoreAddNSelf = StreamLine.getConfig().getMessString("ignore.add.not-self");
    public static final String ignoreRemSelf = StreamLine.getConfig().getMessString("ignore.remove.self");
    public static final String ignoreRemIgnored = StreamLine.getConfig().getMessString("ignore.remove.ignored");
    public static final String ignoreRemAlready = StreamLine.getConfig().getMessString("ignore.remove.already");
    public static final String ignoreRemNSelf = StreamLine.getConfig().getMessString("ignore.remove.not-self");
    public static final String ignoreListMain = StreamLine.getConfig().getMessString("ignore.list.main");
    public static final String ignoreListNLast = StreamLine.getConfig().getMessString("ignore.list.ignores.not-last");
    public static final String ignoreListLast = StreamLine.getConfig().getMessString("ignore.list.ignores.last");
    // Message.
    public static final String messageSender = StreamLine.getConfig().getMessString("message.sender");
    public static final String messageTo = StreamLine.getConfig().getMessString("message.to");
    public static final String messageIgnored = StreamLine.getConfig().getMessString("message.ignored");
    public static final String messageSSPY = StreamLine.getConfig().getMessString("message.sspy");
    // Reply.
    public static final String replySender = StreamLine.getConfig().getMessString("message.sender");
    public static final String replyTo = StreamLine.getConfig().getMessString("message.to");
    public static final String replyIgnored = StreamLine.getConfig().getMessString("message.ignored");
    public static final String replySSPY = StreamLine.getConfig().getMessString("message.sspy");
    // Mute.
    public static final String muteMTempSender = StreamLine.getConfig().getMessString("mute.mute.temp.sender");
    public static final String muteMTempMuted = StreamLine.getConfig().getMessString("mute.mute.temp.muted");
    public static final String muteMTempAlready = StreamLine.getConfig().getMessString("mute.mute.temp.already");
    public static final String muteMPermSender = StreamLine.getConfig().getMessString("mute.mute.perm.sender");
    public static final String muteMPermMuted = StreamLine.getConfig().getMessString("mute.mute.perm.muted");
    public static final String muteMPermAlready = StreamLine.getConfig().getMessString("mute.mute.perm.already");
    public static final String muteUnSender = StreamLine.getConfig().getMessString("mute.unmute.sender");
    public static final String muteUnMuted = StreamLine.getConfig().getMessString("mute.unmute.muted");
    public static final String muteUnAlready = StreamLine.getConfig().getMessString("mute.unmute.already");
    public static final String muteCheckMain = StreamLine.getConfig().getMessString("mute.check.main");
    public static final String muteCheckMuted = StreamLine.getConfig().getMessString("mute.check.muted");
    public static final String muteCheckUnMuted = StreamLine.getConfig().getMessString("mute.check.unmuted");
    // Ban.
    public static final String banBTempSender = StreamLine.getConfig().getMessString("ban.ban.temp.sender");
    public static final String banBTempAlready = StreamLine.getConfig().getMessString("ban.ban.temp.already");
    public static final String banBPermSender = StreamLine.getConfig().getMessString("ban.ban.perm.sender");
    public static final String banBPermAlready = StreamLine.getConfig().getMessString("ban.ban.perm.already");
    public static final String banUnSender = StreamLine.getConfig().getMessString("ban.unban.sender");
    public static final String banUnAlready = StreamLine.getConfig().getMessString("ban.unban.already");
    public static final String banCheckMain = StreamLine.getConfig().getMessString("ban.check.main");
    public static final String banCheckBanned = StreamLine.getConfig().getMessString("ban.check.banned");
    public static final String banCheckUnBanned = StreamLine.getConfig().getMessString("ban.check.unbanned");
    // Ignore.
    public static final String friendReqSelf = StreamLine.getConfig().getMessString("friend.request.self");
    public static final String friendReqOther = StreamLine.getConfig().getMessString("friend.request.other");
    public static final String friendReqAlready = StreamLine.getConfig().getMessString("friend.request.already");
    public static final String friendReqNSelf = StreamLine.getConfig().getMessString("friend.request.not-self");
    public static final String friendReqIgnored = StreamLine.getConfig().getMessString("friend.request.ignored");
    public static final String friendAcceptSelf = StreamLine.getConfig().getMessString("friend.accept.self");
    public static final String friendAcceptOther = StreamLine.getConfig().getMessString("friend.accept.other");
    public static final String friendAcceptNone = StreamLine.getConfig().getMessString("friend.accept.none");
    public static final String friendDenySelf = StreamLine.getConfig().getMessString("friend.deny.self");
    public static final String friendDenyOther = StreamLine.getConfig().getMessString("friend.deny.other");
    public static final String friendDenyNone = StreamLine.getConfig().getMessString("friend.deny.none");
    public static final String friendRemSelf = StreamLine.getConfig().getMessString("friend.remove.self");
    public static final String friendRemOther = StreamLine.getConfig().getMessString("friend.remove.other");
    public static final String friendRemAlready = StreamLine.getConfig().getMessString("friend.remove.already");
    public static final String friendRemNSelf = StreamLine.getConfig().getMessString("friend.remove.not-self");
    public static final String friendListMain = StreamLine.getConfig().getMessString("friend.list.main");
    public static final String friendListFNLast = StreamLine.getConfig().getMessString("friend.list.friends.not-last");
    public static final String friendListFLast = StreamLine.getConfig().getMessString("friend.list.friends.last");
    public static final String friendListPTNLast = StreamLine.getConfig().getMessString("friend.list.pending-to.not-last");
    public static final String friendListPTLast = StreamLine.getConfig().getMessString("friend.list.pending-to.last");
    public static final String friendListPFNLast = StreamLine.getConfig().getMessString("friend.list.pending-from.not-last");
    public static final String friendListPFLast = StreamLine.getConfig().getMessString("friend.list.pending-from.last");
    public static final String friendConnect = StreamLine.getConfig().getMessString("friend.connect");
    public static final String friendDisconnect = StreamLine.getConfig().getMessString("friend.disconnect");
    // GetStats.
    public static final String getStatsNone = StreamLine.getConfig().getMessString("getstats.no-stats");
    public static final String getStatsMessage = StreamLine.getConfig().getMessString("getstats.message.main");
    public static final String getStatsNLast = StreamLine.getConfig().getMessString("getstats.message.not-last");
    public static final String getStatsLast = StreamLine.getConfig().getMessString("getstats.message.last");
    // Kick.
    public static final String kickSender = StreamLine.getConfig().getMessString("kick.sender");
    public static final String kickKicked = StreamLine.getConfig().getMessString("kick.kicked");
    // // Settings.
    // Set.
    public static final String settingsSetMOTD = StreamLine.getConfig().getMessString("settings.set.motd");
    public static final String settingsSetMOTDTime = StreamLine.getConfig().getMessString("settings.set.motd-time");
    public static final String settingsSetVersion = StreamLine.getConfig().getMessString("settings.set.version");
    public static final String settingsSetSample = StreamLine.getConfig().getMessString("settings.set.sample");
    public static final String settingsSetMaxP = StreamLine.getConfig().getMessString("settings.set.max-players");
    public static final String settingsSetOnlineP = StreamLine.getConfig().getMessString("settings.set.online-players");
    // Get.
    public static final String settingsGetMOTD = StreamLine.getConfig().getMessString("settings.get.motd");
    public static final String settingsGetMOTDTime = StreamLine.getConfig().getMessString("settings.get.motd-time");
    public static final String settingsGetVersion = StreamLine.getConfig().getMessString("settings.get.version");
    public static final String settingsGetSample = StreamLine.getConfig().getMessString("settings.get.sample");
    public static final String settingsGetMaxP = StreamLine.getConfig().getMessString("settings.get.max-players");
    public static final String settingsGetOnlineP = StreamLine.getConfig().getMessString("settings.get.online-players");
}