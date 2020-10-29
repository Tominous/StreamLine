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
    // ... Basics.
    // Links.
    public static final String linkPre = config.getString("link.prefix");
    public static final String linkSuff = config.getString("link.suffix");
    // Bot Stuff.
    public static final String botPrefix = config.getString("bot.prefix");
    public static final String botToken = config.getString("bot.token");
    public static final String botStatusMessage = config.getString("bot.server-ip");
    // ... Discord.
    // Text Channels.
    public static final String textChannelReports = config.getString("discord.text-channels.reports");
    public static final String textChannelStaffChat = config.getString("discord.text-channels.staffchat");
    public static final String textChannelOfflineOnline = config.getString("discord.text-channels.offline-online");
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
    public static final List<String> comBPingAliases = config.getStringList("commands.bungee.ping.aliases");
    public static final String comBPingPerm = config.getString("commands.bungee.ping.permission");
    // Plugins.
    public static final boolean comBPlugins = config.getBoolean("commands.bungee.plugins.enabled");
    public static final List<String> comBPluginsAliases = config.getStringList("commands.bungee.plugins.aliases");
    public static final String comBPluginsPerm = config.getString("commands.bungee.plugins.permission");
    // Stream.
    public static final boolean comBStream = config.getBoolean("commands.bungee.stream.enabled");
    public static final List<String> comBStreamAliases = config.getStringList("commands.bungee.stream.aliases");
    public static final String comBStreamPerm = config.getString("commands.bungee.stream.permission");
    // Report.
    public static final boolean comBReport = config.getBoolean("commands.bungee.report.enabled");
    public static final List<String> comBReportAliases = config.getStringList("commands.bungee.report.aliases");
    public static final String comBReportPerm = config.getString("commands.bungee.report.permission");
    // ... Party.
    //
    public static final boolean comBParty = config.getBoolean("commands.bungee.party.enabled");
    public static final String comBParPerm = config.getString("commands.bungee.party.permission");
    public static final List<String> comBParMainAliases = config.getStringList("commands.bungee.party.aliases.main");
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
    // ... Servers.
    // Lobby.
    public static final boolean comBLobby = config.getBoolean("commands.bungee.servers.lobby.enabled");
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
    public static final List<String> comBGlobalOnlineAliases = config.getStringList("commands.bungee.staff.globalonline.aliases");
    public static final String comBGlobalOnlinePerm = config.getString("commands.bungee.staff.globalonline.permission");
    // StaffChat.
    public static final boolean comBStaffChat = config.getBoolean("commands.bungee.staff.staffchat.enabled");
    public static final List<String> comBStaffChatAliases = config.getStringList("commands.bungee.staff.staffchat.aliases");
    public static final String comBStaffChatPerm = config.getString("commands.bungee.staff.staffchat.permission");
    // StaffOnline.
    public static final boolean comBStaffOnline = config.getBoolean("commands.bungee.staff.staffonline.enabled");
    public static final List<String> comBStaffOnlineAliases = config.getStringList("commands.bungee.staff.staffonline.aliases");
    public static final String comBStaffOnlinePerm = config.getString("commands.bungee.staff.staffonline.permission");
    // Reload.
    public static final List<String> comBReloadAliases = config.getStringList("commands.bungee.staff.slreload.aliases");
    public static final String comBReloadPerm = config.getString("commands.bungee.staff.slreload.permission");
    // ... ... Modules.
    // ... Discord.
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
    // Staff logins / logouts.
    public static final boolean moduleDStaffLogin = config.getBoolean("modules.discord.send-staff-login");
    public static final boolean moduleDStaffLogoff = config.getBoolean("modules.discord.send-staff-logoff");
    // ... Bungee.
    // Reports.
    public static final boolean moduleReportsBConfirmation = config.getBoolean("modules.bungee.reports.send-confirmation");
    public static final boolean moduleReportsMToDiscord = config.getBoolean("modules.bungee.reports.minecraft-to-discord");
    public static final boolean moduleReportsSendChat = config.getBoolean("modules.bungee.reports.send-in-chat");
    // StaffChat.
    public static final boolean moduleStaffChat = config.getBoolean("modules.bungee.staffchat.enabled");
    public static final boolean moduleStaffChatDoPrefix = config.getBoolean("modules.bungee.staffchat.enable-prefix");
    public static final String moduleStaffChatPrefix = config.getString("modules.bungee.staffchat.prefix");
    public static final boolean moduleStaffChatMToDiscord = config.getBoolean("modules.bungee.staffchat.minecraft-to-staffchat");
    // Staff logins / logouts.
    public static final boolean moduleBStaffLogin = config.getBoolean("modules.bungee.send-staff-login");
    public static final boolean moduleBStaffLogoff = config.getBoolean("modules.bungee.send-staff-logoff");
    // ... Parties.
    // Max size.
    public static final int partyMax = config.getInt("modules.bungee.parties.max-size");
    public static final String partyMaxPerm = config.getString("modules.bungee.parties.base-permission");
    //public static final Configuration partyMaxSize = config.getSection("modules.bungee.parties.max-size");
    //public static final List<Configuration> partyMaxSize = config.get("modules.bungee.parties.max-size", new ArrayList<>());
}