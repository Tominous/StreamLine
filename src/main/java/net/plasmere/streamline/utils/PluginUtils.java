package net.plasmere.streamline.utils;

import net.md_5.bungee.api.plugin.PluginManager;
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
import net.plasmere.streamline.commands.staff.punishments.IPBanCommand;
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
import net.plasmere.streamline.listeners.PluginMessagingListener;
import net.plasmere.streamline.listeners.ProxyPingListener;
import net.plasmere.streamline.objects.enums.NetworkState;

import java.util.*;

public class PluginUtils {
    public static int commandsAmount = 0;
    public static int listenerAmount = 0;
    public static NetworkState state = NetworkState.NULL;

    public static boolean isLocked(){
        return state.equals(NetworkState.STOPPING) || state.equals(NetworkState.STOPPED);
    }

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

        // Debug.
        if (ConfigUtils.comBDeleteStat) {
            registerCommand(plugin, new DeleteStatCommand(ConfigUtils.comBDeleteStatBase, ConfigUtils.comBDeleteStatPerm, stringListToArray(ConfigUtils.comBDeleteStatAliases)));
        }

        // Staff.
        // // Reg.
        if (ConfigUtils.comBStream) {
            registerCommand(plugin, new StreamCommand(ConfigUtils.comBStreamBase, ConfigUtils.comBStreamPerm, stringListToArray(ConfigUtils.comBStreamAliases)));
        }
        if (ConfigUtils.comBStaffChat) {
            registerCommand(plugin, new StaffChatCommand(ConfigUtils.comBStaffChatBase, ConfigUtils.comBStaffChatPerm, stringListToArray(ConfigUtils.comBStaffChatAliases)));
        }
        if (ConfigUtils.comBSudo) {
            registerCommand(plugin, new SudoCommand(ConfigUtils.comBSudoBase, ConfigUtils.comBSudoPerm, stringListToArray(ConfigUtils.comBSudoAliases)));
        }
        if (ConfigUtils.comBStaffOnline) {
            registerCommand(plugin, new StaffOnlineCommand(ConfigUtils.comBStaffOnlineBase, ConfigUtils.comBStaffOnlinePerm, stringListToArray(ConfigUtils.comBStaffOnlineAliases)));
        }
        if (ConfigUtils.comBGlobalOnline && StreamLine.lpHolder.enabled) {
            registerCommand(plugin, new GlobalOnlineCommand(ConfigUtils.comBGlobalOnlineBase, ConfigUtils.comBGlobalOnlinePerm, stringListToArray(ConfigUtils.comBGlobalOnlineAliases)));
        }
        if (ConfigUtils.comBSettings) {
            if (ConfigUtils.debug) MessagingUtils.logInfo("Settings make start...");
            registerCommand(plugin, new SettingsEditCommand(ConfigUtils.comBSettingsBase, ConfigUtils.comBSettingsPerm, stringListToArray(ConfigUtils.comBSettingsAliases)));
            if (ConfigUtils.debug) MessagingUtils.logInfo("Settings make finish...");
        } else {
            if (ConfigUtils.debug) MessagingUtils.logInfo("Settings enabled = false...");
        }
        if (ConfigUtils.comBTeleport) {
            registerCommand(plugin, new TeleportCommand(ConfigUtils.comBTeleportBase, ConfigUtils.comBTeleportPerm, stringListToArray(ConfigUtils.comBTeleportAliases)));
        }
        // // Spying.
        if (ConfigUtils.comBSSPY) {
            registerCommand(plugin, new SSPYCommand(ConfigUtils.comBSSPYBase, ConfigUtils.comBSSPYPerm, stringListToArray(ConfigUtils.comBSSPYAliases)));
        }
        if (ConfigUtils.comBGSPY) {
            registerCommand(plugin, new GSPYCommand(ConfigUtils.comBGSPYBase, ConfigUtils.comBGSPYPerm, stringListToArray(ConfigUtils.comBGSPYAliases)));
        }
        if (ConfigUtils.comBPSPY) {
            registerCommand(plugin, new PSPYCommand(ConfigUtils.comBPSPYBase, ConfigUtils.comBPSPYPerm, stringListToArray(ConfigUtils.comBPSPYAliases)));
        }
        if (ConfigUtils.comBSCView) {
            registerCommand(plugin, new SCViewCommand(ConfigUtils.comBSCViewBase, ConfigUtils.comBSCViewPerm, stringListToArray(ConfigUtils.comBSCViewAliases)));
        }
        // // Punishments.
        if (ConfigUtils.comBMute && ConfigUtils.punMutes) {
            registerCommand(plugin, new MuteCommand(ConfigUtils.comBMuteBase, ConfigUtils.comBMutePerm, stringListToArray(ConfigUtils.comBMuteAliases)));
        }
        if (ConfigUtils.comBKick) {
            registerCommand(plugin, new KickCommand(ConfigUtils.comBKickBase, ConfigUtils.comBKickPerm, stringListToArray(ConfigUtils.comBKickAliases)));
        }
        if (ConfigUtils.comBBan && ConfigUtils.punBans) {
            registerCommand(plugin, new BanCommand(ConfigUtils.comBBanBase, ConfigUtils.comBBanPerm, stringListToArray(ConfigUtils.comBBanAliases)));
        }
        if (ConfigUtils.comBIPBan && ConfigUtils.punIPBans) {
            registerCommand(plugin, new IPBanCommand(ConfigUtils.comBIPBanBase, ConfigUtils.comBIPBanPerm, stringListToArray(ConfigUtils.comBIPBanAliases)));
        }

        // Utils.
        // // Other.
        registerCommand(plugin, new ReloadCommand(ConfigUtils.comBReloadBase, ConfigUtils.comBReloadPerm, stringListToArray(ConfigUtils.comBReloadAliases)));
        if (ConfigUtils.comBPing) {
            registerCommand(plugin, new PingCommand(ConfigUtils.comBPingBase, ConfigUtils.comBPingPerm, stringListToArray(ConfigUtils.comBPingAliases)));
        }
        if (ConfigUtils.comBPlugins) {
            registerCommand(plugin, new PluginsCommand(ConfigUtils.comBPluginsBase, ConfigUtils.comBPluginsPerm, stringListToArray(ConfigUtils.comBPluginsAliases)));
        }
        if (ConfigUtils.comBSPing) {
            registerCommand(plugin, new JDAPingerCommand(ConfigUtils.comBSPingBase, ConfigUtils.comBSPingPerm, stringListToArray(ConfigUtils.comBSPingAliases)));
        }
        if (ConfigUtils.comBInfo) {
            registerCommand(plugin, new InfoCommand(ConfigUtils.comBInfoBase, ConfigUtils.comBInfoPerm, stringListToArray(ConfigUtils.comBInfoAliases)));
        }
        if (ConfigUtils.onCloseHackEnd) {
            try {
                PluginManager pm = StreamLine.getInstance().getProxy().getPluginManager();

                List<Map.Entry<String, Command>> commands = new ArrayList<>(pm.getCommands());

                List<Command> unregCommands = new ArrayList<>();

                for (Map.Entry<String, Command> commandEntry : commands) {
                    if (commandEntry.getValue().getName().equals("end")) unregCommands.add(commandEntry.getValue());
                }

                for (Command command : unregCommands) {
                    pm.unregisterCommand(command);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (ConfigUtils.comBEnd) {
            registerCommand(plugin, new EndCommand(ConfigUtils.comBEndBase, ConfigUtils.comBEndPerm, stringListToArray(ConfigUtils.comBEndAliases)));
        }
        // // Events.
        if (ConfigUtils.comBEReload) {
            registerCommand(plugin, new EventReloadCommand(ConfigUtils.comBReloadBase, ConfigUtils.comBEReloadPerm, stringListToArray(ConfigUtils.comBEReloadAliases)));
        }

        // All players.
        // // Reports.
        if (ConfigUtils.comBReport) {
            registerCommand(plugin, new ReportCommand(ConfigUtils.comBReportBase, ConfigUtils.comBReportPerm, stringListToArray(ConfigUtils.comBReportAliases)));
        }
        // // Messaging.
        if (ConfigUtils.comBIgnore) {
            registerCommand(plugin, new IgnoreCommand(ConfigUtils.comBIgnoreBase, ConfigUtils.comBIgnorePerm, stringListToArray(ConfigUtils.comBIgnoreAliases)));
        }
        if (ConfigUtils.comBMessage) {
            registerCommand(plugin, new MessageCommand(ConfigUtils.comBMessageBase, ConfigUtils.comBMessagePerm, stringListToArray(ConfigUtils.comBMessageAliases)));
        }
        if (ConfigUtils.comBReply) {
            registerCommand(plugin, new ReplyCommand(ConfigUtils.comBReplyBase, ConfigUtils.comBReplyPerm, stringListToArray(ConfigUtils.comBReplyAliases)));
        }
        if (ConfigUtils.comBFriend) {
            registerCommand(plugin, new FriendCommand(ConfigUtils.comBFriendBase, ConfigUtils.comBFriendPerm, stringListToArray(ConfigUtils.comBFriendAliases)));
        }

        // Servers.
        if (ConfigUtils.comBLobby) {
            registerCommand(plugin, new GoToServerLobbyCommand(ConfigUtils.comBLobbyBase, ConfigUtils.comBLobbyPerm, stringListToArray(ConfigUtils.comBLobbyAliases)));
        }
        if (ConfigUtils.comBFabric) {
            registerCommand(plugin, new GoToServerVanillaCommand(ConfigUtils.comBFabricPerm));
        }

        // Parties / Guilds / Stats.
        // // Stats.
        if (ConfigUtils.comBGetStats) {
            registerCommand(plugin, new GetStatsCommand(ConfigUtils.comBGetStatsBase, ConfigUtils.comBGetStatsPerm, stringListToArray(ConfigUtils.comBGetStatsAliases)));
        }
        if (ConfigUtils.comBStats) {
            registerCommand(plugin, new StatsCommand(ConfigUtils.comBStatsBase, ConfigUtils.comBStatsPerm, stringListToArray(ConfigUtils.comBStatsAliases)));
        }
        if (ConfigUtils.comBBTag) {
            registerCommand(plugin, new BTagCommand(ConfigUtils.comBBTagBase, ConfigUtils.comBBTagPerm, stringListToArray(ConfigUtils.comBBTagAliases)));
        }
        // // Stats.
        if (ConfigUtils.comBParties) {
            registerCommand(plugin, new PartiesCommand(ConfigUtils.comBPartiesBase, ConfigUtils.comBPartiesPerm, stringListToArray(ConfigUtils.comBPartiesAliases)));
        }
        if (ConfigUtils.comBParty) {
            registerCommand(plugin, new PartyCommand(ConfigUtils.comBPartyBase, ConfigUtils.comBParPerm, stringListToArray(ConfigUtils.comBParMainAliases)));
        }
        if (ConfigUtils.comBParQuick) {
            registerCommand(plugin, new PCQuickCommand("pc", ConfigUtils.comBParPerm, stringListToArray(Arrays.asList("pch", "pchat"))));
        }
        if (ConfigUtils.comBPoints) {
            registerCommand(plugin, new NetworkPointsCommand(ConfigUtils.comBPointsBase, ConfigUtils.comBPointsPerm, stringListToArray(ConfigUtils.comBPointsAliases)));
        }
        // // Guilds.
        if (ConfigUtils.comBGuilds) {
            registerCommand(plugin, new GuildsCommand(ConfigUtils.comBPartiesBase, ConfigUtils.comBGuildsPerm, stringListToArray(ConfigUtils.comBGuildsAliases)));
        }
        if (ConfigUtils.comBGuild) {
            registerCommand(plugin, new GuildCommand(ConfigUtils.comBGuildBase, ConfigUtils.comBGuildPerm, stringListToArray(ConfigUtils.comBGuildMainAliases)));
        }
        if (ConfigUtils.comBGuildQuick) {
            registerCommand(plugin, new GCQuickCommand("gc", ConfigUtils.comBGuildPerm, stringListToArray(Arrays.asList("gch", "gchat"))));
        }

        plugin.getLogger().info("Loaded " + commandsAmount + " command(s) into memory...!");
    }

    public static String[] stringListToArray(List<String> aliases){
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
        registerListener(plugin, new PluginMessagingListener());

        plugin.getLogger().info("Loaded " + listenerAmount + " listener(s) into memory...!");
    }

    public static int getCeilingInt(Set<Integer> ints){
        int value = 0;

        for (Integer i : ints) {
            if (i >= value) value = i;
        }

        return value;
    }

    public static boolean checkEqualsStrings(String toCheck, String... checks) {
        for (String check : checks) {
            if (toCheck.equals(check)) return true;
        }

        return false;
    }
}
