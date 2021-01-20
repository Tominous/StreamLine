package net.plasmere.streamline.listeners;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.DiscordMessage;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

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
        String msg = e.getMessage();

        try {
            for (ProxiedPlayer p : StreamLine.getInstance().getProxy().getPlayers()){
                if (GuildUtils.getGuild(p) == null && ! p.equals(sender)) continue;
                if (GuildUtils.getGuild(p) != null) {
                    if (Objects.requireNonNull(GuildUtils.getGuild(p)).hasMember(sender)) break;
                }

                if (GuildUtils.pHasGuild(sender)) {
                    GuildUtils.addGuild(new Guild(sender.getUniqueId(), false));
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
    }
}
