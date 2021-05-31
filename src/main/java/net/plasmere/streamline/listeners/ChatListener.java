package net.plasmere.streamline.listeners;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.events.Event;
import net.plasmere.streamline.events.EventsHandler;
import net.plasmere.streamline.events.enums.Condition;
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

import java.util.Arrays;
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

        stat.updateLastMessage(msg);

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
                    GuildUtils.addGuild(new Guild(stat.guild, false));
                }
                break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (ConfigUtils.punMutes && ConfigUtils.punMutesHard && stat.muted) {
            e.setCancelled(true);
            if (stat.mutedTill != null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.punMutedTemp.replace("%date%", stat.mutedTill.toString()));
            } else {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.punMutedPerm);
            }
            return;
        }

        if (TextUtils.isCommand(msg)) return;

        if (ConfigUtils.punMutes && stat.muted) {
            e.setCancelled(true);
            if (stat.mutedTill != null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.punMutedTemp.replace("%date%", stat.mutedTill.toString()));
            } else {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.punMutedPerm);
            }
            return;
        }

        if (ConfigUtils.moduleStaffChat) {
            if (stat.sc) {
                if (! sender.hasPermission(ConfigUtils.staffPerm)) {
                    return;
                }

                e.setCancelled(true);
                MessagingUtils.sendStaffChatMessage(sender, MessageConfUtils.bungeeStaffChatFrom, msg.substring(prefix.length()), plugin);
                if (ConfigUtils.moduleStaffChatMToDiscord) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender,
                            MessageConfUtils.staffChatEmbedTitle,
                            MessageConfUtils.discordStaffChatMessage
                                    .replace("%user%", sender.getName())
                                    .replace("%message%", msg.substring(prefix.length())),
                            ConfigUtils.textChannelStaffChat));
                }
            } else if (ConfigUtils.moduleStaffChatDoPrefix) {
                if (msg.startsWith(prefix) && !prefix.equals("/")) {
                    if (!sender.hasPermission(ConfigUtils.staffPerm)) {
                        return;
                    }

                    if (msg.equals(prefix)) {
                        sender.sendMessage(TextUtils.codedText(MessageConfUtils.staffChatJustPrefix.replace("%newline%", "\n")));
                        e.setCancelled(true);
                        return;
                    }

                    e.setCancelled(true);
                    MessagingUtils.sendStaffChatMessage(sender, MessageConfUtils.bungeeStaffChatFrom, msg.substring(prefix.length()), plugin);
                    if (ConfigUtils.moduleStaffChatMToDiscord) {
                        MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender,
                                MessageConfUtils.staffChatEmbedTitle,
                                MessageConfUtils.discordStaffChatMessage
                                        .replace("%user%", sender.getName())
                                        .replace("%message%", msg.substring(prefix.length())),
                                ConfigUtils.textChannelStaffChat));
                    }
                }
            }
        }

        if (ConfigUtils.events) {
            if (!msg.startsWith("/")) {
                for (Event event : EventsHandler.getEvents()) {
                    if (!EventsHandler.checkTags(event, stat)) continue;

                    if (!EventsHandler.checkEventConditions(event, stat, Condition.MESSAGE_EXACT, Arrays.asList(msg, "", "null")))
                        continue;

                    EventsHandler.runEvent(event, stat, msg);
                }
            } else {
                for (Event event : EventsHandler.getEvents()) {
                    if (!EventsHandler.checkTags(event, stat)) continue;

                    if (!EventsHandler.checkEventConditions(event, stat, Condition.COMMAND, msg) || !EventsHandler.checkEventConditions(event, stat, Condition.MESSAGE_EXACT, Arrays.asList(msg.substring(1), "", "null")))
                        continue;

                    EventsHandler.runEvent(event, stat, msg);
                }
            }

            for (Event event : EventsHandler.getEvents()) {
                if (!EventsHandler.checkTags(event, stat)) continue;

                if (!EventsHandler.checkEventConditions(event, stat, Condition.MESSAGE_CONTAINS, msg)) continue;

                EventsHandler.runEvent(event, stat, msg);
            }

            for (Event event : EventsHandler.getEvents()) {
                if (!EventsHandler.checkTags(event, stat)) continue;

                if (!EventsHandler.checkEventConditions(event, stat, Condition.MESSAGE_STARTS_WITH, msg)) continue;

                EventsHandler.runEvent(event, stat, msg);
            }

            for (Event event : EventsHandler.getEvents()) {
                if (!EventsHandler.checkTags(event, stat)) continue;

                if (!EventsHandler.checkEventConditions(event, stat, Condition.MESSAGE_ENDS_WITH, msg)) continue;

                EventsHandler.runEvent(event, stat, msg);
            }
        }
    }
}
