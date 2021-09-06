package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;

import java.util.List;

public class ConfigUtils {
    // ConfigHandler //
//    public static String s = StreamLine.config.getConfString("");
    // Important.
//    public static String version = StreamLine.config.getConfString("version");
    // Debug.
    public static boolean debug = StreamLine.config.getConfBoolean("debug");
    // ... Basics.
    // Links.
    public static String linkPre = StreamLine.config.getConfString("link-prefix");
//    public static String linkSuff = StreamLine.config.getConfString("link.suffix");// ... ... Modules.
    public static String staffPerm = StreamLine.config.getConfString("modules.staff-permission");
    public static boolean doNotAutoUpdateConfigs = StreamLine.config.getConfBoolean("modules.automatically-update-configs");
    // ... Discord.
    // Basics.
    public static boolean moduleDMainConsole = StreamLine.config.getConfBoolean("modules.discord.main-console");
    public static boolean moduleDEnabled = StreamLine.config.getConfBoolean("modules.discord.enabled");
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
    public static int cachedPSave = StreamLine.config.getConfInteger("modules.bungee.stats.cache-save");
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
    // // Helper.
    // Teleport.
    public static int helperTeleportDelay = StreamLine.config.getConfInteger("modules.bungee.helper.teleport.delay");
}
