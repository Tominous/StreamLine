package net.plasmere.streamline.listeners;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.events.Event;
import net.plasmere.streamline.events.EventsHandler;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Locale;
import java.util.Objects;

public class ChatListener implements Listener {
    private final Configuration config = Config.getConf();
    private final String prefix = ConfigUtils.moduleStaffChatPrefix;
    private final StreamLine plugin;

    public ChatListener(StreamLine streamLine){
        this.plugin = streamLine;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(ChatEvent e){
        if (e.isCancelled()) return;
        if (! (e.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();

        Player stat = PlayerUtils.getStat(sender);
        String msg = e.getMessage();

        if (stat == null) {
            PlayerUtils.createStat(sender);
            stat = PlayerUtils.getStat(sender);
            if (stat == null) {
                StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + sender.getName());
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
                    GuildUtils.addGuild(new Guild(stat.getUniqueId(), false));
                }
                break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (TextUtils.isCommand(msg)) return;

        if (ConfigUtils.moduleStaffChat) {
            if (ConfigUtils.moduleStaffChatDoPrefix) {
                if (msg.startsWith(prefix) && !prefix.equals("/")) {
                    if (!sender.hasPermission("streamline.staff")) {
                        e.setCancelled(true);
                        return;
                    }

                    if (msg.equals(prefix)) {
                        sender.sendMessage(TextUtils.codedText(MessageConfUtils.staffChatWrongPrefix.replace("%newline%", "\n")));
                        e.setCancelled(true);
                        return;
                    }

                    e.setCancelled(true);
                    MessagingUtils.sendStaffMessage(sender, MessageConfUtils.bungeeStaffChatFrom, msg.substring(prefix.length()), plugin);
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender,
                            MessageConfUtils.staffChatEmbedTitle,
                            MessageConfUtils.discordStaffChatMessage
                                    .replace("%user%", sender.getName())
                                    .replace("%message%", msg.substring(prefix.length())),
                            ConfigUtils.textChannelStaffChat));
                }
            }
        }

        if (! msg.startsWith("/")) {
            for (Event event : EventsHandler.getEvents()) {
                if (! EventsHandler.checkTags(event, stat)) continue;

                if (! (event.condition.equals(Event.Condition.MESSAGE_EXACT) && (event.conVal.equals(msg) || event.conVal.equals("") || event.conVal.toLowerCase(Locale.ROOT).equals("null")))) continue;

                EventsHandler.runEvent(event, stat, msg);
            }
        } else {
            for (Event event : EventsHandler.getEvents()) {
                if (! EventsHandler.checkTags(event, stat)) continue;

                if (! (event.condition.equals(Event.Condition.MESSAGE_EXACT) && (event.conVal.equals(msg.substring(1)) || event.conVal.equals("") || event.conVal.toLowerCase(Locale.ROOT).equals("null")))) continue;

                EventsHandler.runEvent(event, stat, msg);
            }
        }

        for (Event event : EventsHandler.getEvents()) {
            if (! EventsHandler.checkTags(event, stat)) continue;

            if (! (event.condition.equals(Event.Condition.MESSAGE_CONTAINS) && (msg.contains(event.conVal)))) continue;

            EventsHandler.runEvent(event, stat, msg);
        }

        for (Event event : EventsHandler.getEvents()) {
            if (! EventsHandler.checkTags(event, stat)) continue;

            if (! (event.condition.equals(Event.Condition.MESSAGE_STARTS_WITH) && (msg.startsWith(event.conVal)))) continue;

            EventsHandler.runEvent(event, stat, msg);
        }

        for (Event event : EventsHandler.getEvents()) {
            if (! EventsHandler.checkTags(event, stat)) continue;

            if (! (event.condition.equals(Event.Condition.MESSAGE_ENDS_WITH) && (msg.endsWith(event.conVal)))) continue;

            EventsHandler.runEvent(event, stat, msg);
        }
    }
}
