package net.plasmere.streamline.commands.servers;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.CommandsConfUtils;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.utils.TextUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GoToServerLobbyCommand extends Command implements TabExecutor {
    public GoToServerLobbyCommand(String base, String perm, String[] aliases) {
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args){
        Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
        if ( args.length == 0 ) {
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;

                if (player.hasPermission("streamline.server.lobby") || player.hasPermission("streamline.*")) {
                    ProxyServer proxy = StreamLine.getInstance().getProxy();

                    ServerInfo vanServer = proxy.getServerInfo(CommandsConfUtils.comBLobbyEnd);

                    if (!vanServer.canAccess(player))
                        player.sendMessage(new TextComponent(ChatColor.RED + "Cannot connect..."));
                    else {
                        player.sendMessage(new TextComponent(ChatColor.GREEN + "Connecting now..."));
                        player.connect(vanServer, ServerConnectEvent.Reason.COMMAND);
                    }
                } else {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPerm());
                }
            } else {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Sorry, but only a player can do this..."));
            }
        } else {
            if (!(sender instanceof ProxiedPlayer))
                return;

            ProxiedPlayer player = (ProxiedPlayer) sender;

            ServerInfo server = servers.get(args[0]);
            if (server == null)
                player.sendMessage(new TextComponent(ChatColor.RED + "No server specified..."));
            else if (! server.canAccess(player))
                player.sendMessage(new TextComponent(ChatColor.RED + "Cannot connect..."));
            else
            {
                player.sendMessage(new TextComponent(ChatColor.GREEN + "Connecting now..."));
                player.connect(server, ServerConnectEvent.Reason.COMMAND);
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args)
    {
        TreeSet<String> servers = new TreeSet<>();

        for (ServerInfo serverInfo : StreamLine.getInstance().getProxy().getServers().values()) {
            if (! serverInfo.canAccess(sender)) continue;

            servers.add(serverInfo.getName().toLowerCase(Locale.ROOT));
        }

        return TextUtils.getCompletion(servers, args[0]);
    }
}
