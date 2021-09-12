package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;

public class MessageConfUtils {
    // Messages:
    public static String prefix() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("message-prefix");
    }

    public static String noPerm() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("no-permission");
    }

    public static String reload() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("reload-message");
    }

    public static String onlyPlayers() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("only-players");
    }

    public static String noPlayer() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("no-player");
    }

    public static String discordErrTitle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("discord-err-title");
    }

    // ... Command Error.
    // Discord.
    public static String discordCommandErrorUnd() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("command-error.discord.undefined");
    }

    // Bungee.
    public static String bungeeCommandErrorUnd() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("command-error.bungee.undefined");
    }

    public static String bungeeCommandErrorInt() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("command-error.bungee.needs-int");
    }

    public static String bungeeCommandErrorSTime() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("command-error.bungee.needs-stringed-time");
    }

    public static String bungeeCommandErrorNoYou() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("command-error.bungee.cant-find-you");
    }

    // Command Disabled.
    public static String discordCommandDisabled() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("command-disabled.discord");
    }

    public static String bungeeCommandDisabled() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("command-disabled.bungee");
    }

    // Module Disabled.
    public static String discordModuleDisabled() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("module-disabled.discord");
    }

    public static String bungeeModuleDisabled() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("module-disabled.bungee");
    }

    // Not command / improper usage.
    public static String discordNotACommand() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("not-a-command.discord");
    }

    public static String bungeeImproperUsage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("improper-usage.bungee");
    }

    // Command needs args.
    public static String discordNeedsMore() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("command-needs-args.more.discord");
    }

    public static String bungeeNeedsMore() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("command-needs-args.more.bungee");
    }

    public static String discordNeedsLess() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("command-needs-args.less.discord");
    }

    public static String bungeeNeedsLess() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("command-needs-args.less.bungee");
    }

    // Players.
    public static String offlineB() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("players.bungee.offline");
    }

    public static String onlineB() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("players.bungee.online");
    }

    public static String nullB() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("players.bungee.null");
    }

    public static String offlineD() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("players.discord.offline");
    }

    public static String onlineD() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("players.discord.online");
    }

    public static String nullD() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("players.discord.null");
    }

    // Redirect.
    public static String vbBlocked() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("redirect.by-version.blocked");
    }

    // Kicks.
    public static String kicksStopping() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("kicks.stopping");
    }

    // ... Punishments.
    // Mutes.
    public static String punMutedTemp() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("punishments.muted.temp");
    }

    public static String punMutedPerm() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("punishments.muted.perm");
    }

    // Bans.
    public static String punBannedTemp() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("punishments.banned.temp");
    }

    public static String punBannedPerm() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("punishments.banned.perm");
    }

    // IPBans.
    public static String punIPBannedTemp() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("punishments.ip-banned.temp");
    }

    public static String punIPBannedPerm() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("punishments.ip-banned.perm");
    }

    // Reports.
    public static String reportEmbedTitle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("report-message.embed-title");
    }

    public static String dToDReportMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("report-message.from-discord.discord");
    }

    public static String dToBReportMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("report-message.from-discord.bungee");
    }

    public static String dConfirmReportMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("report-message.from-discord.confirmation");
    }

    public static String bToDReportMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("report-message.from-bungee.discord");
    }

    public static String bToBReportMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("report-message.from-bungee.bungee");
    }

    public static String bConfirmReportMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("report-message.from-bungee.confirmation");
    }

    // Start.
    public static String startTitle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("start.embed-title");
    }

    public static String startMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("start.message");
    }

    // Stop.
    public static String shutdownTitle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("shutdown.embed-title");
    }

    public static String shutdownMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("shutdown.message");
    }

    // StaffChat.
    public static String staffChatEmbedTitle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffchat.message.embed-title");
    }

    public static String discordStaffChatMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffchat.message.discord");
    }

    public static String bungeeStaffChatMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffchat.message.bungee");
    }

    public static String discordStaffChatFrom() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffchat.message.from.discord");
    }

    public static String bungeeStaffChatFrom() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffchat.message.from.bungee");
    }

    public static String staffChatJustPrefix() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffchat.just-prefix");
    }

    public static String staffChatToggle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffchat.message.toggle");
    }

    public static String staffChatOn() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffchat.toggle.true");
    }

    public static String staffChatOff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffchat.toggle.false");
    }

    // Online.
    public static String onlineMessageNoPlayers() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("online.message.no-players");
    }

    public static String onlineMessageNoGroups() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("online.message.no-groups");
    }

    public static String onlineMessageEmbedTitle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("online.message.embed-title");
    }

    public static String onlineMessageDiscord() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("online.message.discord");
    }

    public static String onlineMessageBMain() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("online.message.bungee.main");
    }

    public static String onlineMessageBServers() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("online.message.bungee.servers");
    }

    public static String onlineMessageBPlayersMain() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("online.message.bungee.players.main");
    }

    public static String onlineMessageBPlayersBulkNotLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("online.message.bungee.players.playerbulk.if-not-last");
    }

    public static String onlineMessageBPlayersBulkLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("online.message.bungee.players.playerbulk.if-last");
    }

    // ... Join Leaves.
    // Discord.
    public static String discordOnline() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("join-leave.discord.online.text");
    }

    public static String discordOnlineEmbed() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("join-leave.discord.online.embed");
    }

    public static String discordOffline() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("join-leave.discord.offline.text");
    }

    public static String discordOfflineEmbed() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("join-leave.discord.offline.embed");
    }

    // Bungee.
    public static String bungeeOnline() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("join-leave.bungee.online");
    }

    public static String bungeeOffline() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("join-leave.bungee.offline");
    }

    // ... StaffOnline.
    // Discord.
    public static String sOnlineMessageEmbedTitle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffonline.message.embed-title");
    }

    public static String sOnlineDiscordMain() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffonline.message.discord.main");
    }

    public static String sOnlineDiscordBulkNotLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffonline.message.discord.staffbulk.if-not-last");
    }

    public static String sOnlineDiscordBulkLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffonline.message.discord.staffbulk.if-last");
    }

    // Bungee.
    public static String sOnlineBungeeMain() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffonline.message.bungee.main");
    }

    public static String sOnlineBungeeBulkNotLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffonline.message.bungee.staffbulk.if-not-last");
    }

    public static String sOnlineBungeeBulkLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("staffonline.message.bungee.staffbulk.if-last");
    }

    // Stream.
    public static String streamNeedLink() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("stream.need-link");
    }

    public static String streamNotLink() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("stream.not-link");
    }

    public static String streamMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("stream.message");
    }

    public static String streamHoverPrefix() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("stream.hover-prefix");
    }

    // Party.
    public static String partyConnect() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("party.connect");
    }

    public static String partyDisconnect() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("party.disconnect");
    }

    // Guild.
    public static String guildConnect() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guild.connect");
    }

    public static String guildDisconnect() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guild.disconnect");
    }

    // Parties.
    public static String partiesNone() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.no-parties");
    }

    public static String partiesMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.parties");
    }

    public static String partiesModsNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.mods.not-last");
    }

    public static String partiesModsLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.mods.last");
    }

    public static String partiesMemsNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.members.not-last");
    }

    public static String partiesMemsLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.members.last");
    }

    public static String partiesTMemsNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.totalmembers.not-last");
    }

    public static String partiesTMemsLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.totalmembers.last");
    }

    public static String partiesInvsNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.invites.not-last");
    }

    public static String partiesInvsLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.invites.last");
    }

    public static String partiesIsPublicTrue() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.ispublic.true");
    }

    public static String partiesIsPublicFalse() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.ispublic.false");
    }

    public static String partiesIsMutedTrue() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.ismuted.true");
    }

    public static String partiesIsMutedFalse() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("parties.ismuted.false");
    }

    // Guilds.
    public static String guildsNone() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.no-guilds");
    }

    public static String guildsMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.guilds");
    }

    public static String guildsModsNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.mods.not-last");
    }

    public static String guildsModsLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.mods.last");
    }

    public static String guildsMemsNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.members.not-last");
    }

    public static String guildsMemsLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.members.last");
    }

    public static String guildsTMemsNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.totalmembers.not-last");
    }

    public static String guildsTMemsLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.totalmembers.last");
    }

    public static String guildsInvsNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.invites.not-last");
    }

    public static String guildsInvsLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.invites.last");
    }

    public static String guildsIsPublicTrue() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.ispublic.true");
    }

    public static String guildsIsPublicFalse() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.ispublic.false");
    }

    public static String guildsIsMutedTrue() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.ismuted.true");
    }

    public static String guildsIsMutedFalse() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("guilds.ismuted.false");
    }

    // Sudo.
    public static String sudoWorked() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sudo.worked");
    }

    public static String sudoNoWork() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sudo.no-work");
    }

    public static String sudoNoSudo() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sudo.no-sudo");
    }

    // SSPY.
    public static String sspyToggle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sspy.message");
    }

    public static String sspyOn() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sspy.toggle.true");
    }

    public static String sspyOff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sspy.toggle.false");
    }

    // GSPY.
    public static String gspyToggle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("gspy.message");
    }

    public static String gspyOn() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("gspy.toggle.true");
    }

    public static String gspyOff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("gspy.toggle.false");
    }

    // PSPY.
    public static String pspyToggle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("pspy.message");
    }

    public static String pspyOn() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("pspy.toggle.true");
    }

    public static String pspyOff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("pspy.toggle.false");
    }

    // PSPY.
    public static String scViewToggle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sc-view.message");
    }

    public static String scViewOn() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sc-view.toggle.true");
    }

    public static String scViewOff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sc-view.toggle.false");
    }

    // SSPYVS.
    public static String sspyvsToggle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sspyvs.message");
    }

    public static String sspyvsOn() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sspyvs.toggle.true");
    }

    public static String sspyvsOff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("sspyvs.toggle.false");
    }

    // PSPYVS.
    public static String pspyvsToggle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("pspyvs.message");
    }

    public static String pspyvsOn() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("pspyvs.toggle.true");
    }

    public static String pspyvsOff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("pspyvs.toggle.false");
    }

    // GSPYVS.
    public static String gspyvsToggle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("gspyvs.message");
    }

    public static String gspyvsOn() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("gspyvs.toggle.true");
    }

    public static String gspyvsOff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("gspyvs.toggle.false");
    }

    // SCVS.
    public static String scvsToggle() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("scvs.message");
    }

    public static String scvsOn() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("scvs.toggle.true");
    }

    public static String scvsOff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("scvs.toggle.false");
    }

    // EVReload.
    public static String evReload() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("evreload.message");
    }

    // Points.
    public static String pointsViewS() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("points.view.self");
    }

    public static String pointsViewO() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("points.view.other");
    }

    public static String pointsAddS() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("points.add.self");
    }

    public static String pointsAddO() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("points.add.other");
    }

    public static String pointsRemoveS() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("points.remove.self");
    }

    public static String pointsRemoveO() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("points.remove.other");
    }

    public static String pointsSetS() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("points.set.self");
    }

    public static String pointsSetO() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("points.set.other");
    }

    // Ignore.
    public static String ignoreAddSelf() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ignore.add.self");
    }

    public static String ignoreAddIgnored() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ignore.add.ignored");
    }

    public static String ignoreAddAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ignore.add.already");
    }

    public static String ignoreAddNSelf() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ignore.add.not-self");
    }

    public static String ignoreRemSelf() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ignore.remove.self");
    }

    public static String ignoreRemIgnored() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ignore.remove.ignored");
    }

    public static String ignoreRemAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ignore.remove.already");
    }

    public static String ignoreRemNSelf() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ignore.remove.not-self");
    }

    public static String ignoreListMain() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ignore.list.main");
    }

    public static String ignoreListNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ignore.list.ignores.not-last");
    }

    public static String ignoreListLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ignore.list.ignores.last");
    }

    // Message.
    public static String messageSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("message.sender");
    }

    public static String messageTo() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("message.to");
    }

    public static String messageIgnored() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("message.ignored");
    }

    public static String messageSSPY() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("message.sspy");
    }

    // Reply.
    public static String replySender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("message.sender");
    }

    public static String replyTo() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("message.to");
    }

    public static String replyIgnored() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("message.ignored");
    }

    public static String replySSPY() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("message.sspy");
    }

    // Mute.
    public static String muteEmbed() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.discord-embed-title");
    }

    public static String muteCannot() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.cannot");
    }

    public static String muteMTempSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.mute.temp.sender");
    }

    public static String muteMTempMuted() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.mute.temp.muted");
    }

    public static String muteMTempAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.mute.temp.already");
    }

    public static String muteMTempStaff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.mute.temp.staff");
    }

    public static String muteMTempDiscord() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.mute.temp.discord");
    }

    public static String muteMPermSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.mute.perm.sender");
    }

    public static String muteMPermMuted() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.mute.perm.muted");
    }

    public static String muteMPermAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.mute.perm.already");
    }

    public static String muteMPermStaff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.mute.perm.staff");
    }

    public static String muteMPermDiscord() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.mute.perm.discord");
    }

    public static String muteUnSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.unmute.sender");
    }

    public static String muteUnMuted() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.unmute.muted");
    }

    public static String muteUnAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.unmute.already");
    }

    public static String muteUnStaff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.unmute.staff");
    }

    public static String muteUnDiscord() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.unmute.discord");
    }

    public static String muteCheckMain() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.check.main");
    }

    public static String muteCheckMuted() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.check.muted");
    }

    public static String muteCheckUnMuted() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.check.unmuted");
    }

    public static String muteCheckNoDate() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("mute.check.no-date");
    }

    // Kick.
    public static String kickEmbed() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("kick.discord-embed-title");
    }

    public static String kickCannot() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("kick.cannot");
    }

    public static String kickSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("kick.sender");
    }

    public static String kickKicked() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("kick.kicked");
    }

    public static String kickStaff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("kick.staff");
    }

    public static String kickDiscord() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("kick.discord");
    }

    // Ban.
    public static String banEmbed() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.discord-embed-title");
    }

    public static String banCannot() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.cannot");
    }

    public static String banBTempSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.ban.temp.sender");
    }

    public static String banBTempAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.ban.temp.already");
    }

    public static String banBTempStaff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.ban.temp.staff");
    }

    public static String banBTempDiscord() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.ban.temp.discord");
    }

    public static String banBPermSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.ban.perm.sender");
    }

    public static String banBPermAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.ban.perm.already");
    }

    public static String banBPermStaff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.ban.perm.staff");
    }

    public static String banBPermDiscord() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.ban.perm.discord");
    }

    public static String banUnSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.unban.sender");
    }

    public static String banUnAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.unban.already");
    }

    public static String banUnStaff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.unban.staff");
    }

    public static String banUnDiscord() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.unban.discord");
    }

    public static String banCheckMain() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.check.main");
    }

    public static String banCheckBanned() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.check.banned");
    }

    public static String banCheckUnBanned() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.check.unbanned");
    }

    public static String banCheckNoDate() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ban.check.no-date");
    }

    // IPBan.
    public static String ipBanEmbed() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.discord-embed-title");
    }

    public static String ipBanCannot() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.cannot");
    }

    public static String ipBanBTempSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.ban.temp.sender");
    }

    public static String ipBanBTempAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.ban.temp.already");
    }

    public static String ipBanBTempStaff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.ban.temp.staff");
    }

    public static String ipBanBTempDiscord() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.ban.temp.discord");
    }

    public static String ipBanBPermSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.ban.perm.sender");
    }

    public static String ipBanBPermAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.ban.perm.already");
    }

    public static String ipBanBPermStaff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.ban.perm.staff");
    }

    public static String ipBanBPermDiscord() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.ban.perm.discord");
    }

    public static String ipBanUnSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.unban.sender");
    }

    public static String ipBanUnAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.unban.already");
    }

    public static String ipBanUnStaff() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.unban.staff");
    }

    public static String ipBanUnDiscord() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.unban.discord");
    }

    public static String ipBanCheckMain() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.check.main");
    }

    public static String ipBanCheckBanned() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.check.banned");
    }

    public static String ipBanCheckUnBanned() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.check.unbanned");
    }

    public static String ipBanCheckNoDate() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("ipban.check.no-date");
    }

    // Ignore.
    public static String friendReqSelf() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.request.self");
    }

    public static String friendReqOther() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.request.other");
    }

    public static String friendReqAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.request.already");
    }

    public static String friendReqNSelf() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.request.not-self");
    }

    public static String friendReqIgnored() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.request.ignored");
    }

    public static String friendAcceptSelf() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.accept.self");
    }

    public static String friendAcceptOther() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.accept.other");
    }

    public static String friendAcceptNone() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.accept.none");
    }

    public static String friendDenySelf() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.deny.self");
    }

    public static String friendDenyOther() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.deny.other");
    }

    public static String friendDenyNone() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.deny.none");
    }

    public static String friendRemSelf() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.remove.self");
    }

    public static String friendRemOther() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.remove.other");
    }

    public static String friendRemAlready() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.remove.already");
    }

    public static String friendRemNSelf() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.remove.not-self");
    }

    public static String friendListMain() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.list.main");
    }

    public static String friendListFNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.list.friends.not-last");
    }

    public static String friendListFLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.list.friends.last");
    }

    public static String friendListPTNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.list.pending-to.not-last");
    }

    public static String friendListPTLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.list.pending-to.last");
    }

    public static String friendListPFNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.list.pending-from.not-last");
    }

    public static String friendListPFLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.list.pending-from.last");
    }

    public static String friendConnect() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.connect");
    }

    public static String friendDisconnect() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("friend.disconnect");
    }

    // GetStats.
    public static String getStatsNone() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("getstats.no-stats");
    }

    public static String getStatsMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("getstats.message.main");
    }

    public static String getStatsNLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("getstats.message.not-last");
    }

    public static String getStatsLast() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("getstats.message.last");
    }

    // // Settings.
    // Set.
    public static String settingsSetMOTD() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.motd");
    }

    public static String settingsSetMOTDTime() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.motd-time");
    }

    public static String settingsSetVersion() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.version");
    }

    public static String settingsSetSample() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.sample");
    }

    public static String settingsSetMaxP() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.max-players");
    }

    public static String settingsSetOnlineP() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.online-players");
    }

    public static String settingsSetPCEnabled() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.proxy-chat-enabled");
    }

    public static String settingsSetChatToConsole() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.proxy-chat-to-console");
    }

    public static String settingsSetPCChats() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.proxy-chat-chats");
    }

    public static String settingsSetPCBPerm() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.proxy-chat-base-perm");
    }

    public static String settingsSetTagsEnablePing() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.tags-enable-ping");
    }

    public static String settingsSetTagsTagPrefix() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.tags-tag-prefix");
    }

    public static String settingsSetEmotes() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.emotes");
    }

    public static String settingsSetEmotePermissions() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.set.emote-permissions");
    }

    // Get.
    public static String settingsGetMOTD() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.motd");
    }

    public static String settingsGetMOTDTime() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.motd-time");
    }

    public static String settingsGetVersion() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.version");
    }

    public static String settingsGetSample() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.sample");
    }

    public static String settingsGetMaxP() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.max-players");
    }

    public static String settingsGetOnlineP() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.online-players");
    }

    public static String settingsGetPCEnabled() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.proxy-chat-enabled");
    }

    public static String settingsGetChatToConsole() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.proxy-chat-to-console");
    }

    public static String settingsGetPCChats() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.proxy-chat-chats");
    }

    public static String settingsGetPCBPerm() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.proxy-chat-base-perm");
    }

    public static String settingsGetTagsEnablePing() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.tags-enable-ping");
    }

    public static String settingsGetTagsTagPrefix() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.tags-tag-prefix");
    }

    public static String settingsGetEmotes() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.emotes");
    }

    public static String settingsGetEmotePermissions() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("settings.get.emote-permissions");
    }

    // // Info.
    public static String info() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("info");
    }

    // // Graceful End.
    public static String gracefulEndSender() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("graceful-end.sender");
    }

    public static String gracefulEndKickMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("graceful-end.kick-message");
    }

    // Delete Stat.
    public static String deleteStatMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("delete-stat.message");
    }

    // B-Teleport.
    public static String bteleport() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("bteleport");
    }

    // Language
    public static String languageMessage() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("language.message");
    }

    public static String languageInvalidLocale() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("language.invalid-locale");
    }

    public static String chatChannelsLocalLabel() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.local.label");
    }

    public static String chatChannelsLocalSwitch() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.local.switch");
    }

    public static String chatChannelsGlobalLabel() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.global.label");
    }

    public static String chatChannelsGlobalSwitch() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.global.switch");
    }

    public static String chatChannelsGuildLabel() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.guild.label");
    }

    public static String chatChannelsGuildSwitch() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.guild.switch");
    }

    public static String chatChannelsPartyLabel() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.party.label");
    }

    public static String chatChannelsPartySwitch() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.party.switch");
    }

    public static String chatChannelsGOfficerLabel() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.g-officer.label");
    }

    public static String chatChannelsGOfficerSwitch() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.g-officer.switch");
    }

    public static String chatChannelsPOfficerLabel() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.p-officer.label");
    }

    public static String chatChannelsPOfficerSwitch() {
        StreamLine.config.reloadLocales();
        return StreamLine.config.mess.getString("chat-channels.p-officer.switch");
    }
}