package net.plasmere.streamline.listeners;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.*;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.DiscordBotConfUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.events.Event;
import net.plasmere.streamline.events.EventsHandler;
import net.plasmere.streamline.events.enums.Condition;
import net.plasmere.streamline.objects.GeyserFile;
import net.plasmere.streamline.objects.Party;
import net.plasmere.streamline.objects.lists.SingleSet;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.savable.users.Player;
import net.plasmere.streamline.utils.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.plasmere.streamline.utils.holders.GeyserHolder;

import java.util.*;

public class JoinLeaveListener implements Listener {
    private final GeyserFile file = StreamLine.geyserHolder.file;
    private final GeyserHolder holder = StreamLine.geyserHolder;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preJoin(PreLoginEvent ev) {
        if (ev.isCancelled()) return;

        String ip = ev.getConnection().getSocketAddress().toString().replace("/", "").split(":")[0];

        String uuid = UUIDUtils.fetch(ev.getConnection().getName());

        if (ConfigUtils.punBans) {
            String reason = PlayerUtils.checkIfBanned(uuid);
            if (reason != null) {
                ev.setCancelReason(TextUtils.codedText(reason));
                ev.setCancelled(true);
            }
        }

        if (ConfigUtils.punIPBans) {
            String reason = PlayerUtils.checkIfIPBanned(ip);
            if (reason != null) {
                ev.setCancelReason(TextUtils.codedText(reason));
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PostLoginEvent ev) {
        ProxiedPlayer player = ev.getPlayer();

        if (holder.enabled && holder.isGeyserPlayer(player) && !file.hasProperty(player.getUniqueId().toString())) {
            file.updateKey(holder.getGeyserUUID(player.getName()), player.getName());
        }

        Player stat = PlayerUtils.addPlayerStat(player);

        stat.tryAddNewName(player.getName());
        stat.tryAddNewIP(player);

        try {
            if (stat.guild != null) {
                if (! stat.guild.equals("")) {
                    if (! GuildUtils.existsByUUID(stat.guild)) {
                        stat.updateKey("guild", "");
                    } else {
                        if (! GuildUtils.hasOnlineMemberAlready(stat)) {
                            GuildUtils.addGuild(new Guild(stat.guild, false));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String joinsOrder = ConfigUtils.moduleBPlayerJoins;

        if (!joinsOrder.equals("")) {
            String[] order = joinsOrder.split(",");
            for (ProxiedPlayer p : StreamLine.getInstance().getProxy().getPlayers()) {
                if (!p.hasPermission(ConfigUtils.moduleBPlayerJoinsPerm)) continue;

                Player other = PlayerUtils.getOrCreatePlayerStat(p);

                label:
                for (String s : order) {
                    switch (s) {
                        case "staff":
                            if (player.hasPermission(ConfigUtils.staffPerm)) {
                                if (p.hasPermission(ConfigUtils.staffPerm)) {
                                    MessagingUtils.sendBUserMessage(p, MessageConfUtils.bungeeOnline()
                                            .replace("%player_absolute%", player.getName())
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(stat))
                                    );
                                    break label;
                                }
                            }
                            break;
                        case "guild":
                            if (!ConfigUtils.guildSendJoins) continue;

                            Guild guild = GuildUtils.getGuild(other);
                            if (guild == null) continue;

                            if (guild.hasMember(stat)) {
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.guildConnect()
                                        .replace("%player_absolute%", player.getName())
                                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                        case "party":
                            if (!ConfigUtils.partySendJoins) continue;

                            Party party = PartyUtils.getParty(other);
                            if (party == null) continue;

                            if (party.hasMember(stat)) {
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.partyConnect()
                                        .replace("%player_absolute%", player.getName())
                                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                        case "friend":
                            if (stat.friendList.contains(other.uuid)) {
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.friendConnect()
                                        .replace("%player_absolute%", player.getName())
                                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                    }
                }
            }
        }

        if (ConfigUtils.moduleDEnabled) {
            switch (ConfigUtils.moduleDPlayerJoins) {
                case "yes":
                    if (ConfigUtils.joinsLeavesAsConsole) {
                        MessagingUtils.sendDiscordEBMessage(new DiscordMessage(StreamLine.getInstance().getProxy().getConsole(),
                                MessageConfUtils.discordOnlineEmbed(),
                                MessageConfUtils.discordOnline().replace("%player_absolute%", player.getName())
                                        .replace("%player_display%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStat(player))),
                                DiscordBotConfUtils.textChannelBJoins));
                    } else {
                        if (ConfigUtils.joinsLeavesIcon) {
                            MessagingUtils.sendDiscordJoinLeaveMessageIcon(true, stat);
                        } else {
                            MessagingUtils.sendDiscordJoinLeaveMessagePlain(true, stat);
                        }
                    }
                    break;
                case "staff":
                    if (player.hasPermission(ConfigUtils.staffPerm)) {
                        if (ConfigUtils.joinsLeavesAsConsole) {
                            MessagingUtils.sendDiscordEBMessage(new DiscordMessage(StreamLine.getInstance().getProxy().getConsole(),
                                    MessageConfUtils.discordOnlineEmbed(),
                                    MessageConfUtils.discordOnline().replace("%player_absolute%", player.getName())
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStat(player))),
                                    DiscordBotConfUtils.textChannelBJoins));
                        } else {
                            if (ConfigUtils.joinsLeavesIcon) {
                                MessagingUtils.sendDiscordJoinLeaveMessageIcon(true, stat);
                            } else {
                                MessagingUtils.sendDiscordJoinLeaveMessagePlain(true, stat);
                            }
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
                if (! EventsHandler.checkTags(event, stat)) continue;

                if (! (EventsHandler.checkEventConditions(event, stat, Condition.JOIN, "network"))) continue;

                EventsHandler.runEvent(event, stat);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServer(ServerConnectEvent ev){
        ProxiedPlayer player = ev.getPlayer();

        boolean hasServer = false;
        ServerInfo server = ev.getTarget();

        Player stat = PlayerUtils.addPlayerStat(player);

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
                            MessagingUtils.sendBUserMessage(ev.getPlayer(), MessageConfUtils.vbBlocked());
                            ev.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        if (server == null) return;

        ev.setTarget(server);

        stat.setLatestServer(server.getName());

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

        if (player.getServer() == null) return;

        if (PluginUtils.isLocked()) return;

        Player stat = PlayerUtils.addPlayerStat(player);

        try {
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

        Player stat = PlayerUtils.addPlayerStat(player);

//        switch (ConfigUtils.moduleBPlayerLeaves) {
//            case "yes":
//                MessagingUtils.sendBungeeMessage(new BungeeMassMessage(StreamLine.getInstance().getProxy().getConsole(),
//                        MessageConfUtils.bungeeOffline.replace("%player_absolute%", player.getName())
//                                .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStat(player))),
//                        ConfigUtils.moduleBPlayerLeavesPerm));
//                break;
//            case "staff":
//                if (player.hasPermission(ConfigUtils.staffPerm)) {
//                    MessagingUtils.sendBungeeMessage(new BungeeMassMessage(StreamLine.getInstance().getProxy().getConsole(),
//                            MessageConfUtils.bungeeOffline.replace("%player_absolute%", player.getName())
//                                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStat(player))),
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

                Player other = PlayerUtils.getOrCreatePlayerStat(p);

                label:
                for (String s : order) {
                    switch (s) {
                        case "staff":
                            if (player.hasPermission(ConfigUtils.staffPerm)) {
                                if (p.hasPermission(ConfigUtils.staffPerm)) {
                                    MessagingUtils.sendBUserMessage(p, MessageConfUtils.bungeeOffline()
                                            .replace("%player_absolute%", player.getName())
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(stat))
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
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.guildDisconnect()
                                        .replace("%player_absolute%", player.getName())
                                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                        case "party":
                            if (! ConfigUtils.partySendLeaves) continue;

                            Party party = PartyUtils.getParty(other);
                            if (party == null) continue;

                            if (party.hasMember(stat)) {
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.partyDisconnect()
                                        .replace("%player_absolute%", player.getName())
                                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                        case "friend":
                            if (stat.friendList.contains(other.uuid)) {
                                MessagingUtils.sendBUserMessage(p, MessageConfUtils.friendDisconnect()
                                        .replace("%player_absolute%", player.getName())
                                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(stat))
                                );
                                break label;
                            }
                            break;
                    }
                }
            }
        }

        if (ConfigUtils.moduleDEnabled) {
            switch (ConfigUtils.moduleDPlayerLeaves) {
                case "yes":
                    if (ConfigUtils.joinsLeavesAsConsole) {
                        MessagingUtils.sendDiscordEBMessage(new DiscordMessage(StreamLine.getInstance().getProxy().getConsole(),
                                MessageConfUtils.discordOfflineEmbed(),
                                MessageConfUtils.discordOffline().replace("%player_absolute%", player.getName())
                                        .replace("%player_display%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStat(player))),
                                DiscordBotConfUtils.textChannelBLeaves));
                    } else {
                        if (ConfigUtils.joinsLeavesIcon) {
                            MessagingUtils.sendDiscordJoinLeaveMessageIcon(false, stat);
                        } else {
                            MessagingUtils.sendDiscordJoinLeaveMessagePlain(false, stat);
                        }
                    }
                    break;
                case "staff":
                    if (player.hasPermission(ConfigUtils.staffPerm)) {
                        if (ConfigUtils.joinsLeavesAsConsole) {
                            MessagingUtils.sendDiscordEBMessage(new DiscordMessage(StreamLine.getInstance().getProxy().getConsole(),
                                    MessageConfUtils.discordOfflineEmbed(),
                                    MessageConfUtils.discordOffline().replace("%player_absolute%", player.getName())
                                            .replace("%player_display%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrCreatePlayerStat(player))),
                                    DiscordBotConfUtils.textChannelBLeaves));
                        } else {
                            if (ConfigUtils.joinsLeavesIcon) {
                                MessagingUtils.sendDiscordJoinLeaveMessageIcon(false, stat);
                            } else {
                                MessagingUtils.sendDiscordJoinLeaveMessagePlain(false, stat);
                            }
                        }
                    }
                    break;
                case "no":
                default:
                    break;
            }
        }

        try {
            for (ProxiedPlayer pl : StreamLine.getInstance().getProxy().getPlayers()){
                Player p = PlayerUtils.getOrCreatePlayerStat(pl);

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
            PlayerUtils.addToSave(stat);
            PlayerUtils.doSave(stat);
            PlayerUtils.removeStat(stat);
//            PlayerUtils.removeOfflineStats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKick(ServerKickEvent ev){
        if (ev.isCancelled()) return;

        try {
            if (StreamLine.getInstance().getProxy().getPlayer(ev.getPlayer().getUniqueId()) == null) return;
        } catch (Exception e) {
            return;
        }

        ProxiedPlayer player = ev.getPlayer();

        Player stat = PlayerUtils.addPlayerStat(player);

        if (StreamLine.viaHolder.enabled) {
            if (ConfigUtils.lobbies) {
                Server server = PlayerUtils.getPPlayer(stat.uuid).getServer();

                if (server == null) {
                    MessagingUtils.logSevere("Server for " + player.getName() + " returned null during kick!");
                    return;
                }

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

                while (server.getInfo().getName().equals(kickTo)) {
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