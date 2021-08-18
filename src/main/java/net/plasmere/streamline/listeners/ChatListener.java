package net.plasmere.streamline.listeners;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.events.Event;
import net.plasmere.streamline.events.EventsHandler;
import net.plasmere.streamline.events.enums.Condition;
import net.plasmere.streamline.objects.lists.SingleSet;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChatListener implements Listener {
    private final String prefix = ConfigUtils.moduleStaffChatPrefix;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(ChatEvent e){
        if (e.isCancelled()) return;
        if (! (e.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();

        String msg = e.getMessage();

        Player stat = PlayerUtils.addPlayerStat(sender);

        stat.updateLastMessage(msg);

        try {
            for (ProxiedPlayer pl : StreamLine.getInstance().getProxy().getPlayers()){
                Player p = PlayerUtils.getOrCreatePlayerStat(pl);

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
            if (PlayerUtils.checkIfMuted(sender, stat)) {
                e.setCancelled(true);
                return;
            }
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
                MessagingUtils.sendStaffMessage(sender, MessageConfUtils.bungeeStaffChatFrom, msg);
                if (ConfigUtils.moduleStaffChatMToDiscord) {
                    MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender,
                            MessageConfUtils.staffChatEmbedTitle,
                            MessageConfUtils.discordStaffChatMessage
                                    .replace("%user%", sender.getName())
                                    .replace("%message%", msg),
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
                    MessagingUtils.sendStaffMessage(sender, MessageConfUtils.bungeeStaffChatFrom, msg.substring(prefix.length()));
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

        if (StreamLine.serverConfig.getProxyChatEnabled()) {
            SingleSet<String, List<ProxiedPlayer>> msgWithTagged = TextUtils.getMessageWithTags(sender, msg);

            String format = StreamLine.serverConfig.getPermissionedProxyChatMessage(stat);
            MessagingUtils.sendServerMessageFromUser(sender, sender.getServer(), format, msgWithTagged.key);

            for (ProxiedPlayer player : msgWithTagged.value) {
                MessagingUtils.sendTagPingPluginMessageRequest(player);
            }
            e.setCancelled(true);
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
