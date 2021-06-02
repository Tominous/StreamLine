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
import net.plasmere.streamline.commands.staff.punishments.MuteCommand;
import net.plasmere.streamline.commands.staff.spy.GSPYCommand;
import net.plasmere.streamline.commands.staff.spy.PSPYCommand;
import net.plasmere.streamline.commands.staff.spy.SSPYCommand;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.listeners.JoinLeaveListener;
import net.plasmere.streamline.listeners.ChatListener;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;

import java.util.ArrayList;
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

        // Setup...
        List<Command> commands = new ArrayList<>();

        // Misc.
        Command stream = new StreamCommand(ConfigUtils.comBStreamBase, ConfigUtils.comBStreamPerm, getAliases(ConfigUtils.comBStreamAliases));
        commands.add(stream);
        Command ping = new PingCommand(ConfigUtils.comBPingBase, ConfigUtils.comBPingPerm, getAliases(ConfigUtils.comBPingAliases));
        commands.add(ping);
        Command plugins = new PluginsCommand(ConfigUtils.comBPluginsBase, ConfigUtils.comBPluginsPerm, getAliases(ConfigUtils.comBPluginsAliases));
        commands.add(plugins);
        Command report = new ReportCommand(ConfigUtils.comBReportBase, ConfigUtils.comBReportPerm, getAliases(ConfigUtils.comBReportAliases));
        commands.add(report);

        // Staff.
        Command staffChat = new StaffChatCommand(ConfigUtils.comBStaffChatBase, ConfigUtils.comBStaffChatPerm, getAliases(ConfigUtils.comBStaffChatAliases));
        commands.add(staffChat);
        Command staffOnline = new StaffOnlineCommand(ConfigUtils.comBStaffOnlineBase, ConfigUtils.comBStaffOnlinePerm, getAliases(ConfigUtils.comBStaffOnlineAliases));
        commands.add(staffOnline);
        Command globalOnline = new GlobalOnlineCommand(ConfigUtils.comBGlobalOnlineBase, ConfigUtils.comBGlobalOnlinePerm, getAliases(ConfigUtils.comBGlobalOnlineAliases));
        commands.add(globalOnline);
        Command reload = new ReloadCommand(ConfigUtils.comBReloadBase, ConfigUtils.comBReloadPerm, getAliases(ConfigUtils.comBReloadAliases));
        commands.add(reload);
        Command bsudo = new SudoCommand(ConfigUtils.comBSudoBase, ConfigUtils.comBSudoPerm, getAliases(ConfigUtils.comBSudoAliases));
        commands.add(bsudo);
        Command serverping = new JDAPingerCommand(ConfigUtils.comBSPingBase, ConfigUtils.comBSPingPerm, getAliases(ConfigUtils.comBSPingAliases));
        commands.add(serverping);

        // Servers.
        Command lobby = new GoToServerLobbyCommand(ConfigUtils.comBLobbyBase, ConfigUtils.comBLobbyPerm, getAliases(ConfigUtils.comBLobbyAliases));
        commands.add(lobby);
        Command fabric = new GoToServerVanillaCommand(ConfigUtils.comBFabricPerm);
        commands.add(fabric);

        // Parties / Guilds.
        Command parties = new PartiesCommand(ConfigUtils.comBPartiesBase, ConfigUtils.comBPartiesPerm, getAliases(ConfigUtils.comBPartiesAliases));
        commands.add(parties);
        Command guilds = new GuildsCommand(ConfigUtils.comBPartiesBase, ConfigUtils.comBGuildsPerm, getAliases(ConfigUtils.comBGuildsAliases));
        commands.add(guilds);
        Command party = new PartyCommand(ConfigUtils.comBPartyBase, ConfigUtils.comBParPerm, getAliases(ConfigUtils.comBParMainAliases));
        commands.add(party);
        Command guild = new GuildCommand(ConfigUtils.comBGuildBase, ConfigUtils.comBGuildPerm, getAliases(ConfigUtils.comBGuildMainAliases));
        commands.add(guild);

        List<String> pca = new ArrayList<>();
        pca.add("pch");
        pca.add("pchat");
        Command pc = new PCQuickCommand("pc", ConfigUtils.comBParPerm, getAliases(pca));
        commands.add(pc);
        List<String> gca = new ArrayList<>();
        gca.add("gch");
        gca.add("gchat");
        Command gc = new GCQuickCommand("gc", ConfigUtils.comBGuildPerm, getAliases(gca));
        commands.add(gc);

        // Stats.
        Command stats = new StatsCommand(ConfigUtils.comBStatsBase, ConfigUtils.comBStatsPerm, getAliases(ConfigUtils.comBStatsAliases));
        commands.add(stats);

        Command sspy = new SSPYCommand(ConfigUtils.comBSSPYBase, ConfigUtils.comBSSPYPerm, getAliases(ConfigUtils.comBSSPYAliases));
        commands.add(sspy);
        Command gspy = new GSPYCommand(ConfigUtils.comBGSPYPerm, ConfigUtils.comBGSPYPerm, getAliases(ConfigUtils.comBGSPYAliases));
        commands.add(gspy);
        Command pspy = new PSPYCommand(ConfigUtils.comBPSPYBase, ConfigUtils.comBPSPYPerm, getAliases(ConfigUtils.comBPSPYAliases));
        commands.add(pspy);

        // Events.
        Command btag = new BTagCommand(ConfigUtils.comBBTagBase, ConfigUtils.comBBTagPerm, getAliases(ConfigUtils.comBBTagAliases));
        commands.add(btag);
        Command reev = new EventReloadCommand(ConfigUtils.comBReloadBase, ConfigUtils.comBEReloadPerm, getAliases(ConfigUtils.comBEReloadAliases));
        commands.add(reev);
        Command points = new NetworkPointsCommand(ConfigUtils.comBPointsBase, ConfigUtils.comBPointsPerm, getAliases(ConfigUtils.comBPointsAliases));
        commands.add(points);

        // Messaging.
        Command ignore = new IgnoreCommand(ConfigUtils.comBIgnoreBase, ConfigUtils.comBIgnorePerm, getAliases(ConfigUtils.comBIgnoreAliases));
        commands.add(ignore);
        Command message = new MessageCommand(ConfigUtils.comBMessageBase, ConfigUtils.comBMessagePerm, getAliases(ConfigUtils.comBMessageAliases));
        commands.add(message);
        Command reply = new ReplyCommand(ConfigUtils.comBReplyBase, ConfigUtils.comBReplyPerm, getAliases(ConfigUtils.comBReplyAliases));
        commands.add(reply);
        Command friend = new FriendCommand(ConfigUtils.comBFriendBase, ConfigUtils.comBFriendPerm, getAliases(ConfigUtils.comBFriendAliases));
        commands.add(friend);

        // Punishments.
        Command mute = new MuteCommand(ConfigUtils.comBMuteBase, ConfigUtils.comBMutePerm, getAliases(ConfigUtils.comBMuteAliases));
        commands.add(mute);
        Command ban = new BanCommand(ConfigUtils.comBBanBase, ConfigUtils.comBBanPerm, getAliases(ConfigUtils.comBBanAliases));

        try {
            for (Command command : commands) {
                plugin.getProxy().getPluginManager().unregisterCommand(command);
            }
        } catch (Exception e){
            MessagingUtils.logWarning("Command discriminator broke for the following reason: " + e.getMessage());
        }

        // Commands.
        if (ConfigUtils.comBStream)
            registerCommand(plugin, stream);
        if (ConfigUtils.comBStaffChat)
            registerCommand(plugin, staffChat);
        if (ConfigUtils.comBSudo)
            registerCommand(plugin, bsudo);

        // Utils.
        if (ConfigUtils.comBPing)
            registerCommand(plugin, ping);
        if (ConfigUtils.comBPlugins)
            registerCommand(plugin, plugins);
        if (ConfigUtils.comBStaffOnline)
            registerCommand(plugin, staffOnline);
        if (ConfigUtils.comBGlobalOnline && StreamLine.lpHolder.enabled)
            registerCommand(plugin, globalOnline);
        registerCommand(plugin, reload);
        if (ConfigUtils.comBSPing)
            registerCommand(plugin, serverping);

        // All players.
        if (ConfigUtils.comBReport)
            registerCommand(plugin, report);

        // JDA
        //registerCommand(plugin, new JDAPingerCommand(plugin));

        // servers
        if (ConfigUtils.comBLobby)
            registerCommand(plugin, lobby);
        if (ConfigUtils.comBFabric)
            registerCommand(plugin, fabric);


        if (ConfigUtils.comBParties)
            registerCommand(plugin, parties);
        if (ConfigUtils.comBGuilds)
            registerCommand(plugin, guilds);
        if (ConfigUtils.comBParty)
            registerCommand(plugin, party);
        if (ConfigUtils.comBGuild)
            registerCommand(plugin, guild);
        if (ConfigUtils.comBParQuick)
            registerCommand(plugin, pc);
        if (ConfigUtils.comBGuildQuick)
            registerCommand(plugin, gc);

        if (ConfigUtils.comBStats)
            registerCommand(plugin, stats);

        if (ConfigUtils.comBSSPY)
            registerCommand(plugin, sspy);
        if (ConfigUtils.comBGSPY)
            registerCommand(plugin, gspy);
        if (ConfigUtils.comBPSPY)
            registerCommand(plugin, pspy);
        if (ConfigUtils.comBBTag)
            registerCommand(plugin, btag);
        if (ConfigUtils.comBEReload)
            registerCommand(plugin, reev);
        if (ConfigUtils.comBPoints)
            registerCommand(plugin, points);
        if (ConfigUtils.comBIgnore)
            registerCommand(plugin, ignore);
        if (ConfigUtils.comBMessage)
            registerCommand(plugin, message);
        if (ConfigUtils.comBReply)
            registerCommand(plugin, reply);
        if (ConfigUtils.comBFriend)
            registerCommand(plugin, friend);

        // Punishments.
        if (ConfigUtils.comBMute)
            registerCommand(plugin, mute);
        if (ConfigUtils.comBBan)
            registerCommand(plugin, ban);

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

        List<Listener> listeners = new ArrayList<>();
        Listener staffchat = new ChatListener(plugin);
        Listener joinleave = new JoinLeaveListener(plugin);

        listeners.add(staffchat);
        listeners.add(joinleave);

        try {
            for (Listener listener : listeners) {
                plugin.getProxy().getPluginManager().unregisterListener(listener);
            }
        } catch (Exception e){
            MessagingUtils.logWarning("Listener discriminator broke for the following reason: " + e.getMessage());
        }

        registerListener(plugin, staffchat);
        registerListener(plugin, joinleave);

        plugin.getLogger().info("Loaded " + listenerAmount + " listener(s) into memory...!");
    }
}
