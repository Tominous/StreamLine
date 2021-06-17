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
    // ... Command Error.
    // Discord.
    public static final String discordCommandErrorUnd = message.getString("command-error.discord.undefined");
    // Bungee.
    public static final String bungeeCommandErrorUnd = message.getString("command-error.bungee.undefined");
    public static final String bungeeCommandErrorInt = message.getString("command-error.bungee.needs-int");
    public static final String bungeeCommandErrorSTime = message.getString("command-error.bungee.needs-stringed-time");
    // Command Disabled.
    public static final String discordCommandDisabled = message.getString("command-disabled.discord");
    public static final String bungeeCommandDisabled = message.getString("command-disabled.bungee");
    // Module Disabled.
    public static final String discordModuleDisabled = message.getString("module-disabled.discord");
    public static final String bungeeModuleDisabled = message.getString("module-disabled.bungee");
    // Not command / improper usage.
    public static final String discordNotACommand = message.getString("not-a-command.discord");
    public static final String bungeeImproperUsage = message.getString("improper-usage.bungee");
    // Command needs args.
    public static final String discordNeedsMore = message.getString("command-needs-args.more.discord");
    public static final String bungeeNeedsMore = message.getString("command-needs-args.more.bungee");
    public static final String discordNeedsLess = message.getString("command-needs-args.less.discord");
    public static final String bungeeNeedsLess = message.getString("command-needs-args.less.bungee");
    // Players.
    public static final String offlineB = message.getString("players.bungee.offline");
    public static final String onlineB = message.getString("players.bungee.online");
    public static final String offlineD = message.getString("players.discord.offline");
    public static final String onlineD = message.getString("players.discord.online");
    // Redirect.
    public static final String vbBlocked = message.getString("redirect.by-version.blocked");
    // ... Punishments.
    // Mutes.
    public static final String punMutedTemp = message.getString("punishments.muted.temp");
    public static final String punMutedPerm = message.getString("punishments.muted.perm");
    // Bans.
    public static final String punBannedTemp = message.getString("punishments.banned.temp");
    public static final String punBannedPerm = message.getString("punishments.banned.perm");
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
    public static final String staffChatJustPrefix = message.getString("staffchat.just-prefix");
    public static final String staffChatToggle = message.getString("staffchat.toggle.message");
    public static final String staffChatOn = message.getString("staffchat.toggle.on");
    public static final String staffChatOff = message.getString("staffchat.toggle.off");
    // Online.
    public static final String onlineMessageNoPlayers = message.getString("online.message.no-players");
    public static final String onlineMessageNoGroups = message.getString("online.message.no-groups");
    public static final String onlineMessageEmbedTitle = message.getString("online.message.embed-title");
    public static final String onlineMessageDiscord = message.getString("online.message.discord");
    public static final String onlineMessageBMain = message.getString("online.message.bungee.main");
    public static final String onlineMessageBServers = message.getString("online.message.bungee.servers");
    public static final String onlineMessageBPlayersMain = message.getString("online.message.bungee.players.main");
    public static final String onlineMessageBPlayersBulkNotLast = message.getString("online.message.bungee.players.playerbulk.if-not-last");
    public static final String onlineMessageBPlayersBulkLast = message.getString("online.message.bungee.players.playerbulk.if-last");
    // ... Join Leaves.
    // Discord.
    public static final String discordOnline = message.getString("join-leave.discord.online.text");
    public static final String discordOnlineEmbed = message.getString("join-leave.discord.online.embed");
    public static final String discordOffline = message.getString("join-leave.discord.offline.text");
    public static final String discordOfflineEmbed = message.getString("join-leave.discord.offline.embed");
    // Bungee.
    public static final String bungeeOnline = message.getString("join-leave.bungee.online");
    public static final String bungeeOffline = message.getString("join-leave.bungee.offline");
    // ... StaffOnline.
    // Discord.
    public static final String sOnlineMessageEmbedTitle = message.getString("staffonline.message.embed-title");
    public static final String sOnlineDiscordOnline = message.getString("staffonline.message.discord.online");
    public static final String sOnlineDiscordOffline = message.getString("staffonline.message.discord.offline");
    public static final String sOnlineDiscordMain = message.getString("staffonline.message.discord.main");
    public static final String sOnlineDiscordBulkNotLast = message.getString("staffonline.message.discord.staffbulk.if-not-last");
    public static final String sOnlineDiscordBulkLast = message.getString("staffonline.message.discord.staffbulk.if-last");
    // Bungee.
    public static final String sOnlineBungeeMain = message.getString("staffonline.message.bungee.main");
    public static final String sOnlineBungeeBulkNotLast = message.getString("staffonline.message.bungee.staffbulk.if-not-last");
    public static final String sOnlineBungeeBulkLast = message.getString("staffonline.message.bungee.staffbulk.if-last");
    // Stream.
    public static final String streamNeedLink = message.getString("stream.need-link");
    public static final String streamNotLink = message.getString("stream.not-link");
    public static final String streamMessage = message.getString("stream.message");
    public static final String streamHoverPrefix = message.getString("stream.hover-prefix");
    // Party.
    public static final String partyConnect = message.getString("party.connect");
    public static final String partyDisconnect = message.getString("party.disconnect");
    // Guild.
    public static final String guildConnect = message.getString("guild.connect");
    public static final String guildDisconnect = message.getString("guild.disconnect");
    // Parties.
    public static final String partiesNone = message.getString("parties.no-parties");
    public static final String partiesMessage = message.getString("parties.parties");
    public static final String partiesModsNLast = message.getString("parties.mods.not-last");
    public static final String partiesModsLast = message.getString("parties.mods.last");
    public static final String partiesMemsNLast = message.getString("parties.members.not-last");
    public static final String partiesMemsLast = message.getString("parties.members.last");
    public static final String partiesTMemsNLast = message.getString("parties.totalmembers.not-last");
    public static final String partiesTMemsLast = message.getString("parties.totalmembers.last");
    public static final String partiesInvsNLast = message.getString("parties.invites.not-last");
    public static final String partiesInvsLast = message.getString("parties.invites.last");
    public static final String partiesIsPublicTrue = message.getString("parties.ispublic.true");
    public static final String partiesIsPublicFalse = message.getString("parties.ispublic.false");
    public static final String partiesIsMutedTrue = message.getString("parties.ismuted.true");
    public static final String partiesIsMutedFalse = message.getString("parties.ismuted.false");
    // Guilds.
    public static final String guildsNone = message.getString("guilds.no-guilds");
    public static final String guildsMessage = message.getString("guilds.guilds");
    public static final String guildsModsNLast = message.getString("guilds.mods.not-last");
    public static final String guildsModsLast = message.getString("guilds.mods.last");
    public static final String guildsMemsNLast = message.getString("guilds.members.not-last");
    public static final String guildsMemsLast = message.getString("guilds.members.last");
    public static final String guildsTMemsNLast = message.getString("guilds.totalmembers.not-last");
    public static final String guildsTMemsLast = message.getString("guilds.totalmembers.last");
    public static final String guildsInvsNLast = message.getString("guilds.invites.not-last");
    public static final String guildsInvsLast = message.getString("guilds.invites.last");
    public static final String guildsIsPublicTrue = message.getString("guilds.ispublic.true");
    public static final String guildsIsPublicFalse = message.getString("guilds.ispublic.false");
    public static final String guildsIsMutedTrue = message.getString("guilds.ismuted.true");
    public static final String guildsIsMutedFalse = message.getString("guilds.ismuted.false");
    // Sudo.
    public static final String sudoWorked = message.getString("sudo.worked");
    public static final String sudoNoWork = message.getString("sudo.no-work");
    public static final String sudoNoSudo = message.getString("sudo.no-sudo");
    // SSPY.
    public static final String sspyToggle = message.getString("sspy.toggle.message");
    public static final String sspyOn = message.getString("sspy.toggle.on");
    public static final String sspyOff = message.getString("sspy.toggle.off");
    // GSPY.
    public static final String gspyToggle = message.getString("gspy.toggle.message");
    public static final String gspyOn = message.getString("gspy.toggle.on");
    public static final String gspyOff = message.getString("gspy.toggle.off");
    // PSPY.
    public static final String pspyToggle = message.getString("pspy.toggle.message");
    public static final String pspyOn = message.getString("pspy.toggle.on");
    public static final String pspyOff = message.getString("pspy.toggle.off");
    // EVReload.
    public static final String evReload = message.getString("evreload.message");
    // Points.
    public static final String pointsViewS = message.getString("points.view.self");
    public static final String pointsViewO = message.getString("points.view.other");
    public static final String pointsAddS = message.getString("points.add.self");
    public static final String pointsAddO = message.getString("points.add.other");
    public static final String pointsRemoveS = message.getString("points.remove.self");
    public static final String pointsRemoveO = message.getString("points.remove.other");
    public static final String pointsSetS = message.getString("points.set.self");
    public static final String pointsSetO = message.getString("points.set.other");
    // Ignore.
    public static final String ignoreAddSelf = message.getString("ignore.add.self");
    public static final String ignoreAddIgnored = message.getString("ignore.add.ignored");
    public static final String ignoreAddAlready = message.getString("ignore.add.already");
    public static final String ignoreAddNSelf = message.getString("ignore.add.not-self");
    public static final String ignoreRemSelf = message.getString("ignore.remove.self");
    public static final String ignoreRemIgnored = message.getString("ignore.remove.ignored");
    public static final String ignoreRemAlready = message.getString("ignore.remove.already");
    public static final String ignoreRemNSelf = message.getString("ignore.remove.not-self");
    public static final String ignoreListMain = message.getString("ignore.list.main");
    public static final String ignoreListNLast = message.getString("ignore.list.ignores.not-last");
    public static final String ignoreListLast = message.getString("ignore.list.ignores.last");
    // Message.
    public static final String messageSender = message.getString("message.sender");
    public static final String messageTo = message.getString("message.to");
    public static final String messageIgnored = message.getString("message.ignored");
    public static final String messageSSPY = message.getString("message.sspy");
    // Reply.
    public static final String replySender = message.getString("message.sender");
    public static final String replyTo = message.getString("message.to");
    public static final String replyIgnored = message.getString("message.ignored");
    public static final String replySSPY = message.getString("message.sspy");
    // Mute.
    public static final String muteMTempSender = message.getString("mute.mute.temp.sender");
    public static final String muteMTempMuted = message.getString("mute.mute.temp.muted");
    public static final String muteMTempAlready = message.getString("mute.mute.temp.already");
    public static final String muteMPermSender = message.getString("mute.mute.perm.sender");
    public static final String muteMPermMuted = message.getString("mute.mute.perm.muted");
    public static final String muteMPermAlready = message.getString("mute.mute.perm.already");
    public static final String muteUnSender = message.getString("mute.unmute.sender");
    public static final String muteUnMuted = message.getString("mute.unmute.muted");
    public static final String muteUnAlready = message.getString("mute.unmute.already");
    public static final String muteCheckMain = message.getString("mute.check.main");
    public static final String muteCheckMuted = message.getString("mute.check.muted");
    public static final String muteCheckUnMuted = message.getString("mute.check.unmuted");
    // Ban.
    public static final String banBTempSender = message.getString("ban.ban.temp.sender");
    public static final String banBTempAlready = message.getString("ban.ban.temp.already");
    public static final String banBPermSender = message.getString("ban.ban.perm.sender");
    public static final String banBPermAlready = message.getString("ban.ban.perm.already");
    public static final String banUnSender = message.getString("ban.unban.sender");
    public static final String banUnAlready = message.getString("ban.unban.already");
    public static final String banCheckMain = message.getString("ban.check.main");
    public static final String banCheckBanned = message.getString("ban.check.banned");
    public static final String banCheckUnBanned = message.getString("ban.check.unbanned");
    // Ignore.
    public static final String friendReqSelf = message.getString("friend.request.self");
    public static final String friendReqOther = message.getString("friend.request.other");
    public static final String friendReqAlready = message.getString("friend.request.already");
    public static final String friendReqNSelf = message.getString("friend.request.not-self");
    public static final String friendReqIgnored = message.getString("friend.request.ignored");
    public static final String friendAcceptSelf = message.getString("friend.accept.self");
    public static final String friendAcceptOther = message.getString("friend.accept.other");
    public static final String friendAcceptNone = message.getString("friend.accept.none");
    public static final String friendDenySelf = message.getString("friend.deny.self");
    public static final String friendDenyOther = message.getString("friend.deny.other");
    public static final String friendDenyNone = message.getString("friend.deny.none");
    public static final String friendRemSelf = message.getString("friend.remove.self");
    public static final String friendRemOther = message.getString("friend.remove.other");
    public static final String friendRemAlready = message.getString("friend.remove.already");
    public static final String friendRemNSelf = message.getString("friend.remove.not-self");
    public static final String friendListMain = message.getString("friend.list.main");
    public static final String friendListFNLast = message.getString("friend.list.friends.not-last");
    public static final String friendListFLast = message.getString("friend.list.friends.last");
    public static final String friendListPTNLast = message.getString("friend.list.pending-to.not-last");
    public static final String friendListPTLast = message.getString("friend.list.pending-to.last");
    public static final String friendListPFNLast = message.getString("friend.list.pending-from.not-last");
    public static final String friendListPFLast = message.getString("friend.list.pending-from.last");
    public static final String friendConnect = message.getString("friend.connect");
    public static final String friendDisconnect = message.getString("friend.disconnect");
    // GetStats.
    public static final String getStatsNone = message.getString("getstats.no-stats");
    public static final String getStatsMessage = message.getString("getstats.message.main");
    public static final String getStatsNLast = message.getString("getstats.message.not-last");
    public static final String getStatsLast = message.getString("getstats.message.last");
    // Kick.
    public static final String kickSender = message.getString("kick.sender");
    public static final String kickKicked = message.getString("kick.kicked");
}