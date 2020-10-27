package net.plasmere.streamline.commands.servers;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.plasmere.streamline.StreamLine;
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

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class GoToServerLobbyCommand extends Command implements TabExecutor {
    private final StreamLine plugin;

    public GoToServerLobbyCommand(StreamLine streamLine, String perm, String[] aliases) {
        super("hub", perm, aliases);

        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] args){
        Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
        if ( args.length == 0 ) {
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;

                if (player.hasPermission("streamline.server.lobby") || player.hasPermission("streamline.*")) {
                    ProxyServer proxy = plugin.getProxy();

                    ServerInfo vanServer = proxy.getServerInfo(ConfigUtils.comBLobbyEnd);

                    if (!vanServer.canAccess(player))
                        player.sendMessage(new TextComponent(ChatColor.RED + "Cannot connect..."));
                    else {
                        player.sendMessage(new TextComponent(ChatColor.GREEN + "Connecting now..."));
                        player.connect(vanServer, ServerConnectEvent.Reason.COMMAND);
                    }
                } else {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPerm);
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
        return ( args.length > 1 ) ? Collections.EMPTY_LIST : Iterables.transform( Iterables.filter( ProxyServer.getInstance().getServers().values(), new Predicate<ServerInfo>()
        {
            private final String lower = ( args.length == 0 ) ? "" : args[0].toLowerCase( Locale.ROOT );

            @Override
            public boolean apply(ServerInfo input)
            {
                return input.getName().toLowerCase( Locale.ROOT ).startsWith( lower ) && input.canAccess( sender );
            }
        } ), new Function<ServerInfo, String>()
        {
            @Override
            public String apply(ServerInfo input)
            {
                return input.getName();
            }
        } );
    }
}
