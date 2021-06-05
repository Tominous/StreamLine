package net.plasmere.streamline.listeners;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.*;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.events.Event;
import net.plasmere.streamline.events.EventsHandler;
import net.plasmere.streamline.events.enums.Condition;
import net.plasmere.streamline.objects.GeyserFile;
import net.plasmere.streamline.objects.Party;
import net.plasmere.streamline.objects.lists.SingleSet;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.plasmere.streamline.utils.holders.GeyserHolder;

import java.util.*;

public class JoinLeaveListener implements Listener {
    private final Configuration config = Config.getConf();
    private final StreamLine plugin;
    private final GeyserFile file = StreamLine.geyserHolder.file;
    private final GeyserHolder holder = StreamLine.geyserHolder;

    public JoinLeaveListener(StreamLine streamLine){
        this.plugin = streamLine;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preJoin(PreLoginEvent ev) {
        String uuid = UUIDFetcher.fetch(ev.getConnection().getName());

        if (ConfigUtils.punBans) {
            String reason = PlayerUtils.checkIfBanned(uuid);
            if (reason != null) {
                ev.setCancelReason(TextUtils.codedText(reason));
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PostLoginEvent ev) {
        ProxiedPlayer player = ev.getPlayer();

        if (holder.enabled && holder.isGeyserPlayer(player) && ! file.hasProperty(player.getUniqueId().toString())) {
            file.updateKey(holder.getGeyserUUID(player.getName()), player.getName());
        }

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

        stat.tryAddNewName(player.getName());
        stat.tryAddNewIP(player);

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
                    if (GuildUtils.existsByUUID(stat.guild)) {
                        GuildUtils.addGuild(new Guild(stat.guild, false));
                    } else {
                        stat.updateKey("guild", "");
                    }
                } catch (Exception e) {
                    // Do nothing.
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        switch (ConfigUtils.moduleBPlayerJoins) {
//            case "yes":
//                MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
//                        MessageConfUtils.bungeeOnline.replace("%player_default%", player.getName())
//                                .replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
//                        ConfigUtils.moduleBPlayerJoinsPerm));
//                break;
//            case "staff":
//                if (player.hasPermission(ConfigUtils.staffPerm)) {
//                    MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
//                            MessageConfUtils.bungeeOnline.replace("%player_default%", player.getName())
//                                    .replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
//                            ConfigUtils.moduleBPlayerJoinsPerm));
//                }
//                break;
//            case "no":
//            default:
//                break;
//        }

        String joinsOrder = ConfigUtils.moduleBPlayerJoins;

        if (! joinsOrder.equals("")) {
            String[] order = joinsOrder.split(",");
            for (ProxiedPlayer p : StreamLine.getInstance().getProxy().getPlayers()) {
                if (! p.hasPermission(ConfigUtils.moduleBPlayerJoinsPerm)) continue;

                Player other = PlayerUtils.getStat(p);

                if (other == null) {
                    PlayerUtils.addStat(new Player(UUIDFetcher.getCachedUUID(p.getName())));
                    other = PlayerUtils.getStat(p);
                    if (other == null) {
                        StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + p.getName());
                        break;
                    }
                }

                label:
                for (String s : order) {
                    switch (s) {
                        case "staff":
                            if (player.hasPermission(ConfigUtils.staffPerm)) {
                                if (p.hasPermission(ConfigUtils.staffPerm)) {
                                    MessagingUtils.sendBUserMessage(p, MessageConfUtils.bungeeOnline
                                            .replace("%player_default%", player.getName())
                                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat))
                                    );
                                    break label;
                                }
                            }
                            break;
                        case "guild":
                            if (! ConfigUtils.guildSendJoins) continue;

                            Guild guild = GuildUtils.getGuild(other);
                            if (guild == null) continue;

                            if (guild.hasMember(stat)) {
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.guildConnect
                                        .replace("%player_default%", player.getName())
                                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                        case "party":
                            if (! ConfigUtils.partySendJoins) continue;

                            Party party = PartyUtils.getParty(other);
                            if (party == null) continue;

                            if (party.hasMember(stat)) {
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.partyConnect
                                        .replace("%player_default%", player.getName())
                                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                        case "friend":
                            if (stat.friendList.contains(other.uuid)) {
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.friendConnect
                                        .replace("%player_default%", player.getName())
                                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                    }
                }
            }
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
                    if (player.hasPermission(ConfigUtils.staffPerm)) {
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

        if (ConfigUtils.events) {
            for (Event event : EventsHandler.getEvents()) {
                if (!EventsHandler.checkTags(event, stat)) continue;

                if (!(EventsHandler.checkEventConditions(event, stat, Condition.JOIN, "network"))) continue;

                EventsHandler.runEvent(event, stat);
            }
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

        if (ev.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY) && ConfigUtils.redirectEnabled && StreamLine.lpHolder.enabled) {
            for (ServerInfo s : StreamLine.getInstance().getProxy().getServers().values()) {
                String sv = s.getName();
                if (player.hasPermission(ConfigUtils.redirectPre + sv)) {
                    Group group = StreamLine.lpHolder.api.getGroupManager().getGroup(Objects.requireNonNull(StreamLine.lpHolder.api.getUserManager().getUser(player.getName())).getPrimaryGroup());

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

                    Collection<Node> nods = Objects.requireNonNull(StreamLine.lpHolder.api.getUserManager().getUser(player.getName())).getNodes();

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

        if (StreamLine.viaHolder.enabled && ConfigUtils.redirectEnabled) {
            if (! hasServer && ConfigUtils.lobbies && ev.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)){
                for (SingleSet<String, String> set : StreamLine.lobbies.getInfo().values()) {
                    String sName = set.key;

                    int version = StreamLine.viaHolder.via.getPlayerVersion(player.getUniqueId());

                    if (! StreamLine.lobbies.isAllowed(version, sName)) continue;

                    server = StreamLine.getInstance().getProxy().getServerInfo(sName);

                    ev.setTarget(server);

                    return;
                }
            }

            if (! ev.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {
                if (ConfigUtils.vbEnabled) {
                    if (!player.hasPermission(ConfigUtils.vbOverridePerm)) {
                        int version = StreamLine.viaHolder.via.getPlayerVersion(player.getUniqueId());

                        if (!StreamLine.serverPermissions.isAllowed(version, server.getName())) {
                            MessagingUtils.sendBUserMessage(ev.getPlayer(), MessageConfUtils.vbBlocked);
                            ev.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        if (server == null) return;

        ev.setTarget(server);

        if (ConfigUtils.events) {
            for (Event event : EventsHandler.getEvents()) {
                if (!EventsHandler.checkTags(event, stat)) continue;

                if (!(EventsHandler.checkEventConditions(event, stat, Condition.JOIN, server.getName()))) continue;

                EventsHandler.runEvent(event, stat);
            }
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

            if (ConfigUtils.events) {
                for (Event event : EventsHandler.getEvents()) {
                    if (!EventsHandler.checkTags(event, stat)) continue;

                    if (!(EventsHandler.checkEventConditions(event, stat, Condition.LEAVE, server.getName()))) continue;

                    EventsHandler.runEvent(event, stat);
                }
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

//        switch (ConfigUtils.moduleBPlayerLeaves) {
//            case "yes":
//                MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
//                        MessageConfUtils.bungeeOffline.replace("%player_default%", player.getName())
//                                .replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
//                        ConfigUtils.moduleBPlayerLeavesPerm));
//                break;
//            case "staff":
//                if (player.hasPermission(ConfigUtils.staffPerm)) {
//                    MessagingUtils.sendBungeeMessage(new BungeeMassMessage(plugin.getProxy().getConsole(),
//                            MessageConfUtils.bungeeOffline.replace("%player_default%", player.getName())
//                                    .replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(UUIDFetcher.getPlayer(player)))),
//                            ConfigUtils.moduleBPlayerLeavesPerm));
//                }
//                break;
//            case "no":
//            default:
//                break;
//        }

        String leavesOrder = ConfigUtils.moduleBPlayerLeaves;

        if (! leavesOrder.equals("")) {
            String[] order = leavesOrder.split(",");
            for (ProxiedPlayer p : StreamLine.getInstance().getProxy().getPlayers()) {
                if (! p.hasPermission(ConfigUtils.moduleBPlayerLeavesPerm)) continue;

                Player other = PlayerUtils.getStat(p);

                if (other == null) {
                    PlayerUtils.addStat(new Player(UUIDFetcher.getCachedUUID(p.getName())));
                    other = PlayerUtils.getStat(p);
                    if (other == null) {
                        StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + p.getName());
                        break;
                    }
                }

                label:
                for (String s : order) {
                    switch (s) {
                        case "staff":
                            if (player.hasPermission(ConfigUtils.staffPerm)) {
                                if (p.hasPermission(ConfigUtils.staffPerm)) {
                                    MessagingUtils.sendBUserMessage(p, MessageConfUtils.bungeeOffline
                                            .replace("%player_default%", player.getName())
                                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat))
                                    );
                                    break label;
                                }
                            }
                            break;
                        case "guild":
                            if (! ConfigUtils.guildSendLeaves) continue;

                            Guild guild = GuildUtils.getGuild(other);
                            if (guild == null) continue;

                            if (guild.hasMember(stat)) {
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.guildDisconnect
                                        .replace("%player_default%", player.getName())
                                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                        case "party":
                            if (! ConfigUtils.partySendLeaves) continue;

                            Party party = PartyUtils.getParty(other);
                            if (party == null) continue;

                            if (party.hasMember(stat)) {
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.partyDisconnect
                                        .replace("%player_default%", player.getName())
                                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                        case "friend":
                            if (stat.friendList.contains(other.uuid)) {
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.friendDisconnect
                                        .replace("%player_default%", player.getName())
                                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                    }
                }
            }
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
                if (player.hasPermission(ConfigUtils.staffPerm)) {
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

        if (ConfigUtils.events) {
            for (Event event : EventsHandler.getEvents()) {
                if (!EventsHandler.checkTags(event, stat)) continue;

                if (!(EventsHandler.checkEventConditions(event, stat, Condition.LEAVE, "network"))) continue;

                EventsHandler.runEvent(event, stat);
            }
        }

        try {
            PlayerUtils.removeStat(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKick(ServerKickEvent ev){
        try {
            if (StreamLine.getInstance().getProxy().getPlayer(ev.getPlayer().getUniqueId()) == null) return;
        } catch (Exception e) {
            return;
        }

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
                TreeMap<Integer, SingleSet<String, String>> servers = StreamLine.lobbies.getInfo();

                String[] lobbies = new String[servers.size()];

                int i = 0;
                for (SingleSet<String, String> s : servers.values()) {
                    lobbies[i] = s.key;
                    i++;
                }

                PlayerUtils.addConn(stat);
                SingleSet<Integer, Integer> conn = PlayerUtils.getConnection(stat);

                if (conn == null) return;

                String kickTo = lobbies[conn.value];

                while (StreamLine.getInstance().getProxy().getPlayer(stat.latestName).getServer().getInfo().getName().equals(kickTo)) {
                    PlayerUtils.addOneToConn(stat);
                    conn = PlayerUtils.getConnection(stat);
                    if (conn == null) return;
                }

                ev.setCancelServer(StreamLine.getInstance().getProxy().getServerInfo(lobbies[conn.value]));
                ev.setCancelled(true);

                PlayerUtils.addOneToConn(stat);
            }
        }
    }
}