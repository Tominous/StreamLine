package net.plasmere.streamline.listeners;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.*;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.events.Event;
import net.plasmere.streamline.events.EventsHandler;
import net.plasmere.streamline.objects.lists.SingleSet;
import net.plasmere.streamline.objects.messaging.BungeeMassMessage;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.*;

public class JoinLeaveListener implements Listener {
    private final Configuration config = Config.getConf();
    private final StreamLine plugin;
    private final LuckPerms api = LuckPermsProvider.get();

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

                if (stat.guild == null) continue;
                if (stat.guild.equals("")) continue;
                if (p.guild.equals(stat.guild) && ! p.equals(stat)) continue;

                try {
                    GuildUtils.addGuild(new Guild(UUID.fromString(stat.guild), false));
                } catch (Exception e) {
                    // Do nothing.
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (ConfigUtils.moduleBPlayerJoins) {
            case "yes":
                MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
                        MessageConfUtils.bungeeOnline.replace("%player_default%", player.getName())
                                .replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                        "streamline.staff"));
                break;
            case "staff":
                if (player.hasPermission("streamline.staff")) {
                    MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
                            MessageConfUtils.bungeeOnline.replace("%player_default%", player.getName())
                                    .replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                            "streamline.staff"));
                }
                break;
            case "no":
            default:
                break;
        }

        if (ConfigUtils.joinsLeavesIcon) {
            switch (ConfigUtils.moduleDPlayerJoins) {
                case "yes":
                    if (ConfigUtils.joinsLeavesAsConsole) {
                        MessagingUtils.sendDiscordEBMessage(new DiscordMessage(plugin.getProxy().getConsole(),
                                MessageConfUtils.discordOnlineEmbed,
                                MessageConfUtils.discordOnline.replace("%player_default%", player.getName())
                                        .replace("%player%", PlayerUtils.getOffOnRegDiscord(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                                ConfigUtils.textChannelBJoins));
                    } else {
                        MessagingUtils.sendDiscordJoinLeaveMessage(true, stat);
                    }
                    break;
                case "staff":
                    if (player.hasPermission("streamline.staff")) {
                        if (ConfigUtils.joinsLeavesAsConsole) {
                            MessagingUtils.sendDiscordEBMessage(new DiscordMessage(plugin.getProxy().getConsole(),
                                    MessageConfUtils.discordOnlineEmbed,
                                    MessageConfUtils.discordOnline.replace("%player_default%", player.getName())
                                            .replace("%player%", PlayerUtils.getOffOnRegDiscord(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                                    ConfigUtils.textChannelBJoins));
                        } else {
                            MessagingUtils.sendDiscordJoinLeaveMessage(true, stat);
                        }
                    }
                    break;
                case "no":
                default:
                    break;
            }
        }

        for (Event event : EventsHandler.getEvents()) {
            if (! EventsHandler.checkTags(event, stat)) continue;

            if (! (event.condition.equals(Event.Condition.JOIN) && event.conVal.toLowerCase(Locale.ROOT).equals("network"))) continue;

            EventsHandler.runEvent(event, stat);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServer(ServerConnectEvent ev){
        ProxiedPlayer player = ev.getPlayer();

        boolean hasServer = false;
        ServerInfo server = ev.getTarget();

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

        if (ev.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {
            for (ServerInfo s : StreamLine.getInstance().getProxy().getServers().values()) {
                String sv = s.getName();
                if (player.hasPermission(ConfigUtils.redirectPre + sv)) {
                    Group group = api.getGroupManager().getGroup(Objects.requireNonNull(api.getUserManager().getUser(player.getName())).getPrimaryGroup());

                    if (group == null) {
                        hasServer = true;
                        server = s;
                        break;
                    }

                    Collection<Node> nodes = group.getNodes();

                    for (Node node : nodes) {
                        if (node.getKey().equals(ConfigUtils.redirectPre + sv)) {
                            hasServer = true;
                            server = s;
                            break;
                        }
                    }

                    Collection<Node> nods = Objects.requireNonNull(api.getUserManager().getUser(player.getName())).getNodes();

                    for (Node node : nods) {
                        if (node.getKey().equals(ConfigUtils.redirectPre + sv)) {
                            hasServer = true;
                            server = s;
                            break;
                        }
                    }

                    if (hasServer){
                        break;
                    }
                }
            }

            if (! hasServer) {
                server = StreamLine.getInstance().getProxy().getServerInfo(ConfigUtils.redirectMain);
            }
        }

        if (StreamLine.viaHolder.enabled) {
            if (! hasServer && ConfigUtils.lobbies && ev.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)){
                for (SingleSet<String, String> set : StreamLine.lobbies.getInfo().values()) {
                    String sName = set.key;

                    int version = StreamLine.viaHolder.via.getPlayerVersion(player.getUniqueId());

                    //StreamLine.getInstance().getLogger().info("Version: " + version + " | Server: " + sName);

                    if (! StreamLine.lobbies.isAllowed(version, sName)) continue;

                    server = StreamLine.getInstance().getProxy().getServerInfo(sName);

                    ev.setTarget(server);

                    stat.updateKey("connectingStatus", "CONNECTING");

                    return;
                }
            }

            if (! ev.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {
                if (! player.hasPermission(ConfigUtils.vbOverridePerm)) {
                    int version = StreamLine.viaHolder.via.getPlayerVersion(player.getUniqueId());

                    if (! StreamLine.serverPermissions.isAllowed(version, server.getName())) {
                        MessagingUtils.sendBUserMessage(ev.getPlayer(), MessageConfUtils.vbBlocked);
                        ev.setCancelled(true);
                        return;
                    }
                }
            }
        }

        ev.setTarget(server);
        stat.updateKey("connectingStatus", "IDLE");

        for (Event event : EventsHandler.getEvents()) {
            if (! EventsHandler.checkTags(event, stat)) continue;

            if (! (event.condition.equals(Event.Condition.JOIN) && event.conVal.toLowerCase(Locale.ROOT).equals(server.getName()))) continue;

            EventsHandler.runEvent(event, stat);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerDiscon(ServerDisconnectEvent ev) {
        ProxiedPlayer player = ev.getPlayer();

        ServerInfo server = ev.getTarget();

        Player stat = PlayerUtils.getStat(player);

        try {
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

            for (Event event : EventsHandler.getEvents()) {
                if (! EventsHandler.checkTags(event, stat)) continue;

                if (! event.condition.equals(Event.Condition.LEAVE) && ! event.conVal.toLowerCase(Locale.ROOT).equals(server.getName())) continue;

                EventsHandler.runEvent(event, stat);
            }
        } catch (Exception e) {
            // do nothing
        }
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
                        MessageConfUtils.bungeeOffline.replace("%player_default%", player.getName())
                                .replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                        "streamline.staff"));
                break;
            case "staff":
                if (player.hasPermission("streamline.staff")) {
                    MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
                            MessageConfUtils.bungeeOffline.replace("%player_default%", player.getName())
                                    .replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                            "streamline.staff"));
                }
                break;
            case "no":
            default:
                break;
        }
        switch (ConfigUtils.moduleDPlayerLeaves) {
            case "yes":
                if (ConfigUtils.joinsLeavesAsConsole) {
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(plugin.getProxy().getConsole(),
                        MessageConfUtils.discordOfflineEmbed,
                        MessageConfUtils.discordOffline.replace("%player_default%", player.getName())
                                .replace("%player%", PlayerUtils.getOffOnRegDiscord(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                        ConfigUtils.textChannelBLeaves));
                } else {
                    MessagingUtils.sendDiscordJoinLeaveMessage(false, stat);
                }
                break;
            case "staff":
                if (player.hasPermission("streamline.staff")) {
                    if (ConfigUtils.joinsLeavesAsConsole) {
                        MessagingUtils.sendDiscordEBMessage(new DiscordMessage(plugin.getProxy().getConsole(),
                                MessageConfUtils.discordOfflineEmbed,
                                MessageConfUtils.discordOffline.replace("%player_default%", player.getName())
                                        .replace("%player%", PlayerUtils.getOffOnRegDiscord(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
                                ConfigUtils.textChannelBLeaves));
                    } else {
                        MessagingUtils.sendDiscordJoinLeaveMessage(false, stat);
                    }
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

                if (GuildUtils.pHasGuild(stat)) {
                    Guild guild = GuildUtils.getGuild(stat);

                    if (guild == null || p.equals(stat)) continue;
                    if (guild.hasMember(p)) break;
                    if (stat.guild.equals(p.guild)) break;

                    GuildUtils.removeGuild(guild);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Event event : EventsHandler.getEvents()) {
            if (! EventsHandler.checkTags(event, stat)) continue;

            if (! (event.condition.equals(Event.Condition.LEAVE) && event.conVal.toLowerCase(Locale.ROOT).equals("network"))) continue;

            EventsHandler.runEvent(event, stat);
        }

        try {
            PlayerUtils.removeStat(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKick(ServerKickEvent ev){
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

        if (StreamLine.viaHolder.enabled) {
            if (ConfigUtils.lobbies) {
                if (stat.connectingStatus.equals("CONNECTING")) {
                    stat.tryingLobby++;

                    TreeMap<Integer, SingleSet<String, String>> servers = StreamLine.lobbies.getInfo();

                    String[] lobbies = new String[servers.size()];

                    int i = 0;
                    for (SingleSet<String, String> s : servers.values()) {
                        lobbies[i] = s.key;
                        i++;
                    }

                    StreamLine.getInstance().getLogger().info("Trying to reconnect to " + lobbies[stat.tryingLobby]);

                    ev.setCancelServer(ProxyServer.getInstance().getServerInfo(lobbies[stat.tryingLobby]));
                    //ev.getPlayer().connect(ProxyServer.getInstance().getServerInfo(lobbies[stat.tryingLobby]));
                    ev.setCancelled(true);
                }
            }
        }
    }
}