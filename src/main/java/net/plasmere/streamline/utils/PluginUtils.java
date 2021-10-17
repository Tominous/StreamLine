package net.plasmere.streamline.utils;

import net.md_5.bungee.api.plugin.PluginManager;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.commands.*;
import net.plasmere.streamline.commands.messaging.*;
import net.plasmere.streamline.commands.servers.GoToServerLobbyCommand;
import net.plasmere.streamline.commands.servers.GoToServerVanillaCommand;
import net.plasmere.streamline.commands.staff.*;
import net.plasmere.streamline.commands.staff.events.BTagCommand;
import net.plasmere.streamline.commands.staff.events.EventReloadCommand;
import net.plasmere.streamline.commands.staff.punishments.BanCommand;
import net.plasmere.streamline.commands.staff.punishments.IPBanCommand;
import net.plasmere.streamline.commands.staff.punishments.KickCommand;
import net.plasmere.streamline.commands.staff.punishments.MuteCommand;
import net.plasmere.streamline.commands.staff.settings.LanguageCommand;
import net.plasmere.streamline.commands.staff.settings.SettingsEditCommand;
import net.plasmere.streamline.commands.staff.spy.GSPYCommand;
import net.plasmere.streamline.commands.staff.spy.PSPYCommand;
import net.plasmere.streamline.commands.staff.spy.SCViewCommand;
import net.plasmere.streamline.commands.staff.spy.SSPYCommand;
import net.plasmere.streamline.config.CommandsConfUtils;
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
        if (CommandsConfUtils.comBDeleteStat) {
            registerCommand(plugin, new DeleteStatCommand(CommandsConfUtils.comBDeleteStatBase, CommandsConfUtils.comBDeleteStatPerm, stringListToArray(CommandsConfUtils.comBDeleteStatAliases)));
        }

        // Staff.
        // // Reg.
        if (CommandsConfUtils.comBStream) {
            registerCommand(plugin, new StreamCommand(CommandsConfUtils.comBStreamBase, CommandsConfUtils.comBStreamPerm, stringListToArray(CommandsConfUtils.comBStreamAliases)));
        }
        if (CommandsConfUtils.comBStaffChat) {
            registerCommand(plugin, new StaffChatCommand(CommandsConfUtils.comBStaffChatBase, CommandsConfUtils.comBStaffChatPerm, stringListToArray(CommandsConfUtils.comBStaffChatAliases)));
        }
        if (CommandsConfUtils.comBSudo) {
            registerCommand(plugin, new SudoCommand(CommandsConfUtils.comBSudoBase, CommandsConfUtils.comBSudoPerm, stringListToArray(CommandsConfUtils.comBSudoAliases)));
        }
        if (CommandsConfUtils.comBStaffOnline) {
            registerCommand(plugin, new StaffOnlineCommand(CommandsConfUtils.comBStaffOnlineBase, CommandsConfUtils.comBStaffOnlinePerm, stringListToArray(CommandsConfUtils.comBStaffOnlineAliases)));
        }
        if (CommandsConfUtils.comBGlobalOnline && StreamLine.lpHolder.enabled) {
            registerCommand(plugin, new GlobalOnlineCommand(CommandsConfUtils.comBGlobalOnlineBase, CommandsConfUtils.comBGlobalOnlinePerm, stringListToArray(CommandsConfUtils.comBGlobalOnlineAliases)));
        }
        if (CommandsConfUtils.comBSettings) {
            registerCommand(plugin, new SettingsEditCommand(CommandsConfUtils.comBSettingsBase, CommandsConfUtils.comBSettingsPerm, stringListToArray(CommandsConfUtils.comBSettingsAliases)));
        }
        if (CommandsConfUtils.comBLang) {
            registerCommand(plugin, new LanguageCommand(CommandsConfUtils.comBLangBase, CommandsConfUtils.comBLangPerm, stringListToArray(CommandsConfUtils.comBLangAliases)));
        }
        if (CommandsConfUtils.comBTeleport) {
            registerCommand(plugin, new TeleportCommand(CommandsConfUtils.comBTeleportBase, CommandsConfUtils.comBTeleportPerm, stringListToArray(CommandsConfUtils.comBTeleportAliases)));
        }
        // // Spying.
        if (CommandsConfUtils.comBSSPY) {
            registerCommand(plugin, new SSPYCommand(CommandsConfUtils.comBSSPYBase, CommandsConfUtils.comBSSPYPerm, stringListToArray(CommandsConfUtils.comBSSPYAliases)));
        }
        if (CommandsConfUtils.comBGSPY) {
            registerCommand(plugin, new GSPYCommand(CommandsConfUtils.comBGSPYBase, CommandsConfUtils.comBGSPYPerm, stringListToArray(CommandsConfUtils.comBGSPYAliases)));
        }
        if (CommandsConfUtils.comBPSPY) {
            registerCommand(plugin, new PSPYCommand(CommandsConfUtils.comBPSPYBase, CommandsConfUtils.comBPSPYPerm, stringListToArray(CommandsConfUtils.comBPSPYAliases)));
        }
        if (CommandsConfUtils.comBSCView) {
            registerCommand(plugin, new SCViewCommand(CommandsConfUtils.comBSCViewBase, CommandsConfUtils.comBSCViewPerm, stringListToArray(CommandsConfUtils.comBSCViewAliases)));
        }
        // // Punishments.
        if (CommandsConfUtils.comBMute && ConfigUtils.punMutes) {
            registerCommand(plugin, new MuteCommand(CommandsConfUtils.comBMuteBase, CommandsConfUtils.comBMutePerm, stringListToArray(CommandsConfUtils.comBMuteAliases)));
        }
        if (CommandsConfUtils.comBKick) {
            registerCommand(plugin, new KickCommand(CommandsConfUtils.comBKickBase, CommandsConfUtils.comBKickPerm, stringListToArray(CommandsConfUtils.comBKickAliases)));
        }
        if (CommandsConfUtils.comBBan && ConfigUtils.punBans) {
            registerCommand(plugin, new BanCommand(CommandsConfUtils.comBBanBase, CommandsConfUtils.comBBanPerm, stringListToArray(CommandsConfUtils.comBBanAliases)));
        }
        if (CommandsConfUtils.comBIPBan && ConfigUtils.punIPBans) {
            registerCommand(plugin, new IPBanCommand(CommandsConfUtils.comBIPBanBase, CommandsConfUtils.comBIPBanPerm, stringListToArray(CommandsConfUtils.comBIPBanAliases)));
        }

        // Utils.
        // // Other.
        registerCommand(plugin, new ReloadCommand(CommandsConfUtils.comBReloadBase, CommandsConfUtils.comBReloadPerm, stringListToArray(CommandsConfUtils.comBReloadAliases)));
        if (CommandsConfUtils.comBPing) {
            registerCommand(plugin, new PingCommand(CommandsConfUtils.comBPingBase, CommandsConfUtils.comBPingPerm, stringListToArray(CommandsConfUtils.comBPingAliases)));
        }
        if (CommandsConfUtils.comBPlugins) {
            registerCommand(plugin, new PluginsCommand(CommandsConfUtils.comBPluginsBase, CommandsConfUtils.comBPluginsPerm, stringListToArray(CommandsConfUtils.comBPluginsAliases)));
        }
        if (CommandsConfUtils.comBSPing) {
            registerCommand(plugin, new JDAPingerCommand(CommandsConfUtils.comBSPingBase, CommandsConfUtils.comBSPingPerm, stringListToArray(CommandsConfUtils.comBSPingAliases)));
        }
        if (CommandsConfUtils.comBInfo) {
            registerCommand(plugin, new InfoCommand(CommandsConfUtils.comBInfoBase, CommandsConfUtils.comBInfoPerm, stringListToArray(CommandsConfUtils.comBInfoAliases)));
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
        if (CommandsConfUtils.comBEnd) {
            registerCommand(plugin, new EndCommand(CommandsConfUtils.comBEndBase, CommandsConfUtils.comBEndPerm, stringListToArray(CommandsConfUtils.comBEndAliases)));
        }
        // // Events.
        if (CommandsConfUtils.comBEReload) {
            registerCommand(plugin, new EventReloadCommand(CommandsConfUtils.comBEReloadBase, CommandsConfUtils.comBEReloadPerm, stringListToArray(CommandsConfUtils.comBEReloadAliases)));
        }

        // All players.
        // // Reports.
        if (CommandsConfUtils.comBReport) {
            registerCommand(plugin, new ReportCommand(CommandsConfUtils.comBReportBase, CommandsConfUtils.comBReportPerm, stringListToArray(CommandsConfUtils.comBReportAliases)));
        }
        // // Messaging.
        if (CommandsConfUtils.comBIgnore) {
            registerCommand(plugin, new IgnoreCommand(CommandsConfUtils.comBIgnoreBase, CommandsConfUtils.comBIgnorePerm, stringListToArray(CommandsConfUtils.comBIgnoreAliases)));
        }
        if (CommandsConfUtils.comBMessage) {
            registerCommand(plugin, new MessageCommand(CommandsConfUtils.comBMessageBase, CommandsConfUtils.comBMessagePerm, stringListToArray(CommandsConfUtils.comBMessageAliases)));
        }
        if (CommandsConfUtils.comBReply) {
            registerCommand(plugin, new ReplyCommand(CommandsConfUtils.comBReplyBase, CommandsConfUtils.comBReplyPerm, stringListToArray(CommandsConfUtils.comBReplyAliases)));
        }
        if (CommandsConfUtils.comBFriend) {
            registerCommand(plugin, new FriendCommand(CommandsConfUtils.comBFriendBase, CommandsConfUtils.comBFriendPerm, stringListToArray(CommandsConfUtils.comBFriendAliases)));
        }
        if (CommandsConfUtils.comBChatLevel) {
            registerCommand(plugin, new ChatChannelCommand(CommandsConfUtils.comBChatLevelBase, CommandsConfUtils.comBChatLevelPerm, stringListToArray(CommandsConfUtils.comBChatLevelAliases)));
        }
        if (CommandsConfUtils.comBVerify) {
            registerCommand(plugin, new BVerifyCommand(CommandsConfUtils.comBVerifyBase, CommandsConfUtils.comBVerifyPerm, stringListToArray(CommandsConfUtils.comBVerifyAliases)));
        }

        // Servers.
        if (CommandsConfUtils.comBLobby) {
            registerCommand(plugin, new GoToServerLobbyCommand(CommandsConfUtils.comBLobbyBase, CommandsConfUtils.comBLobbyPerm, stringListToArray(CommandsConfUtils.comBLobbyAliases)));
        }
        if (CommandsConfUtils.comBFabric) {
            registerCommand(plugin, new GoToServerVanillaCommand(CommandsConfUtils.comBFabricPerm));
        }

        // Parties / Guilds / Stats.
        // // Stats.
        if (CommandsConfUtils.comBGetStats) {
            registerCommand(plugin, new GetStatsCommand(CommandsConfUtils.comBGetStatsBase, CommandsConfUtils.comBGetStatsPerm, stringListToArray(CommandsConfUtils.comBGetStatsAliases)));
        }
        if (CommandsConfUtils.comBStats) {
            registerCommand(plugin, new StatsCommand(CommandsConfUtils.comBStatsBase, CommandsConfUtils.comBStatsPerm, stringListToArray(CommandsConfUtils.comBStatsAliases)));
        }
        if (CommandsConfUtils.comBBTag) {
            registerCommand(plugin, new BTagCommand(CommandsConfUtils.comBBTagBase, CommandsConfUtils.comBBTagPerm, stringListToArray(CommandsConfUtils.comBBTagAliases)));
        }
        if (CommandsConfUtils.comBPoints) {
            registerCommand(plugin, new NetworkPointsCommand(CommandsConfUtils.comBPointsBase, CommandsConfUtils.comBPointsPerm, stringListToArray(CommandsConfUtils.comBPointsAliases)));
        }
        // // Parties.
        if (CommandsConfUtils.comBParties) {
            registerCommand(plugin, new PartiesCommand(CommandsConfUtils.comBPartiesBase, CommandsConfUtils.comBPartiesPerm, stringListToArray(CommandsConfUtils.comBPartiesAliases)));
        }
        if (CommandsConfUtils.comBParty) {
            registerCommand(plugin, new PartyCommand(CommandsConfUtils.comBPartyBase, CommandsConfUtils.comBParPerm, stringListToArray(CommandsConfUtils.comBParMainAliases)));
        }
        if (CommandsConfUtils.comBParQuick) {
            registerCommand(plugin, new PCQuickCommand("pc", CommandsConfUtils.comBParPerm, stringListToArray(Arrays.asList("pch", "pchat"))));
        }
        // // Guilds.
        if (CommandsConfUtils.comBGuilds) {
            registerCommand(plugin, new GuildsCommand(CommandsConfUtils.comBGuildsBase, CommandsConfUtils.comBGuildsPerm, stringListToArray(CommandsConfUtils.comBGuildsAliases)));
        }
        if (CommandsConfUtils.comBGuild) {
            registerCommand(plugin, new GuildCommand(CommandsConfUtils.comBGuildBase, CommandsConfUtils.comBGuildPerm, stringListToArray(CommandsConfUtils.comBGuildMainAliases)));
        }
        if (CommandsConfUtils.comBGuildQuick) {
            registerCommand(plugin, new GCQuickCommand("gc", CommandsConfUtils.comBGuildPerm, stringListToArray(Arrays.asList("gch", "gchat"))));
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

    public static String[] stringListToArray(TreeSet<String> aliases){
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

    public static boolean isFreshInstall() {
        return ! StreamLine.getInstance().getPlDir().exists() && ! StreamLine.getInstance().getChatHistoryDir().exists() && ! StreamLine.getInstance().getGDir().exists();
    }
}
