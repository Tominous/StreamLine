package net.plasmere.streamline.commands.servers;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Command;

public class GoToServerVanillaCommand extends Command {

    public GoToServerVanillaCommand(String perm) {
        super("fabric", perm,"trampoline", "bungee-trampoline", "tramp");
    }

    @Override
    public void execute(CommandSender sender, String[] args){
        if (sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (player.hasPermission("streamline.server.fabric") || player.hasPermission("streamline.*")) {
                ProxyServer proxy = StreamLine.getInstance().getProxy();

                ServerInfo vanServer = proxy.getServerInfo(ConfigUtils.comBFabricEnd);

                if (!vanServer.canAccess(player))
                    player.sendMessage(new TextComponent(ChatColor.RED + "Cannot connect..."));
                else {
                    player.sendMessage(new TextComponent(ChatColor.GREEN + "Connecting now..."));
                    player.connect(vanServer, ServerConnectEvent.Reason.COMMAND);
                }
            } else {
                player.sendMessage(new TextComponent(ChatColor.RED + "Sorry, but you don't have permission to do this..."));
            }
        } else {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Sorry, but only a player can do this..."));
        }
    }
}
