package net.plasmere.streamline.listeners;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.BungeeMassMessage;
import net.plasmere.streamline.objects.DiscordMessage;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.utils.GuildUtils;
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
    public void onJoin(PostLoginEvent ev) {
        ProxiedPlayer player = ev.getPlayer();

        try {
            GuildUtils.addGuild(new Guild(player.getUniqueId(), false));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (ConfigUtils.moduleBPlayerJoins) {
            case "yes":
                MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
                        MessageConfUtils.bungeeOnline.replace("%player%", player.getName()),
                        "streamline.staff"));
                break;
            case "staff":
                if (player.hasPermission("streamline.staff")) {
                    MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
                            MessageConfUtils.bungeeOnline.replace("%player%", player.getName()),
                            "streamline.staff"));
                }
                break;
            case "no":
            default:
                break;
        }
        switch (ConfigUtils.moduleDPlayerJoins) {
            case "yes":
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(plugin.getProxy().getConsole(),
                        MessageConfUtils.discordOnlineEmbed,
                        MessageConfUtils.discordOnline.replace("%player%", player.getName()),
                        ConfigUtils.textChannelBJoins));
                break;
            case "staff":
                if (player.hasPermission("streamline.staff")) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(plugin.getProxy().getConsole(),
                            MessageConfUtils.discordOnlineEmbed,
                            MessageConfUtils.discordOnline.replace("%player%", player.getName()),
                            ConfigUtils.textChannelBJoins));
                }
                break;
            case "no":
            default:
                break;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerDisconnectEvent ev) {
        ProxiedPlayer player = ev.getPlayer();

        try {
            for (ProxiedPlayer p : StreamLine.getInstance().getProxy().getPlayers()){
                if (p.equals(player)) continue;
                if (GuildUtils.getGuild(player).hasMember(p)) break;


                GuildUtils.removeGuild(GuildUtils.getGuild(player));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (ConfigUtils.moduleBPlayerLeaves) {
            case "yes":
                MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
                        MessageConfUtils.bungeeOffline.replace("%player%", player.getName()),
                        "streamline.staff"));
                break;
            case "staff":
                if (player.hasPermission("streamline.staff")) {
                    MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
                            MessageConfUtils.bungeeOffline.replace("%player%", player.getName()),
                            "streamline.staff"));
                }
                break;
            case "no":
            default:
                break;
        }
        switch (ConfigUtils.moduleDPlayerLeaves) {
            case "yes":
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(plugin.getProxy().getConsole(),
                        MessageConfUtils.discordOfflineEmbed,
                        MessageConfUtils.discordOffline.replace("%player%", player.getName()),
                        ConfigUtils.textChannelBLeaves));
                break;
            case "staff":
                if (player.hasPermission("streamline.staff")) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(plugin.getProxy().getConsole(),
                            MessageConfUtils.discordOfflineEmbed,
                            MessageConfUtils.discordOffline.replace("%player%", player.getName()),
                            ConfigUtils.textChannelBLeaves));
                }
                break;
            case "no":
            default:
                break;
        }
    }
}
