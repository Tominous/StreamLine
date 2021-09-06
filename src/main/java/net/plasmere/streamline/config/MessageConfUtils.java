package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;

public class MessageConfUtils {
    // Messages:
    public static String prefix() {
        return StreamLine.config.mess.getString("message-prefix");
    }

    public static String noPerm() {
        return StreamLine.config.mess.getString("no-permission");
    }

    public static String reload() {
        return StreamLine.config.mess.getString("reload-message");
    }

    public static String onlyPlayers() {
        return StreamLine.config.mess.getString("only-players");
    }

    public static String noPlayer() {
        return StreamLine.config.mess.getString("no-player");
    }

    public static String discordErrTitle() {
        return StreamLine.config.mess.getString("discord-err-title");
    }

    // ... Command Error.
    // Discord.
    public static String discordCommandErrorUnd() {
        return StreamLine.config.mess.getString("command-error.discord.undefined");
    }

    // Bungee.
    public static String bungeeCommandErrorUnd() {
        return StreamLine.config.mess.getString("command-error.bungee.undefined");
    }

    public static String bungeeCommandErrorInt() {
        return StreamLine.config.mess.getString("command-error.bungee.needs-int");
    }

    public static String bungeeCommandErrorSTime() {
        return StreamLine.config.mess.getString("command-error.bungee.needs-stringed-time");
    }

    public static String bungeeCommandErrorNoYou() {
        return StreamLine.config.mess.getString("command-error.bungee.cant-find-you");
    }

    // Command Disabled.
    public static String discordCommandDisabled() {
        return StreamLine.config.mess.getString("command-disabled.discord");
    }

    public static String bungeeCommandDisabled() {
        return StreamLine.config.mess.getString("command-disabled.bungee");
    }

    // Module Disabled.
    public static String discordModuleDisabled() {
        return StreamLine.config.mess.getString("module-disabled.discord");
    }

    public static String bungeeModuleDisabled() {
        return StreamLine.config.mess.getString("module-disabled.bungee");
    }

    // Not command / improper usage.
    public static String discordNotACommand() {
        return StreamLine.config.mess.getString("not-a-command.discord");
    }

    public static String bungeeImproperUsage() {
        return StreamLine.config.mess.getString("improper-usage.bungee");
    }

    // Command needs args.
    public static String discordNeedsMore() {
        return StreamLine.config.mess.getString("command-needs-args.more.discord");
    }

    public static String bungeeNeedsMore() {
        return StreamLine.config.mess.getString("command-needs-args.more.bungee");
    }

    public static String discordNeedsLess() {
        return StreamLine.config.mess.getString("command-needs-args.less.discord");
    }

    public static String bungeeNeedsLess() {
        return StreamLine.config.mess.getString("command-needs-args.less.bungee");
    }

    // Players.
    public static String offlineB() {
        return StreamLine.config.mess.getString("players.bungee.offline");
    }

    public static String onlineB() {
        return StreamLine.config.mess.getString("players.bungee.online");
    }

    public static String nullB() {
        return StreamLine.config.mess.getString("players.bungee.null");
    }

    public static String offlineD() {
        return StreamLine.config.mess.getString("players.discord.offline");
    }

    public static String onlineD() {
        return StreamLine.config.mess.getString("players.discord.online");
    }

    public static String nullD() {
        return StreamLine.config.mess.getString("players.discord.null");
    }

    // Redirect.
    public static String vbBlocked() {
        return StreamLine.config.mess.getString("redirect.by-version.blocked");
    }

    // Kicks.
    public static String kicksStopping() {
        return StreamLine.config.mess.getString("kicks.stopping");
    }

    // ... Punishments.
    // Mutes.
    public static String punMutedTemp() {
        return StreamLine.config.mess.getString("punishments.muted.temp");
    }

    public static String punMutedPerm() {
        return StreamLine.config.mess.getString("punishments.muted.perm");
    }

    // Bans.
    public static String punBannedTemp() {
        return StreamLine.config.mess.getString("punishments.banned.temp");
    }

    public static String punBannedPerm() {
        return StreamLine.config.mess.getString("punishments.banned.perm");
    }

    // IPBans.
    public static String punIPBannedTemp() {
        return StreamLine.config.mess.getString("punishments.ip-banned.temp");
    }

    public static String punIPBannedPerm() {
        return StreamLine.config.mess.getString("punishments.ip-banned.perm");
    }

    // Reports.
    public static String reportEmbedTitle() {
        return StreamLine.config.mess.getString("report-message.embed-title");
    }

    public static String dToDReportMessage() {
        return StreamLine.config.mess.getString("report-message.from-discord.discord");
    }

    public static String dToBReportMessage() {
        return StreamLine.config.mess.getString("report-message.from-discord.bungee");
    }

    public static String dConfirmReportMessage() {
        return StreamLine.config.mess.getString("report-message.from-discord.confirmation");
    }

    public static String bToDReportMessage() {
        return StreamLine.config.mess.getString("report-message.from-bungee.discord");
    }

    public static String bToBReportMessage() {
        return StreamLine.config.mess.getString("report-message.from-bungee.bungee");
    }

    public static String bConfirmReportMessage() {
        return StreamLine.config.mess.getString("report-message.from-bungee.confirmation");
    }

    // Start.
    public static String startTitle() {
        return StreamLine.config.mess.getString("start.embed-title");
    }

    public static String startMessage() {
        return StreamLine.config.mess.getString("start.message");
    }

    // Stop.
    public static String shutdownTitle() {
        return StreamLine.config.mess.getString("shutdown.embed-title");
    }

    public static String shutdownMessage() {
        return StreamLine.config.mess.getString("shutdown.message");
    }

    // StaffChat.
    public static String staffChatEmbedTitle() {
        return StreamLine.config.mess.getString("staffchat.message.embed-title");
    }

    public static String discordStaffChatMessage() {
        return StreamLine.config.mess.getString("staffchat.message.discord");
    }

    public static String bungeeStaffChatMessage() {
        return StreamLine.config.mess.getString("staffchat.message.bungee");
    }

    public static String discordStaffChatFrom() {
        return StreamLine.config.mess.getString("staffchat.message.from.discord");
    }

    public static String bungeeStaffChatFrom() {
        return StreamLine.config.mess.getString("staffchat.message.from.bungee");
    }

    public static String staffChatJustPrefix() {
        return StreamLine.config.mess.getString("staffchat.just-prefix");
    }

    public static String staffChatToggle() {
        return StreamLine.config.mess.getString("staffchat.message.toggle");
    }

    public static String staffChatOn() {
        return StreamLine.config.mess.getString("staffchat.toggle.true");
    }

    public static String staffChatOff() {
        return StreamLine.config.mess.getString("staffchat.toggle.false");
    }

    // Online.
    public static String onlineMessageNoPlayers() {
        return StreamLine.config.mess.getString("online.message.no-players");
    }

    public static String onlineMessageNoGroups() {
        return StreamLine.config.mess.getString("online.message.no-groups");
    }

    public static String onlineMessageEmbedTitle() {
        return StreamLine.config.mess.getString("online.message.embed-title");
    }

    public static String onlineMessageDiscord() {
        return StreamLine.config.mess.getString("online.message.discord");
    }

    public static String onlineMessageBMain() {
        return StreamLine.config.mess.getString("online.message.bungee.main");
    }

    public static String onlineMessageBServers() {
        return StreamLine.config.mess.getString("online.message.bungee.servers");
    }

    public static String onlineMessageBPlayersMain() {
        return StreamLine.config.mess.getString("online.message.bungee.players.main");
    }

    public static String onlineMessageBPlayersBulkNotLast() {
        return StreamLine.config.mess.getString("online.message.bungee.players.playerbulk.if-not-last");
    }

    public static String onlineMessageBPlayersBulkLast() {
        return StreamLine.config.mess.getString("online.message.bungee.players.playerbulk.if-last");
    }

    // ... Join Leaves.
    // Discord.
    public static String discordOnline() {
        return StreamLine.config.mess.getString("join-leave.discord.online.text");
    }

    public static String discordOnlineEmbed() {
        return StreamLine.config.mess.getString("join-leave.discord.online.embed");
    }

    public static String discordOffline() {
        return StreamLine.config.mess.getString("join-leave.discord.offline.text");
    }

    public static String discordOfflineEmbed() {
        return StreamLine.config.mess.getString("join-leave.discord.offline.embed");
    }

    // Bungee.
    public static String bungeeOnline() {
        return StreamLine.config.mess.getString("join-leave.bungee.online");
    }

    public static String bungeeOffline() {
        return StreamLine.config.mess.getString("join-leave.bungee.offline");
    }

    // ... StaffOnline.
    // Discord.
    public static String sOnlineMessageEmbedTitle() {
        return StreamLine.config.mess.getString("staffonline.message.embed-title");
    }

    public static String sOnlineDiscordMain() {
        return StreamLine.config.mess.getString("staffonline.message.discord.main");
    }

    public static String sOnlineDiscordBulkNotLast() {
        return StreamLine.config.mess.getString("staffonline.message.discord.staffbulk.if-not-last");
    }

    public static String sOnlineDiscordBulkLast() {
        return StreamLine.config.mess.getString("staffonline.message.discord.staffbulk.if-last");
    }

    // Bungee.
    public static String sOnlineBungeeMain() {
        return StreamLine.config.mess.getString("staffonline.message.bungee.main");
    }

    public static String sOnlineBungeeBulkNotLast() {
        return StreamLine.config.mess.getString("staffonline.message.bungee.staffbulk.if-not-last");
    }

    public static String sOnlineBungeeBulkLast() {
        return StreamLine.config.mess.getString("staffonline.message.bungee.staffbulk.if-last");
    }

    // Stream.
    public static String streamNeedLink() {
        return StreamLine.config.mess.getString("stream.need-link");
    }

    public static String streamNotLink() {
        return StreamLine.config.mess.getString("stream.not-link");
    }

    public static String streamMessage() {
        return StreamLine.config.mess.getString("stream.message");
    }

    public static String streamHoverPrefix() {
        return StreamLine.config.mess.getString("stream.hover-prefix");
    }

    // Party.
    public static String partyConnect() {
        return StreamLine.config.mess.getString("party.connect");
    }

    public static String partyDisconnect() {
        return StreamLine.config.mess.getString("party.disconnect");
    }

    // Guild.
    public static String guildConnect() {
        return StreamLine.config.mess.getString("guild.connect");
    }

    public static String guildDisconnect() {
        return StreamLine.config.mess.getString("guild.disconnect");
    }

    // Parties.
    public static String partiesNone() {
        return StreamLine.config.mess.getString("parties.no-parties");
    }

    public static String partiesMessage() {
        return StreamLine.config.mess.getString("parties.parties");
    }

    public static String partiesModsNLast() {
        return StreamLine.config.mess.getString("parties.mods.not-last");
    }

    public static String partiesModsLast() {
        return StreamLine.config.mess.getString("parties.mods.last");
    }

    public static String partiesMemsNLast() {
        return StreamLine.config.mess.getString("parties.members.not-last");
    }

    public static String partiesMemsLast() {
        return StreamLine.config.mess.getString("parties.members.last");
    }

    public static String partiesTMemsNLast() {
        return StreamLine.config.mess.getString("parties.totalmembers.not-last");
    }

    public static String partiesTMemsLast() {
        return StreamLine.config.mess.getString("parties.totalmembers.last");
    }

    public static String partiesInvsNLast() {
        return StreamLine.config.mess.getString("parties.invites.not-last");
    }

    public static String partiesInvsLast() {
        return StreamLine.config.mess.getString("parties.invites.last");
    }

    public static String partiesIsPublicTrue() {
        return StreamLine.config.mess.getString("parties.ispublic.true");
    }

    public static String partiesIsPublicFalse() {
        return StreamLine.config.mess.getString("parties.ispublic.false");
    }

    public static String partiesIsMutedTrue() {
        return StreamLine.config.mess.getString("parties.ismuted.true");
    }

    public static String partiesIsMutedFalse() {
        return StreamLine.config.mess.getString("parties.ismuted.false");
    }

    // Guilds.
    public static String guildsNone() {
        return StreamLine.config.mess.getString("guilds.no-guilds");
    }

    public static String guildsMessage() {
        return StreamLine.config.mess.getString("guilds.guilds");
    }

    public static String guildsModsNLast() {
        return StreamLine.config.mess.getString("guilds.mods.not-last");
    }

    public static String guildsModsLast() {
        return StreamLine.config.mess.getString("guilds.mods.last");
    }

    public static String guildsMemsNLast() {
        return StreamLine.config.mess.getString("guilds.members.not-last");
    }

    public static String guildsMemsLast() {
        return StreamLine.config.mess.getString("guilds.members.last");
    }

    public static String guildsTMemsNLast() {
        return StreamLine.config.mess.getString("guilds.totalmembers.not-last");
    }

    public static String guildsTMemsLast() {
        return StreamLine.config.mess.getString("guilds.totalmembers.last");
    }

    public static String guildsInvsNLast() {
        return StreamLine.config.mess.getString("guilds.invites.not-last");
    }

    public static String guildsInvsLast() {
        return StreamLine.config.mess.getString("guilds.invites.last");
    }

    public static String guildsIsPublicTrue() {
        return StreamLine.config.mess.getString("guilds.ispublic.true");
    }

    public static String guildsIsPublicFalse() {
        return StreamLine.config.mess.getString("guilds.ispublic.false");
    }

    public static String guildsIsMutedTrue() {
        return StreamLine.config.mess.getString("guilds.ismuted.true");
    }

    public static String guildsIsMutedFalse() {
        return StreamLine.config.mess.getString("guilds.ismuted.false");
    }

    // Sudo.
    public static String sudoWorked() {
        return StreamLine.config.mess.getString("sudo.worked");
    }

    public static String sudoNoWork() {
        return StreamLine.config.mess.getString("sudo.no-work");
    }

    public static String sudoNoSudo() {
        return StreamLine.config.mess.getString("sudo.no-sudo");
    }

    // SSPY.
    public static String sspyToggle() {
        return StreamLine.config.mess.getString("sspy.message");
    }

    public static String sspyOn() {
        return StreamLine.config.mess.getString("sspy.toggle.true");
    }

    public static String sspyOff() {
        return StreamLine.config.mess.getString("sspy.toggle.false");
    }

    // GSPY.
    public static String gspyToggle() {
        return StreamLine.config.mess.getString("gspy.message");
    }

    public static String gspyOn() {
        return StreamLine.config.mess.getString("gspy.toggle.true");
    }

    public static String gspyOff() {
        return StreamLine.config.mess.getString("gspy.toggle.false");
    }

    // PSPY.
    public static String pspyToggle() {
        return StreamLine.config.mess.getString("pspy.message");
    }

    public static String pspyOn() {
        return StreamLine.config.mess.getString("pspy.toggle.true");
    }

    public static String pspyOff() {
        return StreamLine.config.mess.getString("pspy.toggle.false");
    }

    // PSPY.
    public static String scViewToggle() {
        return StreamLine.config.mess.getString("sc-view.message");
    }

    public static String scViewOn() {
        return StreamLine.config.mess.getString("sc-view.toggle.true");
    }

    public static String scViewOff() {
        return StreamLine.config.mess.getString("sc-view.toggle.false");
    }

    // SSPYVS.
    public static String sspyvsToggle() {
        return StreamLine.config.mess.getString("sspyvs.message");
    }

    public static String sspyvsOn() {
        return StreamLine.config.mess.getString("sspyvs.toggle.true");
    }

    public static String sspyvsOff() {
        return StreamLine.config.mess.getString("sspyvs.toggle.false");
    }

    // PSPYVS.
    public static String pspyvsToggle() {
        return StreamLine.config.mess.getString("pspyvs.message");
    }

    public static String pspyvsOn() {
        return StreamLine.config.mess.getString("pspyvs.toggle.true");
    }

    public static String pspyvsOff() {
        return StreamLine.config.mess.getString("pspyvs.toggle.false");
    }

    // GSPYVS.
    public static String gspyvsToggle() {
        return StreamLine.config.mess.getString("gspyvs.message");
    }

    public static String gspyvsOn() {
        return StreamLine.config.mess.getString("gspyvs.toggle.true");
    }

    public static String gspyvsOff() {
        return StreamLine.config.mess.getString("gspyvs.toggle.false");
    }

    // SCVS.
    public static String scvsToggle() {
        return StreamLine.config.mess.getString("scvs.message");
    }

    public static String scvsOn() {
        return StreamLine.config.mess.getString("scvs.toggle.true");
    }

    public static String scvsOff() {
        return StreamLine.config.mess.getString("scvs.toggle.false");
    }

    // EVReload.
    public static String evReload() {
        return StreamLine.config.mess.getString("evreload.message");
    }

    // Points.
    public static String pointsViewS() {
        return StreamLine.config.mess.getString("points.view.self");
    }

    public static String pointsViewO() {
        return StreamLine.config.mess.getString("points.view.other");
    }

    public static String pointsAddS() {
        return StreamLine.config.mess.getString("points.add.self");
    }

    public static String pointsAddO() {
        return StreamLine.config.mess.getString("points.add.other");
    }

    public static String pointsRemoveS() {
        return StreamLine.config.mess.getString("points.remove.self");
    }

    public static String pointsRemoveO() {
        return StreamLine.config.mess.getString("points.remove.other");
    }

    public static String pointsSetS() {
        return StreamLine.config.mess.getString("points.set.self");
    }

    public static String pointsSetO() {
        return StreamLine.config.mess.getString("points.set.other");
    }

    // Ignore.
    public static String ignoreAddSelf() {
        return StreamLine.config.mess.getString("ignore.add.self");
    }

    public static String ignoreAddIgnored() {
        return StreamLine.config.mess.getString("ignore.add.ignored");
    }

    public static String ignoreAddAlready() {
        return StreamLine.config.mess.getString("ignore.add.already");
    }

    public static String ignoreAddNSelf() {
        return StreamLine.config.mess.getString("ignore.add.not-self");
    }

    public static String ignoreRemSelf() {
        return StreamLine.config.mess.getString("ignore.remove.self");
    }

    public static String ignoreRemIgnored() {
        return StreamLine.config.mess.getString("ignore.remove.ignored");
    }

    public static String ignoreRemAlready() {
        return StreamLine.config.mess.getString("ignore.remove.already");
    }

    public static String ignoreRemNSelf() {
        return StreamLine.config.mess.getString("ignore.remove.not-self");
    }

    public static String ignoreListMain() {
        return StreamLine.config.mess.getString("ignore.list.main");
    }

    public static String ignoreListNLast() {
        return StreamLine.config.mess.getString("ignore.list.ignores.not-last");
    }

    public static String ignoreListLast() {
        return StreamLine.config.mess.getString("ignore.list.ignores.last");
    }

    // Message.
    public static String messageSender() {
        return StreamLine.config.mess.getString("message.sender");
    }

    public static String messageTo() {
        return StreamLine.config.mess.getString("message.to");
    }

    public static String messageIgnored() {
        return StreamLine.config.mess.getString("message.ignored");
    }

    public static String messageSSPY() {
        return StreamLine.config.mess.getString("message.sspy");
    }

    // Reply.
    public static String replySender() {
        return StreamLine.config.mess.getString("message.sender");
    }

    public static String replyTo() {
        return StreamLine.config.mess.getString("message.to");
    }

    public static String replyIgnored() {
        return StreamLine.config.mess.getString("message.ignored");
    }

    public static String replySSPY() {
        return StreamLine.config.mess.getString("message.sspy");
    }

    // Mute.
    public static String muteEmbed() {
        return StreamLine.config.mess.getString("mute.discord-embed-title");
    }

    public static String muteCannot() {
        return StreamLine.config.mess.getString("mute.cannot");
    }

    public static String muteMTempSender() {
        return StreamLine.config.mess.getString("mute.mute.temp.sender");
    }

    public static String muteMTempMuted() {
        return StreamLine.config.mess.getString("mute.mute.temp.muted");
    }

    public static String muteMTempAlready() {
        return StreamLine.config.mess.getString("mute.mute.temp.already");
    }

    public static String muteMTempStaff() {
        return StreamLine.config.mess.getString("mute.mute.temp.staff");
    }

    public static String muteMTempDiscord() {
        return StreamLine.config.mess.getString("mute.mute.temp.discord");
    }

    public static String muteMPermSender() {
        return StreamLine.config.mess.getString("mute.mute.perm.sender");
    }

    public static String muteMPermMuted() {
        return StreamLine.config.mess.getString("mute.mute.perm.muted");
    }

    public static String muteMPermAlready() {
        return StreamLine.config.mess.getString("mute.mute.perm.already");
    }

    public static String muteMPermStaff() {
        return StreamLine.config.mess.getString("mute.mute.perm.staff");
    }

    public static String muteMPermDiscord() {
        return StreamLine.config.mess.getString("mute.mute.perm.discord");
    }

    public static String muteUnSender() {
        return StreamLine.config.mess.getString("mute.unmute.sender");
    }

    public static String muteUnMuted() {
        return StreamLine.config.mess.getString("mute.unmute.muted");
    }

    public static String muteUnAlready() {
        return StreamLine.config.mess.getString("mute.unmute.already");
    }

    public static String muteUnStaff() {
        return StreamLine.config.mess.getString("mute.unmute.staff");
    }

    public static String muteUnDiscord() {
        return StreamLine.config.mess.getString("mute.unmute.discord");
    }

    public static String muteCheckMain() {
        return StreamLine.config.mess.getString("mute.check.main");
    }

    public static String muteCheckMuted() {
        return StreamLine.config.mess.getString("mute.check.muted");
    }

    public static String muteCheckUnMuted() {
        return StreamLine.config.mess.getString("mute.check.unmuted");
    }

    public static String muteCheckNoDate() {
        return StreamLine.config.mess.getString("mute.check.no-date");
    }

    // Kick.
    public static String kickEmbed() {
        return StreamLine.config.mess.getString("kick.discord-embed-title");
    }

    public static String kickCannot() {
        return StreamLine.config.mess.getString("kick.cannot");
    }

    public static String kickSender() {
        return StreamLine.config.mess.getString("kick.sender");
    }

    public static String kickKicked() {
        return StreamLine.config.mess.getString("kick.kicked");
    }

    public static String kickStaff() {
        return StreamLine.config.mess.getString("kick.staff");
    }

    public static String kickDiscord() {
        return StreamLine.config.mess.getString("kick.discord");
    }

    // Ban.
    public static String banEmbed() {
        return StreamLine.config.mess.getString("ban.discord-embed-title");
    }

    public static String banCannot() {
        return StreamLine.config.mess.getString("ban.cannot");
    }

    public static String banBTempSender() {
        return StreamLine.config.mess.getString("ban.ban.temp.sender");
    }

    public static String banBTempAlready() {
        return StreamLine.config.mess.getString("ban.ban.temp.already");
    }

    public static String banBTempStaff() {
        return StreamLine.config.mess.getString("ban.ban.temp.staff");
    }

    public static String banBTempDiscord() {
        return StreamLine.config.mess.getString("ban.ban.temp.discord");
    }

    public static String banBPermSender() {
        return StreamLine.config.mess.getString("ban.ban.perm.sender");
    }

    public static String banBPermAlready() {
        return StreamLine.config.mess.getString("ban.ban.perm.already");
    }

    public static String banBPermStaff() {
        return StreamLine.config.mess.getString("ban.ban.perm.staff");
    }

    public static String banBPermDiscord() {
        return StreamLine.config.mess.getString("ban.ban.perm.discord");
    }

    public static String banUnSender() {
        return StreamLine.config.mess.getString("ban.unban.sender");
    }

    public static String banUnAlready() {
        return StreamLine.config.mess.getString("ban.unban.already");
    }

    public static String banUnStaff() {
        return StreamLine.config.mess.getString("ban.unban.staff");
    }

    public static String banUnDiscord() {
        return StreamLine.config.mess.getString("ban.unban.discord");
    }

    public static String banCheckMain() {
        return StreamLine.config.mess.getString("ban.check.main");
    }

    public static String banCheckBanned() {
        return StreamLine.config.mess.getString("ban.check.banned");
    }

    public static String banCheckUnBanned() {
        return StreamLine.config.mess.getString("ban.check.unbanned");
    }

    public static String banCheckNoDate() {
        return StreamLine.config.mess.getString("ban.check.no-date");
    }

    // IPBan.
    public static String ipBanEmbed() {
        return StreamLine.config.mess.getString("ipban.discord-embed-title");
    }

    public static String ipBanCannot() {
        return StreamLine.config.mess.getString("ipban.cannot");
    }

    public static String ipBanBTempSender() {
        return StreamLine.config.mess.getString("ipban.ban.temp.sender");
    }

    public static String ipBanBTempAlready() {
        return StreamLine.config.mess.getString("ipban.ban.temp.already");
    }

    public static String ipBanBTempStaff() {
        return StreamLine.config.mess.getString("ipban.ban.temp.staff");
    }

    public static String ipBanBTempDiscord() {
        return StreamLine.config.mess.getString("ipban.ban.temp.discord");
    }

    public static String ipBanBPermSender() {
        return StreamLine.config.mess.getString("ipban.ban.perm.sender");
    }

    public static String ipBanBPermAlready() {
        return StreamLine.config.mess.getString("ipban.ban.perm.already");
    }

    public static String ipBanBPermStaff() {
        return StreamLine.config.mess.getString("ipban.ban.perm.staff");
    }

    public static String ipBanBPermDiscord() {
        return StreamLine.config.mess.getString("ipban.ban.perm.discord");
    }

    public static String ipBanUnSender() {
        return StreamLine.config.mess.getString("ipban.unban.sender");
    }

    public static String ipBanUnAlready() {
        return StreamLine.config.mess.getString("ipban.unban.already");
    }

    public static String ipBanUnStaff() {
        return StreamLine.config.mess.getString("ipban.unban.staff");
    }

    public static String ipBanUnDiscord() {
        return StreamLine.config.mess.getString("ipban.unban.discord");
    }

    public static String ipBanCheckMain() {
        return StreamLine.config.mess.getString("ipban.check.main");
    }

    public static String ipBanCheckBanned() {
        return StreamLine.config.mess.getString("ipban.check.banned");
    }

    public static String ipBanCheckUnBanned() {
        return StreamLine.config.mess.getString("ipban.check.unbanned");
    }

    public static String ipBanCheckNoDate() {
        return StreamLine.config.mess.getString("ipban.check.no-date");
    }

    // Ignore.
    public static String friendReqSelf() {
        return StreamLine.config.mess.getString("friend.request.self");
    }

    public static String friendReqOther() {
        return StreamLine.config.mess.getString("friend.request.other");
    }

    public static String friendReqAlready() {
        return StreamLine.config.mess.getString("friend.request.already");
    }

    public static String friendReqNSelf() {
        return StreamLine.config.mess.getString("friend.request.not-self");
    }

    public static String friendReqIgnored() {
        return StreamLine.config.mess.getString("friend.request.ignored");
    }

    public static String friendAcceptSelf() {
        return StreamLine.config.mess.getString("friend.accept.self");
    }

    public static String friendAcceptOther() {
        return StreamLine.config.mess.getString("friend.accept.other");
    }

    public static String friendAcceptNone() {
        return StreamLine.config.mess.getString("friend.accept.none");
    }

    public static String friendDenySelf() {
        return StreamLine.config.mess.getString("friend.deny.self");
    }

    public static String friendDenyOther() {
        return StreamLine.config.mess.getString("friend.deny.other");
    }

    public static String friendDenyNone() {
        return StreamLine.config.mess.getString("friend.deny.none");
    }

    public static String friendRemSelf() {
        return StreamLine.config.mess.getString("friend.remove.self");
    }

    public static String friendRemOther() {
        return StreamLine.config.mess.getString("friend.remove.other");
    }

    public static String friendRemAlready() {
        return StreamLine.config.mess.getString("friend.remove.already");
    }

    public static String friendRemNSelf() {
        return StreamLine.config.mess.getString("friend.remove.not-self");
    }

    public static String friendListMain() {
        return StreamLine.config.mess.getString("friend.list.main");
    }

    public static String friendListFNLast() {
        return StreamLine.config.mess.getString("friend.list.friends.not-last");
    }

    public static String friendListFLast() {
        return StreamLine.config.mess.getString("friend.list.friends.last");
    }

    public static String friendListPTNLast() {
        return StreamLine.config.mess.getString("friend.list.pending-to.not-last");
    }

    public static String friendListPTLast() {
        return StreamLine.config.mess.getString("friend.list.pending-to.last");
    }

    public static String friendListPFNLast() {
        return StreamLine.config.mess.getString("friend.list.pending-from.not-last");
    }

    public static String friendListPFLast() {
        return StreamLine.config.mess.getString("friend.list.pending-from.last");
    }

    public static String friendConnect() {
        return StreamLine.config.mess.getString("friend.connect");
    }

    public static String friendDisconnect() {
        return StreamLine.config.mess.getString("friend.disconnect");
    }

    // GetStats.
    public static String getStatsNone() {
        return StreamLine.config.mess.getString("getstats.no-stats");
    }

    public static String getStatsMessage() {
        return StreamLine.config.mess.getString("getstats.message.main");
    }

    public static String getStatsNLast() {
        return StreamLine.config.mess.getString("getstats.message.not-last");
    }

    public static String getStatsLast() {
        return StreamLine.config.mess.getString("getstats.message.last");
    }

    // // Settings.
    // Set.
    public static String settingsSetMOTD() {
        return StreamLine.config.mess.getString("settings.set.motd");
    }

    public static String settingsSetMOTDTime() {
        return StreamLine.config.mess.getString("settings.set.motd-time");
    }

    public static String settingsSetVersion() {
        return StreamLine.config.mess.getString("settings.set.version");
    }

    public static String settingsSetSample() {
        return StreamLine.config.mess.getString("settings.set.sample");
    }

    public static String settingsSetMaxP() {
        return StreamLine.config.mess.getString("settings.set.max-players");
    }

    public static String settingsSetOnlineP() {
        return StreamLine.config.mess.getString("settings.set.online-players");
    }

    public static String settingsSetPCEnabled() {
        return StreamLine.config.mess.getString("settings.set.proxy-chat-enabled");
    }

    public static String settingsSetChatToConsole() {
        return StreamLine.config.mess.getString("settings.set.proxy-chat-to-console");
    }

    public static String settingsSetPCChats() {
        return StreamLine.config.mess.getString("settings.set.proxy-chat-chats");
    }

    public static String settingsSetPCBPerm() {
        return StreamLine.config.mess.getString("settings.set.proxy-chat-base-perm");
    }

    public static String settingsSetTagsEnablePing() {
        return StreamLine.config.mess.getString("settings.set.tags-enable-ping");
    }

    public static String settingsSetTagsTagPrefix() {
        return StreamLine.config.mess.getString("settings.set.tags-tag-prefix");
    }

    public static String settingsSetEmotes() {
        return StreamLine.config.mess.getString("settings.set.emotes");
    }

    public static String settingsSetEmotePermissions() {
        return StreamLine.config.mess.getString("settings.set.emote-permissions");
    }

    // Get.
    public static String settingsGetMOTD() {
        return StreamLine.config.mess.getString("settings.get.motd");
    }

    public static String settingsGetMOTDTime() {
        return StreamLine.config.mess.getString("settings.get.motd-time");
    }

    public static String settingsGetVersion() {
        return StreamLine.config.mess.getString("settings.get.version");
    }

    public static String settingsGetSample() {
        return StreamLine.config.mess.getString("settings.get.sample");
    }

    public static String settingsGetMaxP() {
        return StreamLine.config.mess.getString("settings.get.max-players");
    }

    public static String settingsGetOnlineP() {
        return StreamLine.config.mess.getString("settings.get.online-players");
    }

    public static String settingsGetPCEnabled() {
        return StreamLine.config.mess.getString("settings.get.proxy-chat-enabled");
    }

    public static String settingsGetChatToConsole() {
        return StreamLine.config.mess.getString("settings.get.proxy-chat-to-console");
    }

    public static String settingsGetPCChats() {
        return StreamLine.config.mess.getString("settings.get.proxy-chat-chats");
    }

    public static String settingsGetPCBPerm() {
        return StreamLine.config.mess.getString("settings.get.proxy-chat-base-perm");
    }

    public static String settingsGetTagsEnablePing() {
        return StreamLine.config.mess.getString("settings.get.tags-enable-ping");
    }

    public static String settingsGetTagsTagPrefix() {
        return StreamLine.config.mess.getString("settings.get.tags-tag-prefix");
    }

    public static String settingsGetEmotes() {
        return StreamLine.config.mess.getString("settings.get.emotes");
    }

    public static String settingsGetEmotePermissions() {
        return StreamLine.config.mess.getString("settings.get.emote-permissions");
    }

    // // Info.
    public static String info() {
        return StreamLine.config.mess.getString("info");
    }

    // // Graceful End.
    public static String gracefulEndSender() {
        return StreamLine.config.mess.getString("graceful-end.sender");
    }

    public static String gracefulEndKickMessage() {
        return StreamLine.config.mess.getString("graceful-end.kick-message");
    }

    // Delete Stat.
    public static String deleteStatMessage() {
        return StreamLine.config.mess.getString("delete-stat.message");
    }

    // B-Teleport.
    public static String bteleport() {
        return StreamLine.config.mess.getString("bteleport()");
    }

    // Language
    public static String languageMessage() {
        return StreamLine.config.mess.getString("language.message");
    }

    public static String languageInvalidLocale() {
        return StreamLine.config.mess.getString("language.invalid-locale");
    }
}