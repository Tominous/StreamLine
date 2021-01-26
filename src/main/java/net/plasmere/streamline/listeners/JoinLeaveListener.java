package net.plasmere.streamline.listeners;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.messaging.BungeeMassMessage;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.Objects;

public class JoinLeaveListener implements Listener {
    private final Configuration config = Config.getConf();
    private final StreamLine plugin;

    public JoinLeaveListener(StreamLine streamLine){
        this.plugin = streamLine;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PostLoginEvent ev) {
        ProxiedPlayer player = ev.getPlayer();

        Player stat = PlayerUtils.getStat(player);

        if (stat == null) {
            if (PlayerUtils.exists(player.getName())) {
                PlayerUtils.addStat(new Player(player, false));
            } else {
                PlayerUtils.createStat(player);
            }
            stat = PlayerUtils.getStat(player);
            if (stat == null) {
                StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + player.getName());
                return;
            }
        }

        try {
            for (ProxiedPlayer pl : StreamLine.getInstance().getProxy().getPlayers()){
                Player p = PlayerUtils.getStat(pl);

                if (p == null) {
                    if (PlayerUtils.exists(pl.getName())) {
                        PlayerUtils.addStat(new Player(pl, false));
                    } else {
                        PlayerUtils.createStat(pl);
                    }
                    p = PlayerUtils.getStat(pl);
                    if (p == null) {
                        StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + pl.getName());
                        continue;
                    }
                }

                if (GuildUtils.getGuild(p) == null && ! p.equals(stat)) continue;
                if (GuildUtils.getGuild(p) != null) {
                    if (Objects.requireNonNull(GuildUtils.getGuild(p)).hasMember(stat)) break;
                }
                if (GuildUtils.pHasGuild(stat)) {
                    GuildUtils.addGuild(new Guild(stat.uuid, false));
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (ConfigUtils.moduleBPlayerJoins) {
            case "yes":
                MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
                        MessageConfUtils.bungeeOnline.replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                        "streamline.staff"));
                break;
            case "staff":
                if (player.hasPermission("streamline.staff")) {
                    MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
                            MessageConfUtils.bungeeOnline.replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
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
                        MessageConfUtils.discordOnline.replace("%player%", PlayerUtils.getOffOnRegDiscord(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                        ConfigUtils.textChannelBJoins));
                break;
            case "staff":
                if (player.hasPermission("streamline.staff")) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(plugin.getProxy().getConsole(),
                            MessageConfUtils.discordOnlineEmbed,
                            MessageConfUtils.discordOnline.replace("%player%", PlayerUtils.getOffOnRegDiscord(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                            ConfigUtils.textChannelBJoins));
                }
                break;
            case "no":
            default:
                break;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServer(ServerConnectEvent ev){
        ProxiedPlayer player = ev.getPlayer();

        boolean hasServer = false;
        ServerInfo server = null;

        for (ServerInfo s : StreamLine.getInstance().getProxy().getServers().values()) {
            String sv = s.getName();
            if (player.hasPermission(ConfigUtils.redirectPre + sv)) {
                hasServer = true;
                server = s;
                break;
            }
        }

        if (!hasServer) {
            server = StreamLine.getInstance().getProxy().getServerInfo(ConfigUtils.redirectMain);
        }
        if (! ev.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)){
            return;
        }
        ev.setTarget(server);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerDisconnectEvent ev) {
        ProxiedPlayer player = ev.getPlayer();

        Player stat = PlayerUtils.getStat(player);

        if (stat == null) {
            if (PlayerUtils.exists(player.getName())) {
                PlayerUtils.addStat(new Player(player, false));
            } else {
                PlayerUtils.createStat(player);
            }
            stat = PlayerUtils.getStat(player);
            if (stat == null) {
                StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + player.getName());
                return;
            }
        }

        switch (ConfigUtils.moduleBPlayerLeaves) {
            case "yes":
                MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
                        MessageConfUtils.bungeeOffline.replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                        "streamline.staff"));
                break;
            case "staff":
                if (player.hasPermission("streamline.staff")) {
                    MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
                            MessageConfUtils.bungeeOffline.replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
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
                        MessageConfUtils.discordOffline.replace("%player%", PlayerUtils.getOffOnRegDiscord(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                        ConfigUtils.textChannelBLeaves));
                break;
            case "staff":
                if (player.hasPermission("streamline.staff")) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(plugin.getProxy().getConsole(),
                            MessageConfUtils.discordOfflineEmbed,
                            MessageConfUtils.discordOffline.replace("%player%", PlayerUtils.getOffOnRegDiscord(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                            ConfigUtils.textChannelBLeaves));
                }
                break;
            case "no":
            default:
                break;
        }

        try {
            for (ProxiedPlayer pl : StreamLine.getInstance().getProxy().getPlayers()){
                Player p = PlayerUtils.getStat(pl);

                if (p == null) {
                    if (PlayerUtils.exists(pl.getName())) {
                        PlayerUtils.addStat(new Player(pl, false));
                    } else {
                        PlayerUtils.createStat(pl);
                    }
                    p = PlayerUtils.getStat(pl);
                    if (p == null) {
                        StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + pl.getName());
                        continue;
                    }
                }

                Guild guild = GuildUtils.getGuild(stat);

                if (guild == null || p.equals(stat)) continue;
                if (guild.hasMember(p)) break;


                GuildUtils.removeGuild(Objects.requireNonNull(GuildUtils.getGuild(stat)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PlayerUtils.removeStat(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
