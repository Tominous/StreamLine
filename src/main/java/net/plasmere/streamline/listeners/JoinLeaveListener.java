package net.plasmere.streamline.listeners;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class JoinLeaveListener implements Listener {
    private final Configuration config = Config.getConf();
    private final StreamLine plugin;

    public JoinLeaveListener(StreamLine streamLine){
        this.plugin = streamLine;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PostLoginEvent e){
        ProxiedPlayer player = e.getPlayer();
        if (player.hasPermission("streamline.staff")) {
            if (ConfigUtils.moduleBStaffLogin)
                MessagingUtils.sendStaffMessageLogin(player, plugin);
            if (ConfigUtils.moduleDStaffLogin)
                MessagingUtils.sendDiscordEBStaffMessage(MessageConfUtils.sOnlineMessageEmbedTitle,
                        MessageConfUtils.sOnlineDiscordOnline
                            .replace("%staff%", player.getName())
                );
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerDisconnectEvent e){
        ProxiedPlayer player = e.getPlayer();
        if (player.hasPermission("streamline.staff")) {
            if (ConfigUtils.moduleBStaffLogoff)
                MessagingUtils.sendStaffMessageLogoff(player, plugin);
            if (ConfigUtils.moduleDStaffLogoff)
                MessagingUtils.sendDiscordEBStaffMessage(MessageConfUtils.sOnlineMessageEmbedTitle,
                        MessageConfUtils.sOnlineDiscordOffline
                            .replace("%staff%", player.getName())
                );
        }
    }
}
