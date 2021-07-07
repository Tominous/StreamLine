package net.plasmere.streamline.utils;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.commands.*;
import net.plasmere.streamline.commands.messaging.FriendCommand;
import net.plasmere.streamline.commands.messaging.IgnoreCommand;
import net.plasmere.streamline.commands.messaging.MessageCommand;
import net.plasmere.streamline.commands.messaging.ReplyCommand;
import net.plasmere.streamline.commands.servers.GoToServerLobbyCommand;
import net.plasmere.streamline.commands.servers.GoToServerVanillaCommand;
import net.plasmere.streamline.commands.staff.*;
import net.plasmere.streamline.commands.staff.events.BTagCommand;
import net.plasmere.streamline.commands.staff.events.EventReloadCommand;
import net.plasmere.streamline.commands.staff.punishments.BanCommand;
import net.plasmere.streamline.commands.staff.punishments.KickCommand;
import net.plasmere.streamline.commands.staff.punishments.MuteCommand;
import net.plasmere.streamline.commands.staff.settings.SettingsEditCommand;
import net.plasmere.streamline.commands.staff.spy.GSPYCommand;
import net.plasmere.streamline.commands.staff.spy.PSPYCommand;
import net.plasmere.streamline.commands.staff.spy.SCViewCommand;
import net.plasmere.streamline.commands.staff.spy.SSPYCommand;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.listeners.JoinLeaveListener;
import net.plasmere.streamline.listeners.ChatListener;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.plasmere.streamline.listeners.ProxyPingListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginUtils {
    public static int commandsAmount = 0;
    public static int listenerAmount = 0;

    public static void unregisterCommand(StreamLine plugin, Command command){
        commandsAmount --;
        plugin.getProxy().getPluginManager().unregisterCommand(command);
    }

    public static void unregisterListener(StreamLine plugin, Listener listener){
        listenerAmount --;
        plugin.getProxy().getPluginManager().unregisterListener(listener);
    }

    public static void registerCommand(StreamLine plugin, Command command){
        commandsAmount ++;
        plugin.getProxy().getPluginManager().registerCommand(plugin, command);
    }

    public static void registerListener(StreamLine plugin, Listener listener){
        listenerAmount ++;
        plugin.getProxy().getPluginManager().registerListener(plugin, listener);
    }

    public static void loadCommands(StreamLine plugin){
        commandsAmount = 0;

        // Staff.
        // // Reg.
        if (ConfigUtils.comBStream) {
            registerCommand(plugin, new StreamCommand(ConfigUtils.comBStreamBase, ConfigUtils.comBStreamPerm, getAliases(ConfigUtils.comBStreamAliases)));
        }
        if (ConfigUtils.comBStaffChat) {
            registerCommand(plugin, new StaffChatCommand(ConfigUtils.comBStaffChatBase, ConfigUtils.comBStaffChatPerm, getAliases(ConfigUtils.comBStaffChatAliases)));
        }
        if (ConfigUtils.comBSudo) {
            registerCommand(plugin, new SudoCommand(ConfigUtils.comBSudoBase, ConfigUtils.comBSudoPerm, getAliases(ConfigUtils.comBSudoAliases)));
        }
        if (ConfigUtils.comBStaffOnline) {
            registerCommand(plugin, new StaffOnlineCommand(ConfigUtils.comBStaffOnlineBase, ConfigUtils.comBStaffOnlinePerm, getAliases(ConfigUtils.comBStaffOnlineAliases)));
        }
        if (ConfigUtils.comBGlobalOnline && StreamLine.lpHolder.enabled) {
            registerCommand(plugin, new GlobalOnlineCommand(ConfigUtils.comBGlobalOnlineBase, ConfigUtils.comBGlobalOnlinePerm, getAliases(ConfigUtils.comBGlobalOnlineAliases)));
        }
        if (ConfigUtils.comBSettings) {
            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("Settings make start...");
            registerCommand(plugin, new SettingsEditCommand(ConfigUtils.comBSettingsBase, ConfigUtils.comBSettingsPerm, getAliases(ConfigUtils.comBSettingsAliases)));
            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("Settings make finish...");
        } else {
            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("Settings enabled = false...");
        }
        // // Spying.
        if (ConfigUtils.comBSSPY) {
            registerCommand(plugin, new SSPYCommand(ConfigUtils.comBSSPYBase, ConfigUtils.comBSSPYPerm, getAliases(ConfigUtils.comBSSPYAliases)));
        }
        if (ConfigUtils.comBGSPY) {
            registerCommand(plugin, new GSPYCommand(ConfigUtils.comBGSPYPerm, ConfigUtils.comBGSPYPerm, getAliases(ConfigUtils.comBGSPYAliases)));
        }
        if (ConfigUtils.comBPSPY) {
            registerCommand(plugin, new PSPYCommand(ConfigUtils.comBPSPYBase, ConfigUtils.comBPSPYPerm, getAliases(ConfigUtils.comBPSPYAliases)));
        }
        if (ConfigUtils.comBSCView) {
            registerCommand(plugin, new SCViewCommand(ConfigUtils.comBSCViewBase, ConfigUtils.comBSCViewPerm, getAliases(ConfigUtils.comBSCViewAliases)));
        }
        // // Punishments.
        if (ConfigUtils.comBMute) {
            registerCommand(plugin, new MuteCommand(ConfigUtils.comBMuteBase, ConfigUtils.comBMutePerm, getAliases(ConfigUtils.comBMuteAliases)));
        }
        if (ConfigUtils.comBBan) {
            registerCommand(plugin, new BanCommand(ConfigUtils.comBBanBase, ConfigUtils.comBBanPerm, getAliases(ConfigUtils.comBBanAliases)));
        }
        if (ConfigUtils.comBKick) {
            registerCommand(plugin, new KickCommand(ConfigUtils.comBKickBase, ConfigUtils.comBKickPerm, getAliases(ConfigUtils.comBKickAliases)));
        }

        // Utils.
        // // Other.
        registerCommand(plugin, new ReloadCommand(ConfigUtils.comBReloadBase, ConfigUtils.comBReloadPerm, getAliases(ConfigUtils.comBReloadAliases)));
        if (ConfigUtils.comBPing) {
            registerCommand(plugin, new PingCommand(ConfigUtils.comBPingBase, ConfigUtils.comBPingPerm, getAliases(ConfigUtils.comBPingAliases)));
        }
        if (ConfigUtils.comBPlugins) {
            registerCommand(plugin, new PluginsCommand(ConfigUtils.comBPluginsBase, ConfigUtils.comBPluginsPerm, getAliases(ConfigUtils.comBPluginsAliases)));
        }
        if (ConfigUtils.comBSPing) {
            registerCommand(plugin, new JDAPingerCommand(ConfigUtils.comBSPingBase, ConfigUtils.comBSPingPerm, getAliases(ConfigUtils.comBSPingAliases)));
        }
        if (ConfigUtils.comBInfo) {
            registerCommand(plugin, new InfoCommand(ConfigUtils.comBInfoBase, ConfigUtils.comBInfoPerm, getAliases(ConfigUtils.comBInfoAliases)));
        }
        // // Events.
        if (ConfigUtils.comBEReload) {
            registerCommand(plugin, new EventReloadCommand(ConfigUtils.comBReloadBase, ConfigUtils.comBEReloadPerm, getAliases(ConfigUtils.comBEReloadAliases)));
        }

        // All players.
        // // Reports.
        if (ConfigUtils.comBReport) {
            registerCommand(plugin, new ReportCommand(ConfigUtils.comBReportBase, ConfigUtils.comBReportPerm, getAliases(ConfigUtils.comBReportAliases)));
        }
        // // Messaging.
        if (ConfigUtils.comBIgnore) {
            registerCommand(plugin, new IgnoreCommand(ConfigUtils.comBIgnoreBase, ConfigUtils.comBIgnorePerm, getAliases(ConfigUtils.comBIgnoreAliases)));
        }
        if (ConfigUtils.comBMessage) {
            registerCommand(plugin, new MessageCommand(ConfigUtils.comBMessageBase, ConfigUtils.comBMessagePerm, getAliases(ConfigUtils.comBMessageAliases)));
        }
        if (ConfigUtils.comBReply) {
            registerCommand(plugin, new ReplyCommand(ConfigUtils.comBReplyBase, ConfigUtils.comBReplyPerm, getAliases(ConfigUtils.comBReplyAliases)));
        }
        if (ConfigUtils.comBFriend) {
            registerCommand(plugin, new FriendCommand(ConfigUtils.comBFriendBase, ConfigUtils.comBFriendPerm, getAliases(ConfigUtils.comBFriendAliases)));
        }

        // Servers.
        if (ConfigUtils.comBLobby) {
            registerCommand(plugin, new GoToServerLobbyCommand(ConfigUtils.comBLobbyBase, ConfigUtils.comBLobbyPerm, getAliases(ConfigUtils.comBLobbyAliases)));
        }
        if (ConfigUtils.comBFabric) {
            registerCommand(plugin, new GoToServerVanillaCommand(ConfigUtils.comBFabricPerm));
        }

        // Parties / Guilds / Stats.
        // // Stats.
        if (ConfigUtils.comBGetStats) {
            registerCommand(plugin, new GetStatsCommand(ConfigUtils.comBGetStatsBase, ConfigUtils.comBGetStatsPerm, getAliases(ConfigUtils.comBGetStatsAliases)));
        }
        if (ConfigUtils.comBStats) {
            registerCommand(plugin, new StatsCommand(ConfigUtils.comBStatsBase, ConfigUtils.comBStatsPerm, getAliases(ConfigUtils.comBStatsAliases)));
        }
        if (ConfigUtils.comBBTag) {
            registerCommand(plugin, new BTagCommand(ConfigUtils.comBBTagBase, ConfigUtils.comBBTagPerm, getAliases(ConfigUtils.comBBTagAliases)));
        }
        // // Stats.
        if (ConfigUtils.comBParties) {
            registerCommand(plugin, new PartiesCommand(ConfigUtils.comBPartiesBase, ConfigUtils.comBPartiesPerm, getAliases(ConfigUtils.comBPartiesAliases)));
        }
        if (ConfigUtils.comBParty) {
            registerCommand(plugin, new PartyCommand(ConfigUtils.comBPartyBase, ConfigUtils.comBParPerm, getAliases(ConfigUtils.comBParMainAliases)));
        }
        if (ConfigUtils.comBParQuick) {
            registerCommand(plugin, new PCQuickCommand("pc", ConfigUtils.comBParPerm, getAliases(Arrays.asList("pch", "pchat"))));
        }
        if (ConfigUtils.comBPoints) {
            registerCommand(plugin, new NetworkPointsCommand(ConfigUtils.comBPointsBase, ConfigUtils.comBPointsPerm, getAliases(ConfigUtils.comBPointsAliases)));
        }
        // // Guilds.
        if (ConfigUtils.comBGuilds) {
            registerCommand(plugin, new GuildsCommand(ConfigUtils.comBPartiesBase, ConfigUtils.comBGuildsPerm, getAliases(ConfigUtils.comBGuildsAliases)));
        }
        if (ConfigUtils.comBGuild) {
            registerCommand(plugin, new GuildCommand(ConfigUtils.comBGuildBase, ConfigUtils.comBGuildPerm, getAliases(ConfigUtils.comBGuildMainAliases)));
        }
        if (ConfigUtils.comBGuildQuick) {
            registerCommand(plugin, new GCQuickCommand("gc", ConfigUtils.comBGuildPerm, getAliases(Arrays.asList("gch", "gchat"))));
        }

        plugin.getLogger().info("Loaded " + commandsAmount + " command(s) into memory...!");
    }

    private static String[] getAliases(List<String> aliases){
        String[] a = new String[aliases.size()];

        int i = 0;
        for (String alias : aliases){
            a[i] = alias;
            i++;
        }

        return a;
    }

    public static void loadListeners(StreamLine plugin){
        listenerAmount = 0;

        registerListener(plugin, new ChatListener());
        registerListener(plugin, new JoinLeaveListener());
        registerListener(plugin, new ProxyPingListener());

        plugin.getLogger().info("Loaded " + listenerAmount + " listener(s) into memory...!");
    }
}
